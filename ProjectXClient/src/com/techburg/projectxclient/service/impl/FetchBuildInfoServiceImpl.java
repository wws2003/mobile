package com.techburg.projectxclient.service.impl;

import java.util.List;

import android.os.Handler;
import android.widget.Toast;

import com.techburg.projectxclient.app.ProjectXClientApp;
import com.techburg.projectxclient.model.BuildInfo;
import com.techburg.projectxclient.service.abstr.AbstractFetchBuildInfoService;

public class FetchBuildInfoServiceImpl extends AbstractFetchBuildInfoService {

	@Override
	protected void onNewBuildInfoListFetched(final List<BuildInfo> newFetchedBuildInfoList, Handler handler) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				int numberOfNewFetchedBuildInfo = newFetchedBuildInfoList.size();
				Toast.makeText(ProjectXClientApp.getContext(), numberOfNewFetchedBuildInfo + " new build(s) conducted !", Toast.LENGTH_LONG).show();	
				Toast.makeText(ProjectXClientApp.getContext(), newFetchedBuildInfoList.get(numberOfNewFetchedBuildInfo - 1).getStatus() == 0 ? "Last build success !" : "Last build failed" , Toast.LENGTH_LONG).show();
			}
		});
	}

}
