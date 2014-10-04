package com.techburg.projectxclient.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.techburg.projectxclient.R;
import com.techburg.projectxclient.model.BuildInfo;
import com.techburg.projectxclient.util.BuildInfoUtil;
import com.techburg.projectxclient.util.DateUtil;
import com.techburg.projectxclient.view.BuildInfoItemView;

public class BuildInfoListAdapter extends ArrayAdapter<BuildInfo> {

	private LayoutInflater mLayoutInflater;
	private View.OnClickListener mItemOnClickListener;
	
	public BuildInfoListAdapter(Context context, View.OnClickListener itemOnClickListener, List<BuildInfo> objects) {
		super(context, 0, objects);
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		BuildInfoItemView retView = null;
		if(convertView == null) {
			retView = (BuildInfoItemView) mLayoutInflater.inflate(R.layout.build_info_item, null);
		}
		else {
			retView = (BuildInfoItemView) convertView;
		}
		BuildInfo buildInfoAtPosition = getItem(position);
		renderBuildInfoToView(buildInfoAtPosition, retView);
		retView.setBuildInfoId(buildInfoAtPosition.getId());
		retView.setOnClickListener(mItemOnClickListener);
		return retView;
	}
	
	private void renderBuildInfoToView(BuildInfo buildInfo, View view) {
		if(buildInfo != null) {
			TextView tvBuildId = (TextView) view.findViewById(R.id.tv_build_id);
			tvBuildId.setText(String.valueOf(buildInfo.getId()));
			TextView tvBuildStatus = (TextView) view.findViewById(R.id.tv_build_id);
			String buildStatusString = BuildInfoUtil.getBuildStatusString(buildInfo);
			tvBuildStatus.setText(buildStatusString);
			TextView tvBuildDate = (TextView)view.findViewById(R.id.tv_build_date);
			tvBuildDate.setText(DateUtil.getYMDString(buildInfo.getBeginTimeStamp()));
		}
	}

}
