package com.tbg.simplestvallet.activity.delegate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
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

/**
 * Created by wws2003 on 4/23/16.
 */
public class SVGoogleDriveAccessDelegate {

    private final static String CREATED_DRIVE_FOLDER_NAME = "SimplestValet";
    private final static String CREATED_DRIVE_SPREADSHEET_NAME = "Log";

    private GoogleApiClient mGoogleApiClient;
    private GoogleAccountCredential mGoogleCredential;
    private com.google.api.services.drive.Drive mDriveClient;
    private ITaskExecutor mTaskExecutor = new AsyncTaskBasedTaskExecutorImpl();

    //Access spread sheet from scratch
    public void accessSpreadSheet(final String googleAccountName,
                                  final String sheetId,
                                  final Activity activity,
                                  final int requestCode,
                                  final ISVGoogleSpreadSheetAccessCallback callback) {

        final ISVGoogleDriveRESTServiceCallback restServiceCallback = new ISVGoogleDriveRESTServiceCallback() {
            @Override
            public void onRESTServiceClientConnected() {
                tryToAccessSheet(sheetId, callback);
            }

            @Override
            public void onRESTServiceClientConnectingFailed(Intent authorizeIntent) {
                activity.startActivityForResult(authorizeIntent, requestCode);
            }
        };
        accessDriveRESTService(googleAccountName, activity, restServiceCallback);
    }

    public void accessSpreadSheet(final String sheetId,
                                  final ISVGoogleSpreadSheetAccessCallback callback) {
        tryToAccessSheet(sheetId, callback);
    }

    //Access Google Drive REST service from scratch
    public void accessDriveRESTService(final String googleAccountName,
                                       final Context context,
                                       final ISVGoogleDriveRESTServiceCallback callback) {

        ISVGoogleDriveAccessCallback driveAccessCallback = new ISVGoogleDriveAccessCallback() {
            @Override
            public void onDriveAPIClientConnected() {
                //Try to access REST service
                setupGoogleAccountCredential(googleAccountName, context);
                tryToAccessRESTService(callback);
            }

            @Override
            public void onDriveAPIConnectingFailed(ConnectionResult connectionResult) {
                onDriveAPIConnectingFailed(connectionResult);
            }
        };
        accessDriveAPI(googleAccountName, context, driveAccessCallback);
    }

    //Access Google Drive API service from scratch
    public void accessDriveAPI(String googleAccountName, Context context, final ISVGoogleDriveAccessCallback callback) {
        GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                callback.onDriveAPIClientConnected();
            }

            @Override
            public void onConnectionSuspended(int i) {
                //Do nothing for now
            }
        };

        GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
                callback.onDriveAPIConnectingFailed(connectionResult);
            }
        };

        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        mGoogleApiClient = new GoogleApiClient.Builder(context).addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addScope(Drive.SCOPE_APPFOLDER)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(connectionFailedListener)
                .setAccountName(googleAccountName)
                .build();
        mGoogleApiClient.connect();
    }

    public void reconnect() {
        mGoogleApiClient.connect();
    }

    public void createNewSpreadSheet(final ISVGoogleSpreadSheetAccessCallback callback) {
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
                    callback.onNewSheetCreated(taskResult.getElement());
                }
                else {
                    callback.onNewSheetCreationFailed();
                }
            }
        };

        mTaskExecutor.executeTask(folderCreateTask, spreadSheetCreateTaskDelegate);
    }

    public void tryToOpenSpreadSheet(Activity activity, int requestCode) {
        IntentSender intentSender = Drive.DriveApi.newOpenFileActivityBuilder()
                .setMimeType(new String[]{"application/vnd.google-apps.spreadsheet"})
                .build(mGoogleApiClient);
        try {
            activity.startIntentSenderForResult(intentSender, requestCode,
                    null,
                    0,
                    0,
                    0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    //MARK: Private methods
    private void setupGoogleAccountCredential(String googleAccountName, Context context) {
        mGoogleCredential = GoogleAccountCredential.usingOAuth2(context, Arrays.asList(DriveScopes.DRIVE,
                "https://spreadsheets.google.com/feeds"));
        mGoogleCredential.setSelectedAccountName(googleAccountName);

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        mDriveClient = new com.google.api.services.drive.Drive.Builder(transport,
                jsonFactory,
                mGoogleCredential)
                .setApplicationName(context.getString(R.string.app_name))
                .build();
    }

    private void tryToAccessRESTService(final ISVGoogleDriveRESTServiceCallback callback) {
        final int ACCESS_RESULT_OK = 0;
        final int ACCESS_RESULT_NOT_AUTHORIZED = 1;

        long taskId = SimplestValetApp.getTaskIdPool().getAvailableTaskId();
        ITask<UserRecoverableAuthIOException> tryToAccessRESTTask = new AbstractTask<UserRecoverableAuthIOException>(taskId) {
            @Override
            public Result<UserRecoverableAuthIOException> doExecute() {
                //If the app has not been authorized before, there would be an exception handle to allow user grant authorization for the app
                try {
                    com.google.api.services.drive.Drive.Files.List files = mDriveClient.files().list();
                    files.execute();
                } catch (IOException iae) {
                    iae.printStackTrace();
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
                    if(exception != null)  {
                        Intent authorizeIntent = exception.getIntent();
                        callback.onRESTServiceClientConnectingFailed(authorizeIntent);
                    }
                    else {
                        Log.d("tryToAccessRESTService", "Some thing strange happened");
                    }
                }
                else {
                    callback.onRESTServiceClientConnected();
                }
            }
        };

        mTaskExecutor.executeTask(tryToAccessRESTTask, taskDelegate);
    }

    private void tryToAccessSheet(final String spreadSheetId, final ISVGoogleSpreadSheetAccessCallback callback) {
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
                if(taskResult.getElement() != null) {
                    String accessToken = taskResult.getElement();
                    callback.onAccessTokenRetrieved(accessToken, spreadSheetId);
                }
                else {
                    callback.onAccessTokenRetrieveFailed();
                }
            }
        };
        mTaskExecutor.executeTask(getGoogleTokenTask, getGoogleTokenTaskDelegate);
    }
}
