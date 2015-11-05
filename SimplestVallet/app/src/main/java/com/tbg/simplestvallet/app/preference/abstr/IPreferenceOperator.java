package com.tbg.simplestvallet.app.preference.abstr;

/**
 * Created by wws2003 on 11/5/15.
 */
public interface IPreferenceOperator {
    void store(String key, String value);
    String get(String key);
    void deletePair(String key, String value);
    void delete(String key);
}
