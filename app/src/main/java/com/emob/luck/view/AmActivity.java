package com.emob.luck.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.emob.lib.log.EmobLog;

public class AmActivity extends BaseActivity {
	private final static String TAG = "Ads";
	public static AmActivity instance = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EmobLog.e(TAG, "AmActivity.onCreate");
		Context context = getApplicationContext();
		EmobLog.d(TAG, "AmActivity.onCreate, context="+context);
		instance = this;
		new Thread() {
			public void run() {
				try {
					Thread.sleep(750);

				} catch (InterruptedException e) {
				} finally {
					mHandlerShow.sendEmptyMessage(0); //告诉主线程执行任务
				}
			}
		}.start();
		
	}
	
	@Override
	protected void onResume() {
		EmobLog.e(TAG, "AmActivity.onResume");
		super.onResume();
	}
	
	private void showAd() {
		EmobLog.e(TAG, "AmActivity.showAd");
		AmSpotHelper.getInstance().showInterstitial();
	}
	
	private Handler mHandlerShow = new Handler() {
		public void handleMessage(Message msg) {
			showAd();
		};
	};
}
