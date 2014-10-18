package com.techburg.projectxclient.delegate.impl;

import android.database.sqlite.SQLiteDatabase;

import com.techburg.projectxclient.db.DBManager;
import com.techburg.projectxclient.delegate.abstr.IBuildInfoDatabaseDelegate;
import com.techburg.projectxclient.model.BuildInfo;
import com.techburg.projectxclient.util.SQLUtil;

public class BuildInfoDatabaseDelegateImpl implements IBuildInfoDatabaseDelegate {

	@Override
	public void storeBuildInfoToDB(BuildInfo buildInfo) throws Exception {
		DBManager dbManager = DBManager.getInstance();
		SQLiteDatabase db = dbManager.getWritableDatabase();
	
		String insertSQL = SQLUtil.createInsertSQL(buildInfo);
		db.beginTransaction();
		try {
			db.execSQL(insertSQL);
			db.setTransactionSuccessful();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			db.endTransaction();
		}
	}

}
