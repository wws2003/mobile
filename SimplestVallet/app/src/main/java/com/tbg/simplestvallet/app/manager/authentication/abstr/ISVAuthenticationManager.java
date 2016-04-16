package com.tbg.simplestvallet.app.manager.authentication.abstr;

import com.tbg.simplestvallet.app.manager.authentication.SVCredential;

/**
 * Created by wws2003 on 10/2/15.
 */
public interface ISVAuthenticationManager {

    //Try to login to an old session, normally by using a stored token
    ISVSession getOldSession() throws ISVSession.SVSessionNotFound;

    //Select an account name to login. The "login" here also includes sign up meaning
    //Exception is declared but just for preserving purpose
    void loginByDeviceAccountName(String selectedAccountName) throws SVLoginException;

    //Get current session
    ISVSession getCurrentSession();

    //Persist current authentication info like account name, token... May be done in background
    void persistSession();

    //Delete current authentication info like account name, token..., both in memory and storage. May be done in background
    void destroySession();

    class SVLoginException extends Exception {
        private static final String MESSAGE = "Login or sign up error";

        public SVLoginException(String causeMessage) {
            super(MESSAGE + " due to " + causeMessage);
        }
    }

}
