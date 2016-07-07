package com.emob.luck.view;

import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.emob.luck.common.CommonDefine;
import com.inmobi.monetization.IMErrorCode;
import com.inmobi.monetization.IMInterstitial;
import com.inmobi.monetization.IMInterstitialListener;

public class ImSpotListener implements IMInterstitialListener{
	private Context mContext;
	private Handler mHandler;
	
	public ImSpotListener(Context mContext, Handler mHandler) {
		super();
		this.mContext = mContext;
		this.mHandler = mHandler;
	}

	@Override
	public void onLeaveApplication(IMInterstitial arg0) {
		mHandler.sendEmptyMessage(ImSpotActivity.ON_LEAVE_APP);
	}

	@Override
	public void onDismissInterstitialScreen(IMInterstitial arg0) {
		mHandler.sendEmptyMessage(ImSpotActivity.ON_DISMISS_MODAL_AD);
	}

	@Override
	public void onInterstitialFailed(IMInterstitial arg0, IMErrorCode eCode) {
		Message msg = mHandler.obtainMessage(ImSpotActivity.AD_REQUEST_FAILED);
		msg.obj = eCode;
		mHandler.sendMessage(msg);
		if(CommonDefine.TEST_MARK) {
			Toast.makeText(mContext, "Inmobi Ad request failed : " + eCode, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onInterstitialInteraction(IMInterstitial arg0,
			Map<String, String> arg1) {
	}

	@Override
	public void onInterstitialLoaded(IMInterstitial arg0) {
		if (arg0.getState() == IMInterstitial.State.READY) {
			arg0.show();
			mHandler.sendEmptyMessage(ImSpotActivity.AD_REQUEST_SUCCEEDED);
			if(CommonDefine.TEST_MARK) {
				Toast.makeText(mContext, "Inmobi Ad request succeeded and show", Toast.LENGTH_SHORT).show();
			}
		} else {
			mHandler.sendEmptyMessage(ImSpotActivity.AD_REQUEST_SUCCEEDED_NOTREADY);
			if(CommonDefine.TEST_MARK) {
				Toast.makeText(mContext, "Inmobi Ad request succeeded, but not ready", Toast.LENGTH_SHORT).show();
			}
		}
		
	}

	@Override
	public void onShowInterstitialScreen(IMInterstitial arg0) {
		mHandler.sendEmptyMessage(ImSpotActivity.ON_SHOW_MODAL_AD);
	}

}
