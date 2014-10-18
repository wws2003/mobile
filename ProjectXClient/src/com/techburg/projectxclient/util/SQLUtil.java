package com.techburg.projectxclient.util;

import java.util.Date;

import com.techburg.projectxclient.db.CommonSQL;
import com.techburg.projectxclient.model.BuildInfo;

public class SQLUtil {
	private static StringBuilder gStringBuilder = new StringBuilder();
	
	public static String createInsertSQL(BuildInfo buildInfo) {
		gStringBuilder.setLength(0);
		Date beginTimeStamp = buildInfo.getBeginTimeStamp();
		Date endTimeStamp = buildInfo.getBeginTimeStamp();
		
		gStringBuilder.append(CommonSQL.BuildInfoSQL.INSERT_SQL_PREFIX)
		.append(" ")
		.append("VALUES")
		.append("(")
		.append(beginTimeStamp != null ? beginTimeStamp.getTime() : "NULL")
		.append(",")
		.append(endTimeStamp != null ? endTimeStamp.getTime() : "NULL")
		.append(",")
		.append("'")
		.append(buildInfo.getLogFilePath())
		.append("'")
		.append(",")
		.append(buildInfo.getStatus())
		.append(",")
		.append(0)
		.append(")")
		.append(";");
		
		String insertSQL = gStringBuilder.toString();
		return insertSQL;
	}
	
	public static String createSelectFromSQL(long startId, long endId) {
		gStringBuilder.setLength(0);
		
		gStringBuilder.append(CommonSQL.BuildInfoSQL.SELECT_SQL_PREFIX)
		.append(" ")
		.append("WHERE id between ")
		.append(startId)
		.append(" and ")
		.append(endId)
		.append(";");
		
		String selectSQL = gStringBuilder.toString();
		return selectSQL;
	}
	
	public static String createSelectFromSQL(long id) {
		gStringBuilder.setLength(0);
		
		gStringBuilder.append(CommonSQL.BuildInfoSQL.SELECT_SQL_PREFIX)
		.append(" ")
		.append("WHERE id = ")
		.append(id)
		.append(";");
		
		String selectSQL = gStringBuilder.toString();
		return selectSQL;
	}
}
