package com.techburg.projectxclient.delegate.abstr;

import com.techburg.projectxclient.model.BuildInfo;

public interface IBuildInfoDatabaseDelegate {
	public void storeBuildInfoToDB(BuildInfo buildInfo) throws Exception;
}
