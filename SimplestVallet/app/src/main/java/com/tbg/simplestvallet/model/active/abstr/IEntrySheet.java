package com.tbg.simplestvallet.model.active.abstr;

import com.tbg.simplestvallet.model.dto.Entry;
import com.tbg.simplestvallet.model.dto.MoneyQuantity;

import java.util.List;

/**
 * Created by wws2003 on 10/12/15.
 */
public interface IEntrySheet {
    int addEntry(Entry entry);
    int getAllEntries(List<Entry> entries);

    MoneyQuantity getAllEntriesAmount();

    //Light-weight method, can be called in main thread
    MoneyQuantity getCachedAllEntriesAmount();
}
