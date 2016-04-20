package com.tbg.simplestvallet.model.active.abstr;

import com.tbg.simplestvallet.model.dto.SVEntry;
import com.tbg.simplestvallet.model.dto.SVMoneyQuantity;

import java.util.List;

/**
 * Created by wws2003 on 10/12/15.
 */
public interface ISVEntrySheet {
    //Try to open the sheet
    void open() throws SVEntryOpenSheetException;

    //MARK: For edit
    int addEntry(SVEntry entry);

    //MARK: For info retrieval
    int getAllEntries(List<SVEntry> entries);
    SVMoneyQuantity getAllEntriesAmount();

    void queryEntries(ISVEntryQueryWrapper entryQueryWrapper, List<SVEntry> entries);
    SVMoneyQuantity queryEntriesAmount(ISVEntryQueryWrapper entryQueryWrapper);

    //MARK: For some exceptions
    class SVEntryOpenSheetException extends Exception {
        private static final String MESSAGE = "Couldn't open sheet";

        public SVEntryOpenSheetException(Throwable throwable) {
            super(MESSAGE, throwable);
        }
    }
}
