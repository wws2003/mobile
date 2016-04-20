package com.tbg.simplestvallet.app.container;

import com.tbg.simplestvallet.model.active.abstr.ISVEntryWrapperBuilder;

/**
 * Created by wws2003 on 4/20/16.
 */
public class SVEntryQueryBuilderContainer {
    private ISVEntryWrapperBuilder mEntryWrapperBuilder;

    public SVEntryQueryBuilderContainer(ISVEntryWrapperBuilder entryWrapperBuilder) {
        this.mEntryWrapperBuilder = entryWrapperBuilder;
    }

    public ISVEntryWrapperBuilder getEntryWrapperBuilder() {
        return mEntryWrapperBuilder;
    }
}
