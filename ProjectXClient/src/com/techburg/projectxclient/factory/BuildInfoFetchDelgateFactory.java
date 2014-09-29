package com.techburg.projectxclient.factory;

import com.techburg.projectxclient.app.ProjectXClientApp;
import com.techburg.projectxclient.delegate.abstr.IBuildInfoFetchDelegate;
import com.techburg.projectxclient.delegate.impl.BuildInfoFetchDelegateImpl;

public class BuildInfoFetchDelgateFactory {
	public static IBuildInfoFetchDelegate getBuildInfoFetchDelegate(int number) {
		BuildInfoFetchDelegateImpl delegate = new BuildInfoFetchDelegateImpl();	
		delegate.setSharedPreferences(ProjectXClientApp.getSharePreference());
		return delegate;
	}
}
