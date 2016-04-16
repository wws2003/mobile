package com.tbg.simplestvallet.app.container;

import com.tbg.simplestvallet.app.manager.authentication.SVCredential;
import com.tbg.simplestvallet.app.manager.sheetservice.abstr.ISVSheetServiceManager;
import com.tbg.simplestvallet.app.manager.sheetservice.impl.SVGoogleSheetServiceManagerImpl;
import com.tbg.simplestvallet.persist.abstr.ISVPersistor;

/**
 * Created by wws2003 on 11/5/15.
 */
public class SheetServiceManagerContainer {

    private ISVSheetServiceManager mCachedServiceManager = null;

    private ISVSheetServiceManager mGoogleSheetServiceManager = null;

    private ISVPersistor mPersistor;

    public SheetServiceManagerContainer(ISVPersistor persistor) {
        this.mPersistor = persistor;
        mGoogleSheetServiceManager = new SVGoogleSheetServiceManagerImpl(mPersistor);
    }

    public ISVSheetServiceManager reloadSheetForService(String serviceName) {
        if(serviceName.equals(SVCredential.SERVICE_NAME_GOOGLE_DRIVE)) {
            mCachedServiceManager = mGoogleSheetServiceManager;
        }
        return mCachedServiceManager;
    }

    public ISVSheetServiceManager getCachedSheetServiceManager() {
        return mCachedServiceManager;
    }
}
