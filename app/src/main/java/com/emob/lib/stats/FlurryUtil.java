package com.emob.lib.stats;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.emob.lib.util.TimeUtils;
import com.flurry.android.FlurryAgent;

public class FlurryUtil {
	/**
	 * 进入activity，建议在Activity的onStart()方法中调用
	 * 
	 * @param context
	 */
	public static void onStart(Context context, String key) {
		FlurryAgent.init(context, key);
		FlurryAgent.setReportLocation(false);
		FlurryAgent.setLogEnabled(false);
		
		FlurryAgent.onStartSession(context, key);
	}

	/**
	 * 退出Activity，建议在Activity的onStop()方法中调用
	 * 
	 * @param context
	 */
	public static void onStop(Context context) {
		FlurryAgent.onEndSession(context);
	}

	/**
	 * 记录日志
	 * 仅EventId
	 * 
	 * @param context
	 * @param eventId
	 */
	public static void onEvent(Context context, String eventId) {
		if (context == null || TextUtils.isEmpty(eventId)) {
			return;
		}
		try {
			HashMap<String, String> params = getPubParams(context);
	
			if (params != null) {
				FlurryAgent.logEvent(eventId, params);
			} else {
				FlurryAgent.logEvent(eventId);
			}
		} catch (Exception e) {
			
		}
	}

	/**
	 * 记录日志
	 * EventID + 参数
	 * 
	 * @param context
	 * @param eventId
	 *            行为ID
	 * @param map
	 *            需要记录的参数
	 */
	public static void onEvent(Context context, String eventId, Map<String, String> map) {
		if (TextUtils.isEmpty(eventId) || context == null) {
			return;
		}
		try {
			HashMap<String, String> params = getPubParams(context);
			if (params != null) {
				if (map != null) {
					params.putAll(map);
				}
			} else if (map != null) {
				params = (HashMap<String, String>) map;
			}
	
			if (params != null) {
				FlurryAgent.logEvent(eventId, params);
			} else {
				FlurryAgent.logEvent(eventId);
			}
		} catch (Exception e) {
			
		}
	}

	/**
	 * 公共参数:
	 * Time - 格式改为：MM-DD
	 * 
	 * @return
	 */
	private static HashMap<String, String> getPubParams(Context context) {
		HashMap<String, String> pubParamMap = new HashMap<String, String>();
		// pubParamMap.put("UID", IdUtils.getUid(context));
		// pubParamMap.put("LANG", DevicesUtils.getLanguage(context));
		String time = TimeUtils.getFormattedTime(System.currentTimeMillis());
		pubParamMap.put("TIME", time);

//		String imei = DevicesUtils.getIMEI(context.getApplicationContext());
//		if (!TextUtils.isEmpty(imei)) {
//			pubParamMap.put("IMEI", imei);
//		}
		return pubParamMap;
	}
}
