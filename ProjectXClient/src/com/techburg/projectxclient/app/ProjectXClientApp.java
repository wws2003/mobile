package com.techburg.projectxclient.app;

import com.techburg.projectxclient.db.DBManager;
import com.techburg.projectxclient.delegate.abstr.DelegateLocator;
import com.techburg.projectxclient.delegate.abstr.IBuildInfoDataDelegate;
import com.techburg.projectxclient.delegate.abstr.IBuildInfoDatabaseDelegate;
import com.techburg.projectxclient.delegate.impl.BuildInfoDataDelegateDBImpl;
import com.techburg.projectxclient.delegate.impl.BuildInfoDatabaseDelegateImpl;
import com.techburg.projectxclient.delegate.impl.BuildInfoFetchDelegateImpl;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

public class ProjectXClientApp extends Application {
	public static final String WEB_URL = "http://192.168.11.4:8080";
	public static final String PREFERENCE_LAST_RECEIVED_BUILD_ID = "lastReceivedBuildId";
	
	private static final String DB_NAME = "projectx.db";
	private static final String DB_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + DB_NAME;
	
	private static Context mContext;
	private static final String SHARED_PREFERENCES_NAME = "projectx_client_sharePreference";

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this.getApplicationContext();
		initDelegateLocator();
		initDatabase();
	}

	public static Context getContext() {
		return mContext;
	}

	public static SharedPreferences getSharePreference(){
		return getContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
	}
	
	private void initDelegateLocator() {
		DelegateLocator delegateLocator = DelegateLocator.getInstance();
		BuildInfoFetchDelegateImpl fetchDelegate = new BuildInfoFetchDelegateImpl();	
		fetchDelegate.setSharedPreferences(getSharePreference());
		delegateLocator.setBuildInfoFetchDelegate(fetchDelegate);
		IBuildInfoDatabaseDelegate databaseDelegate = new BuildInfoDatabaseDelegateImpl();
		delegateLocator.setBuildInfoDatabaseDelegate(databaseDelegate);
		IBuildInfoDataDelegate buildInfoDataDelegate = new BuildInfoDataDelegateDBImpl();
		delegateLocator.setBuildInfoDataDelegate(buildInfoDataDelegate);
	}
	
	private void initDatabase() {
		DBManager dbManager = DBManager.getInstance();
		Log.i("ProjectXClientApp initDatabase", "DB file " + DB_FILE_PATH);
		dbManager.openDB(getContext(), DB_FILE_PATH);
	}
}
