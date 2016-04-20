package com.tbg.simplestvallet.ioc.taskmanager.task;

import com.tbg.simplestvallet.app.SimplestValetApp;
import com.tbg.simplestvallet.model.active.abstr.ISVPendingEntryStore;
import com.tbg.simplestvallet.model.dto.SVLocalEntry;
import com.tbg.taskmanager.abstr.task.AbstractTask;
import com.tbg.taskmanager.common.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wws2003 on 10/29/15.
 */
public class RetrievePendingEntriesTask extends AbstractTask<List<SVLocalEntry>> {

    private ISVPendingEntryStore mPendingEntryStore;

    public RetrievePendingEntriesTask(ISVPendingEntryStore pendingEntryStore) {
        this.mPendingEntryStore = pendingEntryStore;
        setId(SimplestValetApp.getTaskIdPool().getAvailableTaskId());
    }

    @Override
    public Result<List<SVLocalEntry>> doExecute() {
        List<SVLocalEntry> pendingEntries = new ArrayList<>();
        int resultCode = mPendingEntryStore.getAllPendingEntries(pendingEntries);
        Result<List<SVLocalEntry>> result = generateResult(pendingEntries, resultCode);
        return result;
    }
}
