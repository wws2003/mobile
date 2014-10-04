package com.techburg.projectxclient.service.impl;

import java.util.List;

import android.os.Handler;

import com.techburg.projectxclient.model.BuildInfo;
import com.techburg.projectxclient.service.abstr.AbstractFetchBuildInfoService;

public class FetchBuildInfoServiceStdImpl extends AbstractFetchBuildInfoService {

	@Override
	protected void onNewBuildInfoListFetched(List<BuildInfo> newFetchedBuildInfoList, Handler handler) {
		persistNewBuildInfoList(newFetchedBuildInfoList);
		notifyUser(newFetchedBuildInfoList, handler);
	}

	private void persistNewBuildInfoList(List<BuildInfo> newFetchedBuildInfoList) {
		
	}
	
	private void notifyUser(List<BuildInfo> newFetchedBuildInfoList, Handler handler) {
		
	}
}
