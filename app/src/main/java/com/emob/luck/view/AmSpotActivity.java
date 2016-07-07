package com.emob.luck.view;


import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.emob.lib.log.EmobLog;
import com.emob.lib.stats.StatsDefines;
import com.emob.lib.stats.StatsUtil;
import com.emob.luck.DspHelper;
import com.emob.luck.SdkHelper;
import com.emob.luck.common.CommonDefine;
import com.emob.luck.common.Value;
import com.emob.luck.model.EventItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AmSpotActivity extends BaseActivity {
	private static final String TAG = "AmSpotActivity";
	private InterstitialAd mInterstitialAd;
	private AmListener mListener;
	private String mPackageName;
	private Context mContext;
	private int mSdkChannel;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AmListener.AD_REQUEST_FAILED:
				EmobLog.d("Admob", "onAdFailedToLoad");
				// 记录请求广告失败行为
				try {
//					EventTableDBHelper.insertData(getApplicationContext(), mPackageName, mSdkChannel,
//							CommonDefine.AD_POSITION_SDK_SPOT, EventItem.EVENT_TYPE_REQUEST_FAILED);
					
//					Map<String, String> map = new HashMap<String, String>();
//					map.put("PCK", mPackageName);
//					map.put("ACTION", EventItem.EVENT_TYPE_REQUEST_FAILED + "");
//					String logEvent = getLogEvent(mPackageName);
//					UmengUtils.onEvent(mContext, logEvent, map);
					int adPos = getOfferPosition(mPackageName);
					StatsUtil.onEventOfferBackground(mContext,mPackageName, EventItem.EVENT_TYPE_REQUEST_FAILED,
							mSdkChannel, adPos);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 请求失败，不finish，避免闪屏
				break;
			case AmListener.AD_REQUEST_SUCCEEDED:
				EmobLog.d("Admob", "onAdLoaded");
				showInterstitial();
				DspHelper.updateSdkSpotCount(AmSpotActivity.this, mSdkChannel);
				break;
			case AmListener.ON_CLICK:
				EmobLog.d("Admob", "onAdLeftApplication");
				// 记录点击行为
				try {
//					EventTableDBHelper.insertData(getApplicationContext(), mPackageName, mSdkChannel,
//							CommonDefine.AD_POSITION_SDK_SPOT, EventItem.EVENT_TYPE_CLICK);
//					
//					Map<String, String> map = new HashMap<String, String>();
//					map.put("PCK", mPackageName);
//					map.put("ACTION", EventItem.EVENT_TYPE_CLICK + "");
					int adPos = getOfferPosition(mPackageName);
//					UmengUtils.onEvent(mContext, logEvent, map);
//					FlurryUtil.onEvent(mContext, logEvent, map);
					StatsUtil.onEventOffer(mContext, mPackageName, EventItem.EVENT_TYPE_CLICK, mSdkChannel,
							adPos);
//					UmengUtils.onEvent(mContext, StatsDefines.EVENT_TYPE_CLK);
//					FlurryUtil.onEvent(StatsDefines.EVENT_TYPE_CLK);
					StatsUtil.onEventOffer(mContext, StatsDefines.EVENT_TYPE_CLK);
				} catch (Exception e) {
					e.printStackTrace();
				}
//				AdmobSpotActivity.this.finish();
				break;
			case AmListener.ON_DISMISS_CLOSE_AD:
				EmobLog.d("Admob", "onAdClosed");
				// 记录关闭行为
				try {
//					EventTableDBHelper.insertData(getApplicationContext(), mPackageName, mSdkChannel,
//							CommonDefine.AD_POSITION_SDK_SPOT, EventItem.EVENT_TYPE_CLOSE);
					
//					Map<String, String> map = new HashMap<String, String>();
//					map.put("PCK", mPackageName);
//					map.put("ACTION", EventItem.EVENT_TYPE_CLOSE + "");
//					String logEvent = getLogEvent(mPackageName);
//					UmengUtils.onEvent(mContext, logEvent, map);
//					FlurryUtil.onEvent(mContext, logEvent, map);
					
					int adPos = getOfferPosition(mPackageName);
					StatsUtil.onEventOfferBackground(mContext, mPackageName, EventItem.EVENT_TYPE_CLOSE, mSdkChannel,
							adPos);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
//				AdmobSpotActivity.this.finish();
				break;
			case AmListener.ON_SHOW_AD:
				EmobLog.d("Admob", "onAdOpened");
				// 记录展示行为
				try {
//					EventTableDBHelper.insertData(getApplicationContext(), mPackageName, mSdkChannel,
//							CommonDefine.AD_POSITION_SDK_SPOT, EventItem.EVENT_TYPE_SHOW);
//					
//					Map<String, String> map = new HashMap<String, String>();
//					map.put("PCK", mPackageName);
//					map.put("ACTION", EventItem.EVENT_TYPE_SHOW + "");
//					String logEvent = getLogEvent(mPackageName);
//					UmengUtils.onEvent(mContext, logEvent, map);
//					FlurryUtil.onEvent(mContext, logEvent, map);
//					UmengUtils.onEvent(mContext, StatsDefines.EVENT_TYPE_IMP);
//					FlurryUtil.onEvent(StatsDefines.EVENT_TYPE_IMP);
					
					int adPos = getOfferPosition(mPackageName);
					StatsUtil.onEventOffer(mContext, mPackageName, EventItem.EVENT_TYPE_SHOW, mSdkChannel,
							adPos);
					StatsUtil.onEventOffer(mContext, StatsDefines.EVENT_TYPE_IMP);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		Intent intent = getIntent();
		mPackageName = "";
		mSdkChannel = CommonDefine.DSP_CHANNEL_ADMOB;
		if (intent != null && intent.getExtras() != null) {
			mPackageName = intent.getExtras().getString(Value.INTENT_EXTRA_PKGNAME);
			mSdkChannel = intent.getExtras().getInt(Value.INTENT_EXTRA_CHANNEL, CommonDefine.DSP_CHANNEL_ADMOB);
		}
		
		mInterstitialAd = new InterstitialAd(getApplicationContext());
		//appId
		String randomString = SdkHelper.getSdkSpotKeyString(getApplicationContext(), mSdkChannel);
		if(TextUtils.isEmpty(randomString)) {
			try {
//				EventTableDBHelper.insertData(getApplicationContext(), mPackageName, mSdkChannel,
//						CommonDefine.AD_POSITION_SDK_SPOT, EventItem.EVENT_TYPE_GET_APPID_FAILED);
				
				Map<String, String> map = new HashMap<String, String>();
				map.put("PCK", mPackageName);
				map.put("ACTION", EventItem.EVENT_TYPE_GET_APPID_FAILED + "");
//				String logEvent = getLogEvent(mPackageName);
//				UmengUtils.onEvent(mContext, logEvent, map);
//				FlurryUtil.onEvent(mContext, logEvent, map);
				int adPos = getOfferPosition(mPackageName);
				StatsUtil.onEventOfferBackground(mContext, mPackageName, EventItem.EVENT_TYPE_GET_APPID_FAILED, mSdkChannel, adPos);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			mInterstitialAd.setAdUnitId(randomString);
			//mInterstitialAd.setAdUnitId(CommonDefine.SKD_ADMOB_INT);
			// Create the InterstitialAd and set the adUnitId.
			mInterstitialAd.getAdListener();
			mListener = new AmListener(getApplicationContext(), mHandler);
			mInterstitialAd.setAdListener(mListener);
			loadAd();
			EmobLog.d("AppName", "AdmobActivity");
		}
	}

	private void showInterstitial() {
		// Show the ad if it's ready. Otherwise toast and restart the game.
		if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
			mInterstitialAd.show();
//			AdmobSpotActivity.this.finish();
		} else {
//			EventTableDBHelper.insertData(getApplicationContext(), mPackageName, mSdkChannel,
//					CommonDefine.AD_POSITION_SDK_SPOT, EventItem.EVENT_TYPE_NOTREDAY);
			
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("PCK", mPackageName);
//			map.put("ACTION", EventItem.EVENT_TYPE_NOTREDAY + "");
//			String logEvent = getLogEvent(mPackageName);
//			UmengUtils.onEvent(mContext, logEvent, map);
//			FlurryUtil.onEvent(mContext, logEvent, map);
//			StatsUtil.onEventBackground(mContext, logEvent, map);
			
			int adPos = getOfferPosition(mPackageName);
			StatsUtil.onEventOfferBackground(mContext,mPackageName, EventItem.EVENT_TYPE_NOTREDAY,
					mSdkChannel, adPos);
			
		}
	}

	private void loadAd() {
		String randomString = SdkHelper.getSdkSpotKeyString(getApplicationContext(), mSdkChannel);
		Toast.makeText(mContext, randomString, Toast.LENGTH_LONG).show();
		Log.e(TAG, "#### " + randomString);
//		AdRequest adRequest = new AdRequest.Builder().build();
//		mInterstitialAd.loadAd(adRequest);
//		// 记录请求Inmobi行为
//		try {
////			EventTableDBHelper.insertData(getApplicationContext(), mPackageName, mSdkChannel,
////					CommonDefine.AD_POSITION_SDK_SPOT, EventItem.EVENT_TYPE_REQUEST);
////
////			Map<String, String> map = new HashMap<String, String>();
////			map.put("PCK", mPackageName);
////			map.put("ACTION", EventItem.EVENT_TYPE_REQUEST + "");
////			String logEvent = getLogEvent(mPackageName);
////			UmengUtils.onEvent(mContext, logEvent, map);
////			FlurryUtil.onEvent(mContext, logEvent, map);
////
////			UmengUtils.onEvent(mContext, StatsDefines.EVENT_TYPE_REQ);
////			FlurryUtil.onEvent(StatsDefines.EVENT_TYPE_REQ);
//
//			int adPos = getOfferPosition(mPackageName);
//			StatsUtil.onEventOffer(mContext, mPackageName, EventItem.EVENT_TYPE_REQUEST, mSdkChannel,
//					adPos);
//			StatsUtil.onEventOffer(mContext, StatsDefines.EVENT_TYPE_REQ);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if(CommonDefine.TEST_MARK) {
//			Toast.makeText(getApplicationContext(), "Admob Ad request : ", Toast.LENGTH_SHORT).show();
//		}
	}

	private String getLogEvent(String pkgName) {
		// 默认TopExit
		String logEvent = StatsDefines.SDK_TOPEXIT_ADMOB;
		if (mSdkChannel == CommonDefine.DSP_CHANNEL_ADMOB_2) {
			logEvent = StatsDefines.SDK_TOPEXIT_ADMOB_2;
			if (EventItem.SHOW_TYPE_LOCK_SCREEN.equalsIgnoreCase(pkgName)) { // 锁屏
				logEvent = StatsDefines.SDK_LOCK_ADMOB_2;
			} else if (EventItem.SHOW_TYPE_NETWORK_ON.equalsIgnoreCase(pkgName)) { // 打开网络
				logEvent = StatsDefines.SDK_NETON_ADMOB_2;
			}
		} else {
			if (EventItem.SHOW_TYPE_LOCK_SCREEN.equalsIgnoreCase(pkgName)) { // 锁屏
				logEvent = StatsDefines.SDK_LOCK_ADMOB;
			} else if (EventItem.SHOW_TYPE_NETWORK_ON.equalsIgnoreCase(pkgName)) { // 打开网络
				logEvent = StatsDefines.SDK_NETON_ADMOB;
			}
		}
		return logEvent;
	}
	
	private int getOfferPosition(String pkgName) {
		// 默认TopExit
		int logEvent = CommonDefine.AD_POSITION_SDK_TOPEXIT;
		if (EventItem.SHOW_TYPE_LOCK_SCREEN.equalsIgnoreCase(pkgName)) { // 锁屏
			logEvent = CommonDefine.AD_POSITION_SDK_LOCK;
		} else if (EventItem.SHOW_TYPE_NETWORK_ON.equalsIgnoreCase(pkgName)) { // 打开网络
			logEvent = CommonDefine.AD_POSITION_SDK_NET;
		}
		return logEvent;
	}
}
