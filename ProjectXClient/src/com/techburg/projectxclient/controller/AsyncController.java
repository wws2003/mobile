package com.techburg.projectxclient.controller;

import com.techburg.projectxclient.delegate.abstr.IAsyncTaskDelegate;

import android.os.AsyncTask;

public class AsyncController {

	private IAsyncTaskDelegate m_asyncTaskDelegate = null;
	private boolean m_isBusy = false;

	public AsyncController(IAsyncTaskDelegate asyncTaskDelegate) {
		m_asyncTaskDelegate = asyncTaskDelegate;
	}

	public void execute() {
		if(!m_isBusy) {
			m_isBusy = true;
			DataLoadTask task = new DataLoadTask();
			task.execute();
		}
	}

	private class DataLoadTask extends AsyncTask<Void, Integer, Long> {

		@Override
		protected void onPreExecute() {
			m_asyncTaskDelegate.onDataLoadStart();
		}
		
		@Override
		protected Long doInBackground(Void... params) {
			return m_asyncTaskDelegate.onDataBackgroundLoad();
		}

		@Override
		protected void onPostExecute(Long result) {
			m_asyncTaskDelegate.onDataLoadEnd(result);
			m_isBusy = false;
		}

	}
}
