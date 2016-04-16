package com.tbg.simplestvallet.app.manager.sheetservice.abstr;

import com.tbg.simplestvallet.model.active.abstr.IEntrySheet;

/**
 * Created by wws2003 on 11/5/15.
 */
public interface ISVSheetServiceManager {
    String loadSheetId(String accountName, String serviceToken) throws SVSheetNotFoundException;

    void storeSheetId(String sheetId, String accountName, String serviceToken);

    //FIXME: This looks like to violate Single Responsibility Principle. Subject to move to other class
    void accessSheet(String sheetId, String accountName, String serviceToken) throws SVSheetServiceNotAvailableException, SVSheetServiceUnAuthorizedException;

    //FIXME: This looks like to violate Single Responsibility Principle. Subject to move to other class
    IEntrySheet getSVEntrySheet();

    class SVSheetNotFoundException extends Exception {
        private static final String MESSAGE = "Sheet not found";

        public SVSheetNotFoundException() {
            super(MESSAGE);
        }
    }

    class SVSheetServiceNotAvailableException extends Exception {
        private static final String MESSAGE = "Sheet service not available";

        public SVSheetServiceNotAvailableException() {
            super(MESSAGE);
        }
    }

    class SVSheetServiceUnAuthorizedException extends Exception {
        private static final String MESSAGE = "You are not authorized to use the service";

        public SVSheetServiceUnAuthorizedException() {
            super(MESSAGE);
        }
    }
}
