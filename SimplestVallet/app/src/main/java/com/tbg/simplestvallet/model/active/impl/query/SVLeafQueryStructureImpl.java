package com.tbg.simplestvallet.model.active.impl.query;

import com.tbg.simplestvallet.model.active.abstr.query.ISVAcceptor;
import com.tbg.simplestvallet.model.active.abstr.query.ISVEntryQueryStringBuilder;
import com.tbg.simplestvallet.model.active.abstr.query.ISVQueryStructure;

import java.util.List;

/**
 * Created by wws2003 on 4/21/16.
 */
public class SVLeafQueryStructureImpl implements ISVQueryStructure {
    private ISVAcceptor mLeafAcceptor;

    SVLeafQueryStructureImpl(ISVAcceptor leafAcceptor) {
        mLeafAcceptor = leafAcceptor;
    }

    @Override
    public void accept(ISVEntryQueryStringBuilder queryBuilder) {
        mLeafAcceptor.accept(queryBuilder);
    }

    @Override
    public List<ISVQueryStructure> getChildren() {
        return null;
    }
}
