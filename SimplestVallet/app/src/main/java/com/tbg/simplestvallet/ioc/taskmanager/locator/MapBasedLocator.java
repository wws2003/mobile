package com.tbg.simplestvallet.ioc.taskmanager.locator;

import com.tbg.taskmanager.abstr.locator.ILocator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wws2003 on 10/30/15.
 */
public class MapBasedLocator<T> implements ILocator<T> {

    private Map<Long, T> mItemMap = Collections.synchronizedMap(new HashMap<Long, T>());

    @Override
    public void pushItem(T item, long itemId) throws Exception {
        mItemMap.put(itemId, item);
    }

    @Override
    public T getItem(long itemId) {
        return mItemMap.get(itemId);
    }

    @Override
    public void removeItem(long itemId) throws Exception {
        mItemMap.remove(itemId);
    }

    @Override
    public void clearAllItems() {
        mItemMap.clear();
    }
}
