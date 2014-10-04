package com.techburg.projectxclient;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.techburg.projectxclient.adapter.BuildInfoListAdapter;
import com.techburg.projectxclient.delegate.abstr.IAsyncTaskDelegate;
import com.techburg.projectxclient.model.BuildInfo;
import com.techburg.projectxclient.service.abstr.IBuildInfoDataService;
import com.techburg.projectxclient.view.BuildInfoItemView;

public class BuildInfoListActivity extends AbstractDataLoadActivity implements View.OnClickListener, IAsyncTaskDelegate {
	
	public static final long BUILD_INFO_LIST_INITIAL_SIZE = 100; 
	
	private ListView mLstViewBuildInfo;
	private ArrayAdapter<BuildInfo> mLstBuildInfoAdapter;
	private List<BuildInfo> mBuildInfoList;
	
	private long mBuildInfoLoadStartIndex;
	private long mBuildInfoLoadEndIndex;
	
	//TODO Get concrete instance of mBuildInfoDataService
	private IBuildInfoDataService mBuildInfoDataService = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_build_info_list);
		initComponents();
	}
	
	private void initComponents() {
		mBuildInfoList = new ArrayList<BuildInfo>();
		mLstViewBuildInfo = (ListView) findViewById(R.id.lstViewBuildInfo);
		mLstBuildInfoAdapter = new BuildInfoListAdapter(this, this, mBuildInfoList);
		mLstViewBuildInfo.setAdapter(mLstBuildInfoAdapter);
	}
	
	private void updateBuildInfoListView() {
		mLstViewBuildInfo.setAdapter(mLstBuildInfoAdapter);
		mLstBuildInfoAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onClick(View v) {
		try {
			BuildInfoItemView buildInfoItemView = (BuildInfoItemView) v;
			long buildId = buildInfoItemView.getBuildInfoId();
			Intent intent = new Intent(this, com.techburg.projectxclient.BuildInfoActivity.class);
			intent.putExtra(BuildInfoActivity.EXTRA_BUILD_ID, buildId);
			startActivity(intent);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onDataLoadStart() {
		mBuildInfoLoadStartIndex = 1;
		mBuildInfoLoadEndIndex = BUILD_INFO_LIST_INITIAL_SIZE;
		mBuildInfoList.clear();
	}

	@Override
	public Long onDataBackgroundLoad() {
		if(mBuildInfoDataService != null) {
			mBuildInfoDataService.loadBuildInfoData(mBuildInfoList, mBuildInfoLoadStartIndex, mBuildInfoLoadEndIndex);
		}
		return 0L;
	}

	@Override
	public void onDataLoadEnd(Long result) {
		updateBuildInfoListView();
	}

}
