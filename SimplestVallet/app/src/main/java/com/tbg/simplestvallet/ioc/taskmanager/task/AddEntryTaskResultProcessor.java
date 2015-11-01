package com.tbg.simplestvallet.ioc.taskmanager.task;

import com.tbg.simplestvallet.model.active.EntryActionResult;
import com.tbg.simplestvallet.model.active.abstr.IPendingEntryStore;
import com.tbg.simplestvallet.model.dto.Entry;
import com.tbg.taskmanager.abstr.task.ITaskResultProcessor;
import com.tbg.taskmanager.common.Result;

/**
 * Created by wws2003 on 10/29/15.
 */
public class AddEntryTaskResultProcessor implements ITaskResultProcessor<Entry> {

    private IPendingEntryStore mPendingEntryStore;

    public AddEntryTaskResultProcessor(IPendingEntryStore pendingEntryStore) {
        mPendingEntryStore = pendingEntryStore;
    }

    @Override
    public Result<Entry> processResult(Result<Entry> primitiveResult) {
        if(primitiveResult.getResultCode() == EntryActionResult.ADD_RESULT_PENDED) {
            mPendingEntryStore.addPendingEntry(primitiveResult.getElement());
        }
        //Just return the primitive result
        return primitiveResult;
    }
}
