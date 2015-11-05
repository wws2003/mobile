package com.tbg.simplestvallet.app.manager.authentication;

/**
 * Created by wws2003 on 10/2/15.
 */
public class Credential {
    public static final String SERVICE_NAME_GOOGLE_DRIVE = "google_drive";
    public static final String SERVICE_NAME_LOCAL = "local";

    private String mSelectedAccountName;
    private String mAuthToken;

    private String mGoogleDriveAcessToken;

    private Credential() {

    }

    public String getSelectedAccountName() {
        return mSelectedAccountName;
    }

    public String getAuthToken() {
        return mAuthToken;
    }

    public String getServiceAccessToken(String serviceName) {
        //TODO Implement properly for other service
        if(serviceName.equals(SERVICE_NAME_GOOGLE_DRIVE)) {
            return mGoogleDriveAcessToken;
        }
        return null;
    }

    public static class Builder {
        private Credential mCredential = new Credential();

        public Builder() {

        }

        public Builder(Credential credential) {
            mCredential = credential;
        }

        public Builder setSelectedAccountName(String selectedAccountName) {
            mCredential.mSelectedAccountName = selectedAccountName;
            return new Builder(mCredential);
        }

        public Builder setAuthToken(String token) {
            mCredential.mAuthToken = token;
            return new Builder(mCredential);
        }

        public Builder setServiceAccessToken(String serviceName, String token) {
            if (Credential.SERVICE_NAME_GOOGLE_DRIVE.equals(serviceName)) {
                mCredential.mGoogleDriveAcessToken = token;
                return new Builder(mCredential);
            }
            else {
                return this;
            }
        }

        public Credential build() {
            return mCredential;
        }
    }

}
