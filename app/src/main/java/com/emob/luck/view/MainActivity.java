package com.emob.luck.view;

import com.emob.luck.AdsDataHelper;
import com.emob.luck.AdsPreferences;
import com.emob.luck.common.Value;
import com.emob.luck.db.AdTableDB;
import com.emob.luck.db.EventTableDB;
import com.emob.luck.model.AdItem;
import com.emob.luck.model.EventItem;
import com.duduws.recent.R;

import android.app.Activity;
import android.content.Intent;
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
//		requestWindowFeature(getWindow().FEATURE_NO_TITLE);
//		setContentView(R.layout.main_activity);
//		mEventTableDB = new EventTableDB(getApplicationContext());
//		mAdTableDB = new AdTableDB(getApplicationContext());
//		mPref = AdsPreferences.getInstance(getApplicationContext());
//		Button btnSpot = (Button) findViewById(R.id.btn_spot);
//		btnSpot.setOnClickListener(this);
//		Button btnBanner = (Button) findViewById(R.id.btn_top);
//		btnBanner.setOnClickListener(this);
//		Button btnAdmob = (Button) findViewById(R.id.btn_admob);
//		btnAdmob.setOnClickListener(this);
//		Button btnAirpush = (Button) findViewById(R.id.btn_airpush);
//		btnAirpush.setOnClickListener(this);
//		Button btnWindowBanner = (Button) findViewById(R.id.btn_window_banner);
//		btnWindowBanner.setOnClickListener(this);
//		Button btnWindowSpot = (Button) findViewById(R.id.btn_window_spot);
//		btnWindowSpot.setOnClickListener(this);
//
//		boolean isServiceRunning = MobiUtils.isServiceRunning(getApplicationContext(), AdsService.class.getName());
//		if (!isServiceRunning) {
//			Intent intent = new Intent(getApplicationContext(),
//					AdsService.class);
//			startService(intent);
//		}

		showFolder();
		finish();
	}

	private void showFolder() {
		Log.d(TAG, "#### FacebookActivity.showFolder begin");
		try {
			Intent intent = new Intent();
			intent.setClass(this, AmActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
			this.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, "#### FacebookActivity.showFolder end");
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
