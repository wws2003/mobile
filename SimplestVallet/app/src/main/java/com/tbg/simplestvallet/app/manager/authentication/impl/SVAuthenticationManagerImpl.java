package com.tbg.simplestvallet.app.manager.authentication.impl;

import com.tbg.simplestvallet.app.manager.authentication.abstr.ISVAuthenticationManager;
import com.tbg.simplestvallet.app.manager.authentication.abstr.ISVSession;
import com.tbg.simplestvallet.app.manager.authentication.abstr.ISVSession.SVSessionNotFound;
import com.tbg.simplestvallet.persist.abstr.ISVPersistor;

/**
 * Created by wws2003 on 4/10/16.
 */
public class SVAuthenticationManagerImpl implements ISVAuthenticationManager {

    private ISVSession mCurrentSession = null;
    private ISVPersistor mSessionPersistor;

    public SVAuthenticationManagerImpl(ISVPersistor sessionPersistor) {
        mSessionPersistor = sessionPersistor;
    }

    @Override
    public ISVSession getOldSession() throws SVSessionNotFound {
        try {
            ISVSession oldSession = loadOldSession();
            if(oldSession != null) {
                mCurrentSession = oldSession;
            }
            return oldSession;
        }
        catch (ISVPersistor.SVLoadException e) {
            throw new SVSessionNotFound(e);
        }
    }

    @Override
    public void loginByDeviceAccountName(String selectedAccountName) throws SVLoginException {
        mCurrentSession = newSession();
        try {
            mCurrentSession.putAttribute(ISVSession.ATTRIBUTE_KEY_LOGIN_ACCOUNT_NAME, selectedAccountName);
        } catch (ISVSession.SVInvalidatedSessionException e) {
            throw new SVLoginException(e.getLocalizedMessage());
        }
    }

    @Override
    public ISVSession getCurrentSession() {
        return mCurrentSession;
    }

    @Override
    public void persistSession() {
        try {
            mSessionPersistor.persist(mCurrentSession);
        } catch (ISVPersistor.SVPersistException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroySession() {
        mCurrentSession.invalidate();
        try {
            mSessionPersistor.delete(mCurrentSession);
        } catch (ISVPersistor.SVDeleteException e) {
            e.printStackTrace();
        }
    }

    private ISVSession loadOldSession() throws ISVPersistor.SVLoadException {
        ISVSession oldSession = new SVSessionImpl();
        mSessionPersistor.load(oldSession);
        return oldSession;
    }

    private ISVSession newSession() {
        return new SVSessionImpl();
    }
}
