package com.tbg.simplestvallet.app.container;

import com.tbg.simplestvallet.app.manager.authentication.Credential;
import com.tbg.simplestvallet.app.manager.sheetservice.abstr.ISheetServiceManager;
import com.tbg.simplestvallet.app.manager.sheetservice.impl.GoogleSheetServiceManager;
import com.tbg.simplestvallet.app.preference.abstr.IPreferenceOperator;

/**
 * Created by wws2003 on 11/5/15.
 */
public class SheetServiceManagerContainer {

    private ISheetServiceManager mCachedServiceManager = null;

    private ISheetServiceManager mGoogleSheetServiceManager = null;

    private IPreferenceOperator mPreferenceOperator;

    public SheetServiceManagerContainer(IPreferenceOperator preferenceOperator) {
        this.mPreferenceOperator = preferenceOperator;
        mGoogleSheetServiceManager = new GoogleSheetServiceManager(mPreferenceOperator);
    }

    public ISheetServiceManager reloadSheetForService(String serviceName) {
        if(serviceName.equals(Credential.SERVICE_NAME_GOOGLE_DRIVE)) {
            mCachedServiceManager = mGoogleSheetServiceManager;
        }
        return mCachedServiceManager;
    }

    public ISheetServiceManager getCachedSheetServiceManager() {
        return mCachedServiceManager;
    }
}
