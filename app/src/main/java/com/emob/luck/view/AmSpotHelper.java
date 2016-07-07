package com.emob.luck.view;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.emob.lib.log.EmobLog;
import com.emob.lib.stats.StatsDefines;
import com.emob.lib.stats.StatsUtil;
import com.emob.luck.DspHelper;
import com.emob.luck.common.CommonDefine;
import com.emob.luck.common.Value;
import com.emob.luck.model.EventItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AmSpotHelper {
	private final static String TAG = "Ads";
	private static AmSpotHelper mInstance;
	
	private InterstitialAd mInterstitialAd;
	private AmListener mListener;
	private String mPackageName;
	private Context mContext;
	private int mSdkChannel;
	private int mAdPos;
	private String mRandomString;
	private Handler mHandler;
	
	public static AmSpotHelper getInstance() {
		if (mInstance == null) {
			mInstance = new AmSpotHelper();
		}
		
		return mInstance;
	}
	public void init(Context context) {
		mContext = context;
		mInterstitialAd = new InterstitialAd(context);
	}
	
	public InterstitialAd getInterstitialAd(Context context) {
		if (mInterstitialAd == null) {
			mInterstitialAd = new InterstitialAd(context);
		}
		return mInterstitialAd;
	}
	
	public void setPackageName(String pkgName) {
		mPackageName = pkgName;
	}
	
	public void setSdkChannel(int sdkChannel) {
		mSdkChannel = sdkChannel;
	}
	
	public void setAdPos(int adPos) {
		mAdPos = adPos;
	}
	
	public void setRandomString(String randomString) {
		mRandomString = randomString;
	}

	public void setHandler(Handler handler) {
		mHandler = handler;
	}
	
	public void load() {
		EmobLog.d(TAG, "AmSpotHelper.load begin");
		mInterstitialAd.setAdUnitId(mRandomString);
		mInterstitialAd.getAdListener();
		mListener = new AmListener(mContext, mHandler);
		mInterstitialAd.setAdListener(mListener);
		loadAd();
		EmobLog.d(TAG, "AmSpotHelper.load end");
	}

	public void showInterstitial() {
		EmobLog.d(TAG, "AmSpotHelper.showInterstitial begin");
		// Show the ad if it's ready. Otherwise toast and restart the game.
		if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
			mInterstitialAd.show();
			EmobLog.d(TAG, "AmSpotHelper.showInterstitial ok");
		} else {
			EmobLog.d(TAG, "AmSpotHelper.showInterstitial not ready");
			StatsUtil.onEventOfferBackground(mContext,mPackageName, EventItem.EVENT_TYPE_NOTREDAY,
					mSdkChannel, mAdPos);
		}
		EmobLog.d(TAG, "AmSpotHelper.showInterstitial end");
	}

	private void loadAd() {
		try {
			AdRequest adRequest = new AdRequest.Builder().build();
			mInterstitialAd.loadAd(adRequest);
			StatsUtil.onEventOffer(mContext, mPackageName, EventItem.EVENT_TYPE_REQUEST, mSdkChannel,
					mAdPos);
			StatsUtil.onEventOffer(mContext, StatsDefines.EVENT_TYPE_REQ);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
	
}
