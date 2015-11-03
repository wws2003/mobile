package com.techburg.projectxclient.model;

public class BuildScript {
	private long mId;
	private String mScriptFilePath;
	
	public long getId() {
		return mId;
	}
	public void setId(long mId) {
		this.mId = mId;
	}
	
	public String getScriptFilePath() {
		return mScriptFilePath;
	}
	public void setScriptFilePath(String mScriptFilePath) {
		this.mScriptFilePath = mScriptFilePath;
	}

}
