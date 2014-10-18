package com.techburg.projectxclient.delegate.impl;

import java.util.Date;
import java.util.List;

import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;

import com.techburg.projectxclient.db.CommonSQL.BuildInfoSQL;
import com.techburg.projectxclient.db.DBManager;
import com.techburg.projectxclient.delegate.abstr.IBuildInfoDataDelegate;
import com.techburg.projectxclient.model.BuildInfo;
import com.techburg.projectxclient.util.SQLUtil;

public class BuildInfoDataDelegateDBImpl implements IBuildInfoDataDelegate {

	@Override
	public void loadBuildInfoData(List<BuildInfo> buildInfoList, long startIndex, long endIndex) {
		String selectSQL = SQLUtil.createSelectFromSQL(startIndex, endIndex);
		loadDataFromSQL(selectSQL, buildInfoList);
	}

	@Override
	public BuildInfo getBuildInfoById(long id) {
		String selectSQL = SQLUtil.createSelectFromSQL(id);
		return loadDataFromSQL(selectSQL, null);
	}

	private BuildInfo loadDataFromSQL(String sql, List<BuildInfo> buildInfoList) {
		BuildInfo result = null;
		SQLiteDatabase db = DBManager.getInstance().getReadableDataBase();
		db.beginTransactionNonExclusive();
		try {
			SQLiteCursor cursor = (SQLiteCursor) db.rawQuery(sql, null);
			if(cursor.moveToFirst()) {
				//The case load single row, such as select by id
				if(buildInfoList == null) {
					result = new BuildInfo();
					setDataFromCursor(result, cursor);
				}
				else {
					int icount = cursor.getCount();
					for (int j = 0; j < icount; j++) {
						BuildInfo buildInfo = new BuildInfo();
						setDataFromCursor(buildInfo, cursor);
						buildInfoList.add(buildInfo);
						cursor.moveToNext();
					}
					result = buildInfoList.get(0);
				}
			}
			cursor.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			db.endTransaction();
		}
		return result;
	}

	private void setDataFromCursor(BuildInfo buildInfo, SQLiteCursor cursor) throws Exception {
		buildInfo.setId(cursor.getLong(BuildInfoSQL.COLUMN_ID_INDEX));
		buildInfo.setBeginTimeStamp(new Date(cursor.getLong(BuildInfoSQL.COLUMN_BEGIN_BUILD_TIME_INDEX)));
		buildInfo.setEndTimeStamp(new Date(cursor.getLong(BuildInfoSQL.COLUMN_END_BUILD_TIME_INDEX)));
		buildInfo.setLogFilePath(cursor.getString(BuildInfoSQL.COLUMN_LOG_FILE_PATH_INDEX));
		buildInfo.setStatus(cursor.getInt(BuildInfoSQL.COLUMN_STATUS_INDEX));
		buildInfo.setBuildScript(null);
	}
}
