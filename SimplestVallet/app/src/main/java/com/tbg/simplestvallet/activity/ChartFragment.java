package com.tbg.simplestvallet.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.app.SimplestValetApp;
import com.tbg.simplestvallet.model.active.abstr.collection.ISVEntrySheet;
import com.tbg.simplestvallet.model.active.abstr.query.ISVEntryQueryStructureBuilder;
import com.tbg.simplestvallet.model.active.abstr.query.ISVQueryStructure;
import com.tbg.simplestvallet.model.dto.SVMoneyQuantity;
import com.tbg.taskmanager.abstr.delegate.ITaskDelegate;
import com.tbg.taskmanager.abstr.executor.ITaskExecutor;
import com.tbg.taskmanager.abstr.task.AbstractTask;
import com.tbg.taskmanager.abstr.task.ITask;
import com.tbg.taskmanager.common.Result;
import com.tbg.taskmanager.impl.executor.AsyncTaskBasedTaskExecutorImpl;

import java.util.Calendar;
import java.util.Date;


public class ChartFragment extends Fragment {

    private ViewWrapper mViewWrapper = new ViewWrapper();
    private ITaskExecutor mTaskExecutor = new AsyncTaskBasedTaskExecutorImpl();

    private static final int LOAD_AMOUNT_OK = 0;
    private static final int LOAD_AMOUNT_FAILED = 1;

    public ChartFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void setupView() {
        TextView tvSpentAmount = (TextView)getActivity().findViewById(R.id.tv_spent_amount);
        TextView tvSpentAmountThisMonth = (TextView)getActivity().findViewById(R.id.tv_spent_amount_this_month);
        mViewWrapper.setup(tvSpentAmount, tvSpentAmountThisMonth);
    }

    private void loadData() {
        final ISVEntrySheet entrySheet = SimplestValetApp.getSheetServiceManagerContainer().getCachedSheetServiceManager().getSVEntrySheet();

        //Currently just testing with load all amount of this month
        final ISVQueryStructure queryStructure = getEntryQueryWrapper();

        ITask<SVMoneyQuantity> dataTask = getDataLoadTask(entrySheet, queryStructure);

        ITaskDelegate<SVMoneyQuantity> dataTaskDelegate = getDataLoadTaskDelegate();

        mTaskExecutor.executeTask(dataTask, dataTaskDelegate);
    }

    private ISVQueryStructure getEntryQueryWrapper() {
        ISVEntryQueryStructureBuilder queryStructureBuilder = SimplestValetApp.getEntryQueryStructureBuilderContainer().getEntryQueryStructureBuilder();
        queryStructureBuilder = queryStructureBuilder.newInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDateOfMonth = calendar.getTime();
        queryStructureBuilder.setDateRanges(firstDateOfMonth, null);

        return queryStructureBuilder.build();
    }

    private ITask<SVMoneyQuantity> getDataLoadTask(final ISVEntrySheet entrySheet, final ISVQueryStructure queryStructure) {

        ITask<SVMoneyQuantity> dataTask = new AbstractTask<SVMoneyQuantity>() {
            @Override
            public Result<SVMoneyQuantity> doExecute() {
                SVMoneyQuantity amount = entrySheet.queryEntriesAmount(queryStructure);
                return generateResult(amount, LOAD_AMOUNT_OK);
            }
        };

        return dataTask;
    }

    private ITaskDelegate<SVMoneyQuantity> getDataLoadTaskDelegate() {
        ITaskDelegate<SVMoneyQuantity> dataTaskDelegate = new ITaskDelegate<SVMoneyQuantity>() {

            @Override
            public void onTaskToBeExecuted() {
                mViewWrapper.freeze();
            }

            @Override
            public void onTaskCancelled() {
                //Do nothing
            }

            @Override
            public void onTaskExecuted(Result<SVMoneyQuantity> taskResult) {
                mViewWrapper.unFreezing();
                if(taskResult.getResultCode() == LOAD_AMOUNT_OK) {
                    SVMoneyQuantity amountThisMonth = taskResult.getElement();
                    if(amountThisMonth != null) {
                        //Just to test
                        mViewWrapper.render(amountThisMonth, amountThisMonth);
                        return;
                    }
                }
                mViewWrapper.renderError();
            }
        };
        return dataTaskDelegate;
    }

    private class ViewWrapper {
        private TextView mTvSpentAmount;
        private TextView mTvSpentAmountThisMonth;
        private ProgressDialog mProgressDialog = null;
        private Context mContext = null;

        public void setup(TextView tvSpentAmount, TextView tvSpentAmountThisMonth) {
            this.mTvSpentAmount = tvSpentAmount;
            this.mTvSpentAmountThisMonth = tvSpentAmountThisMonth;
            this.mContext = tvSpentAmount.getContext();
            initProgressDialog();
        }

        public void render(SVMoneyQuantity spentAmount, SVMoneyQuantity spentAmountThisMonth) {
            mTvSpentAmount.setText(spentAmount.toString());
            mTvSpentAmountThisMonth.setText(spentAmountThisMonth.toString());
        }

        public void renderError() {
            //TODO Implement
        }

        public void freeze() {
            //Show progress bar
            mProgressDialog.show();
        }

        public void unFreezing() {
            //Hide progress bar
            mProgressDialog.dismiss();
        }

        private void initProgressDialog() {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage(mContext.getString(R.string.dlg_load_amount_message));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setIndeterminate(true);
        }
    }
}
