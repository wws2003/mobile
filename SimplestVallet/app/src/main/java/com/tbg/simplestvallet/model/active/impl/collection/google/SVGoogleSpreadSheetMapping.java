package com.tbg.simplestvallet.model.active.impl.collection.google;

import com.google.gdata.data.spreadsheet.ListEntry;
import com.tbg.simplestvallet.model.active.abstr.collection.google.ISVGoogleSpreadSheetRowAdapter;
import com.tbg.simplestvallet.model.dto.SVEntry;
import com.tbg.simplestvallet.model.dto.SVMoneyQuantity;
import com.tbg.simplestvallet.util.DateUtil;

import java.util.List;

/**
 * Created by wws2003 on 4/20/16.
 */
public class SVGoogleSpreadSheetMapping {

    public static final String DATE_HEADER = "Date";
    public static final String AMOUNT_HEADER = "Amount";
    public static final String TYPE_HEADER = "Type";
    public static final String NOTE_HEADER = "Note";

    //MARK: Inner classes
    public static class GoogleSpreadSheetRowEntryAdapter implements ISVGoogleSpreadSheetRowAdapter<SVEntry> {
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

    public static class GoogleSpreadSheetRowMoneyQuantityAdapter implements ISVGoogleSpreadSheetRowAdapter<SVMoneyQuantity> {

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
