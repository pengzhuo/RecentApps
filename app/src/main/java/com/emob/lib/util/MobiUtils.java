package com.emob.lib.util;

import java.util.List;

import com.emob.lib.log.EmobLog;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.text.TextUtils;

public class MobiUtils {

	public static boolean isServiceRunning(Context context, String serviceName) {   
		EmobLog.d("AdsReceiver.isServiceRunning begin");
		if (TextUtils.isEmpty(serviceName)) {
			return false;
		}
		
		boolean isRunning = false;
		final ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = activityManager.getRunningServices(Integer.MAX_VALUE);
		
		EmobLog.d("AdsReceiver.isServiceRunning serviceName:"+serviceName);
		for (RunningServiceInfo service : runningServices) {
			if (service.service.getClassName().equalsIgnoreCase(serviceName)) {
    			EmobLog.e("serviceName: "+serviceName);
    			EmobLog.e("service.uid: "+service.uid + ", app.uid: "+context.getApplicationInfo().uid);
    			if (service.uid == context.getApplicationInfo().uid) {
    				isRunning = true;
    				break;
    			}
    		}
		}
		EmobLog.d("AdsReceiver.isServiceRunning end, isRunning:"+isRunning);
		return isRunning;
    }
	
	
}
