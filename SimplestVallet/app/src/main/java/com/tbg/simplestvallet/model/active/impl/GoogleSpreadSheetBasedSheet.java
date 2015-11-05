package com.tbg.simplestvallet.model.active.impl;

import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.util.ServiceException;
import com.tbg.simplestvallet.model.active.EntryActionResult;
import com.tbg.simplestvallet.model.active.abstr.IEntrySheet;
import com.tbg.simplestvallet.model.active.abstr.IGoogleSpreadSheetRowAdapter;
import com.tbg.simplestvallet.model.active.abstr.IGoogleSpreadSheetRowFilter;
import com.tbg.simplestvallet.model.dto.Entry;
import com.tbg.simplestvallet.model.dto.MoneyQuantity;
import com.tbg.simplestvallet.util.DateUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wws2003 on 11/2/15.
 */

//TODO More generic solution !!!

public class GoogleSpreadSheetBasedSheet implements IEntrySheet {

    private MyGoogleSpreadSheet<Entry> mGoogleSpreadSheet;

    private static final String DATE_HEADER = "Date";
    private static final String AMOUNT_HEADER = "Amount";
    private static final String TYPE_HEADER = "Type";
    private static final String NOTE_HEADER = "Note";

    public GoogleSpreadSheetBasedSheet(String spreadSheetId, String accessToken) {
        mGoogleSpreadSheet = new MyGoogleSpreadSheet<>(spreadSheetId, accessToken, new GoogleSpreadSheetRowEntryAdapter());
    }

    @Override
    public int addEntry(Entry entry) {
        //Get spreadsheet and its worksheets
        try {
            mGoogleSpreadSheet.insertItem(entry);
            return EntryActionResult.ADD_RESULT_OK;
        } catch (MalformedURLException me) {
            me.printStackTrace();
            return EntryActionResult.ADD_RESULT_INVALID;
        } catch (ServiceException e) {
            e.printStackTrace();
            return EntryActionResult.ADD_RESULT_INVALID;
        } catch (IOException ie) {
            ie.printStackTrace();
            return EntryActionResult.ADD_RESULT_PENDED;
        }
    }

    @Override
    public int getAllEntries(List<Entry> entries) {
        try {
            mGoogleSpreadSheet.getAllItems(entries, null);
            return EntryActionResult.RETRIEVE_RESULT_OK;
        } catch (IOException e) {
            e.printStackTrace();
            entries.clear();
            return EntryActionResult.RETRIEVE_RESULT_NOT_FOUND;
        } catch (ServiceException e) {
            e.printStackTrace();
            entries.clear();
            return EntryActionResult.RETRIEVE_RESULT_NOT_FOUND;
        }
    }

    @Override
    public MoneyQuantity getAllEntriesAmount() {
        //TODO Implement more efficiently
        GoogleSpreadSheetRowMoneyQuantityAdapter moneyQuantityAdapter = new GoogleSpreadSheetRowMoneyQuantityAdapter();
        try {
            mGoogleSpreadSheet.getAllDerivedItems(new ArrayList<MoneyQuantity>(),
                    moneyQuantityAdapter,
                    null);
            //This return statement is just a trick to take advantage of side-effect. Officially required to iterate the list retrieved to collect results
            return moneyQuantityAdapter.getMoneyQuantity();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new MoneyQuantity(0);
    }

    @Override
    public MoneyQuantity getCachedAllEntriesAmount() {
        //TODO Implement more efficiently
        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);

        GoogleSpreadSheetRowMoneyQuantityAdapter moneyQuantityAdapter = new GoogleSpreadSheetRowMoneyQuantityAdapter();
        IGoogleSpreadSheetRowFilter<Entry> googleSpreadSheetRowMonthAdapter = new GoogleSpreadSheetRowFilterByMonth(thisYear, thisMonth);
        try {
            mGoogleSpreadSheet.getAllDerivedItems(new ArrayList<MoneyQuantity>(),
                    moneyQuantityAdapter,
                    googleSpreadSheetRowMonthAdapter);
            //This return statement is just a trick to take advantage of side-effect. Officially required to iterate the list retrieved to collect results
            return moneyQuantityAdapter.getMoneyQuantity();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new MoneyQuantity(0);
    }

    private class GoogleSpreadSheetRowEntryAdapter implements IGoogleSpreadSheetRowAdapter<Entry> {
        @Override
        public Entry getItemFromRow(ListEntry row) {
            return new Entry(DateUtil.getDateFromString(row.getCustomElements().getValue(DATE_HEADER), "dd/MM/yyyy"),
                    new MoneyQuantity(Double.valueOf(row.getCustomElements().getValue(AMOUNT_HEADER))),
                    row.getCustomElements().getValue(TYPE_HEADER),
                    row.getCustomElements().getValue(NOTE_HEADER));
        }

        @Override
        public ListEntry getRowFromItem(Entry entry) {
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
            sheetHeaders.add(DATE_HEADER);
            sheetHeaders.add(TYPE_HEADER);
            sheetHeaders.add(AMOUNT_HEADER);
            sheetHeaders.add(NOTE_HEADER);
        }
    }

    private class GoogleSpreadSheetRowMoneyQuantityAdapter implements IGoogleSpreadSheetRowAdapter<MoneyQuantity> {

        private GoogleSpreadSheetRowEntryAdapter mEntryAdapter = new GoogleSpreadSheetRowEntryAdapter();
        private MoneyQuantity mQuantity = null;

        @Override
        public MoneyQuantity getItemFromRow(ListEntry row) {
            if(mQuantity == null) {
                mQuantity = mEntryAdapter.getItemFromRow(row).getMoneyQuantity();
            }
            else {
                mQuantity = mQuantity.add(mEntryAdapter.getItemFromRow(row).getMoneyQuantity());
            }
            return mQuantity;
        }

        @Override
        public ListEntry getRowFromItem(MoneyQuantity item) {
            return null;
        }

        @Override
        public void getSheetHeaders(List<String> headers) {
            //Do nothing
        }

        public MoneyQuantity getMoneyQuantity() {
            return (mQuantity != null) ? mQuantity : new MoneyQuantity(0);
        }
    }

    private class GoogleSpreadSheetRowFilterByMonth implements IGoogleSpreadSheetRowFilter<Entry> {
        private int mYear;
        private int mMonth;

        public GoogleSpreadSheetRowFilterByMonth(int year, int month) {
            mYear = year;
            mMonth = month;
        }

        @Override
        public boolean filterRowItem(Entry item) {
            Date entryCreatedAt = item.getCreatedAt();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(entryCreatedAt);
            return calendar.get(Calendar.YEAR) == mYear && calendar.get(Calendar.MONTH) == mMonth;
        }
    }
}