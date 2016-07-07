package com.emob.luck.view;

import android.content.Context;
import android.os.Handler;

import com.inmobi.monetization.IMErrorCode;
import com.inmobi.monetization.IMNative;
import com.inmobi.monetization.IMNativeListener;

public class AdInmobNativeListener implements IMNativeListener {
	private Context mContext;
	private Handler mHandler;
	
	public static final int AD_REQUEST_SUCCEEDED = 101;
	public static final int AD_REQUEST_FAILED = 102;
	
	public AdInmobNativeListener(Context mContext, Handler mHandler) {
		super();
		this.mContext = mContext;
		this.mHandler = mHandler;
	}

	@Override
	public void onNativeRequestFailed(IMErrorCode arg0) {
		mHandler.sendEmptyMessage(AD_REQUEST_FAILED);
		
	}

	@Override
	public void onNativeRequestSucceeded(IMNative arg0) {
//		Bundle bundle = new Bundle();
//		bundle.putSerializable("33", (Serializable) arg0);
//		Message msg = new Message();
//		msg.setData(bundle);
//		mHandler.sendMessage(msg);
		mHandler.sendEmptyMessage(AD_REQUEST_SUCCEEDED);
		
	}

}
