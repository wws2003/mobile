package com.techburg.projectxclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	private static DBManager gInstance = null;
	private static final int DB_VERSION = 13;
	private BuildInfoDBHelper mDBHelper;
	
	public static synchronized DBManager getInstance() {
		if(gInstance == null) {
			gInstance = new DBManager();
		}
		return gInstance;
	}
	
	private DBManager(){
		
	}
	
	public void openDB(Context context, String dbName) {
		mDBHelper = new BuildInfoDBHelper(context, dbName, null, DB_VERSION, null);
	}
	
	public SQLiteDatabase getWritableDatabase() {
		return mDBHelper.getWritableDatabase();
	}
	
	public SQLiteDatabase getReadableDataBase() {
		return mDBHelper.getReadableDatabase();
	}
}
