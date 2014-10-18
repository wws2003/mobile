package com.techburg.projectxclient;

import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

import com.techburg.projectxclient.app.ProjectXClientApp;
import com.techburg.projectxclient.delegate.abstr.DelegateLocator;
import com.techburg.projectxclient.delegate.abstr.IBuildInfoDataDelegate;
import com.techburg.projectxclient.model.BuildInfo;
import com.techburg.projectxclient.util.BuildInfoUtil;

public class BuildInfoActivity extends AbstractDataLoadActivity {

	public static final String EXTRA_BUILD_ID = "buildId";
	
	private IBuildInfoDataDelegate mBuildInfoDataDelegate = null;

	private long mBuildInfoId;
	private BuildInfo mBuildInfo = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		mBuildInfoId = intent.getLongExtra(EXTRA_BUILD_ID, 1);
		mBuildInfoDataDelegate = DelegateLocator.getInstance().getBuildInfoDataDelegate();
		setContentView(R.layout.activity_build_info);
	}

	private void renderBuildInfoToView(BuildInfo buildInfo) {
		if(buildInfo != null) {
			TextView tvBuildId = (TextView) findViewById(R.id.tvBuildIdValue);
			tvBuildId.setText(String.valueOf(buildInfo.getId()));
			TextView tvBuildStatus = (TextView) findViewById(R.id.tvBuildStatusValue);
			tvBuildStatus.setText(BuildInfoUtil.getBuildStatusString(buildInfo));
			TextView tvBuildStartTime = (TextView) findViewById(R.id.tvBuildStartTimeValue);
			tvBuildStartTime.setText(buildInfo.getBeginTimeStamp().toString());
			TextView tvBuildEndTime = (TextView) findViewById(R.id.tvBuildEndTimeValue);
			tvBuildEndTime.setText(buildInfo.getEndTimeStamp().toString());
			TextView tvBuildLogUrl = (TextView) findViewById(R.id.tvBuildLogUrlValue);
			tvBuildLogUrl.setText(ProjectXClientApp.WEB_URL + "/autospring/log/" + buildInfo.getId());
			Linkify.addLinks(tvBuildLogUrl, Linkify.ALL);
		}
	}

	@Override
	public void onDataLoadStart() {
		
	}

	@Override
	public Long onDataBackgroundLoad() {
		mBuildInfo = mBuildInfoDataDelegate.getBuildInfoById(mBuildInfoId);
		return 0L;
	}

	@Override
	public void onDataLoadEnd(Long result) {
		renderBuildInfoToView(mBuildInfo);
	}

}
