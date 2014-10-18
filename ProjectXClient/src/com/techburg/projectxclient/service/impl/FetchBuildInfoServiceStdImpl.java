package com.techburg.projectxclient.service.impl;

import java.util.List;

import android.os.Handler;
import android.util.Log;

import com.techburg.projectxclient.delegate.abstr.DelegateLocator;
import com.techburg.projectxclient.delegate.abstr.IBuildInfoDatabaseDelegate;
import com.techburg.projectxclient.delegate.abstr.INotificationDelegate;
import com.techburg.projectxclient.model.BuildInfo;
import com.techburg.projectxclient.service.abstr.AbstractFetchBuildInfoService;

public class FetchBuildInfoServiceStdImpl extends AbstractFetchBuildInfoService {

	private IBuildInfoDatabaseDelegate mDBDelegate = null;
	private INotificationDelegate mNotificationDelegate = null;

	public FetchBuildInfoServiceStdImpl() {
		mDBDelegate = DelegateLocator.getInstance().getBuildInfoDatabaseDelegate();
		mNotificationDelegate = DelegateLocator.getInstance().getNotificationDelegate();
	}

	@Override
	protected void onNewBuildInfoListFetched(List<BuildInfo> newFetchedBuildInfoList, Handler handler) {
		persistNewBuildInfoList(newFetchedBuildInfoList);
		notifyUser(newFetchedBuildInfoList, handler);
	}

	private void persistNewBuildInfoList(List<BuildInfo> newFetchedBuildInfoList) {
		if(mDBDelegate != null) {
			for(BuildInfo buildInfo : newFetchedBuildInfoList) {
				try {
					if(buildInfo != null)
						mDBDelegate.storeBuildInfoToDB(buildInfo);
					else {
						Log.i("FetchBuildInfoServiceStdImpl persistNewBuildInfoList", "A null instance retrieved");
					}
				}
				catch(Exception e) {
					e.printStackTrace();
					return;
				}
			}
			Log.i("FetchBuildInfoServiceStdImpl persistNewBuildInfoList", "Persisted all fetched data");
		}
	}

	private void notifyUser(List<BuildInfo> newFetchedBuildInfoList, Handler handler) {
		if(mNotificationDelegate != null) {
			mNotificationDelegate.notifyUser(newFetchedBuildInfoList, handler);
		}
	}
}
