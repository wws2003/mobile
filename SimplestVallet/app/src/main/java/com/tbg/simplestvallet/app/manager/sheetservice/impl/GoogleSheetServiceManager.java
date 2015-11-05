package com.tbg.simplestvallet.app.manager.sheetservice.impl;

import com.tbg.simplestvallet.app.manager.sheetservice.abstr.ISheetServiceManager;
import com.tbg.simplestvallet.app.preference.abstr.IPreferenceOperator;
import com.tbg.simplestvallet.model.active.abstr.IEntrySheet;
import com.tbg.simplestvallet.model.active.impl.GoogleSpreadSheetBasedSheet;

/**
 * Created by wws2003 on 11/5/15.
 */
public class GoogleSheetServiceManager implements ISheetServiceManager {

    private IEntrySheet mCachedSheet = null;
    private IPreferenceOperator mPreferenceOperator;

    public GoogleSheetServiceManager(IPreferenceOperator preferenceOperator) {
        this.mPreferenceOperator = preferenceOperator;
    }

    //TODO Encapsulate works of mPreferenceOperator and Strings !!!!1

    @Override
    public String getSheetId(String accountName, String serviceToken) throws SVSheetNotFoundException {
        String sheetId = mPreferenceOperator.get(getSheetIdKey(accountName, serviceToken));
        if(sheetId == null) {
            throw new SVSheetNotFoundException();
        }
        return sheetId;
    }

    @Override
    public void storeSheetId(String sheetId, String accountName, String serviceToken) {
        mPreferenceOperator.store(getSheetIdKey(accountName, serviceToken), sheetId);
    }

    @Override
    public void accessSheet(String sheetId, String accountName, String serviceToken) throws SVSheetServiceNotAvailableException, SVSheetServiceUnAuthorizedException {
        //TODO Inspect more
        mCachedSheet = new GoogleSpreadSheetBasedSheet(sheetId, serviceToken);
    }

    @Override
    public IEntrySheet getSVEntrySheet() {
        return mCachedSheet;
    }

    private String getSheetIdKey(String accountName, String serviceToken) {
        //TODO More powerful algorithm
        return accountName + "@._" + serviceToken;
    }
}
