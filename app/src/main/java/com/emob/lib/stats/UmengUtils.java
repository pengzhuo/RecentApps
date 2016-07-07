package com.emob.lib.stats;

import java.util.HashMap;
import java.util.Map;

import com.emob.lib.util.TimeUtils;
import com.emob.luck.common.CommonDefine;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.text.TextUtils;

public class UmengUtils {
	public static void onCreat(Context context) {
		AnalyticsConfig.setAppkey(StatsDefines.APP_KEY_UMENG);
		String ver = CommonDefine.APP_CHANNEL_ID;//CfgUtils.getChannelID();
		AnalyticsConfig.setChannel(ver);
		MobclickAgent.updateOnlineConfig(context);
		AnalyticsConfig.enableEncrypt(true);
	}
	
	public static void onResume(Context context) {
		MobclickAgent.onResume(context);
	}
	
	public static void onPause(Context context) {
		MobclickAgent.onPause(context);
	}
	
	public static void onEvent(Context context, String eventId) {
		if (context == null || TextUtils.isEmpty(eventId)) {
			return;
		}
		try {
			MobclickAgent.onEvent(context, eventId);
		} catch (Exception e) {
			
		}
	}
	
	public static void onEvent(Context context, String eventId, Map<String, String> map) {
		if (context == null || TextUtils.isEmpty(eventId)) {
			return;
		}
		try {
			HashMap<String, String> params = getPubParams(context);
			if( params != null  ){
				if( map != null ){
					params.putAll(map);
				}
			} else if (map != null ){
				params = (HashMap<String, String>) map ;
			}
			if (params != null) {
				MobclickAgent.onEvent(context, eventId, params);
			} else {
				MobclickAgent.onEvent(context, eventId);
			}
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 公共参数:
	 * Time - 格式改为：MM-DD
	 * @return
	 */
	private static HashMap<String, String>  getPubParams(Context context){
		HashMap<String, String> pubParamMap = new HashMap<String, String>();
//		pubParamMap.put("UID", IdUtils.getUid(context));
//		pubParamMap.put("LANG", DevicesUtils.getLanguage(context));
		String time = TimeUtils.getFormattedTime(System.currentTimeMillis());
		pubParamMap.put("TIME", time);
		
//		String imei = DevicesUtils.getIMEI(context.getApplicationContext());
//		if (!TextUtils.isEmpty(imei)) {
//			pubParamMap.put("IMEI", imei);
//		}
		return pubParamMap;
	}
}
