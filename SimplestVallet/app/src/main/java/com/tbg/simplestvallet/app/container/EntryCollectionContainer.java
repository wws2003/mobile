package com.tbg.simplestvallet.app.container;

import com.tbg.simplestvallet.model.active.abstr.IEntrySheet;
import com.tbg.simplestvallet.model.active.abstr.IPendingEntryStore;

/**
 * Created by wws2003 on 10/24/15.
 */
public class EntryCollectionContainer {
    private IEntrySheet mEntrySheet;
    private IPendingEntryStore mPendingEntryStore;

    public void setEntrySheet(IEntrySheet entrySheet) {
        this.mEntrySheet = entrySheet;
    }

    public void setPendingEntryStore(IPendingEntryStore pendingEntryStore) {
        this.mPendingEntryStore = pendingEntryStore;
    }

    public IPendingEntryStore getPendingEntryStore() {
        return mPendingEntryStore;
    }

}
