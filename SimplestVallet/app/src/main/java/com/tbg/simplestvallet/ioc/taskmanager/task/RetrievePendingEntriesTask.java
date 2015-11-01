package com.tbg.simplestvallet.ioc.taskmanager.task;

import com.tbg.simplestvallet.app.SimplestValetApp;
import com.tbg.simplestvallet.model.active.abstr.IPendingEntryStore;
import com.tbg.simplestvallet.model.dto.LocalEntry;
import com.tbg.taskmanager.abstr.task.AbstractTask;
import com.tbg.taskmanager.common.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wws2003 on 10/29/15.
 */
public class RetrievePendingEntriesTask extends AbstractTask<List<LocalEntry>> {

    private IPendingEntryStore mPendingEntryStore;

    public RetrievePendingEntriesTask(IPendingEntryStore pendingEntryStore) {
        this.mPendingEntryStore = pendingEntryStore;
        setId(SimplestValetApp.getTaskIdPool().getAvailableTaskId());
    }

    @Override
    public Result<List<LocalEntry>> doExecute() {
        List<LocalEntry> pendingEntries = new ArrayList<>();
        int resultCode = mPendingEntryStore.getAllPendingEntries(pendingEntries);
        Result<List<LocalEntry>> result = new Result<>(pendingEntries, getId(), resultCode);
        return result;
    }
}
