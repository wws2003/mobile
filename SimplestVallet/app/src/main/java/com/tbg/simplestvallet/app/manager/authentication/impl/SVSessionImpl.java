package com.tbg.simplestvallet.app.manager.authentication.impl;

import com.tbg.simplestvallet.app.manager.authentication.SVCredential;
import com.tbg.simplestvallet.app.manager.authentication.abstr.ISVSession;
import com.tbg.simplestvallet.util.DateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wws2003 on 4/11/16.
 */
//SharedPreferences resembles session, whereas shared preference editor resembles session persistor ?

public class SVSessionImpl implements ISVSession {

    private static final String SV_SESSION_PERSISTABLE_IDENTIFIER = "0_sv_session_id";

    private long mCreatedTime;
    private long mLastAccessedTime;
    private boolean mIsValidate;
    private Map<String, String> mAttributesMap;
    private SVCredential.Builder mCredentialBuilder;

    public SVSessionImpl() {
        mCreatedTime = DateUtil.getCurrentTime();
        mIsValidate = true;
        mAttributesMap = new HashMap<>();
        mCredentialBuilder = new SVCredential.Builder();
    }

    @Override
    public String getAttributeValue(String attributeName) throws SVInvalidatedSessionException {
        tryToAccess();
        return mAttributesMap.get(attributeName);
    }

    @Override
    public void putAttribute(String attributeKey, String attributeValue) throws SVInvalidatedSessionException {
        tryToAccess();
        mAttributesMap.put(attributeKey, attributeValue);
    }

    @Override
    public long getCreatedTime() {
        return mCreatedTime;
    }

    @Override
    public long getLastAccessedTime() {
        return mLastAccessedTime;
    }

    @Override
    public void invalidate() {
        mIsValidate = false;
        mAttributesMap.clear();
    }

    @Override
    public String getLoggedInAccountName() throws SVInvalidatedSessionException {
        tryToAccess();
        return mAttributesMap.get(ATTRIBUTE_KEY_LOGIN_ACCOUNT_NAME);
    }

    @Override
    public boolean isAutoReLoginPermitted() throws SVInvalidatedSessionException {
        tryToAccess();
        return "TRUE".equals(mAttributesMap.get(ATTRIBUTE_KEY_AUTO_RE_LOGIN));
    }

    @Override
    public void permitAutoReLogin() throws SVInvalidatedSessionException {
        putAttribute(ATTRIBUTE_KEY_AUTO_RE_LOGIN, "TRUE");
    }

    @Override
    public void putCredentialServiceAccessToken(String serviceName, String accountName, String serviceAccessToken) throws SVInvalidatedSessionException {
        tryToAccess();
        mCredentialBuilder.configureService(serviceName, accountName, serviceAccessToken);
    }

    @Override
    public SVCredential getCredential() throws SVInvalidatedSessionException {
        tryToAccess();
        return mCredentialBuilder.build();
    }

    //Like getting memento from originator
    /*
     ------------------------MARK: Methods to conform to IPersistable------------------------
     */
    @Override
    public String getIdentifier() {
        return SV_SESSION_PERSISTABLE_IDENTIFIER;
    }

    @Override
    public Iterable<PersistPacket> getIterable() {
        InternalSessionIterable iterable = new InternalSessionIterable();
        iterable.constructFromOuter();
        return iterable;
    }

    //Like saving back a memento object
    @Override
    public void loadFromIterable(Iterable<PersistPacket> iterable) {
        reinitialize();
        InternalSessionIterable internalIterator = new InternalSessionIterable();
        internalIterator.constructOuter(iterable);
    }

    /*
     ------------------------MARK:Private methods------------------------
     */

    private void tryToAccess() throws SVInvalidatedSessionException {
        if (!mIsValidate) {
            throw new SVInvalidatedSessionException();
        }
        //Update last access time also
        mLastAccessedTime = DateUtil.getCurrentTime();
    }

    private void reinitialize() {
        mAttributesMap.clear();
        mCreatedTime = 0;
        mLastAccessedTime = 0;
        mIsValidate = true;
        mCredentialBuilder = new SVCredential.Builder();
    }

    /*
     ------------------------MARK:Private class------------------------
     */

    private class InternalSessionIterable implements Iterable<PersistPacket> {
        private static final String SV_SESSION_ATTRIBUTE_IDENTIFIER = "1_sv_session_att_id";
        private static final String SV_SESSION_TIMESTAMP_CREATED_IDENTIFIER = "2_sv_session_tsp_crt_id";
        private static final String SV_SESSION_TIMESTAMP_LAST_ACCESSED_IDENTIFIER = "3_sv_session_tsp_acc_id";
        private static final String SV_SESSION_CREDENTIAL_IDENTIFIER = "3_sv_session_crd_id";

        private List<PersistPacket> mInternalAttributes = new ArrayList<>();

        @Override
        public Iterator<PersistPacket> iterator() {
            return mInternalAttributes.iterator();
        }

        public void constructFromOuter() {
            loadOuterAttributes();
            loadOuterTimeStampInfo();
            loadOuterCredential();
        }

        public void constructOuter(Iterable<PersistPacket> iterable) {
            restoreAll(iterable);
        }

        private void loadOuterAttributes() {
            for (String key : mAttributesMap.keySet()) {
                String value = mAttributesMap.get(key);
                String[] pair = new String[2];
                pair[0] = key;
                pair[1] = value;
                mInternalAttributes.add(new PersistPacket(SV_SESSION_ATTRIBUTE_IDENTIFIER, pair));
            }
        }

        private void loadOuterCredential() {
            List<String> credentialServiceNames = new ArrayList<>();
            SVCredential credential = mCredentialBuilder.build();
            credential.getAllServiceNames(credentialServiceNames);

            for(String credentialServiceName : credentialServiceNames) {
                String accountName = credential.getServiceAccountName(credentialServiceName);
                String accessToken = credential.getServiceAccessToken(credentialServiceName);

                String[] payload = new String[3];
                payload[0] = credentialServiceName;
                payload[1] = accountName;
                payload[2] = accessToken;

                mInternalAttributes.add(new PersistPacket(SV_SESSION_CREDENTIAL_IDENTIFIER, payload));
            }
        }

        private void loadOuterTimeStampInfo() {
            //Created time
            String[] createdTimePair = new String[1];
            createdTimePair[0] = String.valueOf(mCreatedTime);
            mInternalAttributes.add(new PersistPacket(SV_SESSION_TIMESTAMP_CREATED_IDENTIFIER, createdTimePair));

            //Last accessed time
            String[] lastAccessTimePair = new String[1];
            lastAccessTimePair[0] = String.valueOf(mLastAccessedTime);
            mInternalAttributes.add(new PersistPacket(SV_SESSION_TIMESTAMP_LAST_ACCESSED_IDENTIFIER, lastAccessTimePair));
        }

        private void restoreAll(Iterable<PersistPacket> iterable) {
            Iterator<PersistPacket> iterator = iterable.iterator();
            while (iterator.hasNext()) {

                PersistPacket packet = iterator.next();

                String identifier = packet.getIdentifier();
                String[] payload = packet.getPayload();

                if(identifier.equals(SV_SESSION_ATTRIBUTE_IDENTIFIER)) {
                    String key = payload[0];
                    String value = payload[1];
                    mAttributesMap.put(key, value);
                }
                if(identifier.equals(SV_SESSION_TIMESTAMP_CREATED_IDENTIFIER)) {
                    String value = payload[0];
                    mCreatedTime = Long.parseLong(value);
                }
                if(identifier.equals(SV_SESSION_TIMESTAMP_LAST_ACCESSED_IDENTIFIER)) {
                    String value = payload[0];
                    mLastAccessedTime = Long.parseLong(value);
                }
                if(identifier.equals(SV_SESSION_CREDENTIAL_IDENTIFIER)) {
                    String serviceName = payload[0];
                    String accountName = payload[1];
                    String accessToken = payload[2];
                    mCredentialBuilder.configureService(serviceName, accountName, accessToken);
                }
            }
        }
    }
}
