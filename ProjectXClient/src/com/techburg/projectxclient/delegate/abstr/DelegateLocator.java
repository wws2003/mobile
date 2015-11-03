package com.techburg.projectxclient.delegate.abstr;

public class DelegateLocator {
	private static DelegateLocator gInstance = null;
	
	private IBuildInfoDatabaseDelegate mBuildInfoDatabaseDelegate = null;
	private IBuildInfoFetchDelegate mBuildInfoFetchDelegate = null;
	private INotificationDelegate mNotificationDelegate = null;
	private IBuildInfoDataDelegate mBuildInfoDataDelegate = null;
	
	public static DelegateLocator getInstance() {
		if(gInstance == null) {
			gInstance = new DelegateLocator();
		}
		return gInstance;
	}
	
	private DelegateLocator() {
		
	}
	
	public IBuildInfoDatabaseDelegate getBuildInfoDatabaseDelegate() {
		return mBuildInfoDatabaseDelegate;
	}
	public void setBuildInfoDatabaseDelegate(IBuildInfoDatabaseDelegate buildInfoDatabaseDelegate) {
		this.mBuildInfoDatabaseDelegate = buildInfoDatabaseDelegate;
	}
	
	public IBuildInfoFetchDelegate getBuildInfoFetchDelegate() {
		return mBuildInfoFetchDelegate;
	}
	public void setBuildInfoFetchDelegate(IBuildInfoFetchDelegate buildInfoFetchDelegate) {
		this.mBuildInfoFetchDelegate = buildInfoFetchDelegate;
	}
	
	public INotificationDelegate getNotificationDelegate() {
		return mNotificationDelegate;
	}
	public void setNotificationDelegate(INotificationDelegate notificationDelegate) {
		this.mNotificationDelegate = notificationDelegate;
	}

	public IBuildInfoDataDelegate getBuildInfoDataDelegate() {
		return mBuildInfoDataDelegate;
	}

	public void setBuildInfoDataDelegate(IBuildInfoDataDelegate buildInfoDataDelegate) {
		this.mBuildInfoDataDelegate = buildInfoDataDelegate;
	}
	
}
