package com.tbg.simplestvallet.app.manager.authentication.impl;

import com.tbg.simplestvallet.app.manager.authentication.Credential;
import com.tbg.simplestvallet.app.manager.authentication.abstr.IAuthenticationManager;
import com.tbg.simplestvallet.app.preference.abstr.IPreferenceOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wws2003 on 10/2/15.
 */
public class SampleAuthenticationManager implements IAuthenticationManager {

    private Credential mCredential;
    private IPreferenceOperator mPreferenceOperator;

    private static final String KEY_ACCOUNT_NAME = "NAME_ACCOUNT_KEY";
    private static final String KEY_ACCOUNT_TOKEN = "TOKEN_KEY_ACCOUNT";
    private static final String POSTFIX_KEY_ACCOUNT_SERVICE_NAME = "_SERVICE_NAME";
    private static final String POSTFIX_KEY_ACCOUNT_SERVICE_TOKEN = "_TOKEN_SERVICE";

    public SampleAuthenticationManager(IPreferenceOperator preferenceOperator) {
        this.mPreferenceOperator = preferenceOperator;
    }

    @Override
    public void loginToOldSession() throws SVAccountNotFoundException, SVCredentialNotFoundException {

        //TODO Encapsulate account persistence info !!
        String accountName = mPreferenceOperator.get(KEY_ACCOUNT_NAME);

        if(accountName == null) {
            throw new SVAccountNotFoundException();
        }

        String accountToken = mPreferenceOperator.get(KEY_ACCOUNT_TOKEN);
        String serviceName = mPreferenceOperator.get(getServiceNameKey(accountName));
        String serviceAccessToken = mPreferenceOperator.get(getServiceTokenKey(accountName, serviceName));

        if(accountToken == null || serviceName == null || serviceAccessToken == null) {
            throw new SVCredentialNotFoundException();
        }

        mCredential = new Credential.Builder()
                .setSelectedAccountName(accountName)
                .setAuthToken(accountToken)
                .setServiceAccessToken(serviceName, serviceAccessToken)
                .build();
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
    public void persistSession() {
        String accountName = mCredential.getSelectedAccountName();
        String token = mCredential.getAuthToken();
        String serviceToken = mCredential.getServiceAccessToken(Credential.SERVICE_NAME_GOOGLE_DRIVE);

        //TODO Use current service name rather than SERVICE_NAME_GOOGLE_DRIVE
        mPreferenceOperator.store(KEY_ACCOUNT_NAME, accountName);
        mPreferenceOperator.store(KEY_ACCOUNT_TOKEN, token);
        mPreferenceOperator.store(getServiceNameKey(accountName), Credential.SERVICE_NAME_GOOGLE_DRIVE);
        mPreferenceOperator.store(getServiceTokenKey(accountName, Credential.SERVICE_NAME_GOOGLE_DRIVE), serviceToken);
    }

    @Override
    public void destroySession() {
        String accountName = mCredential.getSelectedAccountName();

        //TODO Use current service name rather than SERVICE_NAME_GOOGLE_DRIVE
        mPreferenceOperator.delete(KEY_ACCOUNT_NAME);
        mPreferenceOperator.delete(KEY_ACCOUNT_TOKEN);
        mPreferenceOperator.delete(getServiceNameKey(accountName));
        mPreferenceOperator.delete(getServiceTokenKey(accountName, Credential.SERVICE_NAME_GOOGLE_DRIVE));
    }

    @Override
    public Credential getCredential() {
        return mCredential;
    }

    private String getServiceNameKey(String accountName) {
        //TODO More powerful algorithm
        return accountName + POSTFIX_KEY_ACCOUNT_SERVICE_NAME;
    }

    private String getServiceTokenKey(String accountName, String serviceName) {
        //TODO More powerful algorithm
        return accountName + "." + serviceName + POSTFIX_KEY_ACCOUNT_SERVICE_TOKEN;
    }
}
