package com.tbg.simplestvallet.model.active.abstr;

/**
 * Created by wws2003 on 11/3/15.
 */
public interface IGoogleSpreadSheetRowFilter<T> {
    boolean filterRowItem(T item);
}
