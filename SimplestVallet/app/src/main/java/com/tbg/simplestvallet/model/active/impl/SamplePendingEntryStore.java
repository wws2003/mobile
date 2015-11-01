package com.tbg.simplestvallet.model.active.impl;

import android.util.SparseArray;
import android.util.SparseLongArray;

import com.tbg.simplestvallet.model.active.EntryActionResult;
import com.tbg.simplestvallet.model.active.abstr.IPendingEntryStore;
import com.tbg.simplestvallet.model.dto.Entry;
import com.tbg.simplestvallet.model.dto.LocalEntry;

import java.util.List;

/**
 * Created by wws2003 on 10/29/15.
 */
public class SamplePendingEntryStore implements IPendingEntryStore {

    private SparseArray<LocalEntry> mLocalEntriesMap = new SparseArray<LocalEntry>();

    @Override
    public int addPendingEntry(Entry entry) {
        int newEntryId = getAvailableLocalEntryId();
        LocalEntry localEntry = new LocalEntry(newEntryId, entry);
        mLocalEntriesMap.append(newEntryId, localEntry);
        return EntryActionResult.ADD_RESULT_OK;
    }

    @Override
    public int getAllPendingEntries(List<LocalEntry> localEntries) {
        int nbEntries = mLocalEntriesMap.size();
        for(int i = 0; i < nbEntries; i++) {
            LocalEntry entry = mLocalEntriesMap.valueAt(i);
            localEntries.add(entry);
        }
        return EntryActionResult.RETRIEVE_RESULT_OK;
    }

    @Override
    public int deletePendingEntry(LocalEntry pendingEntry) {
        LocalEntry localEntry = mLocalEntriesMap.get(pendingEntry.getId());
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
