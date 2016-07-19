package com.mobi.fork;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.emob.lib.log.EmobLog;
import com.emob.lib.util.MobiUtils;
import com.emob.luck.AdsService;
import com.mobi.fork.GuardHelper;

public class MainService extends Service {

	private final static String TAG = "EmobDaemon";
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "#### MainService: onCreate!");

		boolean isServiceRunning = MobiUtils.isServiceRunning(getApplicationContext(), AdsService.class.getName());
		EmobLog.d("#### MilanoReceiver.onReceive ACTION_BOOT_COMPLETED, service running:"+isServiceRunning);
    	if (!isServiceRunning) {
    		GuardHelper.startDaemon(getApplicationContext(), "com.emob.luck.AdsService");
    	}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "MainService: onStartCommand! ");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
