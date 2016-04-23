package com.tbg.simplestvallet.activity.delegate;

/**
 * Created by wws2003 on 4/23/16.
 */
public interface ISVGoogleSpreadSheetAccessCallback {

    //Call after retrieved sheet id for a new sheet
    void onNewSheetCreated(String sheetId);

    //Call after attempt to create a new sheet failed
    void onNewSheetCreationFailed();

    //Call after access token for the sheet has been retrieved. Actually access token is not limited for one sheet though
    void onAccessTokenRetrieved(String accessToken, String sheetId);

    //Call after attempt to retrieve access token failed
    void onAccessTokenRetrieveFailed();
}
