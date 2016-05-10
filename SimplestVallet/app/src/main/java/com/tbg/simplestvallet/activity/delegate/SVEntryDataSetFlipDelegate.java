package com.tbg.simplestvallet.activity.delegate;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.app.SimplestValetApp;
import com.tbg.simplestvallet.model.active.abstr.collection.ISVEntrySheet;
import com.tbg.simplestvallet.model.active.abstr.query.ISVEntryQueryStructureBuilder;
import com.tbg.simplestvallet.model.active.abstr.query.ISVQueryStructure;
import com.tbg.simplestvallet.model.dto.SVEntry;
import com.tbg.simplestvallet.model.dto.SVEntryLineDataSet;
import com.tbg.simplestvallet.model.dto.SVEntryPieDataSet;
import com.tbg.simplestvallet.model.dto.SVMoneyQuantity;
import com.tbg.simplestvallet.util.DateUtil;
import com.tbg.simplestvallet.view.SVChartViewWrapper;
import com.tbg.taskmanager.abstr.delegate.ITaskDelegate;
import com.tbg.taskmanager.abstr.executor.ITaskExecutor;
import com.tbg.taskmanager.abstr.task.AbstractTask;
import com.tbg.taskmanager.abstr.task.ITask;
import com.tbg.taskmanager.common.Result;
import com.tbg.taskmanager.impl.executor.AsyncTaskBasedTaskExecutorImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by wws2003 on 5/2/16.
 */
public class SVEntryDataSetFlipDelegate {

    private SVEntryLineDataSet mEntriesLineDataSet;
    private SVEntryPieDataSet mEntriesPieDataSet;

    private SVChartViewWrapper mChartViewWrapper;

    private Context mContext;
    private TextView mTvCursorMonthSpentAmount;
    private TextView mTvPrevMonthSpentAmount;

    private Date mCursorMonth = null;
    private Date mPrevMonth = null;

    private List<SVEntry>[] mTwoMonthsEntries = new List[2];

    private ITaskExecutor mTaskExecutor = new AsyncTaskBasedTaskExecutorImpl();

    public SVEntryDataSetFlipDelegate(String lineDataSetDescription,
                                      String pieDateSetLabel,
                                      String pieDataSetDescription,
                                      Context context,
                                      TextView tvCursorMonthSpentAmount,
                                      TextView tvPrevMonthSpentAmount,
                                      ViewSwitcher viewSwitcher,
                                      View.OnTouchListener onTouchListener) {

        mEntriesLineDataSet = new SVEntryLineDataSet(lineDataSetDescription);
        mEntriesPieDataSet = new SVEntryPieDataSet(pieDateSetLabel, pieDataSetDescription);

        mContext = context;
        mTvCursorMonthSpentAmount = tvCursorMonthSpentAmount;
        mTvPrevMonthSpentAmount = tvPrevMonthSpentAmount;

        mChartViewWrapper = new SVChartViewWrapper(context, viewSwitcher, mEntriesLineDataSet, mEntriesPieDataSet);
        mChartViewWrapper.setup(onTouchListener);

        initDates();
        initTwoMonthEntries();
    }

    public void firstLoad() {
        loadEntries(mTwoMonthsEntries[0], mPrevMonth);
        loadEntries(mTwoMonthsEntries[1], mCursorMonth);
    }

    public void flipLeftToRight() {
        //Flip view first
        mChartViewWrapper.flipChart(SVChartViewWrapper.FLIP_DIRECTION_LEFT_TO_RIGHT);

        //To previous months
        flipMonth(-1);

        //Load data of previous month
        loadEntries(mTwoMonthsEntries[0], mPrevMonth);
    }

    public void flipRightToLeft() {
        //Flip view first
        mChartViewWrapper.flipChart(SVChartViewWrapper.FLIP_DIRECTION_RIGHT_TO_LEFT);

        //To next months
        flipMonth(1);

        //Load data of next month
        loadEntries(mTwoMonthsEntries[1], mCursorMonth);
    }

    public void showLineChart() {
        mChartViewWrapper.switchChart(SVChartViewWrapper.CHART_TYPE_LINE);
    }

    public void showPieChart() {
        mChartViewWrapper.switchChart(SVChartViewWrapper.CHART_TYPE_PIE);
    }

    //--------------------------MARK: Private methods--------------------------

    private void initDates() {
        mCursorMonth = Calendar.getInstance().getTime();
        mPrevMonth =  DateUtil.getPreviousMonth(mCursorMonth);
    }

    private void initTwoMonthEntries() {
        mTwoMonthsEntries[0] = new ArrayList<>();
        mTwoMonthsEntries[1] = new ArrayList<>();
    }

    private void flipMonth(int d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mCursorMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, d);
        mCursorMonth = calendar.getTime();
        mPrevMonth = DateUtil.getPreviousMonth(mCursorMonth);

        int keepIndex = 0, clearIndex = 0;
        //To prev month
        if(d == -1) {
            keepIndex = 0;
            clearIndex = 1;
        }
        //To next month
        if(d == 1) {
            keepIndex = 1;
            clearIndex = 0;
        }
        mTwoMonthsEntries[clearIndex].clear();
        mTwoMonthsEntries[clearIndex].addAll(mTwoMonthsEntries[keepIndex]);
        mTwoMonthsEntries[keepIndex].clear();
    }

    private void loadEntries(List<SVEntry> entries, Date month) {
        final ISVEntrySheet entrySheet = SimplestValetApp.getSheetServiceManagerContainer().getCachedSheetServiceManager().getSVEntrySheet();

        //Currently just testing with load all amount of this month
        final ISVQueryStructure queryStructure = getEntryQueryStructure(month);

        ITask<Integer> dataTask = getDataLoadTask(entrySheet, queryStructure, entries);

        ITaskDelegate<Integer> dataTaskDelegate = getDataLoadTaskDelegate();

        mTaskExecutor.executeTask(dataTask, dataTaskDelegate);
    }

    private ISVQueryStructure getEntryQueryStructure(Date month) {
        ISVEntryQueryStructureBuilder queryStructureBuilder = SimplestValetApp.getEntryQueryStructureBuilderContainer().getEntryQueryStructureBuilder();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(month);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        Date lastDayOfMonth = calendar.getTime();

        queryStructureBuilder = queryStructureBuilder.newInstance();
        queryStructureBuilder.setDateRanges(firstDayOfMonth, lastDayOfMonth);

        return queryStructureBuilder.build();
    }

    private ITask<Integer> getDataLoadTask(final ISVEntrySheet entrySheet,
                                           final ISVQueryStructure queryStructure,
                                           final List<SVEntry> entries) {

        return new AbstractTask<Integer>() {
            @Override
            public Result<Integer> doExecute() {
                entrySheet.queryEntries(queryStructure, entries);

                mEntriesLineDataSet.updateData(mCursorMonth, mTwoMonthsEntries[1], mTwoMonthsEntries[0]);
                mEntriesPieDataSet.updateData(mCursorMonth, mTwoMonthsEntries[1]);

                return generateResult(entries.size(), 0);
            }
        };
    }

    private ITaskDelegate<Integer> getDataLoadTaskDelegate() {
        return new ITaskDelegate<Integer>() {

            private ProgressDialog mProgressDialog = initProgressDialog();

            @Override
            public void onTaskToBeExecuted() {
                mProgressDialog.show();
            }

            @Override
            public void onTaskCancelled() {
                //Do nothing
            }

            @Override
            public void onTaskExecuted(Result<Integer> taskResult) {
                mProgressDialog.dismiss();
                updateAmountTexts();
                updateCharts();
            }

            private void updateAmountTexts() {
                Locale locale = Locale.getDefault();

                String format = mContext.getResources().getString(R.string.tv_month_amount);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mCursorMonth);

                String cursorMonthAmountText = String.format(format,
                        calendar.get(Calendar.YEAR),
                        calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale),
                        getSumAmount(mTwoMonthsEntries[1]));

                mTvCursorMonthSpentAmount.setText(cursorMonthAmountText);

                calendar.setTime(mPrevMonth);

                String prevMonthAmountText = String.format(format,
                        calendar.get(Calendar.YEAR),
                        calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale),
                        getSumAmount(mTwoMonthsEntries[0]));

                mTvPrevMonthSpentAmount.setText(prevMonthAmountText);
            }

            private void updateCharts() {
                mChartViewWrapper.dataChangedNotified();
            }

            private ProgressDialog initProgressDialog() {
                ProgressDialog dialog = new ProgressDialog(mContext);
                dialog.setMessage(mContext.getString(R.string.dlg_load_amount_message));
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setIndeterminate(true);
                dialog.setCanceledOnTouchOutside(false);
                return dialog;
            }
        };
    }

    private float getSumAmount(List<SVEntry> entries) {
        SVMoneyQuantity amount = new SVMoneyQuantity((float)0.0);
        for(SVEntry entry : entries) {
            amount = amount.add(entry.getMoneyQuantity());
        }
        return amount.getAmount();
    }
}
