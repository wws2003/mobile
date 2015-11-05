package com.tbg.simplestvallet.app.manager.sheetservice.abstr;

import com.tbg.simplestvallet.model.active.abstr.IEntrySheet;

/**
 * Created by wws2003 on 11/5/15.
 */
public interface ISheetServiceManager {
    String getSheetId(String accountName, String serviceToken) throws SVSheetNotFoundException;

    void storeSheetId(String sheetId, String accountName, String serviceToken);

    void accessSheet(String sheetId, String accountName, String serviceToken) throws SVSheetServiceNotAvailableException, SVSheetServiceUnAuthorizedException;

    IEntrySheet getSVEntrySheet();

    class SVSheetNotFoundException extends Exception {
        private static final String MESSAGE = "Sheet not found";

        public SVSheetNotFoundException() {
            super(MESSAGE);
        }

        public SVSheetNotFoundException(Throwable throwable) {
            super(MESSAGE, throwable);
        }
    }

    class SVSheetServiceNotAvailableException extends Exception {
        private static final String MESSAGE = "Sheet service not available";

        public SVSheetServiceNotAvailableException() {
            super(MESSAGE);
        }

        public SVSheetServiceNotAvailableException(Throwable throwable) {
            super(MESSAGE, throwable);
        }
    }

    class SVSheetServiceUnAuthorizedException extends Exception {
        private static final String MESSAGE = "You are not authorized to use the service";

        public SVSheetServiceUnAuthorizedException() {
            super(MESSAGE);
        }

        public SVSheetServiceUnAuthorizedException(Throwable throwable) {
            super(MESSAGE, throwable);
        }
    }
}
