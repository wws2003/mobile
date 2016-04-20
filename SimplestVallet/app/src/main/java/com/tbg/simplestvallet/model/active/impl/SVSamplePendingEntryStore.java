package com.tbg.simplestvallet.model.active.impl;

import android.util.SparseArray;

import com.tbg.simplestvallet.model.active.EntryActionResult;
import com.tbg.simplestvallet.model.active.abstr.ISVPendingEntryStore;
import com.tbg.simplestvallet.model.dto.SVEntry;
import com.tbg.simplestvallet.model.dto.SVLocalEntry;

import java.util.List;

/**
 * Created by wws2003 on 10/29/15.
 */
public class SVSamplePendingEntryStore implements ISVPendingEntryStore {

    private SparseArray<SVLocalEntry> mLocalEntriesMap = new SparseArray<SVLocalEntry>();

    @Override
    public int addPendingEntry(SVEntry entry) {
        int newEntryId = getAvailableLocalEntryId();
        SVLocalEntry localEntry = new SVLocalEntry(newEntryId, entry);
        mLocalEntriesMap.append(newEntryId, localEntry);
        return EntryActionResult.ADD_RESULT_OK;
    }

    @Override
    public int getAllPendingEntries(List<SVLocalEntry> localEntries) {
        int nbEntries = mLocalEntriesMap.size();
        for(int i = 0; i < nbEntries; i++) {
            SVLocalEntry entry = mLocalEntriesMap.valueAt(i);
            localEntries.add(entry);
        }
        return EntryActionResult.RETRIEVE_RESULT_OK;
    }

    @Override
    public int deletePendingEntry(SVLocalEntry pendingEntry) {
        SVLocalEntry localEntry = mLocalEntriesMap.get(pendingEntry.getId());
        if(localEntry != null) {
            mLocalEntriesMap.remove(localEntry.getId());
            return EntryActionResult.DELETE_RESULT_OK;
        }
        else {
            return EntryActionResult.DELETE_RESULT_NOT_FOUND;
        }
    }

    private int getAvailableLocalEntryId() {
        int  mapSize = mLocalEntriesMap.size();
        int key = 0;
        for(; key < mapSize; key++) {
            if(mLocalEntriesMap.get(key) == null) {
                return key;
            }
        }
        return key;
    }

}
