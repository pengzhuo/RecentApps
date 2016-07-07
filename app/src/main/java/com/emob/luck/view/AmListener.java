package com.emob.luck.view;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.emob.luck.common.CommonDefine;
import com.google.android.gms.ads.AdListener;

public class AmListener extends AdListener{
	private Handler mHandler;
	private Context mContext;
	public static final int AD_REQUEST_SUCCEEDED = 101;
	public static final int AD_REQUEST_FAILED = 102;
	public static final int ON_SHOW_AD = 103;
	public static final int ON_DISMISS_CLOSE_AD = 104;
	public static final int ON_CLICK = 105;
	
	public AmListener(Context context, Handler mHandler) {
		super();
		this.mHandler = mHandler;
		this.mContext = context;
	}
	
	@Override
	public void onAdClosed() {
		mHandler.sendEmptyMessage(ON_DISMISS_CLOSE_AD);
		super.onAdClosed();
	}
	
	@Override
	public void onAdFailedToLoad(int errorCode) {
		mHandler.sendEmptyMessage(AD_REQUEST_FAILED);
		if(CommonDefine.TEST_MARK) {
			Toast.makeText(mContext, "Admob Ad request failed : " + errorCode, Toast.LENGTH_SHORT).show();
		}
		super.onAdFailedToLoad(errorCode);
	}
	
	@Override
	public void onAdOpened() {
		mHandler.sendEmptyMessage(ON_SHOW_AD);
		if(CommonDefine.TEST_MARK) {
			Toast.makeText(mContext, "Admob Ad show", Toast.LENGTH_SHORT).show();
		}
		super.onAdOpened();
	}
	
	@Override
	public void onAdLeftApplication() {
		mHandler.sendEmptyMessage(ON_CLICK);
		super.onAdLeftApplication();
	}
	
	@Override
	public void onAdLoaded() {
		mHandler.sendEmptyMessage(AD_REQUEST_SUCCEEDED);
		if(CommonDefine.TEST_MARK) {
			Toast.makeText(mContext, "Admob Ad request succeeded", Toast.LENGTH_SHORT).show();
		}
		super.onAdLoaded();
	}
}
