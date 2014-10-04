package com.techburg.projectxclient.delegate.abstr;

import java.util.List;

import android.os.Handler;

import com.techburg.projectxclient.model.BuildInfo;

public interface INotificationDelegate {
	void notifyUser(List<BuildInfo> newFetchedBuildInfoList, Handler handler);
}
