package com.techburg.projectxclient.delegate.abstr;

public interface IAsyncTaskDelegate {
	void onDataLoadStart();
	Long onDataBackgroundLoad();
	void onDataLoadEnd(Long result);
}
