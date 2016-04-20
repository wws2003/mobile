package com.tbg.simplestvallet.model.active.abstr;

import android.util.SparseArray;

import java.util.List;

/**
 * Created by wws2003 on 4/20/16.
 */
public interface ISVQuery<T> {

    class Range<T> {
        public Range(T first, T second) {
            mFirst = first;
            mSecond = second;
        }

        public T lower() {
            return mFirst;
        }

        public T upper() {
            return mSecond;
        }

        private T mFirst;
        private T mSecond;
    }

    void getAllMatchingRanges(SparseArray<List<Range<T>>> matchingRanges);

    void getMatchingRanges(int fieldIndex, List<Range<T>> matchingRanges);

    void getAllMatchingValues(SparseArray<List<T>> matchingValues);

    void getMatchingValues(int fieldIndex, List<T> matchingValues);

    boolean empty();
}
