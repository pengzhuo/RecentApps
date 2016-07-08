package com.emob.lib.stats;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.emob.lib.log.EmobLog;
import com.emob.lib.util.TimeUtils;
import com.emob.luck.AdsPreferences;
import com.emob.luck.common.CommonDefine;
import com.emob.luck.db.EventTableDBHelper;

public class StatsUtil {
	
	private static final String EVENT_ID_FORMAT = "%02d_%02d";
	private static final String EVENT_ID_FORMAT_EX = "%d_%d_%d_%d";
	/**
	 * 进入activity，建议在Activity的onStart()方法中调用
	 * 
	 * @param context
	 */
	public static void onStart(Context context) {

	}

	/**
	 * 退出Activity，建议在Activity的onStop()方法中调用
	 * 
	 * @param context
	 */
	public static void onStop(Context context) {

	}
	
	public static void onCreat(Context context) {
		UmengUtils.onCreat(context);
	}
	
	public static void onResume(Context context) {
		UmengUtils.onResume(context);
	}
	
	public static void onPause(Context context) {
		UmengUtils.onPause(context);
	}
	
	public static void onEventOffer(Context context, String eventId) {
		EmobLog.e("", "eventId:"+eventId);
		UmengUtils.onEvent(context, eventId+"");
	}
	
	public static void onEventOffer(Context context, String eventId, HashMap<String, String> map) {
		UmengUtils.onEvent(context, eventId, map);
	}
	
	public static void onEventOffer(Context context, String pkg, int action, int channel, int pos) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("PCK", pkg);
		map.put("ACTION", action + "");
		map.put("DSP", channel + "");
		map.put("POS", pos + "");
		String eventId = String.format(EVENT_ID_FORMAT, pos, channel);

		UmengUtils.onEvent(context, eventId, map);
		String time = TimeUtils.getFormattedTime(System.currentTimeMillis());
		EmobLog.e("", "eventId:"+eventId);
		EmobLog.e("", "time:"+time);
		EmobLog.e("", "pkg:"+pkg);
		EmobLog.e("", "action:"+action);
		EmobLog.e("", "channel:"+channel);
		EmobLog.e("", "pos:"+pos);
		int statsOff = AdsPreferences.getInstance(context).getInt(AdsPreferences.STATS_OFF, 0);
		if (statsOff <=0 ) {
			EventTableDBHelper.insertData(context, pkg, channel, pos, action, time);
		}
	}
	
	public static void onEventOfferBackground(Context context, String pkg, int action, int channel, int pos) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("PCK", pkg);
		map.put("ACTION", action + "");
		map.put("DSP", channel + "");
		map.put("POS", pos + "");
		String eventId = String.format(EVENT_ID_FORMAT, pos, channel);
		UmengUtils.onEvent(context, eventId, map);
		String time = TimeUtils.getFormattedTime(System.currentTimeMillis());
		EmobLog.e("", "eventId:"+eventId);
		EmobLog.e("", "time:"+time);
		EmobLog.e("", "pkg:"+pkg);
		EmobLog.e("", "action:"+action);
		EmobLog.e("", "channel:"+channel);
		EmobLog.e("", "pos:"+pos);
	}

	public static void onEventBackground(Context context, String eventId) {
		EmobLog.e("", "eventId:"+eventId);
	}
	
	public static void onEventBackground(Context context, String eventId, int action) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ACTION", action + "");
		EmobLog.e("", "eventId:"+eventId);
		EmobLog.e("", "action:"+action);
	}
	
	public static void onEventBackground(Context context, String eventId, String pkg) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("PCK", pkg);
		EmobLog.e("", "eventId:"+eventId);
		EmobLog.e("", "pkg:"+pkg);
	}
	
	public static void onEventBackground(Context context, String eventId, Map<String, String> map) {
//		FlurryUtil.onEvent(context, eventId, map);
	}

	/**
	 * 打点
	 * @param context
	 * @param adType  广告主  1 facebook  2 admob  3 cm
	 * @param triggerId  触发类型  1 解锁屏  2 开网络  3 App应用进入  4 App应用退出
	 * @param posType  广告类型  1 弹窗  2 视屏  3 banner  4 原生
     * @param resultType  展示点击事件  1 请求广告  2 请求广告成功  3 请求广告失败  4 展示广告  5 点击广告  6 关闭广告
     */
	public static void onEventEx(Context context, int adType, int triggerId, int posType, int resultType){
		String eventId = String.format(EVENT_ID_FORMAT_EX, adType, triggerId, posType, resultType);
		Map<String, String> map = new HashMap<String, String>();
		map.put("cid", CommonDefine.APP_CHANNEL_ID);
		map.put("pid", CommonDefine.APP_PRODUCT_ID);
		map.put("version", CommonDefine.APP_VERSION);
		map.put("oid", CommonDefine.APP_COOPERATION_ID);
		UmengUtils.onEvent(context, eventId, map);
	}

}
