package com.techburg.projectxclient.util;

import com.techburg.projectxclient.R;
import com.techburg.projectxclient.app.ProjectXClientApp;
import com.techburg.projectxclient.model.BuildInfo;
import com.techburg.projectxclient.model.BuildInfo.Status;

public class BuildInfoUtil {
	public static String getBuildStatusString(BuildInfo buildInfo) {
		if(buildInfo.getStatus() == Status.BUILD_SUCCESSFUL) {
			return ProjectXClientApp.getContext().getResources().getString(R.string.build_info_status_success);
		}
		else {
			if(buildInfo.getBeginTimeStamp() != null) {
				return ProjectXClientApp.getContext().getResources().getString(R.string.build_info_status_failed);
			}
			else {
				return ProjectXClientApp.getContext().getResources().getString(R.string.build_info_status_cancelled);
			}
		}
	}
}
