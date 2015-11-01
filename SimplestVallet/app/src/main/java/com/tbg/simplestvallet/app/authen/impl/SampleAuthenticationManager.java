package com.tbg.simplestvallet.app.authen.impl;

import com.tbg.simplestvallet.app.authen.Credential;
import com.tbg.simplestvallet.app.authen.abstr.IAuthenticationManager;

/**
 * Created by wws2003 on 10/2/15.
 */
public class SampleAuthenticationManager implements IAuthenticationManager {

    private SampleAuthenticationManager(){};

    public static IAuthenticationManager newAuthenticationManager() {
        return new SampleAuthenticationManager();
    }

    @Override
    public int loginByToken(String authToken)  {
        return LOGIN_FAILED_TOKEN_EXPIRE;
    }

    @Override
    public void loginByAccountName(String selectedAccountName) {
        //TODO Implements
    }

    @Override
    public Credential getCredential() {
        return null;
    }
}
