package com.emob.luck;

import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.emob.lib.log.EmobLog;
import com.emob.lib.stats.StatsDefines;
import com.emob.lib.stats.StatsUtil;
import com.emob.lib.util.DevicesUtils;
import com.emob.lib.util.MobiUtils;
import com.emob.lib.util.Utils;
import com.emob.luck.common.CommonDefine;
import com.emob.luck.common.DefaultValues;
import com.emob.luck.common.Value;
import com.emob.luck.db.AdTableDBHelper;
import com.emob.luck.model.EventItem;
import com.emob.luck.protocol.app.RecentTasksHelper;
import com.emob.luck.view.CmActivity;
import com.emob.luck.view.FacebookActivity;
import com.emob.luck.view.ImSpotActivity;
import com.emob.luck.view.LoadingActivity;
import com.mobi.fork.GuardHelper;
import com.mobi.fork.MainService;

/**
 * 监听手机软件中安装和卸载\锁屏\网络变化
 * 
 */
public class AdsReceiver extends BroadcastReceiver {
	
	private static final int REASON_LOCK 	= 11;
	private static final int REASON_NETON 	= 21;
    @Override
    public void onReceive(Context context, Intent arg1) {
    	EmobLog.e("####MilanoReceiver.onReceive begin");
    	String action = arg1.getAction();
    	EmobLog.e("####MilanoReceiver.onReceive action:"+action);
    	if (TextUtils.isEmpty(action)) {
    		return;
    	}
    	if (action.equalsIgnoreCase(MobiBroadcast.INTENT_ACTION_SELF_RESTART)) {
			boolean isServiceRunning = MobiUtils.isServiceRunning(context, AdsService.class.getName());
			EmobLog.e("MilanoReceiver.onReceive INTENT_ACTION_SELF_RESTART, service running:" + isServiceRunning);
        	if (!isServiceRunning) {
        		startService(context, AdsService.SERVICE_START_BOOT);
        		return;
        	}
		} else if (Intent.ACTION_USER_PRESENT.equalsIgnoreCase(action)) {
			handleLockScreen(context);
		} else if (Intent.ACTION_SCREEN_OFF.equalsIgnoreCase(action)) { // 锁屏
//        	int lockCount = AdsPreferences.getInstance(context).getInt(AdsPreferences.LOCK_COUNT, 0);
//        	lockCount++;
//        	AdsPreferences.getInstance(context).setInt(AdsPreferences.LOCK_COUNT, lockCount);
//        	HashMap<String, String> map = new HashMap<String, String>();
//    		map.put("count", lockCount+"");
//        	FlurryUtil.onEvent(context, LogEvents.EVENT_TYPE_SCREEN_OFF, null);
    		//UmengUtils.onEvent(context, LogEvents.EVENT_TYPE_SCREEN_OFF, null);
        	
//        	handleLockScreen(context);
        } else if (Intent.ACTION_SCREEN_ON.equalsIgnoreCase(action)) { // 解锁
//        	int lockCount = AdsPreferences.getInstance(context).getInt(AdsPreferences.UNLOCK_COUNT, 0);
//        	lockCount++;
//        	AdsPreferences.getInstance(context).setInt(AdsPreferences.UNLOCK_COUNT, lockCount);
//        	HashMap<String, String> map = new HashMap<String, String>();
//    		map.put("count", lockCount+"");
//        	FlurryUtil.onEvent(context, LogEvents.EVENT_TYPE_SCREEN_ON, null);
    		//UmengUtils.onEvent(context, LogEvents.EVENT_TYPE_SCREEN_ON, null);
        	EmobLog.d("MilanoReceiver.onReceive ACTION_SCREEN_ON");
        } else if (action.equalsIgnoreCase(Intent.ACTION_PACKAGE_ADDED)) { // 新应用被安装了
        	EmobLog.d("MilanoReceiver.onReceive ACTION_PACKAGE_ADDED");
		} else if (action.equalsIgnoreCase(ConnectivityManager.CONNECTIVITY_ACTION)) {
			EmobLog.d("MilanoReceiver.onReceive CONNECTIVITY_ACTION");
			networkChange(context);
		} else if (action.equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {  
//			EventTableDBHelper.insertData(context, "reboot", 0, 0, EventItem.EVENT_TYPE_REBOOT);
			boolean isServiceRunning = MobiUtils.isServiceRunning(context, AdsService.class.getName());
			EmobLog.d("MilanoReceiver.onReceive ACTION_BOOT_COMPLETED, service running:"+isServiceRunning);
        	if (!isServiceRunning) {
        		GuardHelper.startDaemon(context, "com.emob.luck.AdsService");
//        		startService(context, AdsService.SERVICE_START_BOOT);
        	}
		} else if (action.equalsIgnoreCase(AlarmMgrHelper.ACTION_ALARM_PLAY)) {
			EmobLog.e("MilanoReceiver.onReceive ACTION_ALARM_PLAY");
//			FlurryUtil.onEvent(context, StatsDefines.EVENT_TYPE_ALARM_REQUEST, null);
			StatsUtil.onEventBackground(context, StatsDefines.EVENT_TYPE_ALARM_REQUEST);
			start2Request(context);
		} else if (action.equalsIgnoreCase(AlarmMgrHelper.ACTION_ALARM_HEART)) {
			EmobLog.e("MilanoReceiver.onReceive ACTION_ALARM_HEART");
//			FlurryUtil.onEvent(context, StatsDefines.EVENT_TYPE_ALARM_HEART, null);
			StatsUtil.onEventBackground(context, StatsDefines.EVENT_TYPE_ALARM_HEART);
			start2Heart(context);
		} else if (action.equalsIgnoreCase(AlarmMgrHelper.ACTION_ALARM_RECENT_APP)) {
			EmobLog.e("MilanoReceiver.onReceive ACTION_ALARM_RECENT_APP");
//			FlurryUtil.onEvent(context, LogEvents.EVENT_TYPE_ALARM_RECENT, null);
			updateRecentApp(context);
			if(DevicesUtils.getSDKVersion() >= 11) {
				if (Utils.hasActiveNetwork(context)) { 
					AdTableDBHelper.deleteAll();
					ImageMemoryCache.getInstance(context).clearCache();
//					int folderIconOn = SdkPreferences.getInstance(context).getInt(CommonDefine.DSP_CHANNEL_INMOBI, SdkPreferences.FOLDER_ICON_ONOFF, 1);
					int topSpotOn = SdkPreferences.getInstance(context).getInt(CommonDefine.DSP_CHANNEL_INMOBI, SdkPreferences.TOP_SPOT_ONOFF, 1);
//					if(folderIconOn == 1) {
//						FolderIconNativeHelper.getInstance(context).loadFolderIconNative();
//						Map<String, String> map = new HashMap<String, String>();
//						map.put("PCK", EventItem.SHOW_TYPE_INMOBI_NATIVE_ICON);
//						map.put("ACTION", EventItem.EVENT_TYPE_REQUEST + "");
//						UmengUtils.onEvent(context, LogEvents.FOLDER_ICON_INMOBI, map);
//						FlurryUtil.onEvent(context, LogEvents.FOLDER_ICON_INMOBI, map);
//					}
					if(topSpotOn == 1) {
						TopSpotNativeHelper.getInstance(context).loadTopSpotNative();
//						Map<String, String> map = new HashMap<String, String>();
//						map.put("PCK", EventItem.SHOW_TYPE_INMOBI_NATIVE_SPOT);
//						map.put("ACTION", EventItem.EVENT_TYPE_REQUEST + "");
////						UmengUtils.onEvent(context, StatsDefines.TOP_SPOT_INMOBI, map);
////						FlurryUtil.onEvent(context, StatsDefines.TOP_SPOT_INMOBI, map);
//						StatsUtil.onEventBackground(context, StatsDefines.TOP_SPOT_INMOBI, map);
						StatsUtil.onEventOfferBackground(context,EventItem.SHOW_TYPE_INMOBI_NATIVE_SPOT,
								EventItem.EVENT_TYPE_REQUEST,
								CommonDefine.DSP_CHANNEL_INMOBI, CommonDefine.AD_POSITION_TOP_SPOT);
						
					}
				} else {
//					EventTableDBHelper.insertData(context, "ficon", CommonDefine.DSP_CHANNEL_INMOBI, 
//							CommonDefine.AD_POSITION_FOLDER_ICON, EventItem.EVENT_TYPE_NO_NETWORK_ICON);
//					HashMap<String, String> map = new HashMap<String, String>();
//		    		map.put("POS", CommonDefine.AD_POSITION_SDK_SPOT+"");
//		    		FlurryUtil.onEvent(context, LogEvents.EVENT_TYPE_NO_NETWORK_ICON, null);
				}
			}
		}
        EmobLog.e("MilanoReceiver.onReceive end");
    }
    
    private void showActivity(Context context, int sdkChannel, int reason, int triggerType)  {
//    	SdkHelper.savaSpotPrefData(context);
    	Intent intent = new Intent();
		if(sdkChannel == CommonDefine.DSP_CHANNEL_INMOBI) {
			intent.setClass(context.getApplicationContext(), ImSpotActivity.class);
		} else if(sdkChannel == CommonDefine.DSP_CHANNEL_ADMOB ||
				sdkChannel == CommonDefine.DSP_CHANNEL_ADMOB_2) {
			intent.setClass(context.getApplicationContext(), LoadingActivity.class);
		} else if (sdkChannel == CommonDefine.DSP_CHANNEL_FACEBOOK){
			intent.setClass(context.getApplicationContext(), FacebookActivity.class);
		} else if (sdkChannel == CommonDefine.DSP_CHANNEL_CM){
			intent.setClass(context.getApplicationContext(), CmActivity.class);
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (reason == REASON_LOCK) {
			intent.putExtra(Value.INTENT_EXTRA_PKGNAME, EventItem.SHOW_TYPE_LOCK_SCREEN);
		} else {
			intent.putExtra(Value.INTENT_EXTRA_PKGNAME, EventItem.SHOW_TYPE_NETWORK_ON);
		}
		intent.putExtra(Value.INTENT_EXTRA_CHANNEL, sdkChannel);
		intent.putExtra(Value.AD_TRIGGER_TYPE, triggerType);
		context.startActivity(intent);
    }
    
    private void networkChange(Context context) {
    	EmobLog.e("MilanoReceiver.networkChange begin");
    	ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE); 
        NetworkInfo ni = cm.getActiveNetworkInfo();
        
        boolean isMServiceRunning = MobiUtils.isServiceRunning(context, MainService.class.getName());
        if (!isMServiceRunning) {
        	startMService(context);
        }
        
        if( ni != null) {
//        	int lockCount = AdsPreferences.getInstance(context).getInt(AdsPreferences.NET_ON_COUNT, 0);
//        	lockCount++;
//        	AdsPreferences.getInstance(context).setInt(AdsPreferences.NET_ON_COUNT, lockCount);
        	
        	EmobLog.e("MilanoReceiver.networkChange network closed");
        	boolean isServiceRunning = MobiUtils.isServiceRunning(context, AdsService.class.getName());
        	if (!isServiceRunning) {
        		EmobLog.e("MilanoReceiver.networkChange to startService");
        		startService(context, AdsService.SERVICE_START_NET_OPEN);
        	} else {
        		EmobLog.e("MilanoReceiver.networkChange to Check if need request");
        		check2RequestForAds(context);
        		check2RequestForHeart(context);
        		handleNwOpen(context);
        	}
//        	HashMap<String, String> map = new HashMap<String, String>();
//    		map.put("count", lockCount+"");
//        	FlurryUtil.onEvent(context, LogEvents.EVENT_TYPE_NETWORK_ON, map);
    		//UmengUtils.onEvent(context, LogEvents.EVENT_TYPE_NETWORK_ON, null);
        	
    		
    		
        } else {
        	EmobLog.d("MilanoReceiver.networkChange network opened");
//        	int lockCount = AdsPreferences.getInstance(context).getInt(AdsPreferences.NET_OFF_COUNT, 0);
//        	lockCount++;
//        	AdsPreferences.getInstance(context).setInt(AdsPreferences.NET_OFF_COUNT, lockCount);
        	boolean isServiceRunning = MobiUtils.isServiceRunning(context, AdsService.class.getName());
        	if (!isServiceRunning) {
        		EmobLog.d("MilanoReceiver.networkChange to startService");
        		startService(context, AdsService.SERVICE_START_NET_COLSE);
        	}
//        	HashMap<String, String> map = new HashMap<String, String>();
//    		map.put("count", lockCount+"");
//        	FlurryUtil.onEvent(context, LogEvents.EVENT_TYPE_NETWORK_OFF, null);
    		//UmengUtils.onEvent(context, LogEvents.EVENT_TYPE_NETWORK_OFF, null);
        }
        EmobLog.d("MilanoReceiver.networkChange end");
    }
    
    private void startService(Context context, int startType) {
    	EmobLog.d("MilanoReceiver.startService begin");
    	try {
			Intent intent = new Intent(context, AdsService.class);
			intent.putExtra("start", startType);
			context.startService(intent);
		} catch (Exception e) {
		}
        EmobLog.d("MilanoReceiver.startService end");
    }
    
    private void startMService(Context context) {
    	EmobLog.d("MilanoReceiver.startService begin");
    	try {
			Intent intent = new Intent(context, MainService.class);
			context.startService(intent);
		} catch (Exception e) {
		}
        EmobLog.d("MilanoReceiver.startService end");
    }
    
    private void check2RequestForAds(Context context) {
    	EmobLog.d("MilanoReceiver.check2RequestForAds begin");
    	long nextTime = AdsPreferences.getInstance(context).getLong(AdsPreferences.NEXT_CONNECT_TIME, 0L);
    	long curTime = System.currentTimeMillis();
    	EmobLog.d("MilanoReceiver.check2RequestForAds nextTime:"+ nextTime +", curTime:" +curTime);
    	
//    	if (EmobLog.isDebug()) {
//    		String nextTimeString = TimeUtils.getTime(nextTime);
//    		String curTimeString = TimeUtils.getTime(curTime);
//    		EmobLog.d("MilanoReceiver.check2RequestForAds nextTime:"+ nextTimeString +", curTime:" +curTimeString);
//    	}
    	
    	if (nextTime == 0L || curTime > nextTime) {
    		EmobLog.d("MilanoReceiver.check2RequestForAds time to Request");
			start2Request(context);
    	} 
    	EmobLog.d("MilanoReceiver.check2RequestForAds end");
    }
    
    private void check2RequestForHeart(Context context) {
    	EmobLog.d("MilanoReceiver.check2RequestForHeart begin");
    	long nextTime = AdsPreferences.getInstance(context).getLong(AdsPreferences.NEXT_HEART_TIME, 0L);
    	long curTime = System.currentTimeMillis();
    	EmobLog.d("MilanoReceiver.check2RequestForHeart nextTime:"+ nextTime +", curTime:" +curTime);
    	
    	if (nextTime == 0L || curTime > nextTime) {
    		EmobLog.d("MilanoReceiver.check2RequestForHeart time to Request");
			start2Heart(context);
    	} 
    	EmobLog.d("MilanoReceiver.check2RequestForHeart end");
    }
    
    private void start2Request(Context context) {
    	EmobLog.d("MilanoReceiver.start2Request begin");
    	
//    	long nextRestartConnect = System.currentTimeMillis() + Value.DEFAULT_NEXT_CONNECT_TIME * 1000L;
//    	AdsPreferences.getInstance(context).setLong(AdsPreferences.NEXT_RESTART_CONNECT_TIME, nextRestartConnect);
    	
    	if (Utils.hasActiveNetwork(context)) { 
			EmobLog.d("MilanoReceiver.ACTION_ALARM_PLAY start2Request");
			
			context.startService(new Intent(context, ReqService.class));
//			AdsDataHelper.request4Ads(context, Value.REQUEST_TYPE_LUCK);
			long nextRestartConnect = System.currentTimeMillis() + DefaultValues.DEFAULT_NEXT_START_REQUEST_TIME * 1000L;
			AdsPreferences.getInstance(context).setLong(AdsPreferences.NEXT_CONNECT_TIME, nextRestartConnect);
		} else { // 到联网时间，无网络，修改触发时间为30分钟后
			EmobLog.d("MilanoReceiver.ACTION_ALARM_PLAY No Network to Request");
//			EventTableDBHelper.insertData(context, "NoNetwrok", 0, 0, EventItem.EVENT_TYPE_NO_NETWORK_REQ);
			StatsUtil.onEventBackground(context, StatsDefines.EVENT_TYPE_NO_NETWORK_REQ);
		}
    	
    	// 默认闹钟设为30分钟之后。请求正常返回之后，会重新设置
    	
		long interval = DefaultValues.DEFAULT_NEXT_START_REQUEST_TIME * 1000L;
		AlarmMgrHelper.setAlram(context, interval);
		EmobLog.d("MilanoReceiver.start2Request setAlarm interval: "+interval);
    	EmobLog.d("MilanoReceiver.start2Request end");
    	
    }
    
    private void start2Heart(Context context) {
    	EmobLog.e("MilanoReceiver.start2Heart begin");
    	
//    	long nextRestartConnect = System.currentTimeMillis() + Value.DEFAULT_NEXT_CONNECT_TIME * 1000L;
//    	AdsPreferences.getInstance(context).setLong(AdsPreferences.NEXT_RESTART_CONNECT_TIME, nextRestartConnect);
    	
    	if (Utils.hasActiveNetwork(context)) { 
			EmobLog.e("MilanoReceiver.start2Heart network ok");
			context.startService(new Intent(context, HeaService.class));
//			AdsDataHelper.request4Heart(context);
			long nextRestartConnect = System.currentTimeMillis() + DefaultValues.DEFAULT_NEXT_START_REQUEST_TIME * 1000L;
			AdsPreferences.getInstance(context).setLong(AdsPreferences.NEXT_HEART_TIME, nextRestartConnect);
		} else { // 到联网时间，无网络，修改触发时间为30分钟后
			EmobLog.e("MilanoReceiver.start2Heart No Network to Request");
//			EventTableDBHelper.insertData(context, "NoNetwrok", 0, 0, EventItem.EVENT_TYPE_NO_NETWORK_REQ);
//			FlurryUtil.onEvent(context, LogEvents.EVENT_TYPE_NO_NETWORK_REQ, null);
		}
    	
    	// 默认闹钟设为30分钟之后。请求正常返回之后，会重新设置
    	
		long interval = DefaultValues.DEFAULT_NEXT_START_REQUEST_TIME * 1000L;
		AlarmMgrHelper.setHeartAlarm(context, interval);
		EmobLog.e("MilanoReceiver.start2Heart setAlarm interval: "+interval);
    	EmobLog.e("MilanoReceiver.start2Heart end");
    	
    }
    
    private void updateRecentApp(Context context) {
    	RecentTasksHelper.updateRecentApp(context);
    	
    	int recentInteval = AdsPreferences.getInstance(context).getInt(AdsPreferences.RECENTAPP_INTEVAL, DefaultValues.DEFAULT_RECENT_APP_INTERVAL);
    	long recentNextTime = System.currentTimeMillis() + recentInteval * 1000L; 
    	AdsPreferences.getInstance(context).setLong(AdsPreferences.RECENTAPP_NEXTTIME, recentNextTime);
    	
    	
    	EmobLog.e("MilanoReceiver.updateRecentApp recentInterval: "+recentInteval*1000L);
    	AlarmMgrHelper.setAlram(context, AlarmMgrHelper.ACTION_ALARM_RECENT_APP, recentInteval*1000L);
    	
    	EmobLog.e("MilanoReceiver.updateRecentApp end");
    }
    
    private void handleLockScreen(Context context) {
    	boolean ret = DspHelper.checkInterval(context);
    	if (!ret) {
    		EmobLog.e("inte", "####handleLockScreen tiem too short");
    		return;
    	} else {
    		EmobLog.e("inte", "####handleLockScreen tiem ok");
    	}
    	int lockEnabled =  DspHelper.isGloablLockEnable(context);
		EmobLog.e("inte", "####handleLockScreen check " + lockEnabled);
    	if (lockEnabled <= 0) { // 全局锁屏开关未开启
//    		EventTableDBHelper.insertData(context, "", 0, 0, EventItem.EVENT_TYPE_LOCK_DISABLE);
//    		FlurryUtil.onEvent(context, LogEvents.EVENT_TYPE_LOCK_DISABLE, null);
    		//UmengUtils.onEvent(context, LogEvents.EVENT_TYPE_LOCK_DISABLE, null);
    		return;
    	}
    	
    	// 当前app是自己，不再触发
    	try {
	    	String topPkg = Utils.getTopPkgName(context);
	    	if (context.getApplicationInfo().packageName.equalsIgnoreCase(topPkg)) {
//	    		HashMap<String, String> map = new HashMap<String, String>();
//	    		map.put("POS", "lo");
////	    		FlurryUtil.onEvent(context, StatsDefines.EVENT_TYPE_SELF, map);
//	    		StatsUtil.onEventBackground(context, StatsDefines.EVENT_TYPE_SELF, map);
	    		
	    		StatsUtil.onEventOfferBackground(context, EventItem.SHOW_TYPE_INMOBI_NATIVE_SPOT,
						EventItem.EVENT_TYPE_REQUEST,
						CommonDefine.DSP_CHANNEL_INMOBI, CommonDefine.AD_POSITION_TOP_SPOT);
	    		
	    		return;
	    	}
    	} catch (Exception e) {
    		
    	}
    	
    	// TODO：以后要加上服务器可控
    	int launcher = DspHelper.isLauncherEnable(context);
		EmobLog.e("inte", "#### handleLockScreen check launcher " + launcher);
    	if (launcher == 1 && !Utils.isLauncher(context)) { // 
    		String topPkg = Utils.getTopPkgName(context);
//    		EventTableDBHelper.insertData(context, topPkg, 0, 0, EventItem.EVENT_TYPE_LAUNCHER_ENABLE);
//    		FlurryUtil.onEvent(context, LogEvents.EVENT_TYPE_LAUNCHER_ENABLE, null);
    		//UmengUtils.onEvent(context, LogEvents.EVENT_TYPE_LAUNCHER_ENABLE, null);
			return;
		}
    	
    	long lastTime = DspHelper.getLastSdkSpotTime(context);
		//当天
		if(!Utils.isCurrentDay(context, lastTime)) {
			DspHelper.resetRequestCount(context);
			DspHelper.resetTopRequestCount(context);
		}
		
		int channel = DspHelper.getSdkSpotLockChannel(context);
		if (channel == CommonDefine.DSP_GLOABL) {
//			EventTableDBHelper.insertData(context, "lock", 0, CommonDefine.AD_POSITION_SDK_SPOT, EventItem.EVENT_TYPE_LOCK);
//			FlurryUtil.onEvent(context, LogEvents.EVENT_TYPE_LOCK, null);
    		//UmengUtils.onEvent(context, LogEvents.EVENT_TYPE_LOCK, null);
    		
		} else {
			// 记录锁屏，无网络的情况
	    	if(!Utils.hasActiveNetwork(context)) {
//	    		EventTableDBHelper.insertData(context, "lock", channel, CommonDefine.AD_POSITION_SDK_SPOT, EventItem.EVENT_TYPE_NO_NETWORK);
	    		HashMap<String, String> map = new HashMap<String, String>();
	    		map.put("CH", channel+"");
	    		map.put("POS", CommonDefine.AD_POSITION_SDK_LOCK+"");
//	    		FlurryUtil.onEvent(context, StatsDefines.EVENT_TYPE_NO_NETWORK, map);
	    		StatsUtil.onEventBackground(context, StatsDefines.EVENT_TYPE_NO_NETWORK, map);
	    		//UmengUtils.onEvent(context, LogEvents.EVENT_TYPE_NO_NETWORK, map);
	    		
				return;
			}
	    	
	    	DspHelper.updateSdkSpotRecord(context, channel);
			showActivity(context, channel, REASON_LOCK, CommonDefine.TRIGGER_TYPE_UNLOCK);
		}
		
    }
    
    private void handleNwOpen(Context context) {
    	
    	boolean ret = DspHelper.checkInterval(context);
    	if (!ret) {
    		EmobLog.e("inte", "#### handleNwOpen tiem too short");
    		return;
    	} else {
    		EmobLog.e("inte", "#### handleNwOpen tiem ok");
    	}
    	int netEnabled =  DspHelper.isGloablNetworkEnable(context);
		EmobLog.e("inte", "#### handleNwOpen check " + netEnabled);
    	if (netEnabled <= 0) { // 全局锁屏开关未开启
//    		EventTableDBHelper.insertData(context, "", 0, 0, EventItem.EVENT_TYPE_NETON_DISABLE);
//    		FlurryUtil.onEvent(context, LogEvents.EVENT_TYPE_NETON_DISABLE, null);
    		//UmengUtils.onEvent(context, LogEvents.EVENT_TYPE_LOCK_DISABLE, null);

    		return;
    	}
    	
    	// 当前app是自己，不再触发
    	try {
	    	String topPkg = Utils.getTopPkgName(context);
	    	if (context.getApplicationInfo().packageName.equalsIgnoreCase(topPkg)) {
	    		HashMap<String, String> map = new HashMap<String, String>();
	    		map.put("POS", "ne");
//	    		FlurryUtil.onEvent(context, StatsDefines.EVENT_TYPE_SELF, map);
	    		StatsUtil.onEventBackground(context, StatsDefines.EVENT_TYPE_SELF, map);
				Log.e("", "#### handleNwOpen package " + topPkg);
	    		return;
	    	}
    	} catch (Exception e) {
    		
    	}
    	
    	// TODO：以后要加上服务器可控
    	int launcher = DspHelper.isLauncherEnable(context);
    	if (launcher == 1 && !Utils.isLauncher(context)) { // 
    		String topPkg = Utils.getTopPkgName(context);
//    		EventTableDBHelper.insertData(context, topPkg, 0, 0, EventItem.EVENT_TYPE_LAUNCHER_ENABLE_NETON);
//    		FlurryUtil.onEvent(context, LogEvents.EVENT_TYPE_LAUNCHER_ENABLE_NETON, null);
    		//UmengUtils.onEvent(context, LogEvents.EVENT_TYPE_LAUNCHER_ENABLE, null);
			Log.e("", "#### handleNwOpen launcher  " + launcher);
			return;
		}
    	
    	long lastTime = DspHelper.getLastSdkSpotTime(context);
		//当天
		if(!Utils.isCurrentDay(context, lastTime)) {
			DspHelper.resetRequestCount(context);
			DspHelper.resetTopRequestCount(context);
		}
		
		int channel = DspHelper.getSdkSpotLockChannel(context);
		if (channel == CommonDefine.DSP_GLOABL) {
//			EventTableDBHelper.insertData(context, "net", 0, CommonDefine.AD_POSITION_SDK_SPOT, EventItem.EVENT_TYPE_NETON);
//			FlurryUtil.onEvent(context, LogEvents.EVENT_TYPE_NETON, null);
    		//UmengUtils.onEvent(context, LogEvents.EVENT_TYPE_LOCK, null);
    		Log.e("", "###### handleNwOpen channel " + channel);
		} else {
	    	DspHelper.updateSdkSpotRecord(context, channel);
			showActivity(context, channel, REASON_NETON, CommonDefine.TRIGGER_TYPE_NETWORK);
		}
		
    }
}