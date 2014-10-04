package com.techburg.projectxclient.service.impl;

import java.util.List;

import android.os.Handler;

import com.techburg.projectxclient.delegate.abstr.IBuildInfoDatabaseDelegate;
import com.techburg.projectxclient.delegate.abstr.INotificationDelegate;
import com.techburg.projectxclient.model.BuildInfo;
import com.techburg.projectxclient.service.abstr.AbstractFetchBuildInfoService;

public class FetchBuildInfoServiceStdImpl extends AbstractFetchBuildInfoService {

	//TODO Retrieve delegates
	private IBuildInfoDatabaseDelegate mDBDelegate = null;
	private INotificationDelegate mNotificationDelegate = null;
	
	@Override
	protected void onNewBuildInfoListFetched(List<BuildInfo> newFetchedBuildInfoList, Handler handler) {
		persistNewBuildInfoList(newFetchedBuildInfoList);
		notifyUser(newFetchedBuildInfoList, handler);
	}

	private void persistNewBuildInfoList(List<BuildInfo> newFetchedBuildInfoList) {
		if(mDBDelegate != null) {
			for(BuildInfo buildInfo : newFetchedBuildInfoList) {
				try {
					mDBDelegate.storeBuildInfoToDB(buildInfo);
				}
				catch(Exception e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
	
	private void notifyUser(List<BuildInfo> newFetchedBuildInfoList, Handler handler) {
		if(mNotificationDelegate != null) {
			mNotificationDelegate.notifyUser(newFetchedBuildInfoList, handler);
		}
	}
}
