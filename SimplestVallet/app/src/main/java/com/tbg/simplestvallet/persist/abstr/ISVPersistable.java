package com.tbg.simplestvallet.persist.abstr;

/**
 * Created by wws2003 on 4/14/16.
 */

//Work as a memento object
public interface ISVPersistable {
    String getIdentifier();

    //Iterable<PersistPacket> should be considered as an immutable memento from the orginator
    Iterable<PersistPacket> getIterable();

    void loadFromIterable(Iterable<PersistPacket> iterable);

    class PersistPacket {
        private String mIdentifier;
        private String[] mPayload;

        public PersistPacket(String identifier, String[] payload) {
            mIdentifier = identifier;
            mPayload = payload;
        }

        public String getIdentifier() {
            return mIdentifier;
        }

        public String[] getPayload() {
            return mPayload;
        }
    }

}
