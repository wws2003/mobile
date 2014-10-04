package com.techburg.projectxclient.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class ProjectXClientApp extends Application {
	public static final String WEB_URL = "http://192.168.11.4:8080";
	public static final String PREFERENCE_LAST_RECEIVED_BUILD_ID = "lastReceivedBuildId";
	
	private static Context mContext;
	private static final String SHARED_PREFERENCES_NAME = "projectx_client_sharePreference";

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this.getApplicationContext();
	}

	public static Context getContext() {
		return mContext;
	}

	public static SharedPreferences getSharePreference(){
		return getContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
	}
}
