package com.tbg.simplestvallet.ioc.taskmanager.task;

import com.tbg.simplestvallet.model.active.EntryActionResult;
import com.tbg.simplestvallet.model.active.abstr.ISVPendingEntryStore;
import com.tbg.simplestvallet.model.dto.SVEntry;
import com.tbg.taskmanager.abstr.task.ITaskResultProcessor;
import com.tbg.taskmanager.common.Result;

/**
 * Created by wws2003 on 10/29/15.
 */
public class AddEntryTaskResultProcessor implements ITaskResultProcessor<SVEntry> {

    private ISVPendingEntryStore mPendingEntryStore;

    public AddEntryTaskResultProcessor(ISVPendingEntryStore pendingEntryStore) {
        mPendingEntryStore = pendingEntryStore;
    }

    @Override
    public Result<SVEntry> processResult(Result<SVEntry> primitiveResult) {
        if(primitiveResult.getResultCode() == EntryActionResult.ADD_RESULT_PENDED) {
            mPendingEntryStore.addPendingEntry(primitiveResult.getElement());
        }
        //Just return the primitive result
        return primitiveResult;
    }
}
