package com.tbg.simplestvallet.activity;

import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.AccountPicker;
import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.app.SimplestValetApp;
import com.tbg.simplestvallet.app.authen.Credential;
import com.tbg.simplestvallet.app.authen.abstr.IAuthenticationManager;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CONFIRM_PERMISSION_GET_ACCOUNTS = 1;
    private static final int REQUEST_CODE_CHOOSE_GOOGLE_ACCOUNT = 2;

    private static final int REQUEST_CODE_INIT_GOOGLE_SHEET = 3;

    private IAuthenticationManager mAuthenticationManager = null;

    private Button mDoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        mAuthenticationManager = SimplestValetApp.getAuthenticationManagerContainer().getAuthenticationManager();
        confirmGetAccountsPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void onSpreadSheetInitializedForAccount(String spreadSheetId, String googleDriveAccessToken) {
        mAuthenticationManager.setServiceAccessToken(Credential.SERVICE_NAME_GOOGLE_DRIVE, googleDriveAccessToken);
        Credential credential = mAuthenticationManager.getCredential();
        SimplestValetApp.reloadEntrySheetForCredential(credential, spreadSheetId);
        toMainScreen();
    }

    /*private void tryToLogin() {
        String authToken = mAuthenticationManager.getCredential().getAuthToken();
        int resultCode = mAuthenticationManager.loginByToken(authToken);
        if(resultCode == IAuthenticationManager.LOGIN_RESULT_OK) {
            onLoggedIn();
        }
    }*/

    private void onLoggedIn() {
        boolean hasInitialSettingDone = false; //TODO Evaluate this value

        if(hasInitialSettingDone) {
            toMainScreen();
        }
        else {
            toInitialSettingScreen();
        }
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
