package com.tbg.simplestvallet.app.preference.impl;

import android.content.SharedPreferences;

import com.tbg.simplestvallet.app.preference.abstr.IPreferenceOperator;

/**
 * Created by wws2003 on 11/5/15.
 */
public class SamplePreferenceOperator implements IPreferenceOperator {
    private SharedPreferences mSharedPreferences;

    public SamplePreferenceOperator(SharedPreferences sharedPreferences) {
        this.mSharedPreferences = sharedPreferences;
    }

    @Override
    public void store(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    @Override
    public String get(String key) {
        return mSharedPreferences.getString(key, null);
    }

    @Override
    public void deletePair(String key, String value) {
        if(value == mSharedPreferences.getString(key, null)) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.remove(key);
            editor.commit();
        }
    }

    @Override
    public void delete(String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }
}
