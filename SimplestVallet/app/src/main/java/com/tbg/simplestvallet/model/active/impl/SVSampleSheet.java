package com.tbg.simplestvallet.model.active.impl;

import com.tbg.simplestvallet.model.active.EntryActionResult;
import com.tbg.simplestvallet.model.active.abstr.ISVEntryQueryWrapper;
import com.tbg.simplestvallet.model.active.abstr.ISVEntrySheet;
import com.tbg.simplestvallet.model.dto.SVEntry;
import com.tbg.simplestvallet.model.dto.SVMoneyQuantity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wws2003 on 10/24/15.
 */
public class SVSampleSheet implements ISVEntrySheet {

    private List<SVEntry> mEntries = new ArrayList<SVEntry>();
    private SVMoneyQuantity mCachedQuantity = new SVMoneyQuantity(0);

    @Override
    public void open() throws SVEntryOpenSheetException {
        //Do nothing
    }

    @Override
    public int addEntry(SVEntry entry) {
        mEntries.add(entry);
        mCachedQuantity = mCachedQuantity.add(entry.getMoneyQuantity());

        //Return result for test purpose
        return entry.getMoneyQuantity().getAmount() >100 ? EntryActionResult.ADD_RESULT_PENDED : EntryActionResult.ADD_RESULT_OK;
    }

    @Override
    public int getAllEntries(List<SVEntry> entries) {
        entries.clear();
        entries.addAll(mEntries);
        return EntryActionResult.RETRIEVE_RESULT_OK;
    }

    @Override
    public void queryEntries(ISVEntryQueryWrapper entryQueryWrapper, List<SVEntry> entries) {
        //TODO Implement
    }

    @Override
    public SVMoneyQuantity queryEntriesAmount(ISVEntryQueryWrapper entryQueryWrapper) {
        //TODO Implement
        return new SVMoneyQuantity(0);
    }

    @Override
    public SVMoneyQuantity getAllEntriesAmount() {
        SVMoneyQuantity quantity = new SVMoneyQuantity(0);
        for(SVEntry entry : mEntries) {
            quantity = quantity.add(entry.getMoneyQuantity());
        }
        return quantity;
    }
}
