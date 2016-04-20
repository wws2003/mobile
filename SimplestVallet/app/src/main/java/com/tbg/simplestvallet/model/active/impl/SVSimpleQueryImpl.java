package com.tbg.simplestvallet.model.active.impl;

import android.util.SparseArray;

import com.tbg.simplestvallet.model.active.abstr.ISVQuery;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by wws2003 on 4/20/16.
 */
public class SVSimpleQueryImpl<T> implements ISVQuery<T> {

    private SparseArray<List<Range<T>>> mRangesMap = new SparseArray<>();
    private SparseArray<List<T>> mValueMap = new SparseArray<>();

    public SVSimpleQueryImpl<T> addMatchingRanges(int fieldIndex, T lower, T upper) {
        Range<T> range = new Range<>(lower, upper);

        addElementsToMap(mRangesMap, fieldIndex, range);
        return this;
    }

    public SVSimpleQueryImpl<T> addMatchingValue(int fieldIndex, T value) {
        addElementsToMap(mValueMap, fieldIndex, value);
        return this;
    }

    @Override
    public void getAllMatchingRanges(SparseArray<List<Range<T>>> matchingRanges) {
        getAllMapElements(mRangesMap, matchingRanges);
    }

    @Override
    public void getMatchingRanges(int fieldIndex, List<Range<T>> matchingRanges) {
        getMapElement(mRangesMap, fieldIndex, matchingRanges);
    }

    @Override
    public void getAllMatchingValues(SparseArray<List<T>> matchingValues) {
        getAllMapElements(mValueMap, matchingValues);
    }

    @Override
    public void getMatchingValues(int fieldIndex, List<T> matchingValues) {
        getMapElement(mValueMap, fieldIndex, matchingValues);
    }

    @Override
    public boolean empty() {
        return mValueMap.size() == 0 && mRangesMap.size() == 0;
    }

    //MARK: Generic method for both mRangesMap and mValueMap
    private <E> void addElementsToMap(SparseArray<List<E>> map, int key, E value) {
        List<E> valuesList = map.get(key);
        if (valuesList == null) {
            valuesList = new LinkedList<>();
            valuesList.add(value);
            map.append(key, valuesList);
        }
        else {
            valuesList.add(value);
        }
    }

    private <E> void getAllMapElements(SparseArray<List<E>> srcMap, SparseArray<List<E>> destMap) {
        for(int keyIndex = 0; keyIndex < srcMap.size() - 1; keyIndex++) {
            int key = srcMap.keyAt(keyIndex);
            destMap.append(key, srcMap.valueAt(keyIndex));
        }
    }

    private <E> void getMapElement(SparseArray<List<E>> srcMap, int key, List<E> values) {
        List<E> value = srcMap.get(key);
        if (value != null) {
            values.addAll(value);
        }
    }
}
