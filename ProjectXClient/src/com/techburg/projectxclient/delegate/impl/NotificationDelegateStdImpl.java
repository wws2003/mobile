package com.techburg.projectxclient.delegate.impl;

import java.util.ArrayList;
import java.util.List;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.techburg.projectxclient.BuildInfoActivity;
import com.techburg.projectxclient.R;
import com.techburg.projectxclient.app.ProjectXClientApp;
import com.techburg.projectxclient.delegate.abstr.INotificationDelegate;
import com.techburg.projectxclient.model.BuildInfo;
import com.techburg.projectxclient.util.BuildInfoUtil;

public class NotificationDelegateStdImpl implements INotificationDelegate {
	
	private List<BuildInfo> mBuildInfoBuffer;
	private int mNotificationId;
	private static final String NOTIFICATION_TITLE = "New build info fetched !";
	
	public NotificationDelegateStdImpl() {
		mBuildInfoBuffer = new ArrayList<BuildInfo>();
		mNotificationId = 001;
	}
	
	@Override
	public void notifyUser(List<BuildInfo> newFetchedBuildInfoList, Handler handler) {
		mBuildInfoBuffer.clear();
		mBuildInfoBuffer.addAll(newFetchedBuildInfoList);
		
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				for(BuildInfo buildInfo : mBuildInfoBuffer) {
					//Set up builder for notification
					NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ProjectXClientApp.getContext());
					notificationBuilder.setSmallIcon(buildInfo.getStatus() == BuildInfo.Status.BUILD_SUCCESSFUL ? R.drawable.icon_ok : R.drawable.icon_error)
						.setContentTitle(NOTIFICATION_TITLE)
						.setContentText(BuildInfoUtil.getBuildStatusString(buildInfo) + " " + BuildInfoUtil.getTimeStampString(buildInfo.getBeginTimeStamp()))
						.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
						.setLights(0xffffffff, 300, 100);
					
					//Assign pending intent
					//TODO Think about the context. Currently have to use the application context
					Context context = ProjectXClientApp.getContext();
					Intent intent = new Intent(context, com.techburg.projectxclient.BuildInfoActivity.class);
					intent.putExtra(BuildInfoActivity.EXTRA_BUILD_ID, buildInfo.getId());
					int flags = PendingIntent.FLAG_UPDATE_CURRENT;
					PendingIntent pendingIntent = PendingIntent.getActivity(context,
							0,
							intent,
							flags);
					notificationBuilder.setContentIntent(pendingIntent);
					
					// Issue the notification
					// Gets an instance of the NotificationManager service
					NotificationManager mNotifyMgr = (NotificationManager) ProjectXClientApp.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
					// Builds the notification and issues it.
					mNotifyMgr.notify(mNotificationId++, notificationBuilder.build());
				}
			}
		});
		
	}

}
