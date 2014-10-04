package com.techburg.projectxclient.service.abstr;

import java.util.List;

import com.techburg.projectxclient.model.BuildInfo;

public interface IBuildInfoDataService {
	void loadBuildInfoData(List<BuildInfo> buildInfoList, long startIndex, long endIndex);
	BuildInfo getBuildInfoById(long id);
}
