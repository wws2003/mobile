package com.tbg.simplestvallet.model.active.impl.query.google;

import com.tbg.simplestvallet.model.active.abstr.query.ISVEntryQueryStringBuilder;
import com.tbg.simplestvallet.model.active.abstr.query.ISVQuery;
import com.tbg.simplestvallet.model.active.impl.query.SVBinaryOperationQueryStructureImpl;
import com.tbg.simplestvallet.model.dto.SVEntry;
import com.tbg.simplestvallet.model.dto.SVMoneyQuantity;
import com.tbg.simplestvallet.util.DateUtil;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wws2003 on 4/21/16.
 */
public class SVGoogleEntryStringQueryBuilderImpl implements ISVEntryQueryStringBuilder {

    private static final String GOOGLE_DATE_QUERY_KEY = "date";
    private static final String GOOGLE_AMOUNT_QUERY_KEY = "amount";
    private static final String GOOGLE_TYPE_QUERY_KEY = "type";
    private static final String GOOGLE_NOTE_QUERY_KEY = "note";

    private static final String GOOGLE_QUERY_AND = " and ";
    private static final String GOOGLE_QUERY_OR = " or ";

    private StringBuilder mQueryStringBuilder;

    public SVGoogleEntryStringQueryBuilderImpl() {
        mQueryStringBuilder = new StringBuilder();
    }

    @Override
    public void visitOperand(int operand) {
        switch (operand) {
            case SVBinaryOperationQueryStructureImpl.QUERY_OPERAND_AND:
                mQueryStringBuilder.append(GOOGLE_QUERY_AND);
                break;
            case SVBinaryOperationQueryStructureImpl.QUERY_OPERAND_OR:
                mQueryStringBuilder.append(GOOGLE_QUERY_OR);
                break;
            default:
                break;
        }
    }

    @Override
    public void visitDateQuery(ISVQuery<Date> dateQuery) {
        List<ISVQuery.Range<Date>> dates = new LinkedList<>();
        dateQuery.getMatchingRanges(SVEntry.FIELD_INDEX_CREATED_DATE, dates);

        //Currently only accept one range
        if (dates.isEmpty() || dates.size() != 1) {
            return;
        }

        Date fromDate = dates.get(0).lower();
        Date toDate = dates.get(0).upper();

        int lowerDateNumeric = (fromDate == null) ? 0 : DateUtil.getDateNumericValueForGoogleSpreadSheet(fromDate);
        int upperDateNumeric = (toDate == null) ? Integer.MAX_VALUE : DateUtil.getDateNumericValueForGoogleSpreadSheet(toDate);

        mQueryStringBuilder.append(GOOGLE_DATE_QUERY_KEY)
                .append(" >= ")
                .append(lowerDateNumeric)
                .append(" and ")
                .append(GOOGLE_DATE_QUERY_KEY)
                .append(" <= ")
                .append(upperDateNumeric);
    }

    @Override
    public void visitStringQuery(ISVQuery<String> stringQuery) {
        //TODO Implement
    }

    @Override
    public void visitAmountQuery(ISVQuery<SVMoneyQuantity> amountQuery) {
        //TODO Implement
    }

    @Override
    public String build() {
        mQueryStringBuilder = mQueryStringBuilder.insert(0, '(');
        mQueryStringBuilder.append(')');
        return mQueryStringBuilder.toString();
    }

    @Override
    public void reset() {
        mQueryStringBuilder.setLength(0);
    }
}
