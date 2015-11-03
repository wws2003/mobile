package com.tbg.simplestvallet.ioc.taskmanager.task;

import com.tbg.simplestvallet.app.SimplestValetApp;
import com.tbg.simplestvallet.model.active.EntryActionResult;
import com.tbg.simplestvallet.model.active.abstr.IEntrySheet;
import com.tbg.simplestvallet.model.dto.Entry;
import com.tbg.taskmanager.abstr.task.AbstractTask;
import com.tbg.taskmanager.common.Result;

/**
 * Created by wws2003 on 10/18/15.
 */
public class AddEntryTask extends AbstractTask<Entry> {

    private IEntrySheet mSheet;
    private Entry mEntry;

    public AddEntryTask(IEntrySheet sheet, Entry entry) {
        this.mSheet = sheet;
        this.mEntry = entry;
        setId(SimplestValetApp.getTaskIdPool().getAvailableTaskId());
    }

    @Override
    public Result<Entry> doExecute() {
        int resultCode = mSheet != null ?  mSheet.addEntry(mEntry) : EntryActionResult.ADD_RESULT_PENDED;
        return generateResult(mEntry, resultCode);
    }
}
