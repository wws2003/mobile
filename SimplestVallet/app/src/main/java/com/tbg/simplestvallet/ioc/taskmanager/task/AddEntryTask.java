package com.tbg.simplestvallet.ioc.taskmanager.task;

import com.tbg.simplestvallet.app.SimplestValetApp;
import com.tbg.simplestvallet.model.active.EntryActionResult;
import com.tbg.simplestvallet.model.active.abstr.collection.ISVEntrySheet;
import com.tbg.simplestvallet.model.dto.SVEntry;
import com.tbg.taskmanager.abstr.task.AbstractTask;
import com.tbg.taskmanager.common.Result;

/**
 * Created by wws2003 on 10/18/15.
 */
public class AddEntryTask extends AbstractTask<SVEntry> {

    private ISVEntrySheet mSheet;
    private SVEntry mEntry;

    public AddEntryTask(ISVEntrySheet sheet, SVEntry entry) {
        this.mSheet = sheet;
        this.mEntry = entry;
        setId(SimplestValetApp.getTaskIdPool().getAvailableTaskId());
    }

    @Override
    public Result<SVEntry> doExecute() {
        int resultCode = mSheet != null ?  mSheet.addEntry(mEntry) : EntryActionResult.ADD_RESULT_PENDED;
        return generateResult(mEntry, resultCode);
    }
}
