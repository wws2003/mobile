package com.tbg.simplestvallet.activity.delegate;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ListView;
import android.widget.TextView;

import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.app.SimplestValetApp;
import com.tbg.simplestvallet.model.active.abstr.collection.ISVEntrySheet;
import com.tbg.simplestvallet.model.dto.SVEntry;
import com.tbg.simplestvallet.view.SVSearchViewWrapper;
import com.tbg.taskmanager.abstr.delegate.ITaskDelegate;
import com.tbg.taskmanager.abstr.executor.ITaskExecutor;
import com.tbg.taskmanager.abstr.task.AbstractTask;
import com.tbg.taskmanager.abstr.task.ITask;
import com.tbg.taskmanager.common.Result;
import com.tbg.taskmanager.impl.executor.AsyncTaskBasedTaskExecutorImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wws2003 on 5/4/16.
 */
public class SVEntrySearchDelegate {

    private ITaskExecutor mTaskExecutor = new AsyncTaskBasedTaskExecutorImpl();

    private Context mContext;
    private SVSearchViewWrapper mSearchViewWrapper;

    public SVEntrySearchDelegate(Context context, ListView lvSearchResults, TextView tvSearchSummary) {
        mContext = context;
        mSearchViewWrapper = new SVSearchViewWrapper(context, lvSearchResults, tvSearchSummary);
    }

    public void search(String query) {
        final ISVEntrySheet entrySheet = SimplestValetApp.getSheetServiceManagerContainer().getCachedSheetServiceManager().getSVEntrySheet();

        ITask<List<SVEntry>> dataTask = getDataLoadTask(entrySheet, query);

        ITaskDelegate<List<SVEntry>> dataTaskDelegate = getDataLoadTaskDelegate(query);

        mTaskExecutor.executeTask(dataTask, dataTaskDelegate);
    }

    private ITask<List<SVEntry>> getDataLoadTask(final ISVEntrySheet entrySheet,
                                                 final String query) {

        return new AbstractTask<List<SVEntry>>() {
            @Override
            public Result<List<SVEntry>> doExecute() {
                List<SVEntry> matchedEntries = new ArrayList<>();
                entrySheet.queryFullTextEntries(query, matchedEntries);
                return generateResult(matchedEntries, 0);
            }
        };
    }

    private ITaskDelegate<List<SVEntry>> getDataLoadTaskDelegate(final String query) {
        return new ITaskDelegate<List<SVEntry>>() {

            private ProgressDialog mProgressDialog = initProgressDialog();

            @Override
            public void onTaskToBeExecuted() {
                mProgressDialog.show();
                mSearchViewWrapper.hideSearchSummary();
            }

            @Override
            public void onTaskCancelled() {
                //Do nothing
            }

            @Override
            public void onTaskExecuted(Result<List<SVEntry>> taskResult) {
                mProgressDialog.dismiss();
                if (taskResult.getResultCode() == 0) {
                    mSearchViewWrapper.renderPendingEntryList(query, taskResult.getElement());
                }
                else {
                    mSearchViewWrapper.renderError(taskResult.getResultCode());
                }
            }

            private ProgressDialog initProgressDialog() {
                ProgressDialog dialog = new ProgressDialog(mContext);
                dialog.setMessage(mContext.getString(R.string.dlg_load_amount_message));
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setIndeterminate(true);
                return dialog;
            }
        };
    }

}
