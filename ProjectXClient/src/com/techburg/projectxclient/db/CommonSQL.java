package com.techburg.projectxclient.db;

public class CommonSQL {

	public static class BuildInfoSQL {
		
		public static final String TABLE_NAME = "build_info";
		
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_BEGIN_BUILD_TIME = "begin_build_time";
		public static final String COLUMN_END_BUILD_TIME = "end_build_time";
		public static final String COLUMN_LOG_FILE_PATH = "log_file_path";
		public static final String COLUMN_STATUS = "status";
		public static final String COLUMN_BUILD_SCRIPT_ID = "build_script_id";

		public static final int COLUMN_ID_INDEX = 0;
		public static final int COLUMN_BEGIN_BUILD_TIME_INDEX = 1;
		public static final int COLUMN_END_BUILD_TIME_INDEX = 2;
		public static final int COLUMN_LOG_FILE_PATH_INDEX = 3;
		public static final int COLUMN_STATUS_INDEX = 4;
		public static final int COLUMN_BUILD_SCRIPT_ID_INDEX = 5;
		
		public static final String CREATE_TABLE = new StringBuilder().append("CREATE TABLE ")
				.append(TABLE_NAME)
				.append(" (")
				.append(COLUMN_ID)
				.append(" integer,")
				.append(COLUMN_BEGIN_BUILD_TIME)
				.append(" datetime,")
				.append(COLUMN_END_BUILD_TIME)
				.append(" datetime,")
				.append(COLUMN_LOG_FILE_PATH)
				.append(" varchar(255),")
				.append(COLUMN_STATUS)
				.append(" integer,")
				.append(COLUMN_BUILD_SCRIPT_ID)
				.append(" bigint,")
				.append(" primary key(")
				.append(COLUMN_ID)
				.append(")")
				.append(");")
				.toString();

		public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
		
		public static final String INSERT_SQL_PREFIX = new StringBuilder().append("INSERT INTO")
				.append(" ")
				.append(TABLE_NAME)
				.append(" (")
				.append(COLUMN_BEGIN_BUILD_TIME)
				.append(",")
				.append(COLUMN_END_BUILD_TIME)
				.append(",")
				.append(COLUMN_LOG_FILE_PATH)
				.append(",")
				.append(COLUMN_STATUS)
				.append(",")
				.append(COLUMN_BUILD_SCRIPT_ID)
				.append(")")
				.toString();
		
		public static final String SELECT_SQL_PREFIX = "SELECT * FROM " + TABLE_NAME;

	}
	public static class BuildScriptSQL {
		public static final String TABLE_NAME = "build_info";
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_SCRIPT_FILE_PATH = "script_file_path";

		public static final String CREATE_TABLE = new StringBuilder().append("CREATE TABLE ")
				.append(TABLE_NAME)
				.append(" ")
				.append(COLUMN_ID)
				.append(" integer,")
				.append(COLUMN_SCRIPT_FILE_PATH)
				.append(" varchar(255),")
				.append(" primary key(")
				.append(COLUMN_ID)
				.append(")")
				.append(");")
				.toString();
		
		public static final String DROP_TABLE = "DROP TABLE " + TABLE_NAME;
	}
}
