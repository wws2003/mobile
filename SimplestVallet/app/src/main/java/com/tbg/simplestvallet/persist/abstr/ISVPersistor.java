package com.tbg.simplestvallet.persist.abstr;

/**
 * Created by wws2003 on 4/14/16.
 */
public interface ISVPersistor {

    void persist(ISVPersistable persistable) throws SVPersistException;

    void load(ISVPersistable persistable) throws SVLoadException;

    void delete(ISVPersistable persistable) throws SVDeleteException;

    class SVPersistException extends Exception {
        private static final String MESSAGE = "Can't persist object";

        public SVPersistException() {
            super(MESSAGE);
        }

        public SVPersistException(Throwable throwable) {
            super(MESSAGE, throwable);
        }
    }

    class SVLoadException extends Exception {
        private static final String MESSAGE = "Can't load object";

        public SVLoadException() {
            super(MESSAGE);
        }

        public SVLoadException(Throwable throwable) {
            super(MESSAGE, throwable);
        }
    }

    class SVDeleteException extends Exception {
        private static final String MESSAGE = "Can't delete object";

        public SVDeleteException() {
            super(MESSAGE);
        }
    }
}
