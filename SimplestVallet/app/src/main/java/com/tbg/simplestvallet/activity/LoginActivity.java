package com.tbg.simplestvallet.activity;

import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.AccountPicker;
import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.app.authen.abstr.IAuthenticationManager;
import com.tbg.simplestvallet.app.authen.impl.SampleAuthenticationManager;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CONFIRM_PERMISSION_GET_ACCOUNTS = 1;
    private static final int REQUEST_CODE_CHOOSE_GOOGLE_ACCOUNT = 2;

    private IAuthenticationManager mAuthenticationManager = SampleAuthenticationManager.newAuthenticationManager();

    private String mSelectedAccountName;

    private Button mDoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        confirmGetAccountsPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tryToLogin();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_GOOGLE_ACCOUNT:
                if(resultCode == RESULT_OK) {
                    String selectedAccountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    onAccountNameSelected(selectedAccountName);
                    onLoggedIn();
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
        mSelectedAccountName = selectedAccountName;
        TextView tvSelectedAccountName = (TextView)findViewById(R.id.tv_selected_email);
        tvSelectedAccountName.setText(selectedAccountName);
        mDoneButton.setEnabled(true);

        //TODO Review
        mAuthenticationManager.loginByAccountName(selectedAccountName);
    }

    private void initViews() {
        mDoneButton = (Button)findViewById(R.id.btn_done);
        mDoneButton.setEnabled(false);
    }

    private void tryToLogin() {
        String authToken = "sss";//TODO Retrieve token
        int resultCode = mAuthenticationManager.loginByToken(authToken);
        if(resultCode == IAuthenticationManager.LOGIN_RESULT_OK) {
            onLoggedIn();
        }
    }

    private void onLoggedIn() {
        Intent mainIntent = new Intent();
        mainIntent.setClass(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
    }

}
