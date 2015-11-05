package com.tbg.simplestvallet.activity;

import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.AccountPicker;
import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.app.SimplestValetApp;
import com.tbg.simplestvallet.app.container.SheetServiceManagerContainer;
import com.tbg.simplestvallet.app.manager.authentication.Credential;
import com.tbg.simplestvallet.app.manager.authentication.abstr.IAuthenticationManager;
import com.tbg.simplestvallet.app.manager.sheetservice.abstr.ISheetServiceManager;
import com.tbg.taskmanager.abstr.delegate.AbstractTaskResultListener;
import com.tbg.taskmanager.abstr.delegate.ITaskDelegate;
import com.tbg.taskmanager.abstr.executor.ITaskExecutor;
import com.tbg.taskmanager.abstr.task.AbstractTask;
import com.tbg.taskmanager.abstr.task.ITask;
import com.tbg.taskmanager.common.Result;
import com.tbg.taskmanager.impl.executor.AsyncTaskBasedTaskExecutorImpl;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CONFIRM_PERMISSION_GET_ACCOUNTS = 1;
    private static final int REQUEST_CODE_CHOOSE_GOOGLE_ACCOUNT = 2;

    private static final int REQUEST_CODE_INIT_GOOGLE_SHEET = 3;

    private IAuthenticationManager mAuthenticationManager = null;
    private SheetServiceManagerContainer mSheetServiceManagerContainer = null;
    private ITaskExecutor mTaskExecutor = null;

    private Button mDoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        mAuthenticationManager = SimplestValetApp.getAuthenticationManagerContainer().getAuthenticationManager();
        mTaskExecutor = new AsyncTaskBasedTaskExecutorImpl();
        mSheetServiceManagerContainer = SimplestValetApp.getSheetServiceManagerContainer();
        confirmGetAccountsPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent com.tbg.simplestvallet.activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        mDoneButton = (Button)findViewById(R.id.btn_done);
        mDoneButton.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_GOOGLE_ACCOUNT:
                if(resultCode == RESULT_OK) {
                    String selectedAccountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    onAccountNameSelected(selectedAccountName);
                }
                break;
            case REQUEST_CODE_INIT_GOOGLE_SHEET:
                if(resultCode == RESULT_OK) {
                    String spreadSheetId = data.getStringExtra(InitialSettingActivity.RESULT_KEY_SPREAD_SHEET_ID);
                    String googleDriveAccessToken = data.getStringExtra(InitialSettingActivity.RESULT_KEY_GOOGLE_DRIVE_ACCESS_TOKEN);
                    onSpreadSheetInitializedForAccount(spreadSheetId, googleDriveAccessToken);
                }
                else {
                    Log.d("Login.onActivityResult","Can't retrieve spreadsheet");
                }
                break;
            default:
                break;
        }
    }

    //Callback method
    public void onBtnChooseAccountClicked(View view) {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null,
                null,
                accountTypes,
                false,
                null,
                null,
                null,
                null);
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_GOOGLE_ACCOUNT);
    }

    //Callback method
    public void onBtnDoneClicked(View view) {
        onLoggedIn();
    }

    private void confirmGetAccountsPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkCallingOrSelfPermission("android.permission.GET_ACCOUNTS") != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{"android.permission.GET_ACCOUNTS"}, REQUEST_CODE_CONFIRM_PERMISSION_GET_ACCOUNTS);
            }
        }
    }

    private void onAccountNameSelected(String selectedAccountName) {
        TextView tvSelectedAccountName = (TextView)findViewById(R.id.tv_selected_email);
        tvSelectedAccountName.setText(selectedAccountName);
        mDoneButton.setEnabled(true);

        mAuthenticationManager.loginByAccountName(selectedAccountName);
    }

    private void onSpreadSheetInitializedForAccount(final String spreadSheetId, final String googleDriveAccessToken) {
        mAuthenticationManager.setServiceAccessToken(Credential.SERVICE_NAME_GOOGLE_DRIVE, googleDriveAccessToken);

        //Try to persist session before go to main screen
        ITask<Integer> persistSessionTask = new AbstractTask<Integer>() {
            @Override
            public Result<Integer> doExecute() {
                ISheetServiceManager sheetServiceManager = mSheetServiceManagerContainer.reloadSheetForService(Credential.SERVICE_NAME_GOOGLE_DRIVE);
                String accountName = mAuthenticationManager.getCredential().getSelectedAccountName();

                try {
                    sheetServiceManager.accessSheet(spreadSheetId, accountName, googleDriveAccessToken);
                } catch (ISheetServiceManager.SVSheetServiceNotAvailableException e) {
                    e.printStackTrace();
                } catch (ISheetServiceManager.SVSheetServiceUnAuthorizedException e) {
                    e.printStackTrace();
                }

                sheetServiceManager.storeSheetId(spreadSheetId, accountName, googleDriveAccessToken);
                mAuthenticationManager.persistSession();
                return generateResult(0, 0);
            }
        };
        ITaskDelegate<Integer> persistSessionTaskDelegate = new AbstractTaskResultListener<Integer>() {
            @Override
            public void onTaskExecuted(Result<Integer> taskResult) {
                toMainScreen();
            }
        };
        mTaskExecutor.executeTask(persistSessionTask, persistSessionTaskDelegate);
    }

    private void onLoggedIn() {
        toInitialSettingScreen();
    }

    private void toInitialSettingScreen() {
        Intent initialSettingIntent = new Intent(this, InitialSettingActivity.class);
        initialSettingIntent.putExtra(InitialSettingActivity.KEY_GOOGLE_ACCOUNT_NAME, mAuthenticationManager.getCredential().getSelectedAccountName());
        startActivityForResult(initialSettingIntent, REQUEST_CODE_INIT_GOOGLE_SHEET);
    }

    private void toMainScreen() {
        Intent mainIntent = new Intent();
        mainIntent.setClass(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
    }

}
