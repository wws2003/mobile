package com.tbg.simplestvallet.model.active.impl.collection.google;

import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gdata.client.spreadsheet.ListQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.InvalidEntryException;
import com.google.gdata.util.ServiceException;
import com.tbg.simplestvallet.model.active.abstr.collection.google.ISVGoogleSpreadSheetRowAdapter;
import com.tbg.simplestvallet.model.active.abstr.collection.google.ISVGoogleSpreadSheetRowFilter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wws2003 on 11/3/15.
 */
public class SVGoogleSpreadSheet<T> {
    private String mSpreadSheetURL;
    private SpreadsheetService mService = new SpreadsheetService("MySpreadsheetIntegration-v1");;
    private ISVGoogleSpreadSheetRowAdapter<T> mAdapter;

    public SVGoogleSpreadSheet(String spreadSheetId, String accessToken, ISVGoogleSpreadSheetRowAdapter<T> adapter) {
        this.mSpreadSheetURL = getFeedURLForSpreadSheet(spreadSheetId);
        this.mAdapter = adapter;

        //Set expiration for one week
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken).setExpiresInSeconds((long)(60 * 60 * 24 * 7));

        mService.setOAuth2Credentials(credential);
    }

    public void open() throws IOException, ServiceException {
        //Test access to the sheet. Probably use the retrieved result for future purpose
        mService.getEntry(new URL(mSpreadSheetURL), SpreadsheetEntry.class);
    }

    public void insertItem(T item) throws ServiceException, IOException {
        SpreadsheetEntry spreadsheetEntry;
        List<WorksheetEntry> worksheets;

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

    public void getAllItems(ISVGoogleSpreadSheetRowFilter<T> filter, List<T> items) throws ServiceException, IOException {
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

    public <T2> void getAllDerivedItems(ISVGoogleSpreadSheetRowAdapter<T2> derivedAdapter,
                                        ISVGoogleSpreadSheetRowFilter<T> filter,
                                        List<T2> items) throws ServiceException, IOException {
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

    public <T2> void queryDerivedItems(String structuredQuery,
                                       ISVGoogleSpreadSheetRowAdapter<T2> derivedAdapter,
                                       ISVGoogleSpreadSheetRowFilter<T> filter,
                                       List<T2> items) throws ServiceException, IOException {
        items.clear();
        SpreadsheetEntry spreadsheetEntry = mService.getEntry(new URL(mSpreadSheetURL), SpreadsheetEntry.class);
        List<WorksheetEntry> worksheets = spreadsheetEntry.getWorksheets();

        for(WorksheetEntry worksheet : worksheets) {
            URL worksheetListFeedURL = worksheet.getListFeedUrl();

            ListQuery query = new ListQuery(worksheetListFeedURL);
            query.setSpreadsheetQuery(structuredQuery);

            Log.d("---Query feed url", query.getFeedUrl().toString());
            Log.d("---------Query sq", query.getSpreadsheetQuery().toString());

            ListFeed listFeed = mService.query(query, ListFeed.class);

            for (ListEntry row : listFeed.getEntries()) {
                T item = mAdapter.getItemFromRow(row);
                if(filter == null || filter.filterRowItem(item)) {
                    T2 derivedItem = derivedAdapter.getItemFromRow(row);
                    items.add(derivedItem);
                }
            }
        }
    }

    public void queryItems(String structuredQuery,
                           ISVGoogleSpreadSheetRowFilter<T> filter,
                           List<T> items) throws ServiceException, IOException {
        items.clear();
        SpreadsheetEntry spreadsheetEntry = mService.getEntry(new URL(mSpreadSheetURL), SpreadsheetEntry.class);
        List<WorksheetEntry> worksheets = spreadsheetEntry.getWorksheets();

        for(WorksheetEntry worksheet : worksheets) {
            URL worksheetListFeedURL = worksheet.getListFeedUrl();

            ListQuery query = new ListQuery(worksheetListFeedURL);
            query.setSpreadsheetQuery(structuredQuery);

            ListFeed listFeed = mService.query(query, ListFeed.class);

            for (ListEntry row : listFeed.getEntries()) {
                T item = mAdapter.getItemFromRow(row);
                if(filter == null || filter.filterRowItem(item)) {
                    items.add(item);
                }
            }
        }
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
