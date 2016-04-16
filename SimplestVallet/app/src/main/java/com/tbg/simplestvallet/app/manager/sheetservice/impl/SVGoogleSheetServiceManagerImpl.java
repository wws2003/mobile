package com.tbg.simplestvallet.app.manager.sheetservice.impl;

import com.tbg.simplestvallet.app.manager.authentication.SVCredential;
import com.tbg.simplestvallet.app.manager.sheetservice.abstr.ISVSheetServiceManager;
import com.tbg.simplestvallet.model.active.abstr.IEntrySheet;
import com.tbg.simplestvallet.model.active.impl.GoogleSpreadSheetBasedSheet;
import com.tbg.simplestvallet.persist.abstr.ISVPersistable;
import com.tbg.simplestvallet.persist.abstr.ISVPersistor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wws2003 on 11/5/15.
 */
public class SVGoogleSheetServiceManagerImpl implements ISVSheetServiceManager {

    private IEntrySheet mCachedSheet = null;
    private ISVPersistor mPersistor;

    public SVGoogleSheetServiceManagerImpl(ISVPersistor persistor) {
        this.mPersistor = persistor;
    }

    @Override
    public String loadSheetId(String accountName, String serviceToken) throws SVSheetNotFoundException {
        GoogleSheetPersistable persistable = new GoogleSheetPersistable();
        try {
            mPersistor.load(persistable);
            String sheetId = persistable.findSheetId(accountName, serviceToken);
            return sheetId;
        } catch (ISVPersistor.SVLoadException e) {
            e.printStackTrace();
            throw new SVSheetNotFoundException();
        }
    }

    @Override
    public void storeSheetId(String sheetId, String accountName, String serviceToken) {
        GoogleSheetPersistable persistable = new GoogleSheetPersistable();
        persistable.addAccount(accountName, serviceToken, sheetId);
        try {
            mPersistor.persist(persistable);
        } catch (ISVPersistor.SVPersistException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void accessSheet(String sheetId, String accountName, String serviceToken) throws SVSheetServiceNotAvailableException, SVSheetServiceUnAuthorizedException {
        //TODO Inspect more
        mCachedSheet = new GoogleSpreadSheetBasedSheet(sheetId, serviceToken);
    }

    @Override
    public IEntrySheet getSVEntrySheet() {
        return mCachedSheet;
    }

    private class GoogleSheetPersistable implements ISVPersistable {
        List<PersistPacket> mPersistPackets = new ArrayList<>();

        public void addAccount(String accountName, String accessToken, String sheetId) {
            mPersistPackets.add(getPersistPacket(accountName, accessToken, sheetId));
        }

        @Override
        public String getIdentifier() {
            return SVCredential.SERVICE_NAME_GOOGLE_DRIVE;
        }

        @Override
        public Iterable<PersistPacket> getIterable() {
            return mPersistPackets;
        }

        @Override
        public void loadFromIterable(Iterable<PersistPacket> iterable) {
            mPersistPackets.clear();
            Iterator<PersistPacket> iterator = iterable.iterator();
            if (iterator.hasNext()) {
                PersistPacket packet = iterator.next();
                mPersistPackets.add(packet);
            }
        }

        public String findSheetId(String accountName, String accessToken) {
            for(PersistPacket persistPacket : mPersistPackets) {
                String packetIdentifier = persistPacket.getIdentifier();
                if(packetIdentifier != null && packetIdentifier.equals(accountName)) {
                    String[] payload = persistPacket.getPayload();
                    if(payload != null && payload.length == 2) {
                        if (payload[0].equals(accessToken)) {
                            return payload[1];
                        }
                    }
                }
            }
            return null;
        }

        private PersistPacket getPersistPacket(String accountName, String accessToken, String sheetId) {
            String packetIdentifier = accountName;

            //Packet payload = [access token, sheet id]
            String[] payload = new String[2];
            payload[0] = accessToken;
            payload[1] = sheetId;
            return new PersistPacket(packetIdentifier, payload);
        }
    }
}
