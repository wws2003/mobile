package com.tbg.simplestvallet.app.container;

import com.tbg.simplestvallet.model.active.abstr.query.ISVEntryQueryStructureBuilder;

/**
 * Created by wws2003 on 4/20/16.
 */
public class SVEntryQueryBuilderContainer {
    private ISVEntryQueryStructureBuilder mEntryStructureBuilder;

    public SVEntryQueryBuilderContainer(ISVEntryQueryStructureBuilder entryStructureBuilder) {
        this.mEntryStructureBuilder = entryStructureBuilder;
    }

    public ISVEntryQueryStructureBuilder getEntryQueryStructureBuilder() {
        return mEntryStructureBuilder;
    }
}
