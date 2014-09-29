package com.techburg.projectxclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.techburg.projectxclient.service.abstr.AbstractFetchBuildInfoService;

public class MainActivity extends ActionBarActivity {

	private static final String FETCH_URL = "http://192.168.11.4:8080/autospring/buildlist";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onBtnStartService(View v) {
		Log.i("MainActivity onBtnStartService", "To start service");
		Intent serviceIntent = new Intent(this, com.techburg.projectxclient.service.impl.FetchBuildInfoServiceImpl.class);
		serviceIntent.putExtra(AbstractFetchBuildInfoService.FETCH_ADDRESS_EXTRA_NAME, FETCH_URL);
		serviceIntent.putExtra(AbstractFetchBuildInfoService.FETCH_INTERVAL_EXTRA_NAME, 20);
		startService(serviceIntent);
	}

	public void onBtnStopService(View v) {
		Intent serviceIntent = new Intent(this, com.techburg.projectxclient.service.impl.FetchBuildInfoServiceImpl.class);
		stopService(serviceIntent);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
