package com.techburg.projectxclient;

import com.techburg.projectxclient.controller.AsyncController;
import com.techburg.projectxclient.delegate.abstr.IAsyncTaskDelegate;

import android.app.Activity;
public abstract class AbstractDataLoadActivity extends Activity implements IAsyncTaskDelegate {
	
	private AsyncController mDataLoadController = new AsyncController(this);
	
	protected void startLoadData() {
		mDataLoadController.execute();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		startLoadData();
	}
	
	
}
