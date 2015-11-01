package com.tbg.simplestvallet.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.adapter.PendingEntryListAdapter;
import com.tbg.simplestvallet.app.SimplestValetApp;
import com.tbg.simplestvallet.model.active.abstr.IPendingEntryStore;
import com.tbg.simplestvallet.model.dto.LocalEntry;
import com.tbg.simplestvallet.ioc.taskmanager.task.RetrievePendingEntriesTask;
import com.tbg.taskmanager.abstr.delegate.ITaskDelegate;
import com.tbg.taskmanager.abstr.executor.ITaskExecutor;
import com.tbg.taskmanager.abstr.task.ITask;
import com.tbg.taskmanager.common.Result;
import com.tbg.taskmanager.impl.executor.AsyncTaskBasedTaskExecutorImpl;

import java.util.ArrayList;
import java.util.List;

public class PendingListFragment extends Fragment {

    private ViewWrapper mViewWrapper;
    private ITaskExecutor mTaskExecutor;
    private ListView mLvPendingEntries;

    public PendingListFragment() {
        mTaskExecutor = new AsyncTaskBasedTaskExecutorImpl();
        mViewWrapper = new ViewWrapper();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPendingEntryList();
    }

    private void setupView() {
        mLvPendingEntries = (ListView)getActivity().findViewById(R.id.lv_pending_entries);
        mViewWrapper.setup(mLvPendingEntries);
    }

    private void loadPendingEntryList() {
        IPendingEntryStore pendingEntryStore = SimplestValetApp.getEntryCollectionContainer().getPendingEntryStore();
        ITask<List<LocalEntry>> retrievePendingEntriesTask = new RetrievePendingEntriesTask(pendingEntryStore);
        ITaskDelegate<List<LocalEntry>> taskDelegate = new RetrievePendingEntryListTaskDelegate(mViewWrapper);
        mTaskExecutor.executeTask(retrievePendingEntriesTask, taskDelegate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pendinglist, container, false);
    }

    private class RetrievePendingEntryListTaskDelegate implements ITaskDelegate<List<LocalEntry> >{
        private ViewWrapper mViewWrapper;

        public RetrievePendingEntryListTaskDelegate(ViewWrapper viewWrapper) {
            this.mViewWrapper = viewWrapper;
        }

        @Override
        public void onTaskToBeExecuted() {
            //TODO Implement
        }

        @Override
        public void onTaskExecuted(Result<List<LocalEntry>> taskResult) {
            mViewWrapper.renderPendingEntryList(taskResult.getElement());
        }

        @Override
        public void onTaskCancelled() {
            //TODO Implement
        }
    }

    private class ViewWrapper {
        private ListView mLvPendingEntries;
        private List<LocalEntry> mPendingEntries = new ArrayList<>();
        private ArrayAdapter<LocalEntry> mPendingEntriesAdapter;

        public void setup(ListView lvPendingEntries) {
            mLvPendingEntries = lvPendingEntries;
            mPendingEntriesAdapter = new PendingEntryListAdapter(lvPendingEntries.getContext(), mPendingEntries);
            mLvPendingEntries.setAdapter(mPendingEntriesAdapter);
        }

        public void renderPendingEntryList(List<LocalEntry> pendingEntries) {
            mPendingEntries.clear();
            mPendingEntries.addAll(pendingEntries);
            mPendingEntriesAdapter.notifyDataSetChanged();
        }
    }
}
