package com.tbg.simplestvallet.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.activity.delegate.ISVGoogleSpreadSheetAccessCallback;
import com.tbg.simplestvallet.activity.delegate.SVGoogleDriveAccessDelegate;
import com.tbg.simplestvallet.activity.delegate.SVGoogleDriveConstants;
import com.tbg.simplestvallet.app.SimplestValetApp;
import com.tbg.simplestvallet.app.manager.authentication.SVCredential;
import com.tbg.simplestvallet.app.manager.authentication.abstr.ISVSession;
import com.tbg.simplestvallet.app.manager.sheetservice.abstr.ISVSheetServiceManager;
import com.tbg.taskmanager.abstr.delegate.AbstractTaskResultListener;
import com.tbg.taskmanager.abstr.delegate.ITaskDelegate;
import com.tbg.taskmanager.abstr.task.AbstractTask;
import com.tbg.taskmanager.abstr.task.ITask;
import com.tbg.taskmanager.common.Result;

public class StartingActivity extends SVAbstractPreMainActivity {

    private ViewWrapper mViewWrapper;
    private SVGoogleDriveAccessDelegate mDriveAccessDelegate = new SVGoogleDriveAccessDelegate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkInternetConnection();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SVGoogleDriveConstants.REQUEST_CODE_DRIVE_API_CLIENT_CONNECT:
                //Do nothing for now
                break;
            default:
                break;
        }
    }

    @Override
    protected void toLoginScreen() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    @Override
    protected void fillSession(ISVSession oldSession, String googleDriveAccessToken) throws ISVSession.SVInvalidatedSessionException {
        //In this screen, session if ready is loaded from storage. Therefore only reset token is required
        SVCredential credential = oldSession.getCredential();
        String sheetServiceName = oldSession.getAttributeValue(ISVSession.ATTRIBUTE_KEY_SHEET_SERVICE_NAME);
        final String sheetServiceAccessAccountName = credential.getServiceAccountName(sheetServiceName);

        oldSession.putCredentialServiceAccessToken(SVCredential.SERVICE_NAME_GOOGLE_DRIVE, sheetServiceAccessAccountName, googleDriveAccessToken);
    }

    private void initViews() {
        mViewWrapper = new ViewWrapper((TextView) findViewById(R.id.tv_connectivity_status),
                (TextView) findViewById(R.id.tv_login_message),
                (ProgressBar)findViewById(R.id.prb_loading));
    }

    private void checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null) {
            onInternetConnected(activeNetwork);
        }
        else {
            onInternetConnectingFailed();
        }
    }

    private void onInternetConnected(NetworkInfo activeNetwork) {
        //Show network info
        mViewWrapper.showOK(activeNetwork);

        //Continue to log in
        initialRoute();
    }

    private void onInternetConnectingFailed() {
        //Show error message
        mViewWrapper.showError();
    }

    private void initialRoute() {
        try {
            //Try to login to saved account session / current session (if app hasn't been terminated yet)
            ISVSession currentSession = mAuthenticationManager.getCurrentSession();
            ISVSession oldSession = currentSession == null ? mAuthenticationManager.getOldSession() : currentSession;
            boolean isSessionExpired = oldSession.isExpired();
            SVCredential credential = oldSession.getCredential();
            String sheetServiceName = oldSession.getAttributeValue(ISVSession.ATTRIBUTE_KEY_SHEET_SERVICE_NAME);

            if (isSessionExpired) {
                reLoginExpiredSession(oldSession, credential, sheetServiceName);
            }
            else {
                continueOldSession(oldSession, credential, sheetServiceName);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            toLoginScreen();
        }
    }

    private void reLoginExpiredSession(final ISVSession oldSession,
                                       final SVCredential credential,
                                       final String sheetServiceName) throws ISVSession.SVInvalidatedSessionException, ISVSheetServiceManager.SVSheetNotFoundException {

        final String sheetServiceAccessAccountName = credential.getServiceAccountName(sheetServiceName);
        final String oldSheetServiceAccessToken = credential.getServiceAccessToken(sheetServiceName);
        final ISVSheetServiceManager sheetServiceManager = mSheetServiceManagerContainer.reloadSheetForService(sheetServiceName);

        //Note: oldSheetServiceAccessToken is still the old one !
        final String sheetId = sheetServiceManager.loadSheetId(sheetServiceAccessAccountName, oldSheetServiceAccessToken);

        tryToReLogin(oldSession, sheetServiceAccessAccountName, sheetId);
    }

    private void continueOldSession(final ISVSession oldSession,
                                    final SVCredential credential,
                                    final String sheetServiceName) throws ISVSession.SVInvalidatedSessionException, ISVSheetServiceManager.SVSheetNotFoundException {

        final String sheetServiceAccessAccountName = credential.getServiceAccountName(sheetServiceName);
        final String oldSheetServiceAccessToken = credential.getServiceAccessToken(sheetServiceName);
        final ISVSheetServiceManager sheetServiceManager = mSheetServiceManagerContainer.reloadSheetForService(sheetServiceName);

        //Note: oldSheetServiceAccessToken is still the old one !
        final String sheetId = sheetServiceManager.loadSheetId(sheetServiceAccessAccountName, oldSheetServiceAccessToken);

        ITask<Exception> loadSheetTask = new AbstractTask<Exception>() {
            @Override
            public Result<Exception> doExecute() {
                try {
                    sheetServiceManager.accessSheet(sheetId, sheetServiceAccessAccountName, oldSheetServiceAccessToken);
                    return generateResult(null, 0);
                }
                catch (Exception e) {
                    return generateResult(e, 0);
                }
            }
        };

        ITaskDelegate<Exception> loadSheetTaskDelegate = new AbstractTaskResultListener<Exception>() {
            @Override
            public void onTaskToBeExecuted() {
                super.onTaskToBeExecuted();
                //Hide message
                findViewById(R.id.tv_session_expired_warning).setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTaskExecuted(Result<Exception> taskResult) {
                Exception exception = taskResult.getElement();
                if(exception != null) {
                    exception.printStackTrace();
                    //Try to re login if possible
                    try {
                        if (oldSession.isAutoReLoginPermitted()) {
                            //Show message
                            findViewById(R.id.tv_session_expired_warning).setVisibility(View.VISIBLE);
                            tryToReLogin(oldSession, sheetServiceAccessAccountName, sheetId);
                        }
                        else {
                            //Also invalidate loaded session
                            mAuthenticationManager.destroySession();
                            toLoginScreen();
                        }
                    }
                    catch (ISVSession.SVInvalidatedSessionException ise) {
                        ise.printStackTrace();
                        toLoginScreen();
                    }
                }
                else {
                    toMainScreen();
                }
            }
        };

        mTaskExecutor.executeTask(loadSheetTask, loadSheetTaskDelegate);
    }

    //FIXME: This brings the dependency to Google Drive here, not good for portability...
    private void tryToReLogin(final ISVSession oldSession, final String sheetServiceAccessAccountName, String sheetId) {

        ISVGoogleSpreadSheetAccessCallback callback = new ISVGoogleSpreadSheetAccessCallback() {
            @Override
            public void onNewSheetCreated(String sheetId) {
                //Do nothing now
            }

            @Override
            public void onNewSheetCreationFailed() {
                //Do nothing now
            }

            @Override
            public void onAccessTokenRetrieved(String accessToken, String sheetId) {
                try {
                    Log.d("Newly retrieved token", accessToken);
                    onSpreadSheetInitializedForAccount(oldSession, sheetId, accessToken);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    toLoginScreen();
                }
            }

            @Override
            public void onAccessTokenRetrieveFailed() {
                showError("Authentication error", "Can not re-login");
                mAuthenticationManager.destroySession();
                toLoginScreen();
            }
        };

        mDriveAccessDelegate.accessSpreadSheet(sheetServiceAccessAccountName,
                sheetId,
                this,
                callback);
    }

    private static class ViewWrapper {
        private TextView mTvNetworkInfo;
        private TextView mTvLoginActionInfo;
        private ProgressBar mPrbLoggingIn;

        public ViewWrapper(TextView tvNetworkInfo, TextView tvLoginActionInfo, ProgressBar prbLoggingIn) {
            mTvNetworkInfo = tvNetworkInfo;
            mTvLoginActionInfo = tvLoginActionInfo;
            mPrbLoggingIn = prbLoggingIn;
        }

        void showError() {
            mTvNetworkInfo.setText(SimplestValetApp.getContext().getString(R.string.tv_network_error));
            mTvNetworkInfo.setTextColor(Color.RED);
            mTvLoginActionInfo.setVisibility(View.GONE);
            mPrbLoggingIn.setVisibility(View.GONE);
        }

        void showOK(NetworkInfo networkInfo) {
            mTvNetworkInfo.setTextColor(Color.GREEN);
            Resources resources = SimplestValetApp.getContext().getResources();
            String networkInfoMessage = String.format(resources.getString(R.string.tv_connected_info), networkInfo.getTypeName());
            mTvNetworkInfo.setText(networkInfoMessage);
        }
    }

}
