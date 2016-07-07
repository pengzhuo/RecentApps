package com.emob.luck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {
	private static final String TAG = "BootBroadcastReceiver";
	private static final String ACTION = "android.intent.action.BOOT_COMPLETED";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION)) {
			//开机自启服务
			Intent intentService = new Intent("com.add.AdService.ACTION");
			// 5.0 必须强制指定package，才能启动Service
			if (Build.VERSION.SDK_INT >= 21) {
				intentService.setPackage("com.eworks.workfolder");
			}
			context.startService(intentService);
			Log.i(TAG, "boot start service success!");
		}
	}

}
