package com.techburg.projectxclient.db;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public class BuildInfoDBHelper extends SQLiteOpenHelper {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public BuildInfoDBHelper(Context context, String name, CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler)
	{
		super(context, name, factory, version, errorHandler);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(CommonSQL.BuildScriptSQL.CREATE_TABLE);
			db.execSQL(CommonSQL.BuildInfoSQL.CREATE_TABLE);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			db.execSQL(CommonSQL.BuildScriptSQL.DROP_TABLE);
			db.execSQL(CommonSQL.BuildInfoSQL.DROP_TABLE);
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		try {
			db.execSQL(CommonSQL.BuildScriptSQL.CREATE_TABLE);
			db.execSQL(CommonSQL.BuildInfoSQL.CREATE_TABLE);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
