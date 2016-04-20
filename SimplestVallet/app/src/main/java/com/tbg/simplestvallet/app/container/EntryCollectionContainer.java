package com.tbg.simplestvallet.app.container;

import com.tbg.simplestvallet.model.active.abstr.ISVEntrySheet;
import com.tbg.simplestvallet.model.active.abstr.ISVPendingEntryStore;

/**
 * Created by wws2003 on 10/24/15.
 */
public class EntryCollectionContainer {
    private ISVEntrySheet mEntrySheet;
    private ISVPendingEntryStore mPendingEntryStore;

    public void setEntrySheet(ISVEntrySheet entrySheet) {
        this.mEntrySheet = entrySheet;
    }

    public void setPendingEntryStore(ISVPendingEntryStore pendingEntryStore) {
        this.mPendingEntryStore = pendingEntryStore;
    }

    public ISVPendingEntryStore getPendingEntryStore() {
        return mPendingEntryStore;
    }

}
