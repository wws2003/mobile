package com.tbg.simplestvallet.model.active.impl;

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
import com.tbg.simplestvallet.model.active.abstr.IGoogleSpreadSheetRowAdapter;
import com.tbg.simplestvallet.model.active.abstr.IGoogleSpreadSheetRowFilter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wws2003 on 11/3/15.
 */
public class MyGoogleSpreadSheet<T> {
    private String mSpreadSheetURL;
    private SpreadsheetService mService = new SpreadsheetService("MySpreadsheetIntegration-v1");;
    private IGoogleSpreadSheetRowAdapter<T> mAdapter;

    public MyGoogleSpreadSheet(String spreadSheetId, String accessToken, IGoogleSpreadSheetRowAdapter<T> adapter) {
        this.mSpreadSheetURL = getFeedURLForSpreadSheet(spreadSheetId);
        this.mAdapter = adapter;

        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        mService.setOAuth2Credentials(credential);
    }

    public void insertItem(T item) throws MalformedURLException, ServiceException, IOException {
        SpreadsheetEntry spreadsheetEntry = null;
        List<WorksheetEntry> worksheets = null;

        //Get spreadsheet and its worksheets
        spreadsheetEntry = mService.getEntry(new URL(mSpreadSheetURL), SpreadsheetEntry.class);
        worksheets = spreadsheetEntry.getWorksheets();

        while (true) {
            //Use the last worksheet. This logic may be changed later
            WorksheetEntry worksheet = worksheets.get(worksheets.size() - 1);
            try {
                //Create a local representation of the new row.
                ListEntry row = mAdapter.getRowFromItem(item);

                //Insert the row
                mService.insert(worksheet.getListFeedUrl(), row);

                //If no problem, just return
                return;
            }
            catch (InvalidEntryException ie) {
                insertWorksheetHeader(worksheet);
            }
        }
    }

    public void getAllItems(List<T> items, IGoogleSpreadSheetRowFilter<T> filter) throws MalformedURLException, ServiceException, IOException {
        items.clear();
        SpreadsheetEntry spreadsheetEntry = mService.getEntry(new URL(mSpreadSheetURL), SpreadsheetEntry.class);
        List<WorksheetEntry> worksheets = spreadsheetEntry.getWorksheets();

        for(WorksheetEntry worksheet : worksheets) {
            URL worksheetListFeedURL = worksheet.getListFeedUrl();
            ListFeed listFeed = mService.getFeed(worksheetListFeedURL, ListFeed.class);

            for (ListEntry row : listFeed.getEntries()) {
                T item = mAdapter.getItemFromRow(row);
                if(filter == null || filter.filterRowItem(item)) {
                    items.add(item);
                }
            }
        }
    }

    public <T2> void getAllDerivedItems(List<T2> items,
                                       IGoogleSpreadSheetRowAdapter<T2> derivedAdapter,
                                       IGoogleSpreadSheetRowFilter<T> filter) throws MalformedURLException, ServiceException, IOException {
        items.clear();
        SpreadsheetEntry spreadsheetEntry = mService.getEntry(new URL(mSpreadSheetURL), SpreadsheetEntry.class);
        List<WorksheetEntry> worksheets = spreadsheetEntry.getWorksheets();

        for(WorksheetEntry worksheet : worksheets) {
            URL worksheetListFeedURL = worksheet.getListFeedUrl();
            ListFeed listFeed = mService.getFeed(worksheetListFeedURL, ListFeed.class);

            for (ListEntry row : listFeed.getEntries()) {
                T item = mAdapter.getItemFromRow(row);
                if(filter == null || filter.filterRowItem(item)) {
                    T2 derivedItem = derivedAdapter.getItemFromRow(row);
                    items.add(derivedItem);
                }
            }
        }
    }

    //TODO Add more methods

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
            mAdapter.getSheetHeaders(sheetHeaders);

            for(int i = 0; i < sheetHeaders.size(); i++) {
                CellEntry cellEntry = new CellEntry(1, i + 1, sheetHeaders.get(i));
                cellFeed.insert(cellEntry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
