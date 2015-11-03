package com.tbg.simplestvallet.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.app.SimplestValetApp;
import com.tbg.simplestvallet.model.active.abstr.IEntrySheet;
import com.tbg.simplestvallet.model.dto.MoneyQuantity;
import com.tbg.taskmanager.abstr.delegate.AbstractTaskResultListener;
import com.tbg.taskmanager.abstr.delegate.ITaskDelegate;
import com.tbg.taskmanager.abstr.executor.ITaskExecutor;
import com.tbg.taskmanager.abstr.task.AbstractTask;
import com.tbg.taskmanager.abstr.task.ITask;
import com.tbg.taskmanager.common.Result;
import com.tbg.taskmanager.impl.executor.AsyncTaskBasedTaskExecutorImpl;

import java.util.ArrayList;
import java.util.List;


public class ChartFragment extends Fragment {

    private ViewWrapper mViewWrapper = new ViewWrapper();
    private ITaskExecutor mTaskExecutor = new AsyncTaskBasedTaskExecutorImpl();

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
        final IEntrySheet entrySheet = SimplestValetApp.getEntryCollectionContainer().getEntrySheet();
        final int LOAD_AMOUNT_OK = 0;
        //final int LOAD_AMOUNT_FAILED = 1;

        ITask<List<MoneyQuantity>> dataTask = new AbstractTask<List<MoneyQuantity>>() {
            @Override
            public Result<List<MoneyQuantity>> doExecute() {
                List<MoneyQuantity> amounts = new ArrayList<>();
                //TODO Change implementation logic to handle error
                amounts.add(entrySheet.getAllEntriesAmount());
                amounts.add(entrySheet.getCachedAllEntriesAmount());
                return generateResult(amounts, LOAD_AMOUNT_OK);
            }
        };
        ITaskDelegate<List<MoneyQuantity>> dataTaskDelegate = new AbstractTaskResultListener<List<MoneyQuantity>>() {
            @Override
            public void onTaskExecuted(Result<List<MoneyQuantity>> taskResult) {
                if(taskResult.getResultCode() == LOAD_AMOUNT_OK) {
                    MoneyQuantity sumAmount = taskResult.getElement().get(0);
                    MoneyQuantity amountThisMonth = taskResult.getElement().get(1);
                    mViewWrapper.render(sumAmount, amountThisMonth);
                }
                else {
                    mViewWrapper.renderError();
                }
            }
        };

        mTaskExecutor.executeTask(dataTask, dataTaskDelegate);
    }

    private class ViewWrapper {
        private TextView mTvSpentAmount;
        private TextView mTvSpentAmountThisMonth;

        public void setup(TextView tvSpentAmount, TextView tvSpentAmountThisMonth) {
            this.mTvSpentAmountThisMonth = tvSpentAmount;
            this.mTvSpentAmount = tvSpentAmountThisMonth;
        }

        public void render(MoneyQuantity spentAmount, MoneyQuantity spentAmountThisMonth) {
            mTvSpentAmount.setText(spentAmount.toString());
            mTvSpentAmountThisMonth.setText(spentAmountThisMonth.toString());
        }

        public void renderError() {
            //TODO Implement
        }
    }
}
