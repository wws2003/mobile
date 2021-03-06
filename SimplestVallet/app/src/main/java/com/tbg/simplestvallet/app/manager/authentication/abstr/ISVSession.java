package com.tbg.simplestvallet.app.manager.authentication.abstr;

import com.tbg.simplestvallet.app.manager.authentication.SVCredential;
import com.tbg.simplestvallet.persist.abstr.ISVPersistable;

import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * Created by wws2003 on 4/10/16.
 */
public interface ISVSession extends ISVPersistable {

    //FIXME: Other types than string for attribute value ?
    String ATTRIBUTE_KEY_SHEET_SERVICE_NAME = "0_sheet_service_name";
    String ATTRIBUTE_KEY_LOGIN_ACCOUNT_NAME = "1_login_account_name";
    String ATTRIBUTE_KEY_AUTO_RE_LOGIN = "2_auto_re_login";

    String getAttributeValue(String attributeName) throws SVInvalidatedSessionException;
    void putAttribute(String attributeKey, String attributeValue) throws SVInvalidatedSessionException;

    long getCreatedTime();
    long getLastAccessedTime();

    boolean isExpired();

    //Make the session invalidate and unbound any attributes (when logout...)
    void invalidate();

    //Get account name bound for this session. Probably just a shortcut for getAttribute(ATTRIBUTE_KEY_LOGIN_ACCOUNT_NAME)
    String getLoggedInAccountName() throws SVInvalidatedSessionException;

    //Return if user allowed to re-login without providing service name, account name again. Probably just a shortcut for getAttribute(ATTRIBUTE_KEY_AUTO_RE_LOGIN)
    boolean isAutoReLoginPermitted() throws SVInvalidatedSessionException;

    void permitAutoReLogin() throws SVInvalidatedSessionException;

    //Is this really good to bind session and credential ?
    void putCredentialServiceAccessToken(String serviceName, String accountName, String serviceAccessToken) throws SVInvalidatedSessionException;
    SVCredential getCredential() throws SVInvalidatedSessionException;

    //MARK: For some exceptions
    class SVInvalidatedSessionException extends Exception {
        private static final String MESSAGE = "Session invalidated";

        public SVInvalidatedSessionException() {
            super(MESSAGE);
        }
    }

    class SVSessionNotFound extends Exception {
        private static final String MESSAGE = "Session not found";

        public SVSessionNotFound(Throwable throwable) {
            super(MESSAGE, throwable);
        }
    }

    class SVSessionExpiredException extends Exception {
        private static final String MESSAGE = "Session expired";

        public SVSessionExpiredException(Throwable throwable) {
            super(MESSAGE, throwable);
        }
    }
}
