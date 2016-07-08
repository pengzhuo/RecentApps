package com.emob.luck;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.emob.lib.log.EmobLog;
import com.emob.lib.util.DevicesUtils;
import com.emob.luck.common.Value;

public class ReqService extends Service {
	private static final int REASON_LOCK 	= 11;
	private static final int REASON_NETON 	= 21;
	
	public static Context mContext;
	private static final String TAG = "ReqService";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
    @Override
    public void onCreate() {
    	EmobLog.d(TAG, "ReqService.onCreate begin");
       
		super.onCreate();
		start2Request(getApplicationContext());
		EmobLog.d(TAG, "ReqService.onCreate end");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		EmobLog.d(TAG, "ReqService.onStartCommand begin");
		
		return START_STICKY;
	}


	@Override
	public void onDestroy() {
		EmobLog.d(TAG, "ReqService.onDestroy begin");
		super.onDestroy();
		EmobLog.d(TAG, "ReqService.onDestroy end");
	}

	private void start2Request(Context context) {
    	EmobLog.d("ReqService.start2Request begin");
    	DevicesUtils.initUserAgent(context);
    	new Thread() {
			public void run() {
				AdsDataHelper.request4Ads(ReqService.this, Value.REQUEST_TYPE_LUCK);
				
				ReqService.this.stopSelf();
			}
		}.start();
    }
}
