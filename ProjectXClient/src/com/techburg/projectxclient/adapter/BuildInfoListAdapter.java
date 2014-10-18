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
import com.techburg.projectxclient.view.BuildInfoItemViewHolder;

public class BuildInfoListAdapter extends ArrayAdapter<BuildInfo> {

	private LayoutInflater mLayoutInflater;
	private View.OnClickListener mItemOnClickListener;
	
	public BuildInfoListAdapter(Context context, View.OnClickListener itemOnClickListener, List<BuildInfo> objects) {
		super(context, 0, objects);
		mItemOnClickListener = itemOnClickListener;
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		View retView = convertView;
		BuildInfoItemViewHolder viewHolder = null;
		BuildInfo buildInfoAtPosition = getItem(position);
		
		if(retView == null) {
			retView = mLayoutInflater.inflate(R.layout.build_info_item, null);
			viewHolder = new BuildInfoItemViewHolder();
			viewHolder.tvBuildId = (TextView) retView.findViewById(R.id.tv_build_id);
			viewHolder.tvBuildStatus = (TextView) retView.findViewById(R.id.tv_build_status);
			viewHolder.tvBuildDate = (TextView) retView.findViewById(R.id.tv_build_date);
			viewHolder.buildId = buildInfoAtPosition.getId();
			retView.setTag(viewHolder);
		}
		else {
			viewHolder = (BuildInfoItemViewHolder) retView.getTag();
		}
		
		renderBuildInfoToView(buildInfoAtPosition, viewHolder);
		retView.setOnClickListener(mItemOnClickListener);
		return retView;
	}
	
	private void renderBuildInfoToView(BuildInfo buildInfo, BuildInfoItemViewHolder viewHolder) {
		if(buildInfo != null) {
			viewHolder.tvBuildId.setText(String.valueOf(buildInfo.getId()));
			String buildStatusString = BuildInfoUtil.getBuildStatusString(buildInfo);
			viewHolder.tvBuildStatus.setText(buildStatusString);
			viewHolder.tvBuildDate.setText(DateUtil.getYMDString(buildInfo.getBeginTimeStamp()));
		}
	}

}
