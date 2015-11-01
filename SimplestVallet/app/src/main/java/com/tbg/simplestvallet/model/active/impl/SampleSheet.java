package com.tbg.simplestvallet.model.active.impl;

import com.tbg.simplestvallet.model.active.EntryActionResult;
import com.tbg.simplestvallet.model.active.abstr.IEntrySheet;
import com.tbg.simplestvallet.model.dto.Entry;
import com.tbg.simplestvallet.model.dto.MoneyQuantity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wws2003 on 10/24/15.
 */
public class SampleSheet implements IEntrySheet {

    private List<Entry> mEntries = new ArrayList<Entry>();
    private MoneyQuantity mCachedQuantity = new MoneyQuantity(0);

    @Override
    public int addEntry(Entry entry) {
        mEntries.add(entry);
        mCachedQuantity = mCachedQuantity.add(entry.getMoneyQuantity());

        //Return result for test purpose
        return entry.getMoneyQuantity().getAmount() >100 ? EntryActionResult.ADD_RESULT_PENDED : EntryActionResult.ADD_RESULT_OK;
    }

    @Override
    public int getAllEntries(List<Entry> entries) {
        entries.clear();
        entries.addAll(mEntries);
        return EntryActionResult.RETRIEVE_RESULT_OK;
    }

    @Override
    public MoneyQuantity getCachedAllEntriesAmount() {
        return mCachedQuantity;
    }

    @Override
    public MoneyQuantity getAllEntriesAmount() {
        MoneyQuantity quantity = new MoneyQuantity(0);
        for(Entry entry : mEntries) {
            quantity = quantity.add(entry.getMoneyQuantity());
        }
        return quantity;
    }
}
