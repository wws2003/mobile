package com.tbg.simplestvallet.app.container;

import com.tbg.simplestvallet.app.manager.authentication.abstr.IAuthenticationManager;

/**
 * Created by wws2003 on 11/3/15.
 */
public class AuthenticationManagerContainer {
    private IAuthenticationManager mAuthenticationManager;

    public IAuthenticationManager getAuthenticationManager() {
        return mAuthenticationManager;
    }

    public void setAuthenticationManager(IAuthenticationManager authenticationManager) {
        this.mAuthenticationManager = authenticationManager;
    }
}
