package com.tbg.simplestvallet.app.authen.abstr;

import com.tbg.simplestvallet.app.authen.Credential;

/**
 * Created by wws2003 on 10/2/15.
 */
public interface IAuthenticationManager {

    int LOGIN_RESULT_OK = 0;
    int LOGIN_RESULT_FAILED_UNKNOWN = 1;
    int LOGIN_FAILED_TOKEN_EXPIRE = -1;
    int LOGIN_FAILED_TOKEN_NOT_FOUND = -2;

    //Try to login by a token, normally read from storage or DB. Return one of the result code above
    int loginByToken(String authToken);

    //Select an account name to login
    void loginByAccountName(String selectedAccountName);

    void setServiceAccessToken(String serviceName, String serviceAccessToken);

    //Retrieve the current credential object
    Credential getCredential();

}
