package com.tbg.simplestvallet.activity.delegate;

import com.google.android.gms.common.ConnectionResult;

/**
 * Created by wws2003 on 4/23/16.
 */
public interface ISVGoogleDriveAccessCallback {

    //Call after Google Drive API connection established
    void onDriveAPIClientConnected();

    //Call after attempt to make connection to Google Drive API failed
    void onDriveAPIConnectingFailed(ConnectionResult connectionResult);
}
