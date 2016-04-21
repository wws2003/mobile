package com.tbg.simplestvallet.model.active.abstr.collection;

import com.tbg.simplestvallet.model.dto.SVEntry;
import com.tbg.simplestvallet.model.dto.SVLocalEntry;

import java.util.List;

/**
 * Created by wws2003 on 10/29/15.
 */
public interface ISVPendingEntryStore {
    int addPendingEntry(SVEntry entry);
    int getAllPendingEntries(List<SVLocalEntry> localEntries);
    int deletePendingEntry(SVLocalEntry pendingEntry);
}
