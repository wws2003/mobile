package com.tbg.simplestvallet.model.active.abstr.collection.google;

import com.google.gdata.data.spreadsheet.ListEntry;

import java.util.List;

/**
 * Created by wws2003 on 11/3/15.
 */
public interface ISVGoogleSpreadSheetRowAdapter<T> {
    T getItemFromRow(ListEntry row);
    ListEntry getRowFromItem(T item);
    void getSheetHeaders(List<String> headers);
}
