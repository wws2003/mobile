package com.tbg.simplestvallet.app.authen.impl;

import com.tbg.simplestvallet.app.authen.Credential;
import com.tbg.simplestvallet.app.authen.abstr.IAuthenticationManager;

/**
 * Created by wws2003 on 10/2/15.
 */
public class SampleAuthenticationManager implements IAuthenticationManager {

    private SampleAuthenticationManager(){};

    private Credential mCredential;

    public static IAuthenticationManager newAuthenticationManager() {
        return new SampleAuthenticationManager();
    }

    @Override
    public int loginByToken(String authToken)  {
        return LOGIN_FAILED_TOKEN_EXPIRE;
    }

    @Override
    public void loginByAccountName(String selectedAccountName) {
        String token = "DFSDSDF"; //TODO Generate token
        mCredential = new Credential.Builder()
                .setAuthToken(token)
                .setSelectedAccountName(selectedAccountName)
                .build();
    }

    @Override
    public void setServiceAccessToken(String serviceName, String serviceAccessToken) {
        mCredential = new Credential.Builder(mCredential)
                .setSelectedAccountName(mCredential.getSelectedAccountName())
                .setAuthToken(mCredential.getAuthToken())
                .setServiceAccessToken(serviceName, serviceAccessToken)
                .build();
    }

    @Override
    public Credential getCredential() {
        return mCredential;
    }
}
