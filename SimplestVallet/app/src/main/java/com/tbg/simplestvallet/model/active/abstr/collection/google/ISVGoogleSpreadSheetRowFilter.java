package com.tbg.simplestvallet.model.active.abstr.collection.google;

/**
 * Created by wws2003 on 11/3/15.
 */
public interface ISVGoogleSpreadSheetRowFilter<T> {
    boolean filterRowItem(T item);
}
