package com.techburg.projectxclient;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.techburg.projectxclient.adapter.BuildInfoListAdapter;
import com.techburg.projectxclient.delegate.abstr.DelegateLocator;
import com.techburg.projectxclient.delegate.abstr.IAsyncTaskDelegate;
import com.techburg.projectxclient.delegate.abstr.IBuildInfoDataDelegate;
import com.techburg.projectxclient.model.BuildInfo;
import com.techburg.projectxclient.view.BuildInfoItemViewHolder;

public class BuildInfoListActivity extends AbstractDataLoadActivity implements View.OnClickListener, IAsyncTaskDelegate {
	
	public static final long BUILD_INFO_LIST_INITIAL_SIZE = 20; 
	public static final long BUILD_INFO_LIST_LOAD_MORE_SIZE = 10;
	
	private ListView mLstViewBuildInfo;
	private Button mBtnLoadMore;
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
		initLoadScope();
	}
	
	private void initComponents() {
		mBuildInfoList = new ArrayList<BuildInfo>();
		mBuildInfoListBuffer = new ArrayList<BuildInfo>();
		mLstViewBuildInfo = (ListView) findViewById(R.id.lstViewBuildInfo);
		mLstBuildInfoAdapter = new BuildInfoListAdapter(this, this, mBuildInfoList);
		
		// LoadMore button
        mBtnLoadMore = new Button(this);
        mBtnLoadMore.setText(getResources().getString(R.string.btn_load_more));
        mBtnLoadMore.setOnClickListener(this);
 
        // Adding Load More button to list view at bottom
        mLstViewBuildInfo.addFooterView(mBtnLoadMore);
        
        mLstViewBuildInfo.setAdapter(mLstBuildInfoAdapter);
		mBuildInfoDataDelegate = DelegateLocator.getInstance().getBuildInfoDataDelegate();
	}
	
	private void initLoadScope() {
		mBuildInfoLoadStartIndex = 1;
		mBuildInfoLoadEndIndex = BUILD_INFO_LIST_INITIAL_SIZE;
	}
	
	private void updateBuildInfoListView() {
		mLstViewBuildInfo.setAdapter(mLstBuildInfoAdapter);
		mLstBuildInfoAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onClick(View v) {
		if(v.equals(mBtnLoadMore)) {
			onLoadMoteBtnClicked();
		}
		else {
			onItemClicked(v);
		}
	}
	
	private void onItemClicked(View v) {
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
	
	private void onLoadMoteBtnClicked() {
		mBuildInfoLoadEndIndex += BUILD_INFO_LIST_LOAD_MORE_SIZE;
		startLoadData();
	}

	@Override
	public void onDataLoadStart() {
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
		int fetchSize = mBuildInfoListBuffer.size();
		for(int i = 0; i < fetchSize; i++) {
			mBuildInfoList.add(mBuildInfoListBuffer.get(fetchSize -1 - i));
		}
		updateBuildInfoListView();
	}

}
