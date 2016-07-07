package com.emob.luck;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.emob.lib.log.EmobLog;

public class HeaService extends Service {
	
	public static Context mContext;
	private static final String TAG = "HeaService";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
    @Override
    public void onCreate() {
    	EmobLog.d(TAG, "HeaService.onCreate begin");
		super.onCreate();
		start2Heart(getApplicationContext());
		EmobLog.d(TAG, "HeaService.onCreate end");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		EmobLog.d(TAG, "HeaService.onStartCommand begin");
		
		return START_STICKY;
	}


	@Override
	public void onDestroy() {
		EmobLog.d(TAG, "HeaService.onDestroy begin");
		super.onDestroy();
		EmobLog.d(TAG, "HeaService.onDestroy end");
	}

    private void start2Heart(Context context) {
    	EmobLog.e("HeaService.start2Heart begin");
    	
    	new Thread() {
			public void run() {
				AdsDataHelper.request4Heart(HeaService.this);
				
				HeaService.this.stopSelf();
			}
		}.start();
    }
   
}
