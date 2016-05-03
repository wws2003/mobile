package com.tbg.simplestvallet.model.active.impl.collection.google;

import android.util.Log;

import com.google.gdata.util.ServiceException;
import com.tbg.simplestvallet.model.active.EntryActionResult;
import com.tbg.simplestvallet.model.active.abstr.collection.ISVEntrySheet;
import com.tbg.simplestvallet.model.active.abstr.query.ISVEntryQueryStringBuilder;
import com.tbg.simplestvallet.model.active.abstr.query.ISVQueryStructure;
import com.tbg.simplestvallet.model.active.impl.query.google.SVGoogleEntryStringQueryBuilderImpl;
import com.tbg.simplestvallet.model.dto.SVEntry;
import com.tbg.simplestvallet.model.dto.SVMoneyQuantity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wws2003 on 11/2/15.
 */

//TODO More generic solution !!!

public class SVGoogleSpreadSheetBasedSheet implements ISVEntrySheet {

    private SVGoogleSpreadSheet<SVEntry> mGoogleSpreadSheet;

    private ISVEntryQueryStringBuilder mEntryQueryStringBuilder;

    public SVGoogleSpreadSheetBasedSheet(String spreadSheetId, String accessToken) {
        mGoogleSpreadSheet = new SVGoogleSpreadSheet<>(spreadSheetId, accessToken, new SVGoogleSpreadSheetMapping.GoogleSpreadSheetRowEntryAdapter());
        mEntryQueryStringBuilder = new SVGoogleEntryStringQueryBuilderImpl();
    }

    @Override
    public void open() throws SVEntryOpenSheetException, SVEntrySheetUnAuthorizedException {
        try {
            mGoogleSpreadSheet.open();
        }
        catch (ServiceException se) {
            se.printStackTrace();
            Log.d("Open spreadsheet error", se.getDebugInfo() + " " + se.getInternalReason());
            throw new SVEntryOpenSheetException(se);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new SVEntryOpenSheetException(e);
        }
    }

    @Override
    public int addEntry(SVEntry entry) {
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
    public int getAllEntries(List<SVEntry> entries) {
        try {
            mGoogleSpreadSheet.getAllItems(null, entries);
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
    public SVMoneyQuantity getAllEntriesAmount() {
        //TODO Implement more efficiently
        SVGoogleSpreadSheetMapping.GoogleSpreadSheetRowMoneyQuantityAdapter moneyQuantityAdapter = new SVGoogleSpreadSheetMapping.GoogleSpreadSheetRowMoneyQuantityAdapter();
        try {
            mGoogleSpreadSheet.getAllDerivedItems(moneyQuantityAdapter, null, new ArrayList<SVMoneyQuantity>());
            //This return statement is just a trick to take advantage of side-effect. Officially required to iterate the list retrieved to collect results
            return moneyQuantityAdapter.getMoneyQuantity();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new SVMoneyQuantity(0);
    }

    @Override
    public void queryEntries(ISVQueryStructure queryStructure, List<SVEntry> entries) {
        mEntryQueryStringBuilder.reset();
        queryStructure.accept(mEntryQueryStringBuilder);

        String structuredQuery = mEntryQueryStringBuilder.build();
        try {
            mGoogleSpreadSheet.queryItems(structuredQuery, false, null, entries);
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void queryFullTextEntries(String query, List<SVEntry> entries) {
        try {
            mGoogleSpreadSheet.queryItems(query, true, null, entries);
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SVMoneyQuantity queryEntriesAmount(ISVQueryStructure queryStructure) {
        mEntryQueryStringBuilder.reset();
        queryStructure.accept(mEntryQueryStringBuilder);
        String structuredQuery = mEntryQueryStringBuilder.build();

        SVGoogleSpreadSheetMapping.GoogleSpreadSheetRowMoneyQuantityAdapter moneyQuantityAdapter = new SVGoogleSpreadSheetMapping.GoogleSpreadSheetRowMoneyQuantityAdapter();
        try {
            mGoogleSpreadSheet.queryDerivedItems(structuredQuery,
                    moneyQuantityAdapter,
                    null,
                    new ArrayList<SVMoneyQuantity>());
            //This return statement is just a trick to take advantage of side-effect. Officially required to iterate the list retrieved to collect results
            return moneyQuantityAdapter.getMoneyQuantity();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new SVMoneyQuantity(0);
    }
}