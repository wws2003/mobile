package com.tbg.simplestvallet.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.activity.delegate.ISVGoogleDriveRESTServiceCallback;
import com.tbg.simplestvallet.activity.delegate.ISVGoogleSpreadSheetAccessCallback;
import com.tbg.simplestvallet.activity.delegate.SVGoogleDriveAccessDelegate;
import com.tbg.simplestvallet.activity.delegate.SVGoogleDriveConstants;

public class InitialSettingActivity extends AppCompatActivity implements ISVGoogleDriveRESTServiceCallback,
        ISVGoogleSpreadSheetAccessCallback {

    public final static String KEY_GOOGLE_ACCOUNT_NAME = "KEY_GOOGLE_ACCOUNT_NAME";
    public final static String RESULT_KEY_SPREAD_SHEET_ID = "RESULT_KEY_SPREAD_SHEET_ID";
    public final static String RESULT_KEY_GOOGLE_DRIVE_ACCESS_TOKEN = "RESULT_KEY_GOOGLE_DRIVE_ACCESS_TOKEN";

    private SVGoogleDriveAccessDelegate mGoogleDriveAccessDelegate = new SVGoogleDriveAccessDelegate();

    private final static String CREATED_DRIVE_FOLDER_NAME = "SimplestValet";
    private final static String CREATED_DRIVE_SPREADSHEET_NAME = "Log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setting);
        String googleAccountName = getIntent().getStringExtra(KEY_GOOGLE_ACCOUNT_NAME);

        //TODO How to avoid authorization asked twice?
        mGoogleDriveAccessDelegate = new SVGoogleDriveAccessDelegate();
        mGoogleDriveAccessDelegate.accessDriveRESTService(googleAccountName, this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_initial_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRESTServiceClientConnected() {
        enableBtnOpenSheet();
        enableBtnCreateSheet();
    }

    @Override
    public void onRESTServiceClientConnectingFailed(Intent authorizeIntent) {
        startActivityForResult(authorizeIntent, SVGoogleDriveConstants.REQUEST_CODE_REST_CLIENT_CONNECT);
    }

    @Override
    public void onNewSheetCreated(String sheetId) {
        showSpreadSheetCreatedDialog(sheetId);
    }

    @Override
    public void onNewSheetCreationFailed() {
        returnError();
    }

    @Override
    public void onAccessTokenRetrieved(String accessToken, String sheetId) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(RESULT_KEY_SPREAD_SHEET_ID, sheetId);
        resultIntent.putExtra(RESULT_KEY_GOOGLE_DRIVE_ACCESS_TOKEN, accessToken);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onAccessTokenRetrieveFailed() {
        returnError();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SVGoogleDriveConstants.REQUEST_CODE_DRIVE_API_CLIENT_CONNECT:
                if(resultCode == RESULT_OK) {
                    //Re-connecting is required
                    mGoogleDriveAccessDelegate.reconnect();
                }
                break;
            case SVGoogleDriveConstants.REQUEST_CODE_REST_CLIENT_CONNECT:
                if (resultCode == RESULT_OK) {
                    onRESTServiceClientConnected();
                }
                break;
            case SVGoogleDriveConstants.REQUEST_CODE_OPEN_FILE:
                if(resultCode == RESULT_OK) {
                    DriveId fileDriveId = data.getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                    onSpreadSheetConnected(fileDriveId.getResourceId());
                }
                else {
                    returnError();
                }
                break;
            default:
                break;
        }
    }

    //Button "Open Sheet" onClick handler
    public void onBtnChooseSheetClicked(View view) {
        mGoogleDriveAccessDelegate.tryToOpenSpreadSheet(this);
    }

    //Button "Create Sheet" onClick handler
    public void onBtnCreateSheetClicked(View view) {
        mGoogleDriveAccessDelegate.createNewSpreadSheet(this);
    }

    private void enableBtnOpenSheet() {
        Button btnOpen = (Button)findViewById(R.id.btn_choose_sheet);
        btnOpen.setEnabled(true);
    }

    private void enableBtnCreateSheet() {
        Button btnCreate = (Button)findViewById(R.id.btn_create_sheet);
        btnCreate.setEnabled(true);
    }

    //Call back when spreadsheet specified and connected
    private void onSpreadSheetConnected(String spreadSheetId) {
        Log.d("onSpreadSheetConnected", "Connected to spreadsheet " + spreadSheetId);
        mGoogleDriveAccessDelegate.accessSpreadSheet(spreadSheetId, this);
    }

    private void returnError() {
        Intent resultIntent = new Intent();
        setResult(RESULT_CANCELED, resultIntent);
        finish();
    }

    private void showSpreadSheetCreatedDialog(final String spreadSheetId) {
        DialogInterface.OnClickListener onClickListener = new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                onSpreadSheetConnected(spreadSheetId);
            }
        };

        new AlertDialog.Builder(this).setMessage("Spreadsheet created at DriveRootFolder/" + CREATED_DRIVE_FOLDER_NAME + "/" + CREATED_DRIVE_SPREADSHEET_NAME)
                .setTitle("Simplest Valet says")
                .setPositiveButton("OK", onClickListener)
                .show();
    }

}
