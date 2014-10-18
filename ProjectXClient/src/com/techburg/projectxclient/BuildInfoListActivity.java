package com.techburg.projectxclient;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.techburg.projectxclient.adapter.BuildInfoListAdapter;
import com.techburg.projectxclient.delegate.abstr.DelegateLocator;
import com.techburg.projectxclient.delegate.abstr.IAsyncTaskDelegate;
import com.techburg.projectxclient.delegate.abstr.IBuildInfoDataDelegate;
import com.techburg.projectxclient.model.BuildInfo;
import com.techburg.projectxclient.view.BuildInfoItemViewHolder;

public class BuildInfoListActivity extends AbstractDataLoadActivity implements View.OnClickListener, IAsyncTaskDelegate {
	
	public static final long BUILD_INFO_LIST_INITIAL_SIZE = 50; 
	
	private ListView mLstViewBuildInfo;
	private ArrayAdapter<BuildInfo> mLstBuildInfoAdapter;
	private List<BuildInfo> mBuildInfoList;
	private List<BuildInfo> mBuildInfoListBuffer;
	
	private long mBuildInfoLoadStartIndex;
	private long mBuildInfoLoadEndIndex;
	
	private IBuildInfoDataDelegate mBuildInfoDataDelegate = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_build_info_list);
		initComponents();
	}
	
	private void initComponents() {
		mBuildInfoList = new ArrayList<BuildInfo>();
		mBuildInfoListBuffer = new ArrayList<BuildInfo>();
		mLstViewBuildInfo = (ListView) findViewById(R.id.lstViewBuildInfo);
		mLstBuildInfoAdapter = new BuildInfoListAdapter(this, this, mBuildInfoList);
		mLstViewBuildInfo.setAdapter(mLstBuildInfoAdapter);
		mBuildInfoDataDelegate = DelegateLocator.getInstance().getBuildInfoDataDelegate();
	}
	
	private void updateBuildInfoListView() {
		mLstViewBuildInfo.setAdapter(mLstBuildInfoAdapter);
		mLstBuildInfoAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onClick(View v) {
		try {
			BuildInfoItemViewHolder buildInfoItemViewHolder = (BuildInfoItemViewHolder) v.getTag();
			long buildId = buildInfoItemViewHolder.buildId;
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
		mBuildInfoListBuffer.clear();
	}

	@Override
	public Long onDataBackgroundLoad() {
		if(mBuildInfoDataDelegate != null) {
			mBuildInfoDataDelegate.loadBuildInfoData(mBuildInfoListBuffer, mBuildInfoLoadStartIndex, mBuildInfoLoadEndIndex);
		}
		return 0L;
	}

	@Override
	public void onDataLoadEnd(Long result) {
		mBuildInfoList.clear();
		mBuildInfoList.addAll(mBuildInfoListBuffer);
		updateBuildInfoListView();
	}

}
