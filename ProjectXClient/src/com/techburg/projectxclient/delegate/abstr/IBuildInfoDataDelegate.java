package com.techburg.projectxclient.delegate.abstr;

import java.util.List;

import com.techburg.projectxclient.model.BuildInfo;

public interface IBuildInfoDataDelegate {
	void loadBuildInfoData(List<BuildInfo> buildInfoList, long startIndex, long endIndex);
	BuildInfo getBuildInfoById(long id);
}
