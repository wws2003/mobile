package com.tbg.simplestvallet.app.manager.authentication.abstr;

import com.tbg.simplestvallet.app.manager.authentication.Credential;

/**
 * Created by wws2003 on 10/2/15.
 */
public interface IAuthenticationManager {

    //Try to login to an old session, normally by using a stored token
    void loginToOldSession() throws SVAccountNotFoundException, SVCredentialNotFoundException;

    //Select an account name to login. The "login" here also includes signup meaning
    void loginByAccountName(String selectedAccountName);

    void setServiceAccessToken(String serviceName, String serviceAccessToken);

    //Retrieve the current credential object
    Credential getCredential();

    //Persist current authentication info like account name, token... May be done in background
    void persistSession();

    //Delete current authentication info like account name, token..., both in memory and storage. May be done in background
    void destroySession();

    class SVAccountNotFoundException extends Exception {
        private static final String MESSAGE = "Account not found";
        public SVAccountNotFoundException() {
            super(MESSAGE);
        }

        public SVAccountNotFoundException(Throwable throwable) {
            super(MESSAGE, throwable);
        }
    }

    class SVCredentialNotFoundException extends Exception {
        private static final String MESSAGE = "Account not found";

        public SVCredentialNotFoundException() {
            super(MESSAGE);
        }

        public SVCredentialNotFoundException(Throwable throwable) {
            super(MESSAGE, throwable);
        }
    }

}
