package com.tbg.simplestvallet.app.container;

import com.tbg.simplestvallet.app.manager.authentication.abstr.ISVAuthenticationManager;

/**
 * Created by wws2003 on 11/3/15.
 */
public class AuthenticationManagerContainer {
    private ISVAuthenticationManager mAuthenticationManager;

    public ISVAuthenticationManager getAuthenticationManager() {
        return mAuthenticationManager;
    }

    public void setAuthenticationManager(ISVAuthenticationManager authenticationManager) {
        this.mAuthenticationManager = authenticationManager;
    }
}
