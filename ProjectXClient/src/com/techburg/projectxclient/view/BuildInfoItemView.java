package com.techburg.projectxclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class BuildInfoItemView extends View {

	private long mBuildInfoId;
	
	public BuildInfoItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BuildInfoItemView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public BuildInfoItemView(Context context) {
		super(context);
	}

	public long getBuildInfoId() {
		return mBuildInfoId;
	}

	public void setBuildInfoId(long buildInfoId) {
		this.mBuildInfoId = buildInfoId;
	}

}
