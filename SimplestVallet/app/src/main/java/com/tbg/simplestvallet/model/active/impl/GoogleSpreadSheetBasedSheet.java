package com.tbg.simplestvallet.model.active.impl;

import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.InvalidEntryException;
import com.google.gdata.util.ServiceException;
import com.tbg.simplestvallet.model.active.EntryActionResult;
import com.tbg.simplestvallet.model.active.abstr.IEntrySheet;
import com.tbg.simplestvallet.model.dto.Entry;
import com.tbg.simplestvallet.model.dto.MoneyQuantity;
import com.tbg.simplestvallet.util.DateUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wws2003 on 11/2/15.
 */

//TODO More generic solution !!!

public class GoogleSpreadSheetBasedSheet implements IEntrySheet {

    private String mSpreadSheetURL;
    private SpreadsheetService mService = new SpreadsheetService("MySpreadsheetIntegration-v1");;

    private static final String DATE_HEADER = "Date";
    private static final String AMOUNT_HEADER = "Amount";
    private static final String TYPE_HEADER = "Type";
    private static final String NOTE_HEADER = "Note";

    public GoogleSpreadSheetBasedSheet(String spreadSheetId, String accessToken) {
        this.mSpreadSheetURL = getFeedURLForSpreadSheet(spreadSheetId);

        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        mService.setOAuth2Credentials(credential);
    }

    @Override
    public int addEntry(Entry entry) {
        SpreadsheetEntry spreadsheetEntry = null;
        List<WorksheetEntry> worksheets = null;

        //Get spreadsheet and its worksheets
        try {
            spreadsheetEntry = mService.getEntry(new URL(mSpreadSheetURL), SpreadsheetEntry.class);
            worksheets = spreadsheetEntry.getWorksheets();
        }
        catch (MalformedURLException me) {
            me.printStackTrace();
            return EntryActionResult.ADD_RESULT_INVALID;
        }
        catch (ServiceException e) {
            e.printStackTrace();
            return EntryActionResult.ADD_RESULT_INVALID;
        }
        catch (IOException ie) {
            ie.printStackTrace();
            return EntryActionResult.ADD_RESULT_PENDED;
        }

        while (true) {
            //Find the last worksheet. This logic may be changed later
            WorksheetEntry worksheet = worksheets.get(worksheets.size() - 1);

            try {
                //Create a local representation of the new row.
                ListEntry row = getRowFromEntry(entry);

                //Insert the row
                mService.insert(worksheet.getListFeedUrl(), row);

                return EntryActionResult.ADD_RESULT_OK;
            }
            catch (InvalidEntryException ie) {
                insertWorksheetHeader(worksheet);
            }
            catch (Exception ioe) {
                return EntryActionResult.ADD_RESULT_INVALID;
            }
        }
    }

    @Override
    public int getAllEntries(List<Entry> entries) {
        entries.clear();

        try {
            SpreadsheetEntry spreadsheetEntry = mService.getEntry(new URL(mSpreadSheetURL), SpreadsheetEntry.class);
            List<WorksheetEntry> worksheets = spreadsheetEntry.getWorksheets();

            for(WorksheetEntry worksheet : worksheets) {
                URL worksheetListFeedURL = worksheet.getListFeedUrl();
                ListFeed listFeed = mService.getFeed(worksheetListFeedURL, ListFeed.class);

                for (ListEntry row : listFeed.getEntries()) {
                    Entry entry = getEntryFromRow(row);
                    entries.add(entry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            entries.clear();
            return EntryActionResult.RETRIEVE_RESULT_NOT_FOUND;
        } catch (ServiceException e) {
            e.printStackTrace();
            entries.clear();
            return EntryActionResult.RETRIEVE_RESULT_NOT_FOUND;
        }
        return EntryActionResult.RETRIEVE_RESULT_OK;
    }

    @Override
    public MoneyQuantity getAllEntriesAmount() {
        //TODO Implement more efficiently
        MoneyQuantity quantity = new MoneyQuantity(0, null);
        try {
            SpreadsheetEntry spreadsheetEntry = mService.getEntry(new URL(mSpreadSheetURL), SpreadsheetEntry.class);
            List<WorksheetEntry> worksheets = spreadsheetEntry.getWorksheets();

            for(WorksheetEntry worksheet : worksheets) {
                URL worksheetListFeedURL = worksheet.getListFeedUrl();
                ListFeed listFeed = mService.getFeed(worksheetListFeedURL, ListFeed.class);

                for (ListEntry row : listFeed.getEntries()) {
                    Entry entry = getEntryFromRow(row);
                    quantity = quantity.add(entry.getMoneyQuantity());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ServiceException e) {
            e.printStackTrace();
            return null;
        }
        return quantity;
    }

    @Override
    public MoneyQuantity getCachedAllEntriesAmount() {
        //TODO Implement
        return new MoneyQuantity(0);
    }

    private String getFeedURLForSpreadSheet(String spreadSheetId) {
        return "https://spreadsheets.google.com/feeds/spreadsheets/" + spreadSheetId;
    }

    //Try to set header for worksheet before attempt inserting row again
    private void insertWorksheetHeader(WorksheetEntry worksheetEntry) {
        //Set header row for sheet
        URL cellFeedUrl = worksheetEntry.getCellFeedUrl();
        try {
            CellFeed cellFeed = mService.getFeed(cellFeedUrl, CellFeed.class);
            List<String> sheetHeaders = new ArrayList<>();
            getSheetHeaders(sheetHeaders);

            for(int i = 0; i < sheetHeaders.size(); i++) {
                CellEntry cellEntry = new CellEntry(1, i + 1, sheetHeaders.get(i));
                cellFeed.insert(cellEntry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Should delegate the following 3 methods for genericalness ?
    private void getSheetHeaders(List<String> sheetHeaders) {
        sheetHeaders.clear();
        sheetHeaders.add(DATE_HEADER);
        sheetHeaders.add(TYPE_HEADER);
        sheetHeaders.add(AMOUNT_HEADER);
        sheetHeaders.add(NOTE_HEADER);
    }

    private ListEntry getRowFromEntry(Entry entry) {
        ListEntry row = new ListEntry();
        row.getCustomElements().setValueLocal(DATE_HEADER, DateUtil.getYMDString(entry.getCreatedAt()));
        row.getCustomElements().setValueLocal(TYPE_HEADER, entry.getType());
        row.getCustomElements().setValueLocal(AMOUNT_HEADER, String.valueOf((int)entry.getMoneyQuantity().getAmount()));
        row.getCustomElements().setValueLocal(NOTE_HEADER, entry.getNote());
        return row;
    }

    private Entry getEntryFromRow(ListEntry row) {

        return new Entry(DateUtil.getDateFromString(row.getCustomElements().getValue(DATE_HEADER), "dd/MM/yyyy"),
                new MoneyQuantity(Double.valueOf(row.getCustomElements().getValue(AMOUNT_HEADER))),
                row.getCustomElements().getValue(TYPE_HEADER),
                row.getCustomElements().getValue(NOTE_HEADER));
    }
}
