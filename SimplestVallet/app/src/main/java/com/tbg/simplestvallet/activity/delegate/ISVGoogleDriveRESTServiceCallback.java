package com.tbg.simplestvallet.activity.delegate;

import android.content.Intent;

/**
 * Created by wws2003 on 4/23/16.
 */
public interface ISVGoogleDriveRESTServiceCallback {

    //Call after connection to Google Drive REST service has been established
    void onRESTServiceClientConnected();

    //Call after attempt to connect Google Drive REST service failed due to authorization error
    void onRESTServiceClientConnectingFailed(Intent authorizeIntent);
}
