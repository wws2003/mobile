package com.techburg.projectxclient.model;

import java.util.Date;

public class BuildInfo {
	public static class Status {
		public static final int BUILD_SUCCESSFUL = 0;
		public static final int BUILD_FAILED = 1;
	}
	
	private long mId;
	private int mStatus;
	private Date mBeginBuildTime;
	private Date mEndBuildTime;
	private String mLogFilePath;
	private BuildScript mBuildScript = null;
	
	public long getId() {
		return mId;
	}
	public void setId(long mId) {
		this.mId = mId;
	}
	public int getStatus() {
		return mStatus;
	}
	public void setStatus(int mStatus) {
		this.mStatus = mStatus;
	}
	public Date getBeginTimeStamp() {
		return mBeginBuildTime;
	}
	public void setBeginTimeStamp(Date mBeginTimeStamp) {
		this.mBeginBuildTime = mBeginTimeStamp;
	}
	public Date getEndTimeStamp() {
		return mEndBuildTime;
	}
	public void setEndTimeStamp(Date mEndTimeStamp) {
		this.mEndBuildTime = mEndTimeStamp;
	}
	public String getLogFilePath() {
		return mLogFilePath;
	}
	public void setLogFilePath(String mLogFilePath) {
		this.mLogFilePath = mLogFilePath;
	}
	public BuildScript getBuildScript() {
		return mBuildScript;
	}
	public void setBuildScript(BuildScript mBuildScript) {
		this.mBuildScript = mBuildScript;
	}
}
