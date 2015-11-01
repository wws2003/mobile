package com.tbg.simplestvallet.model.active;

/**
 * Created by wws2003 on 10/12/15.
 */
public class EntryActionResult {
    public static final int ADD_RESULT_OK = 0;
    public static final int ADD_RESULT_PENDED = 1;
    public static final int ADD_RESULT_INVALID = 2;

    public static final int RETRIEVE_RESULT_OK = 3;
    public static final int RETRIEVE_RESULT_NOT_FOUND = 4;

    public static final int DELETE_RESULT_OK = 5;
    public static final int DELETE_RESULT_NOT_FOUND = 6;
}
