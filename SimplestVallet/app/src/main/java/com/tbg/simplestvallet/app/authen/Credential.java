package com.tbg.simplestvallet.app.authen;

/**
 * Created by wws2003 on 10/2/15.
 */
public class Credential {
    private String mSelectedAccountName;
    private String mAuthToken;

    private Credential() {

    }

    public String getSelectedAccountName() {
        return mSelectedAccountName;
    }

    public String getAuthToken() {
        return mAuthToken;
    }

    public void reset() {
        mAuthToken = null;
        mSelectedAccountName = null;
    }

    public static class Builder {
        private Credential mCredential = new Credential();

        private Builder(Credential credential) {
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

        public Credential build() {
            return mCredential;
        }
    }

}
