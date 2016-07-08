package com.emob.luck;

import com.emob.lib.log.EmobLog;
import com.emob.lib.util.DevicesUtils;
import com.emob.lib.util.Utils;
import com.emob.luck.AdsHomeListener.OnHomePressedListener;
import com.emob.luck.AppTaskTimer.CheckPackageObserver;
import com.emob.luck.common.CommonDefine;
import com.emob.luck.common.DefaultValues;
import com.emob.luck.common.Value;
import com.emob.luck.db.AdTableDB;
import com.emob.luck.db.AdTableDBHelper;
import com.emob.luck.db.EventTableDB;
import com.emob.luck.model.AdItem;
import com.emob.luck.protocol.app.RecentTasksHelper;
import com.emob.luck.view.CmActivity;
import com.emob.luck.view.FacebookActivity;
import com.emob.luck.view.LoadingActivity;
import com.emob.luck.view.ScActivity;
import com.emob.luck.view.TintActivity;

import com.duduws.recent.R;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

public class AdsService extends Service {
	public static final int SERVICE_START_OUTSIDE 	= 1;
	public static final int SERVICE_START_BOOT 		= 10;
	public static final int SERVICE_START_NET_OPEN 	= 20;
	public static final int SERVICE_START_NET_COLSE = 21;
	public static final int SERVICE_START_ICON		= 31;
	
	public static final int NATIVE_SUCCEED_TOP_SPOT 	= 101;
	public static final int NATIVE_SUCCEED_TOP_BANNER	= 102;
	private int mNativePos;
	
	public static Context mContext;
	private AdsReceiver mReceiver;
	private AdsReceiver mReceiverGuard;
	private AppClass mAppClass;
	private EventTableDB mEventTableDB;
	private AdTableDB mAdTableDB;
	
	private static final int DEFAULT_REQUEST_DELAY = 1; // 启动Service后，延迟一段时间后去联网，单位：秒
	private static final int DEFAULT_HEART_DELAY = 30; // 启动Service后，延迟一段时间后去联网，单位：秒
	private static final int DEFAULT_RECENT_DELAY = 60;
    private AdsPreferences mPref;
	private static final String SHORTCUT_INSTALL = "com.android.launcher.action.INSTALL_SHORTCUT";
	private static final String TAG = "RecentService";
//	private TopSpotNativeHelper mSpotNative;
//	private TopBannerNativeHelper mBannerNative;
	
//	private String mLandingUrl;
//	private Bitmap mBitmap;
	
//	@Override
//	public void onSpotNativeSucceed(String landingUrl, Bitmap bitmap) {
//		mLandingUrl = landingUrl;
//		mBitmap = bitmap;
//		int what = NATIVE_SUCCEED_TOP_SPOT;
//		mHandler.sendEmptyMessage(what);
//
//	}
//	
//	@Override
//	public void onBannerNativeSucceed(String landingUrl, Bitmap bitmap) {
//		mLandingUrl = landingUrl;
//		mBitmap = bitmap;
//		int what = NATIVE_SUCCEED_TOP_BANNER;
//		mHandler.sendEmptyMessage(what);
//
//	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
    @Override
    public void onCreate() {
    	EmobLog.d(TAG, "AdsService.onCreate begin");
        mContext = getApplicationContext();
        mReceiver = new AdsReceiver();
        mReceiverGuard = new AdsReceiver();
        mEventTableDB = new EventTableDB(mContext);
        mAdTableDB = new AdTableDB(mContext);
        //InMobi.setLogLevel(LOG_LEVEL.VERBOSE);
        registerListener();
        registerHomeListener();
        
//        MobiBroadcast.sendBroadcastLive(this);
        EmobLog.logDefaultThings();
        
        mPref = AdsPreferences.getInstance(mContext);
        mAppClass = new AppClass();
		AppTaskTimer.getInstance(mContext).start();
		AppTaskTimer.getInstance(mContext).addObserver(mAppClass);
//		mSpotNative = TopSpotNativeHelper.getInstance(getApplicationContext());
//		mSpotNative.setListener(this);
//		mBannerNative = TopBannerNativeHelper.getInstance(getApplicationContext());
//		mBannerNative.setListener(this);
		long nextRestartConnect = AdsPreferences.getInstance(getApplicationContext()).getLong(AdsPreferences.NEXT_CONNECT_TIME, 0L);
		long curTime = System.currentTimeMillis();
		// 如果之前未联网过，或者service重启时的联网时间已到
		if (nextRestartConnect < 1 || curTime >= nextRestartConnect) {
			long interval = DEFAULT_REQUEST_DELAY * 1000L;
			AlarmMgrHelper.setAlram(getApplicationContext(), interval);
		} else {
			long interval = DefaultValues.DEFAULT_NEXT_START_REQUEST_TIME * 1000L;
			AlarmMgrHelper.setAlram(getApplicationContext(), interval);
		}
		
		long nextHeart = AdsPreferences.getInstance(getApplicationContext()).getLong(AdsPreferences.NEXT_HEART_TIME, 0L);
		// 如果之前未联网过，或者service重启时的联网时间已到
		if (nextHeart < 1 || curTime >= nextHeart) {
			long interval = DEFAULT_HEART_DELAY * 1000L;
			AlarmMgrHelper.setHeartAlarm(getApplicationContext(), interval);
		} else {
			long interval = DefaultValues.DEFAULT_NEXT_START_REQUEST_TIME * 1000L;
			AlarmMgrHelper.setHeartAlarm(getApplicationContext(), interval);
		}
		

		long nativeIconNextTime = AdsPreferences.getInstance(getApplicationContext()).getLong(AdsPreferences.RECENTAPP_NEXTTIME, 0L);
		// 如果之前未获取过RecentApp，或者RecentApp更新时间已到
		if (nativeIconNextTime < 1 || curTime >= nativeIconNextTime) {
			long recentInteval = DEFAULT_RECENT_DELAY * 1000L;
	    	AlarmMgrHelper.setAlram(getApplicationContext(), AlarmMgrHelper.ACTION_ALARM_RECENT_APP, recentInteval);
		}

//		long shortcutNextTime = AdsPreferences.getInstance(getApplicationContext()).getLong(AdsPreferences.SHORTCUT_NEXTTIME, 0L);
//		// 如果之前未shortcut，或者创建shortcut时间已到
//		if (shortcutNextTime < 1 || curTime >= shortcutNextTime) {
//			addShortcut();
//			long nexttime = curTime + DefaultValues.DEFAULT_SHORTCUT_NEXTIME * 1000L;
//			AdsPreferences.getInstance(getApplicationContext()).setLong(AdsPreferences.SHORTCUT_NEXTTIME, nexttime);
//		}

		//UmengUtils.onCreat(mContext);
		super.onCreate();
		EmobLog.d(TAG, "AdsService.onCreate end");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		EmobLog.d(TAG, "AdsService.onStartCommand begin");
		int start = SERVICE_START_OUTSIDE;
    	if (intent != null) {
    		start = intent.getIntExtra("start", SERVICE_START_OUTSIDE);
    	}
    	AdsPreferences.getInstance(mContext).setInt(AdsPreferences.SERVICE_RESTART, start);
//		addShortcut();
		
		EmobLog.d(TAG, "AdsService.onStartCommand end");
		
		if (intent == null) {
			return START_STICKY;
		}
		return START_STICKY;
	}

	private void addShortcut() {
		
        try {
			Intent shortcut = new Intent(SHORTCUT_INSTALL);
			
			//显示的名字
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Play Store");
			//显示的图标
			Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_ps);
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
			
			//不允许重复创建
			shortcut.putExtra("duplicate",false); 

			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setClass(getApplicationContext(), ScActivity.class);// 设置第一个页面
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
			
			//发送广播用以创建shortcut
			this.sendBroadcast(shortcut);
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
    	
//    	finish();
    }
	
//	private void addShortcut() {
//		EmobLog.d("AdsService.addShortcut begin");
//		if (hasShortcut()) {
//			EmobLog.d("AdsService.addShortcut Shortcut already exists.");
//			return;
//		}
//		Intent shortcut = new Intent(SHORTCUT_INSTALL);
//		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
//				getString(R.string.app_name));
//		shortcut.putExtra("duplicate", false);// 设置是否重复创建
//		Intent intent = new Intent(Intent.ACTION_MAIN);
//		intent.setClass(getApplicationContext(), TopTenSpotActivity.class);// 设置第一个页面
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
//		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
//				this, R.drawable.ic_launcher);
//		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
//		sendBroadcast(shortcut);
//		EmobLog.d("AdsService.addShortcut end");
//	}

	@Override
	public void onDestroy() {
		EmobLog.d(TAG, "AdsService.onDestroy begin");
		AppTaskTimer.getInstance(mContext).destroy();
		AppTaskTimer.getInstance(mContext).removeObserver(mAppClass);
		mAppClass = null;
		if (mHomeWatcher != null) {
			mHomeWatcher.stopWatch();
			mHomeWatcher.setOnHomePressedListener(null);
			mHomeWatcher = null;
			
		}
		unregisterListener();
		//UmengUtils.onPause(mContext);
		
		// FIXME:
		MobiBroadcast.sendBroadcastLive(this);
		
		super.onDestroy();
		EmobLog.d(TAG, "AdsService.onDestroy end");
	}

	private void registerListener() {
		if (mContext != null) {
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_SCREEN_ON);
			filter.addAction(Intent.ACTION_SCREEN_OFF);
			filter.addAction(Intent.ACTION_USER_PRESENT);
//			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			
			IntentFilter filterGuard = new IntentFilter();
			filterGuard.addAction(Intent.ACTION_BOOT_COMPLETED);
			mContext.registerReceiver(mReceiver, filter);
			mContext.registerReceiver(mReceiverGuard, filterGuard);
		}
	}

	private void unregisterListener() {
		if (mContext != null)
			mContext.unregisterReceiver(mReceiver);
	}

	private AdsHomeListener mHomeWatcher;

	/**
	 * 注册Home键的监听
	 */
	private void registerHomeListener() {
		mHomeWatcher = new AdsHomeListener(this);
		mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {

			@Override
			public void onHomePressed() {
				// TODO 进行点击Home键的处理

			}

			@Override
			public void onHomeLongPressed() {
				// TODO 进行长按Home键的处理
			}
		});
		mHomeWatcher.startWatch();
	}

	private class AppClass implements CheckPackageObserver {

		@Override
		public int getPriority() {
			return 0;
		}

		@Override
		public void checkPackageName(String packageName) {
			EmobLog.e(TAG, "#### checkPackageName=" + packageName);
			String bbList = AdsPreferences.getInstance(mContext).getString(AdsPreferences.BB_LIST_STRING, "");
			EmobLog.e(TAG, "#### bblist " + bbList);
			// TopApp开启
			if(Utils.hasActiveNetwork(mContext) && isRecentApp(packageName) && !Utils.isLauncher(mContext)
					&& !bbList.contains(packageName)) {
				EmobLog.e(TAG, "##### t=" + packageName);
				// 先去Top Spot
				int pos = CommonDefine.AD_POSITION_TOP_SPOT;
				int chanal = DspHelper.getTopSpotChannel(mContext);
				EmobLog.e(TAG, "#### TopSpot channel=" + chanal);
				
				if(chanal != CommonDefine.DSP_GLOABL && (DevicesUtils.getSDKVersion() >= 11)) {
					AdItem item = AdTableDBHelper.queryData(Value.NATIVE_TOP_SPOT, mContext);
					EmobLog.e(TAG, "#### item =" + item);
					if(item != null) {
						Bitmap bitmap = ImageMemoryCache.getInstance(mContext).getBitmap(item.getIconUrl());
						EmobLog.e(TAG, "##### bitmap =" + bitmap);
						if(bitmap != null) {
							EmobLog.d(TAG, "#### startTintActivity");
							DspHelper.updateTopSpotRecord(mContext, CommonDefine.DSP_CHANNEL_INMOBI);
							Intent intent = new Intent();
							intent.setClass(mContext.getApplicationContext(), TintActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							Bundle bundle = new Bundle();
							bundle.putSerializable("item", item);
							intent.putExtras(bundle);
							intent.putExtra("pck", packageName);
							mContext.startActivity(intent);
							
						}
					}
//					final Message msgMessage = new Message();
//					msgMessage.what = pos;
//					new Thread(new Runnable() {
//
//						@Override
//						public void run() {
//							
//							EmobLog.d(TAG, "t=" + "符合弹出条件");
//							mHandler.sendEmptyMessage(msgMessage.what);
//						}
//					}).start();
				}
			}else{
				EmobLog.e(TAG, "##### $$$$$$$$$ %%%%%%%%%%");
			}
			
			//获取展示的时间
			long lastTime = DspHelper.getLastSdkSpotTime(mContext);
			if(!Utils.isCurrentDay(mContext, lastTime)) {
				// 重置所有count
				DspHelper.resetRequestCount(mContext);
				DspHelper.resetTopRequestCount(mContext);
			}
			
			int channel = DspHelper.getSdkSpotTopExitChannel(mContext);
    		if (channel != CommonDefine.DSP_GLOABL) {
    			if(Utils.hasActiveNetwork(mContext)) {
					EmobLog.d(TAG, "#### showActivity =");
    				showActivity(packageName, channel, CommonDefine.TRIGGER_TYPE_APP_EXIT);
    			} else {
//    				if(isRecentApp(packageName)) {
//						EventTableDBHelper.insertData(mContext, "topexit", channel, CommonDefine.AD_POSITION_SDK_SPOT, EventItem.EVENT_TYPE_NO_NETWORK_TOPEXIT);
//						
//						HashMap<String, String> map = new HashMap<String, String>();
//			    		map.put("POS", CommonDefine.AD_POSITION_SDK_TOPEXIT+"");
//			    		map.put("CH", channel + "");
//			    		FlurryUtil.onEvent(mContext, LogEvents.EVENT_TYPE_NO_NETWORK_TOPEXIT, null);
//			    		
//    				}
    			}
    		}
		}
	}
	
	private void showActivity(String packageName, int sdkChannel, int triggerType) {
		Log.e(TAG, "#### showActivity " + packageName + " " + sdkChannel);
		if (TextUtils.isEmpty(packageName)) {
			return;
		}
		int topTag = mPref.getInt(AdsPreferences.SPOT_TOP_OPENED, 0);
		
		if (topTag > 0) {
			//String lastPkgName = mPref.getString(AdsPreferences.SPOT_TOP_PKGNAME, "");
			if (Utils.isLauncher(getApplicationContext())) {
				//mPref.setInt(AdsPreferences.SPOT_TOP_OPENED, 0);
				
				boolean ret = DspHelper.checkInterval(mContext);
		    	if (!ret) {
		    		EmobLog.e("inte", "#### TopExit tiem too short");
		    		return;
		    	} else {
		    		EmobLog.e("inte", "#### TopExit tiem ok");
		    	}
		    	EmobLog.e("inte", "#### TopExit check");
		    	
				DspHelper.updateSdkSpotRecord(getApplicationContext(), sdkChannel);
				EmobLog.d(TAG, "#### exit app + topTag=1");
//				SdkHelper.savaAppTopPrefData(mContext);
				mPref.setInt(AdsPreferences.SPOT_TOP_OPENED, 0);
				Intent intent = new Intent();
				if(sdkChannel == CommonDefine.DSP_CHANNEL_INMOBI) {

				} else if(sdkChannel == CommonDefine.DSP_CHANNEL_ADMOB ||
						sdkChannel == CommonDefine.DSP_CHANNEL_ADMOB_2) {
					intent.setClass(mContext.getApplicationContext(), LoadingActivity.class);
				} else if (sdkChannel == CommonDefine.DSP_CHANNEL_FACEBOOK){
					intent.setClass(mContext.getApplicationContext(), FacebookActivity.class);
				} else if (sdkChannel == CommonDefine.DSP_CHANNEL_CM){
					intent.setClass(mContext.getApplicationContext(), CmActivity.class);
				}
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(Value.INTENT_EXTRA_PKGNAME, mPref.getString(AdsPreferences.SPOT_TOP_PKGNAME, ""));
				intent.putExtra(Value.INTENT_EXTRA_CHANNEL, sdkChannel);
				mPref.setString(AdsPreferences.SPOT_TOP_PKGNAME, "");
				intent.putExtra(Value.AD_TRIGGER_TYPE, triggerType);
				mContext.startActivity(intent);
				return;
			}
		}else{
			Log.e(TAG, "#### topTag " + topTag);
		}
		//如果top_ten列表中包含该packageName, 将该packageName保存；
		//如果top_ten列表中不包含packageName, 判断当前值与保存的值， 如果不等,弹出TOP_TEN插屏
		if(isRecentApp(packageName)) {
			mPref.setInt(AdsPreferences.SPOT_TOP_OPENED, 1);
			mPref.setString(AdsPreferences.SPOT_TOP_PKGNAME, packageName);
			EmobLog.d(TAG, "#### Start app recentApps");
		}else{
			Log.e(TAG, "#### is not recentApp!");
		}
	}

	private boolean isRecentApp(String pckName) {
		EmobLog.e(TAG, "#### isRecentApp " + pckName);
		if(TextUtils.isEmpty(pckName)) {
			return false;
		}
		String recentApps = RecentTasksHelper.getRecentAppString(getApplicationContext());
		if(!TextUtils.isEmpty(recentApps) && recentApps.contains(pckName)) {
			return true;
		}
		return false;
	}
	
//	private Handler mHandler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			if(msg.what == CommonDefine.AD_POSITION_TOP_BANNER) {
//				
//				mNativePos = CommonDefine.AD_POSITION_TOP_BANNER;
//				if (mBannerNative != null) {
//					mBannerNative.loadTopBannerNative();
//					DspHelper.updateTopBannerRecord(mContext, CommonDefine.DSP_CHANNEL_INMOBI);
//				}
////					TopBannerNativeHelper.getInstance(mContext).loadTopBannerNative();
//			} else if (msg.what == CommonDefine.AD_POSITION_TOP_SPOT){
//				mNativePos = CommonDefine.AD_POSITION_TOP_SPOT;
//				if (mSpotNative != null) {
//					mSpotNative.loadTopSpotNative();
//					DspHelper.updateTopSpotRecord(mContext, CommonDefine.DSP_CHANNEL_INMOBI);
//				}
//			} else if (msg.what == NATIVE_SUCCEED_TOP_SPOT) {
////				if (floatView != null) {
////					floatView.showSpotView(mLandingUrl, mBitmap);
////				}
//				Intent intent = new Intent();
//				intent.setClass(mContext.getApplicationContext(), TextActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.putExtra("bitmap", mBitmap);
//				intent.putExtra("landingUrl", mLandingUrl);
//				mContext.startActivity(intent);
//			} else if (msg.what == NATIVE_SUCCEED_TOP_BANNER) {
//				if (floatView != null) {
//					floatView.showBannerView(mLandingUrl, mBitmap);
//				}
//			}
//			super.handleMessage(msg);
//		}
//	};
}
