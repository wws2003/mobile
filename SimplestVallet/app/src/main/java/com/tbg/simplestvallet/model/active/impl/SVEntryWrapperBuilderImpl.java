package com.tbg.simplestvallet.model.active.impl;

import com.tbg.simplestvallet.model.active.abstr.ISVEntryQueryWrapper;
import com.tbg.simplestvallet.model.active.abstr.ISVEntryWrapperBuilder;
import com.tbg.simplestvallet.model.active.abstr.ISVQuery;
import com.tbg.simplestvallet.model.dto.SVMoneyQuantity;

import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by wws2003 on 4/20/16.
 */
public class SVEntryWrapperBuilderImpl implements ISVEntryWrapperBuilder {

    private SVSimpleQueryImpl<Date> mCreateDateQuery;

    //These below 2 field are mergeable
    private SVSimpleQueryImpl<String> mNoteQuery;
    private SVSimpleQueryImpl<String> mTypeQuery;

    private SVSimpleQueryImpl<SVMoneyQuantity> mAmountQuery;

    public SVEntryWrapperBuilderImpl() {
        reset();
    }

    @Override
    public void setDateRanges(Date fromDate, Date toDate) {
        mCreateDateQuery = mCreateDateQuery.addMatchingRanges(ISVEntryQueryWrapper.FIELD_INDEX_CREATED_DATE, fromDate, toDate);
    }

    @Override
    public void setType(String type) {
        mTypeQuery.addMatchingValue(ISVEntryQueryWrapper.FIELD_INDEX_TYPE, type);
    }

    @Override
    public void setNote(String note) {
        mNoteQuery.addMatchingValue(ISVEntryQueryWrapper.FIELD_INDEX_NOTE, note);
    }

    @Override
    public void setMoneyQuantityRanges(SVMoneyQuantity lower, SVMoneyQuantity upper) {
        //Check if valid range
        if((lower != null && upper != null) && (lower.getCurrency() != upper.getCurrency())) {
            throw new RuntimeException("Invalid range of quantity (different currency)");
        }
        mAmountQuery.addMatchingRanges(ISVEntryQueryWrapper.FIELD_INDEX_AMOUNT, lower, upper);
    }

    @Override
    public void reset() {
        mCreateDateQuery = new SVSimpleQueryImpl<>();
        mNoteQuery = new SVSimpleQueryImpl<>();
        mTypeQuery = new SVSimpleQueryImpl<>();
        mAmountQuery = new SVSimpleQueryImpl<>();
    }

    @Override
    public ISVEntryQueryWrapper build() {
        ISVEntryQueryWrapper result = new SVEntryWrapperBuilderImpl.SVEntryQueryWrapper(mCreateDateQuery, mNoteQuery, mTypeQuery, mAmountQuery);
        reset();
        return result;
    }

    private static class SVEntryQueryWrapper implements ISVEntryQueryWrapper {

        private ISVQuery<Date> mCreateDateQuery;

        //These below 2 field are mergeable
        private ISVQuery<String> mNoteQuery;
        private ISVQuery<String> mTypeQuery;

        private ISVQuery<SVMoneyQuantity> mAmountQuery;

        public SVEntryQueryWrapper(ISVQuery<Date> dateQuery,
                                   ISVQuery<String> noteQuery,
                                   ISVQuery<String> typeQuery,
                                   ISVQuery<SVMoneyQuantity> amountQuery) {
            mCreateDateQuery = dateQuery;
            mNoteQuery = noteQuery;
            mTypeQuery = typeQuery;
            mAmountQuery = amountQuery;
        }

        public ISVQuery<Date> getCreateDateQuery() {
            return mCreateDateQuery;
        }

        public ISVQuery<String> getNoteQuery() {
            return mNoteQuery;
        }

        public ISVQuery<String> getTypeQuery() {
            return mTypeQuery;
        }

        public ISVQuery<SVMoneyQuantity> getAmountQuery() {
            return mAmountQuery;
        }
    }

}
