package com.tbg.simplestvallet.model.active.impl.query;

import com.tbg.simplestvallet.model.active.abstr.query.ISVAcceptor;
import com.tbg.simplestvallet.model.active.abstr.query.ISVEntryQueryStringBuilder;
import com.tbg.simplestvallet.model.active.abstr.query.ISVEntryQueryStructureBuilder;
import com.tbg.simplestvallet.model.active.abstr.query.ISVQueryStructure;
import com.tbg.simplestvallet.model.dto.SVEntry;
import com.tbg.simplestvallet.model.dto.SVMoneyQuantity;

import java.util.Date;

/**
 * Created by wws2003 on 4/21/16.
 */
public class SVEntryQueryStructureBuilderImpl implements ISVEntryQueryStructureBuilder {

    private ISVQueryStructure mProduct;

    public SVEntryQueryStructureBuilderImpl() {
        reset();
    }

    @Override
    public ISVEntryQueryStructureBuilder newInstance() {
        return new SVEntryQueryStructureBuilderImpl();
    }

    @Override
    public ISVEntryQueryStructureBuilder and(ISVEntryQueryStructureBuilder lhs, ISVEntryQueryStructureBuilder rhs) {
        mProduct = new SVBinaryOperationQueryStructureImpl(SVBinaryOperationQueryStructureImpl.QUERY_OPERAND_AND, lhs.build(), rhs.build());
        return this;
    }

    @Override
    public ISVEntryQueryStructureBuilder or(ISVEntryQueryStructureBuilder lhs, ISVEntryQueryStructureBuilder rhs) {
        mProduct = new SVBinaryOperationQueryStructureImpl(SVBinaryOperationQueryStructureImpl.QUERY_OPERAND_OR, lhs.build(), rhs.build());
        return this;
    }

    @Override
    public ISVEntryQueryStructureBuilder setDateRanges(Date fromDate, Date toDate) {
        final SVSimpleQueryImpl<Date> dateQuery = new SVSimpleQueryImpl<>();
        dateQuery.addMatchingRanges(SVEntry.FIELD_INDEX_CREATED_DATE, fromDate, toDate);

        mProduct = new SVLeafQueryStructureImpl(new ISVAcceptor() {
            @Override
            public void accept(ISVEntryQueryStringBuilder queryBuilder) {
                queryBuilder.visitDateQuery(dateQuery);
            }
        });
        return this;
    }

    @Override
    public ISVEntryQueryStructureBuilder setType(String type) {
        final SVSimpleQueryImpl<String> typeQuery = new SVSimpleQueryImpl<>();
        typeQuery.addMatchingValue(SVEntry.FIELD_INDEX_TYPE, type);

        mProduct = new SVLeafQueryStructureImpl(new ISVAcceptor() {
            @Override
            public void accept(ISVEntryQueryStringBuilder queryBuilder) {
                queryBuilder.visitStringQuery(typeQuery);
            }
        });
        return this;
    }

    @Override
    public ISVEntryQueryStructureBuilder setNote(String note) {
        final SVSimpleQueryImpl<String> noteQuery = new SVSimpleQueryImpl<>();
        noteQuery.addMatchingValue(SVEntry.FIELD_INDEX_NOTE, note);

        mProduct = new SVLeafQueryStructureImpl(new ISVAcceptor() {
            @Override
            public void accept(ISVEntryQueryStringBuilder queryBuilder) {
                queryBuilder.visitStringQuery(noteQuery);
            }
        });
        return this;
    }

    @Override
    public ISVEntryQueryStructureBuilder setMoneyQuantityRanges(SVMoneyQuantity lower, SVMoneyQuantity upper) {
        final SVSimpleQueryImpl<SVMoneyQuantity> amountQuery = new SVSimpleQueryImpl<>();
        amountQuery.addMatchingRanges(SVEntry.FIELD_INDEX_AMOUNT, lower, upper);

        mProduct = new SVLeafQueryStructureImpl(new ISVAcceptor() {
            @Override
            public void accept(ISVEntryQueryStringBuilder queryBuilder) {
                queryBuilder.visitAmountQuery(amountQuery);
            }
        });
        return this;
    }

    @Override
    public void reset() {
        mProduct = null;
    }

    @Override
    public ISVQueryStructure build() {
        return mProduct;
    }
}
