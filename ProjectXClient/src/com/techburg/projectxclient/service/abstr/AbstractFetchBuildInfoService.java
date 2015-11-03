package com.techburg.projectxclient.service.abstr;

import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.techburg.projectxclient.delegate.abstr.DelegateLocator;
import com.techburg.projectxclient.delegate.abstr.IBuildInfoFetchDelegate;
import com.techburg.projectxclient.model.BuildInfo;

public abstract class AbstractFetchBuildInfoService extends IntentService {

	public static final String FETCH_ADDRESS_EXTRA_NAME = "fetchAddress";
	public static final String FETCH_INTERVAL_EXTRA_NAME = "fetchInterval";
	public static final String FETCH_DELEGATE_NUMBER_EXTRA_NAME = "fetchDelegateNumber";
	public static final String FETCH_ALL = "fetchAll";
	
	private String mFetchAddress;
	private List<BuildInfo> mNewBuildInfoList;
	private int mFetchIntervalSeconds;
	private IBuildInfoFetchDelegate mBuildInfoFetchDelegate;
	private Handler mHandler;
	private boolean mToFetchAll = false;
	
	private boolean mToContinueWork = true;
	
	public AbstractFetchBuildInfoService() {
		super("BuildInfoFetchService");
		mNewBuildInfoList = new ArrayList<BuildInfo>();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("AbstractFetchBuildInfoService onStartCommand", "Service started");
		mHandler = new Handler();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		Log.i("AbstractFetchBuildInfoService onDestroy", "Service is being destroyed");
		stopWork();
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i("AbstractFetchBuildInfoService onHandleIntent", "To handle intent");
		initAttributesByIntent(intent);
		while (toContinueWork()) {
			long lastReceivedBuildId = mToFetchAll ? 0 : mBuildInfoFetchDelegate.getLastReceivedBuildId();
			
			String fetchUrl = mFetchAddress + "/" + lastReceivedBuildId;
			
			mBuildInfoFetchDelegate.getNewBuildInfoListFromURL(fetchUrl, mNewBuildInfoList);
			
			Log.i("AbstractFetchBuildInfoService onHandleIntent", "Fetched data size " + mNewBuildInfoList.size());
			if(!mNewBuildInfoList.isEmpty()) {
				mBuildInfoFetchDelegate.updateLastReceivedId();
				onNewBuildInfoListFetched(mNewBuildInfoList, mHandler);
			}
			
			try {
				Thread.sleep(mFetchIntervalSeconds * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			
			mToFetchAll = false;
		}
		Log.i("AbstractFetchBuildInfoService onHandleIntent", "Finish handle intent");
	}
	
	private void initAttributesByIntent(Intent intent) {
		mFetchAddress = intent.getStringExtra(FETCH_ADDRESS_EXTRA_NAME);
		mFetchIntervalSeconds = intent.getIntExtra(FETCH_INTERVAL_EXTRA_NAME, 10);
		mToFetchAll = intent.getBooleanExtra(FETCH_ALL, false);
		mBuildInfoFetchDelegate = DelegateLocator.getInstance().getBuildInfoFetchDelegate();
	}
	
	private synchronized boolean toContinueWork() {
		return mToContinueWork;
	}
	
	private synchronized void stopWork() {
		mToContinueWork = false;
	}
	
	protected abstract void onNewBuildInfoListFetched(List<BuildInfo> newFetchedBuildInfoList, Handler handler);

}
