package com.tbg.simplestvallet.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.app.SimplestValetApp;
import com.tbg.taskmanager.abstr.delegate.AbstractTaskResultListener;
import com.tbg.taskmanager.abstr.delegate.ITaskDelegate;
import com.tbg.taskmanager.abstr.executor.ITaskExecutor;
import com.tbg.taskmanager.abstr.task.AbstractTask;
import com.tbg.taskmanager.abstr.task.ITask;
import com.tbg.taskmanager.common.Result;
import com.tbg.taskmanager.impl.executor.AsyncTaskBasedTaskExecutorImpl;

import java.io.IOException;
import java.util.Arrays;

public class InitialSettingActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleAccountCredential mGoogleCredential;
    private com.google.api.services.drive.Drive mDriveClient;
    private GoogleApiClient mGoogleApiClient = null;

    private ITaskExecutor mTaskExecutor = new AsyncTaskBasedTaskExecutorImpl();

    public final static String KEY_GOOGLE_ACCOUNT_NAME = "KEY_GOOGLE_ACCOUNT_NAME";
    public final static String RESULT_KEY_SPREAD_SHEET_ID = "RESULT_KEY_SPREAD_SHEET_ID";
    public final static String RESULT_KEY_GOOGLE_DRIVE_ACCESS_TOKEN = "RESULT_KEY_GOOGLE_DRIVE_ACCESS_TOKEN";

    private final static int REQUEST_CODE_DRIVE_API_CLIENT_CONNECT = 1;
    private final static int REQUEST_CODE_REST_CLIENT_CONNECT = 3;
    private final static int REQUEST_CODE_OPEN_FILE = 4;

    private final static String CREATED_DRIVE_FOLDER_NAME = "SimplestValet";
    private final static String CREATED_DRIVE_SPREADSHEET_NAME = "Log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setting);
        String googleAccountName = getIntent().getStringExtra(KEY_GOOGLE_ACCOUNT_NAME);

        //TODO How to avoid authorization asked twice?
        connectToDrive(googleAccountName);
        setupDriveRESTService(googleAccountName);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_DRIVE_API_CLIENT_CONNECT:
                if(resultCode == RESULT_OK) {
                    //Re-connecting is required
                    mGoogleApiClient.connect();
                }
                break;
            case REQUEST_CODE_REST_CLIENT_CONNECT:
                if (resultCode == RESULT_OK) {
                    onRESTServiceClientConnected();
                }
                break;
            case REQUEST_CODE_OPEN_FILE:
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
        tryToOpenSpreadSheet();
    }

    //Button "Create Sheet" onClick handler
    public void onBtnCreateSheetClicked(View view) {
        createNewSpreadSheet();
    }

    @Override
    public void onConnected(Bundle bundle) {
        onDriveApiClientConnected();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Currently do nothing
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("onConnectionFailed", connectionResult.toString() + " Error code = " + connectionResult.getErrorCode());
        if(connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, REQUEST_CODE_DRIVE_API_CLIENT_CONNECT);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.d("onConnectionFailed", "Can not find resolution");
        }
    }

    //Call back when drive api client connected
    private void onDriveApiClientConnected() {
        enableBtnOpenSheet();
    }

    private void enableBtnOpenSheet() {
        Button btnOpen = (Button)findViewById(R.id.btn_choose_sheet);
        btnOpen.setEnabled(true);
    }

    //Call back when rest service client connected
    private void onRESTServiceClientConnected() {
        enableBtnCreateSheet();
    }

    private void enableBtnCreateSheet() {
        Button btnCreate = (Button)findViewById(R.id.btn_create_sheet);
        btnCreate.setEnabled(true);
    }

    //Call back when spreadsheet specified and connected
    private void onSpreadSheetConnected(String spreadSheetId) {
        Log.d("onSpreadSheetConnected", "Connected to spreadsheet " + spreadSheetId);
        returnResult(spreadSheetId);
    }

    private void returnResult(final String spreadSheetId) {
        ITask<String> getGoogleTokenTask = new AbstractTask<String>() {
            @Override
            public Result<String> doExecute() {
                try {
                    String googleToken = mGoogleCredential.getToken();
                    return generateResult(googleToken, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GoogleAuthException e) {
                    e.printStackTrace();
                }
                return generateResult(null, 0);
            }
        };
        ITaskDelegate<String> getGoogleTokenTaskDelegate = new AbstractTaskResultListener<String>() {
            @Override
            public void onTaskExecuted(Result<String> taskResult) {
                Intent resultIntent = new Intent();
                if(taskResult.getElement() != null) {
                    resultIntent.putExtra(RESULT_KEY_SPREAD_SHEET_ID, spreadSheetId);
                    resultIntent.putExtra(RESULT_KEY_GOOGLE_DRIVE_ACCESS_TOKEN, taskResult.getElement());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
                else {
                    returnError();
                }
            }
        };
        mTaskExecutor.executeTask(getGoogleTokenTask, getGoogleTokenTaskDelegate);
    }

    private void returnError() {
        Intent resultIntent = new Intent();
        setResult(RESULT_CANCELED, resultIntent);
        finish();
    }

    /*----------------------Initial setup for Google Drive Api Client----------------------*/
    private void connectToDrive(String googleAccountName) {
        if(mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        else {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Drive.SCOPE_APPFOLDER)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .setAccountName(googleAccountName)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    /*----------------------Initial setup for Google RESTFul Service Client----------------------*/
    private void setupDriveRESTService(String googleAccountName) {
        mGoogleCredential = GoogleAccountCredential.usingOAuth2(this, Arrays.asList(DriveScopes.DRIVE,
                "https://spreadsheets.google.com/feeds"));
        mGoogleCredential.setSelectedAccountName(googleAccountName);

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        mDriveClient = new com.google.api.services.drive.Drive.Builder(transport,
                jsonFactory,
                mGoogleCredential)
                .setApplicationName(getApplicationContext().getString(R.string.app_name))
                .build();

        tryToAccessRESTService();
    }

    private void tryToAccessRESTService() {
        final int ACCESS_RESULT_OK = 0;
        final int ACCESS_RESULT_NOT_AUTHORIZED = 1;

        long taskId = SimplestValetApp.getTaskIdPool().getAvailableTaskId();
        ITask<UserRecoverableAuthIOException> tryToAccessRESTTask = new AbstractTask<UserRecoverableAuthIOException>(taskId) {
            @Override
            public Result<UserRecoverableAuthIOException> doExecute() {
                try {
                    com.google.api.services.drive.Drive.Files.List files = mDriveClient.files().list();
                    files.execute();
                } catch (IOException iae) {
                    if (iae.getClass() == UserRecoverableAuthIOException.class) {
                        UserRecoverableAuthIOException urae = (UserRecoverableAuthIOException) iae;
                        return generateResult(urae, ACCESS_RESULT_NOT_AUTHORIZED);
                    }
                    else {
                        return generateResult(null, ACCESS_RESULT_NOT_AUTHORIZED);
                    }
                }
                return generateResult(null, ACCESS_RESULT_OK);
            }
        };
        ITaskDelegate<UserRecoverableAuthIOException> taskDelegate = new AbstractTaskResultListener<UserRecoverableAuthIOException>() {
            @Override
            public void onTaskExecuted(Result<UserRecoverableAuthIOException> taskResult) {
                if(taskResult.getResultCode() == ACCESS_RESULT_NOT_AUTHORIZED) {
                    UserRecoverableAuthIOException exception = taskResult.getElement();
                    Intent authorizeIntent = exception.getIntent();
                    startActivityForResult(authorizeIntent, REQUEST_CODE_REST_CLIENT_CONNECT);
                }
                else {
                   onRESTServiceClientConnected();
                }
            }
        };

        mTaskExecutor.executeTask(tryToAccessRESTTask, taskDelegate);
    }

    /*--------------------------Main actions----------------------------*/
    private void tryToOpenSpreadSheet() {
        IntentSender intentSender = Drive.DriveApi.newOpenFileActivityBuilder()
                .setMimeType(new String[]{"application/vnd.google-apps.spreadsheet"})
                .build(mGoogleApiClient);
        try {
            startIntentSenderForResult(intentSender, REQUEST_CODE_OPEN_FILE,
                    null,
                    0,
                    0,
                    0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void createNewSpreadSheet() {
        final int CREATE_SPREADSHEET_RESULT_OK = 0;
        final int CREATE_SPREADSHEET_RESULT_FAILED = 1;

        final long taskId = SimplestValetApp.getTaskIdPool().getAvailableTaskId();
        ITask<String> folderCreateTask = new AbstractTask<String>(taskId) {
            @Override
            public Result<String> doExecute() {
                try {
                    //Create folder in drive root
                    File newFolder = new File();
                    newFolder.setMimeType("application/vnd.google-apps.folder");
                    newFolder.setTitle(CREATED_DRIVE_FOLDER_NAME);
                    newFolder.setOriginalFilename(CREATED_DRIVE_FOLDER_NAME);

                    com.google.api.services.drive.Drive.Files files = mDriveClient.files();
                    com.google.api.services.drive.Drive.Files.Insert insertRequest = files.insert(newFolder);

                    File createdFolder = insertRequest.execute();

                    //Create spreadsheet inside new folder
                    File newSpreadSheet = new File();
                    newSpreadSheet.setMimeType("application/vnd.google-apps.spreadsheet");
                    newSpreadSheet.setTitle(CREATED_DRIVE_SPREADSHEET_NAME);
                    newSpreadSheet.setOriginalFilename(CREATED_DRIVE_SPREADSHEET_NAME);
                    newSpreadSheet.setParents(Arrays.asList(new ParentReference().setId(createdFolder.getId())));

                    File createdFile = files.insert(newSpreadSheet).execute();

                    return generateResult(createdFile.getId(), CREATE_SPREADSHEET_RESULT_OK);

                } catch (IOException iae) {
                    return generateResult("", CREATE_SPREADSHEET_RESULT_FAILED);
                }
            }
        };

        ITaskDelegate<String> spreadSheetCreateTaskDelegate = new AbstractTaskResultListener<String>() {
            @Override
            public void onTaskExecuted(Result<String> taskResult) {
                if(taskResult.getResultCode() == CREATE_SPREADSHEET_RESULT_OK) {
                    showSpreadSheetCreatedDialog(taskResult.getElement());
                }
                else {
                    returnError();
                }
            }
        };

        mTaskExecutor.executeTask(folderCreateTask, spreadSheetCreateTaskDelegate);
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
