package com.tbg.simplestvallet.model.active.impl.google;

import com.google.gdata.data.spreadsheet.ListEntry;
import com.tbg.simplestvallet.model.active.abstr.IGoogleSpreadSheetRowAdapter;
import com.tbg.simplestvallet.model.active.abstr.ISVEntryQueryWrapper;
import com.tbg.simplestvallet.model.active.abstr.ISVQuery;
import com.tbg.simplestvallet.model.dto.SVEntry;
import com.tbg.simplestvallet.model.dto.SVMoneyQuantity;
import com.tbg.simplestvallet.util.DateUtil;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wws2003 on 4/20/16.
 */
public class SVGoogleSpreadSheetMapping {

    public static final String DATE_HEADER = "Date";
    public static final String AMOUNT_HEADER = "Amount";
    public static final String TYPE_HEADER = "Type";
    public static final String NOTE_HEADER = "Note";

    private static final String DATE_QUERY_KEY = "date";
    private static final String AMOUNT_QUERY_KEY = "amount";
    private static final String TYPE_QUERY_KEY = "type";
    private static final String NOTE__QUERY_KEY = "note";

    public String createEntryQuery(ISVEntryQueryWrapper entryQueryWrapper) {
        StringBuilder queryBuilder = new StringBuilder();
        buildDateQuery(queryBuilder, entryQueryWrapper.getCreateDateQuery());
        return queryBuilder.toString();
    }

    private void buildDateQuery(StringBuilder queryBuilder, ISVQuery<Date> dateQuery) {
        if (dateQuery == null || dateQuery.empty()) {
            return;
        }

        List<ISVQuery.Range<Date>> dates = new LinkedList<>();
        dateQuery.getMatchingRanges(ISVEntryQueryWrapper.FIELD_INDEX_CREATED_DATE, dates);

        //Currently only accept one range
        if (dates.isEmpty() || dates.size() != 1) {
            return;
        }

        Date fromDate = dates.get(0).lower();
        Date toDate = dates.get(0).upper();

        int lowerDateNumeric = (fromDate == null) ? 0 : DateUtil.getDateNumericValueForGoogleSpreadSheet(fromDate);
        int upperDateNumeric = (toDate == null) ? Integer.MAX_VALUE : DateUtil.getDateNumericValueForGoogleSpreadSheet(toDate);

        queryBuilder.append(" ")
                .append(DATE_QUERY_KEY)
                .append(" >= ")
                .append(lowerDateNumeric)
                .append(" and ")
                .append(DATE_QUERY_KEY)
                .append(" <= ")
                .append(upperDateNumeric);
    }

    private void buildStringQuery(StringBuilder queryBuilder, ISVQuery<String> dateQuery) {
        //TODO Implement
    }


    //MARK: Inner classes
    public static class GoogleSpreadSheetRowEntryAdapter implements IGoogleSpreadSheetRowAdapter<SVEntry> {
        @Override
        public SVEntry getItemFromRow(ListEntry row) {
            return new SVEntry(DateUtil.getDateFromString(row.getCustomElements().getValue(DATE_HEADER), "dd/MM/yyyy"),
                    new SVMoneyQuantity(Double.valueOf(row.getCustomElements().getValue(AMOUNT_HEADER))),
                    row.getCustomElements().getValue(TYPE_HEADER),
                    row.getCustomElements().getValue(NOTE_HEADER));
        }

        @Override
        public ListEntry getRowFromItem(SVEntry entry) {
            ListEntry row = new ListEntry();
            row.getCustomElements().setValueLocal(DATE_HEADER, DateUtil.getYMDString(entry.getCreatedAt()));
            row.getCustomElements().setValueLocal(TYPE_HEADER, entry.getType());
            row.getCustomElements().setValueLocal(AMOUNT_HEADER, String.valueOf((int) entry.getMoneyQuantity().getAmount()));
            row.getCustomElements().setValueLocal(NOTE_HEADER, entry.getNote());
            return row;
        }

        @Override
        public void getSheetHeaders(List<String> sheetHeaders) {
            sheetHeaders.clear();
            sheetHeaders.add(SVGoogleSpreadSheetMapping.DATE_HEADER);
            sheetHeaders.add(SVGoogleSpreadSheetMapping.TYPE_HEADER);
            sheetHeaders.add(SVGoogleSpreadSheetMapping.AMOUNT_HEADER);
            sheetHeaders.add(SVGoogleSpreadSheetMapping.NOTE_HEADER);
        }
    }

    public static class GoogleSpreadSheetRowMoneyQuantityAdapter implements IGoogleSpreadSheetRowAdapter<SVMoneyQuantity> {

        private GoogleSpreadSheetRowEntryAdapter mEntryAdapter = new GoogleSpreadSheetRowEntryAdapter();
        private SVMoneyQuantity mQuantity = null;

        @Override
        public SVMoneyQuantity getItemFromRow(ListEntry row) {
            if(mQuantity == null) {
                mQuantity = mEntryAdapter.getItemFromRow(row).getMoneyQuantity();
            }
            else {
                mQuantity = mQuantity.add(mEntryAdapter.getItemFromRow(row).getMoneyQuantity());
            }
            return mQuantity;
        }

        @Override
        public ListEntry getRowFromItem(SVMoneyQuantity item) {
            return null;
        }

        @Override
        public void getSheetHeaders(List<String> headers) {
            //Do nothing
        }

        public SVMoneyQuantity getMoneyQuantity() {
            return (mQuantity != null) ? mQuantity : new SVMoneyQuantity(0);
        }
    }

}
