package com.tbg.simplestvallet.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.app.SimplestValetApp;
import com.tbg.simplestvallet.app.container.SheetServiceManagerContainer;
import com.tbg.simplestvallet.app.manager.authentication.SVCredential;
import com.tbg.simplestvallet.app.manager.authentication.abstr.ISVAuthenticationManager;
import com.tbg.simplestvallet.app.manager.authentication.abstr.ISVSession;
import com.tbg.simplestvallet.app.manager.sheetservice.abstr.ISVSheetServiceManager;
import com.tbg.taskmanager.abstr.delegate.AbstractTaskResultListener;
import com.tbg.taskmanager.abstr.delegate.ITaskDelegate;
import com.tbg.taskmanager.abstr.executor.ITaskExecutor;
import com.tbg.taskmanager.abstr.task.AbstractTask;
import com.tbg.taskmanager.abstr.task.ITask;
import com.tbg.taskmanager.common.Result;
import com.tbg.taskmanager.impl.executor.AsyncTaskBasedTaskExecutorImpl;

/**
 * Created by wws2003 on 4/24/16.
 */
public abstract class SVAbstractPreMainActivity extends AppCompatActivity {

    protected ISVAuthenticationManager mAuthenticationManager = SimplestValetApp.getAuthenticationManagerContainer().getAuthenticationManager();;
    protected SheetServiceManagerContainer mSheetServiceManagerContainer = SimplestValetApp.getSheetServiceManagerContainer();;
    protected ITaskExecutor mTaskExecutor = new AsyncTaskBasedTaskExecutorImpl();

    protected void onSpreadSheetInitializedForAccount(final ISVSession currentSession,
                                                      final String spreadSheetId,
                                                      final String googleDriveAccessToken) {
        try {
            //Put information to current session
            fillSession(currentSession, googleDriveAccessToken);

            //Persist session before taking further steps
            storeSheetCredential(currentSession, spreadSheetId, googleDriveAccessToken);
            mAuthenticationManager.persistSession();

        } catch (ISVSession.SVInvalidatedSessionException e) {
            e.printStackTrace();
            return;
        }

        //Try to access sheet before go to main screen
        tryToAccessSheet(currentSession, spreadSheetId, googleDriveAccessToken);
    }

    protected void showError(String title, String errorMessage) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(errorMessage);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    protected abstract void fillSession(final ISVSession currentSession, final String googleDriveAccessToken) throws ISVSession.SVInvalidatedSessionException;

    protected abstract void toLoginScreen();

    private void storeSheetCredential(final ISVSession currentSession,
                                      final String spreadSheetId,
                                      final String googleDriveAccessToken) throws ISVSession.SVInvalidatedSessionException {

        ISVSheetServiceManager sheetServiceManager = mSheetServiceManagerContainer.reloadSheetForService(SVCredential.SERVICE_NAME_GOOGLE_DRIVE);
        String accountName = currentSession.getCredential().getServiceAccountName(SVCredential.SERVICE_NAME_GOOGLE_DRIVE);
        sheetServiceManager.storeSheetId(spreadSheetId, accountName, googleDriveAccessToken);
    }

    private void tryToAccessSheet(final ISVSession currentSession,
                                  final String spreadSheetId,
                                  final String googleDriveAccessToken) {

        ITask<Exception> accessSheetTask = new AbstractTask<Exception>() {
            @Override
            public Result<Exception> doExecute() {
                ISVSheetServiceManager sheetServiceManager = mSheetServiceManagerContainer.reloadSheetForService(SVCredential.SERVICE_NAME_GOOGLE_DRIVE);

                try {
                    String accountName = currentSession.getCredential().getServiceAccountName(SVCredential.SERVICE_NAME_GOOGLE_DRIVE);
                    sheetServiceManager.accessSheet(spreadSheetId, accountName, googleDriveAccessToken);
                    return generateResult(null, 0);
                }
                catch (ISVSession.SVInvalidatedSessionException e) {
                    e.printStackTrace();
                    return generateResult(e, -1);
                } catch (ISVSheetServiceManager.SVSheetServiceNotAvailableException e) {
                    e.printStackTrace();
                    return generateResult(e, -1);
                } catch (ISVSheetServiceManager.SVSheetServiceUnAuthorizedException e) {
                    e.printStackTrace();
                    return generateResult(e, -1);
                }
            }
        };

        ITaskDelegate<Exception> persistSessionTaskDelegate = new AbstractTaskResultListener<Exception>() {
            @Override
            public void onTaskExecuted(Result<Exception> taskResult) {
                if (taskResult.getResultCode() != 0) {
                    showError(getString(R.string.msg_login_error), taskResult.getElement().getLocalizedMessage());
                    toLoginScreen();
                } else {
                    toMainScreen();
                }
            }
        };

        mTaskExecutor.executeTask(accessSheetTask, persistSessionTaskDelegate);
    }

    protected void toMainScreen() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(mainIntent);
    }

}
