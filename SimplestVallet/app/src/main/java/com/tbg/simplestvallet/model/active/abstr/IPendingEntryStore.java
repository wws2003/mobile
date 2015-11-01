package com.tbg.simplestvallet.model.active.abstr;

import com.tbg.simplestvallet.model.dto.Entry;
import com.tbg.simplestvallet.model.dto.LocalEntry;

import java.util.List;

/**
 * Created by wws2003 on 10/29/15.
 */
public interface IPendingEntryStore {
    int addPendingEntry(Entry entry);
    int getAllPendingEntries(List<LocalEntry> localEntries);
    int deletePendingEntry(LocalEntry pendingEntry);
}
