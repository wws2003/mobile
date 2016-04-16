package com.tbg.simplestvallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

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

public class StartingActivity extends AppCompatActivity {

    private ISVAuthenticationManager mAuthenticationManager = null;
    private ITaskExecutor mTaskExecutor = null;
    private SheetServiceManagerContainer mSheetServiceManagerContainer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        mAuthenticationManager = SimplestValetApp.getAuthenticationManagerContainer().getAuthenticationManager();
        mTaskExecutor = new AsyncTaskBasedTaskExecutorImpl();
        mSheetServiceManagerContainer = SimplestValetApp.getSheetServiceManagerContainer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initialRoute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_starting, menu);
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

    private void initialRoute() {
        try {
            //Try to login to saved account session
            ISVSession oldSession = mAuthenticationManager.getOldSession();
            SVCredential credential = oldSession.getCredential();
            String sheetServiceName = oldSession.getAttributeValue(ISVSession.ATTRIBUTE_KEY_SHEET_SERVICE_NAME);
            continueOldSession(oldSession, credential, sheetServiceName);
        }
        catch (ISVSession.SVSessionNotFound e) {
            e.printStackTrace();
            toLoginScreen();
        }
        catch (ISVSession.SVInvalidatedSessionException e) {
            e.printStackTrace();
            toLoginScreen();
        }
    }

    private void toLoginScreen() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void continueOldSession(final ISVSession oldSession,
                                    final SVCredential credential,
                                    final String sheetServiceName) {
        ITask<Exception> loadSheetTask = new AbstractTask<Exception>() {
            @Override
            public Result<Exception> doExecute() {
                try {
                    String sheetServiceAccessAccountName = credential.getServiceAccountName(sheetServiceName);
                    String sheetServiceAccessToken = credential.getServiceAccessToken(sheetServiceName);

                    ISVSheetServiceManager sheetServiceManager = mSheetServiceManagerContainer.reloadSheetForService(sheetServiceName);
                    String sheetId = sheetServiceManager.loadSheetId(sheetServiceAccessAccountName, sheetServiceAccessToken);
                    sheetServiceManager.accessSheet(sheetId, sheetServiceAccessAccountName, sheetServiceAccessToken);

                    return generateResult(null, 0);
                } catch (Exception e) {
                    return generateResult(e, 0);
                }
            }
        };

        ITaskDelegate<Exception> loadSheetTaskDelegate = new AbstractTaskResultListener<Exception>() {
            @Override
            public void onTaskExecuted(Result<Exception> taskResult) {
                Exception exception = taskResult.getElement();
                if(exception != null) {
                    exception.printStackTrace();

                    //Also invalidate loaded session
                    oldSession.invalidate();
                    mAuthenticationManager.destroySession();

                    toLoginScreen();
                }
                else {
                    toMainScreen();
                }
            }
        };

        mTaskExecutor.executeTask(loadSheetTask, loadSheetTaskDelegate);
    }

    private void toMainScreen() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}
