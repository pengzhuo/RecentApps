package com.emob.luck;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.emob.lib.log.EmobLog;
import com.emob.luck.common.CommonDefine;
import com.emob.luck.common.DefaultValues;

import java.util.logging.LogRecord;

public class DspHelper {
	
	private static final String TAG = "dsp";

	private static Context m_Context = null;
	
	private static int[] SDK_SPOT_CHANNELS = {
			CommonDefine.DSP_CHANNEL_FACEBOOK,
			CommonDefine.DSP_CHANNEL_ADMOB,
		};
	private static int[] TOP_BANNER_CHANNELS = {
			CommonDefine.DSP_CHANNEL_FACEBOOK,
			CommonDefine.DSP_CHANNEL_ADMOB,
		};
	private static int[] TOP_SPOT_CHANNELS = {
			CommonDefine.DSP_CHANNEL_FACEBOOK,
			CommonDefine.DSP_CHANNEL_ADMOB,
		};
	private static int[] FOLDER_ICON_CHANNELS = {
			CommonDefine.DSP_CHANNEL_FACEBOOK,
			CommonDefine.DSP_CHANNEL_ADMOB,
		};
	
	public static int isGloablLockEnable(Context context) {
		int channel = CommonDefine.DSP_GLOABL;
		return isLockEnable(context, channel);
	}
	
	public static int isGloablNetworkEnable(Context context) {
		int channel = CommonDefine.DSP_GLOABL;
		return isNetworkEnable(context, channel);
	}
	
	public static int isNetworkEnable(Context context, int channel) {
		int ret = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.SDK_SPOT_NETWORK_ONOFF, 1);
		return ret;
	}
	
	public static int isLauncherEnable(Context context) {
		int channel = CommonDefine.DSP_GLOABL;
		int launcher = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.SDK_SPOT_LAUNCHER, 0);
		return launcher;
	}
	
	public static int isLockEnable(Context context, int channel) {
		int ret = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.SDK_SPOT_LOCK_ONOFF, 1);
		return ret;
	}
	
	public static long getLastSdkSpotTime(Context context) {
		return SdkPreferences.getInstance(context).getLong(CommonDefine.DSP_GLOABL,
				SdkPreferences.SDK_SPOT_NEXT_TIME, 0L);
	}
	
	public static void resetRequestCount(Context context) {
		SdkPreferences.getInstance(context).setInt(CommonDefine.DSP_GLOABL, SdkPreferences.SDK_SPOT_REUQEST_COUNT, 0);
		SdkPreferences.getInstance(context).setInt(CommonDefine.DSP_GLOABL, SdkPreferences.SDK_SPOT_SHOW_COUNT, 0);
		for (int i=0; i<SDK_SPOT_CHANNELS.length; i++) {
			int channel = SDK_SPOT_CHANNELS[i];
			SdkPreferences.getInstance(context).setInt(channel, SdkPreferences.SDK_SPOT_REUQEST_COUNT, 0);
			SdkPreferences.getInstance(context).setInt(channel, SdkPreferences.SDK_SPOT_SHOW_COUNT, 0);
		}
	}
	
	public static void resetTopRequestCount(Context context) {
		SdkPreferences.getInstance(context).setInt(CommonDefine.DSP_GLOABL, SdkPreferences.TOP_BANNER_REUQEST_COUNT, 0);
		SdkPreferences.getInstance(context).setInt(CommonDefine.DSP_GLOABL, SdkPreferences.TOP_SPOT_REUQEST_COUNT, 0);
		for (int i=0; i<SDK_SPOT_CHANNELS.length; i++) {
			int channel = SDK_SPOT_CHANNELS[i];
			SdkPreferences.getInstance(context).setInt(channel, SdkPreferences.TOP_BANNER_REUQEST_COUNT, 0);
			SdkPreferences.getInstance(context).setInt(channel, SdkPreferences.TOP_SPOT_REUQEST_COUNT, 0);
		}
	}
	
	public static void updateSdkSpotRecord(Context context, int channel) {
		setSdkSpotNextTime(context, CommonDefine.DSP_GLOABL);
		setSdkSpotNextTime(context, channel);
		
		setSdkSpotRequestCount(context, CommonDefine.DSP_GLOABL);
		setSdkSpotRequestCount(context, channel);
	}
	
	public static void updateSdkSpotCount(Context context, int channel) {
		setSdkSpotShowCount(context, CommonDefine.DSP_GLOABL);
		setSdkSpotShowCount(context, channel);
	}
	
	public static void updateTopBannerRecord(Context context, int channel) {
		setTopBannerNextTime(context, CommonDefine.DSP_GLOABL);
		setTopBannerNextTime(context, channel);
		
		setTopBannerRequestCount(context, CommonDefine.DSP_GLOABL);
		setTopBannerRequestCount(context, channel);
	}
	
	public static void updateTopSpotRecord(Context context, int channel) {
		setTopSpotNextTime(context, CommonDefine.DSP_GLOABL);
		setTopSpotNextTime(context, channel);
		
		setTopSpotRequestCount(context, CommonDefine.DSP_GLOABL);
		setTopSpotRequestCount(context, channel);
	}
	
	private static void setSdkSpotShowCount(Context context, int channel) {
		SdkPreferences prefs = SdkPreferences.getInstance(context);
		int count = prefs.getInt(channel, SdkPreferences.SDK_SPOT_SHOW_COUNT, 0);
		count++;
		prefs.setInt(channel, SdkPreferences.SDK_SPOT_SHOW_COUNT, count);
	}
	
	private static void setSdkSpotRequestCount(Context context, int channel) {
		SdkPreferences prefs = SdkPreferences.getInstance(context);
		int count = prefs.getInt(channel, SdkPreferences.SDK_SPOT_REUQEST_COUNT, 0);
		count++;
		prefs.setInt(channel, SdkPreferences.SDK_SPOT_REUQEST_COUNT, count);
	}
	
	private static void setSdkSpotNextTime(Context context, int channel) {
		EmobLog.d(TAG, "setSdkSpotNextTime begin, channel="+channel);
		SdkPreferences prefs = SdkPreferences.getInstance(context);
		
		int defValue = DefaultValues.GLOABL_SDK_SPOT_SHOW_INTERVAL;
		if (channel != CommonDefine.DSP_GLOABL) {
			defValue = DefaultValues.SDK_SPOT_SHOW_INTERVAL;
		}
		long interval = prefs.getInt(channel, SdkPreferences.SDK_SPOT_SHOW_INTERVAL, defValue);
		EmobLog.e(TAG, "##### setSdkSpotNextTime, default interval: "+defValue + " | " + interval);
		long curtime = System.currentTimeMillis();
		long nexttime = curtime + interval * 1000L;
		prefs.setLong(channel, SdkPreferences.SDK_SPOT_NEXT_TIME, nexttime);
		EmobLog.d(TAG, "setSdkSpotNextTime begin, curTime=" + curtime + ", nextTime=" + nexttime);
	}
	
	private static void setTopBannerRequestCount(Context context, int channel) {
		SdkPreferences prefs = SdkPreferences.getInstance(context);
		int count = prefs.getInt(channel, SdkPreferences.TOP_BANNER_REUQEST_COUNT, 0);
		count++;
		prefs.setInt(channel, SdkPreferences.TOP_BANNER_REUQEST_COUNT, count);
	}
	
	private static void setTopBannerNextTime(Context context, int channel) {
		EmobLog.d(TAG, "setTopBannerNextTime begin, channel="+channel);
		SdkPreferences prefs = SdkPreferences.getInstance(context);
		
		int defValue = DefaultValues.GLOABL_TOP_BANNER_SHOW_INTERVAL;
		if (channel != CommonDefine.DSP_GLOABL) {
			defValue = DefaultValues.TOP_BANNER_SHOW_INTERVAL;
		}
		EmobLog.d(TAG, "setTopBannerNextTime, default interval: "+defValue);
		long interval = prefs.getInt(channel, SdkPreferences.TOP_BANNER_SHOW_INTERVAL, defValue);
		long curtime = System.currentTimeMillis();
		long nexttime = curtime + interval * 1000L;
		prefs.setLong(channel, SdkPreferences.TOP_BANNER_NEXT_TIME, nexttime);
		EmobLog.d(TAG, "setTopBannerNextTime begin, curTime=" + curtime + ", nextTime=" + nexttime);
	}
	
	private static void setTopSpotRequestCount(Context context, int channel) {
		SdkPreferences prefs = SdkPreferences.getInstance(context);
		int count = prefs.getInt(channel, SdkPreferences.TOP_SPOT_REUQEST_COUNT, 0);
		count++;
		prefs.setInt(channel, SdkPreferences.TOP_SPOT_REUQEST_COUNT, count);
	}
	
	private static void setTopSpotNextTime(Context context, int channel) {
		EmobLog.d(TAG, "setTopSpotNextTime begin, channel="+channel);
		SdkPreferences prefs = SdkPreferences.getInstance(context);
		
		int defValue = DefaultValues.GLOABL_TOP_SPOT_SHOW_INTERVAL;
		if (channel != CommonDefine.DSP_GLOABL) {
			defValue = DefaultValues.TOP_SPOT_SHOW_INTERVAL;
		}
		EmobLog.d(TAG, "setTopSpotNextTime, default interval: "+defValue);
		long interval = prefs.getInt(channel, SdkPreferences.TOP_SPOT_SHOW_INTERVAL, defValue);
		long curtime = System.currentTimeMillis();
		long nexttime = curtime + interval * 1000L;
		prefs.setLong(channel, SdkPreferences.TOP_SPOT_NEXT_TIME, nexttime);
		EmobLog.d(TAG, "setTopSpotNextTime begin, curTime="+curtime+", nextTime="+nexttime);
	}
	
	/*
	 * 获取当前弹窗的渠道号（已测试）
	 * 0、返回Gloabl，不弹窗；返回某channel，弹窗，请求指定channel的广告；
	 * 1、如果全局次数已到，或者全局间隔时间未到。返回Gloabl；
	 * 2、全局检查ok，依次检查SDK_SPOT_CHANNELS里面定义的channels，定义顺序即为优先级；
	 * 3、返回条件检查通过的，即单DSP次数未到，并且DSP间隔时间已到；
	 * 4、都不符合，也返回Gloable。
	 */
	public static int getSdkSpotLockChannel(Context context) {
		int sdkChannel = CommonDefine.DSP_GLOABL;
		boolean maskFlag = AdsPreferences.getInstance(context).getBoolean(AdsPreferences.AD_MASK_FLAG, CommonDefine.AD_MASK_FLAG);
		if (maskFlag){
			Log.e(TAG, "#### CommonDefine.AD_MASK_FLAG " + maskFlag);
			return sdkChannel;
		}
		EmobLog.d(TAG, "#### getSdkSpotChannel begin");
		// 全局限制检查：包括总次数判断以及全局时间间隔判断
		boolean gloablCheck = checkSdkSpotLockChannel(context, sdkChannel);
		if (!gloablCheck) { // 全局限制检查不通过，说明总次数已到或者时间间隔太短。返回GLOABL，不弹窗。
			EmobLog.d(TAG, "#### getSdkSpotChannel result, gloabl check failed");
			return sdkChannel;
		}
		for (int i=0; i< SDK_SPOT_CHANNELS.length; i++) {
			int channel = SDK_SPOT_CHANNELS[i];
			boolean success = checkSdkSpotLockChannel(context, channel);
			if (success) {
				sdkChannel = channel;
				break;
			}
		}

		EmobLog.d(TAG, "#### getSdkSpotChannel result, channel="+sdkChannel);
		return sdkChannel;
	}
	
	public static int getSdkSpotTopExitChannel(Context context) {
		int sdkChannel = CommonDefine.DSP_GLOABL;
		boolean maskFlag = AdsPreferences.getInstance(context).getBoolean(AdsPreferences.AD_MASK_FLAG, CommonDefine.AD_MASK_FLAG);
		if (maskFlag){
			Log.e(TAG, "CommonDefine.AD_MASK_FLAG " + maskFlag);
			return sdkChannel;
		}
		EmobLog.e(TAG, "##### getSdkSpotTopExitChannel begin");
		// 全局限制检查：包括总次数判断以及全局时间间隔判断
		boolean gloablCheck = checkSdkSpotTopExitChannel(context, sdkChannel);
		if (!gloablCheck) { // 全局限制检查不通过，说明总次数已到或者时间间隔太短。返回GLOABL，不弹窗。
			EmobLog.e(TAG, "#### getSdkSpotTopExitChannel result, gloabl check failed");
			return sdkChannel;
		}
			
		for (int i=0; i< SDK_SPOT_CHANNELS.length; i++) {
			int channel = SDK_SPOT_CHANNELS[i];
			boolean success = checkSdkSpotTopExitChannel(context, channel);
			if (success) {
				sdkChannel = channel;
				break;
			}
		}
		EmobLog.e(TAG, "####  getSdkSpotTopExitChannel result, channel=" + sdkChannel);
		return sdkChannel;
	}
	
	/*
	 * 获取当前弹窗的渠道号（已测试）
	 * 0、返回Gloabl，不弹窗；返回某channel，弹窗，请求指定channel的广告；
	 * 1、如果全局次数已到，或者全局间隔时间未到。返回Gloabl；
	 * 2、全局检查ok，依次检查TOP_BANNER_CHANNELS里面定义的channels，定义顺序即为优先级；
	 * 3、返回条件检查通过的，即单DSP次数未到，并且DSP间隔时间已到；
	 * 4、都不符合，也返回Gloable。
	 */
	public static int getTopBannerChannel(Context context) {
		int sdkChannel = CommonDefine.DSP_GLOABL;
		EmobLog.d(TAG, "getTopBannerChannel begin");
		// 全局限制检查：包括总次数判断以及全局时间间隔判断
		boolean gloablCheck = checkTopBannerChannel(context, sdkChannel);
		if (!gloablCheck) { // 全局限制检查不通过，说明总次数已到或者时间间隔太短。返回GLOABL，不弹窗。
			EmobLog.d(TAG, "getTopBannerChannel, gloabl check failed");
			return sdkChannel;
		}
				
		for (int i=0; i< TOP_BANNER_CHANNELS.length; i++) {
			int channel = TOP_BANNER_CHANNELS[i];
			boolean success = checkTopBannerChannel(context, channel);
			if (success) {
				sdkChannel = channel;
				break;
			}
		}
		EmobLog.d(TAG, "getTopBannerChannel result, channel="+sdkChannel);
		return sdkChannel;
	}
	
	/*
	 * 获取当前弹窗的渠道号（已测试）
	 * 0、返回Gloabl，不弹窗；返回某channel，弹窗，请求指定channel的广告；
	 * 1、如果全局次数已到，或者全局间隔时间未到。返回Gloabl；
	 * 2、全局检查ok，依次检查TOP_SPOT_CHANNELS里面定义的channels，定义顺序即为优先级；
	 * 3、返回条件检查通过的，即单DSP次数未到，并且DSP间隔时间已到；
	 * 4、都不符合，也返回Gloable。
	 */
	public static int getTopSpotChannel(Context context) {
		int sdkChannel = CommonDefine.DSP_GLOABL;
		boolean maskFlag = AdsPreferences.getInstance(context).getBoolean(AdsPreferences.AD_MASK_FLAG, CommonDefine.AD_MASK_FLAG);
		if (maskFlag){
			Log.e(TAG, "CommonDefine.AD_MASK_FLAG " + maskFlag);
			return sdkChannel;
		}
		EmobLog.d(TAG, "#### getTopSpotChannel begin");
		
		// 全局限制检查：包括总次数判断以及全局时间间隔判断
		boolean gloablCheck = checkTopSpotChannel(context, sdkChannel);
		if (!gloablCheck) { // 全局限制检查不通过，说明总次数已到或者时间间隔太短。返回GLOABL，不弹窗。
			EmobLog.d(TAG, "#### getTopSpotChannel, gloabl check failed");
			return sdkChannel;
		}
		
		for (int i=0; i< TOP_SPOT_CHANNELS.length; i++) {
			int channel = TOP_SPOT_CHANNELS[i];
			boolean success = checkTopSpotChannel(context, channel);
			if (success) {
				sdkChannel = channel;
				break;
			}
		}
		EmobLog.d(TAG, "#### getTopSpotChannel result, channel="+sdkChannel);
		return sdkChannel;
	}
	
	public static int getFolderIconChannel(Context context) {
		int sdkChannel = CommonDefine.DSP_CHANNEL_INMOBI;
		// Folder icon暂时不加总次数和间隔限制
		
//		EmobLog.d(TAG, "getFolderIconChannel begin");
//		// TODO: 需要加上总次数判断以及全局时间间隔判断
//		// 暂时不用加
//		for (int i=0; i< FOLDER_ICON_CHANNELS.length; i++) {
//			int channel = FOLDER_ICON_CHANNELS[i];
//			boolean success = checkFolderIconChannel(context, channel);
//			if (success) {
//				sdkChannel = channel;
//				break;
//			}
//		}
		EmobLog.d(TAG, "getFolderIconChannel result, channel="+sdkChannel);
		return sdkChannel;
	}
	
	private static boolean checkSdkSpotLockChannel(Context context, int channel) {
		boolean ret = false;
		EmobLog.d(TAG, "#### checkSdkSpotLockChannel begin, channel="+channel);
		int onoff = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.SDK_SPOT_LOCK_ONOFF, 1);
		if (onoff == 0) {
			EmobLog.d(TAG, "#### checkSdkSpotLockChannel onoff="+onoff);
			return false;
		}
		
		// 次数检查
		int limit = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.SDK_SPOT_TOTAL_COUNT, DefaultValues.SDK_SPOT_TOTAL_LIMIT);
		int request = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.SDK_SPOT_REUQEST_COUNT, 0);
		int show = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.SDK_SPOT_SHOW_COUNT, 0);
		
		EmobLog.d(TAG, "#### checkSdkSpotLockChannel, count check: total="+limit+", request="+request + ", show="+show);
		if (show < limit && (request < limit *2)) {	// 次数未达到Limit
			// 下次请求时间检查（时间间隔检查）
			long curTime = System.currentTimeMillis();
			long nextTime = SdkPreferences.getInstance(context).getLong(channel, SdkPreferences.SDK_SPOT_NEXT_TIME, 0L);
			EmobLog.d(TAG, "#### checkSdkSpotLockChannel, time check: curTime="+curTime+", nexttime="+nextTime);
			
			if (nextTime < 1L || curTime > nextTime) {
				EmobLog.d(TAG, "#### checkSdkSpotLockChannel, it's my turn");
				ret = true;
			}
		}
		EmobLog.d(TAG, "#### checkSdkSpotLockChannel end, ret="+ret);
		return ret;
	}
	
	private static boolean checkSdkSpotTopExitChannel(Context context, int channel) {
		boolean ret = false;
		EmobLog.e(TAG, "#### checkSdkSpotTopExitChannel begin, channel=" + channel);
		// 开关检查
		int onoff = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.SDK_SPOT_TOP_EXIT_ONOFF, 1);
		if (onoff == 0) {
			EmobLog.e(TAG, "#### checkSdkSpotTopExitChannel onoff=" + onoff);
			return false;
		}
		// 次数检查
		int limit = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.SDK_SPOT_TOTAL_COUNT, DefaultValues.SDK_SPOT_TOTAL_LIMIT);
		int request = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.SDK_SPOT_REUQEST_COUNT, 0);
		int show = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.SDK_SPOT_SHOW_COUNT, 0);
		
		EmobLog.e(TAG, "#### checkSdkSpotTopExitChannel, count check: total=" + limit + ", request=" + request + ", show=" + show);
		if (request < limit && (request < limit *2)) {	// 次数未达到Limit
			// 下次请求时间检查（时间间隔检查）
			long curTime = System.currentTimeMillis();
			long nextTime = SdkPreferences.getInstance(context).getLong(channel, SdkPreferences.SDK_SPOT_NEXT_TIME, 0L);
			EmobLog.e(TAG, "#### checkSdkSpotTopExitChannel, time check: curTime=" + curTime + ", nexttime=" + nextTime);
			
			if (nextTime < 1L || curTime > nextTime) {
				EmobLog.e(TAG, "#### checkSdkSpotTopExitChannel, it's my turn");
				ret = true;
			}
		}
		EmobLog.e(TAG, "#### checkSdkSpotTopExitChannel end, ret=" + ret);
		return ret;
	}
	
	private static boolean checkTopBannerChannel(Context context, int channel) {
		boolean ret = false;
		EmobLog.d(TAG, "#### checkTopBannerChannel begin, channel="+channel);
		// 开关检查
		int onoff = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.TOP_BANNER_ONOFF, 1);
		if (onoff == 0) {
			EmobLog.d(TAG, "#### checkTopBannerChannel onoff="+onoff);
			return false;
		}
		// 次数检查
		int limit = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.TOP_BANNER_TOTAL_COUNT, DefaultValues.TOP_BANNER_TOTAL_LIMIT);
		int request = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.TOP_BANNER_REUQEST_COUNT, 0);
		
		EmobLog.d(TAG, "#### checkTopBannerChannel, count check: total="+limit+", request="+request);
		if (request < limit) {	// 次数未达到Limit
			// 下次请求时间检查（时间间隔检查）
			long curTime = System.currentTimeMillis();
			long nextTime = SdkPreferences.getInstance(context).getLong(channel, SdkPreferences.TOP_BANNER_NEXT_TIME, 0L);
			EmobLog.d(TAG, "#### checkTopBannerChannel, time check: curTime="+curTime+", nexttime="+nextTime);
			
			if (nextTime < 1L || curTime > nextTime) {
				EmobLog.d(TAG, "#### checkTopBannerChannel, it's my turn");
				ret = true;
			}
		}
		EmobLog.d(TAG, "#### checkTopBannerChannel end, ret="+ret);
		return ret;
	}
	
	private static boolean checkTopSpotChannel(Context context, int channel) {
		boolean ret = false;
		EmobLog.d(TAG, "#### checkTopSpotChannel begin, channel="+channel);
		// 开关检查
		int onoff = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.TOP_SPOT_ONOFF, 1);
		if (onoff == 0) {
			EmobLog.d(TAG, "#### checkTopSpotChannel onoff="+onoff);
			return false;
		}
		// 次数检查
		int limit = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.TOP_SPOT_TOTAL_COUNT, DefaultValues.TOP_SPOT_TOTAL_LIMIT);
		int request = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.TOP_SPOT_REUQEST_COUNT, 0);
		
		EmobLog.d(TAG, "#### checkTopSpotChannel, count check: total="+limit+", request="+request);
		if (request < limit) {	// 次数未达到Limit
			// 下次请求时间检查（时间间隔检查）
			long curTime = System.currentTimeMillis();
			long nextTime = SdkPreferences.getInstance(context).getLong(channel, SdkPreferences.TOP_SPOT_NEXT_TIME, 0L);
			EmobLog.d(TAG, "#### checkTopSpotChannel, time check: curTime="+curTime+", nexttime="+nextTime);
			
			if (nextTime < 1L || curTime > nextTime) {
				EmobLog.d(TAG, "#### checkTopSpotChannel, it's my turn");
				ret = true;
			}
		}
		EmobLog.d(TAG, "#### checkTopSpotChannel end, ret="+ret);
		return ret;
	}
	
	private static boolean checkFolderIconChannel(Context context, int channel) {
		boolean ret = false;
		EmobLog.d(TAG, "checkFolderIconChannel begin, channel="+channel);
		// 次数检查
		int limit = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.FOLDER_ICON_TOTAL_COUNT, DefaultValues.FOLDER_ICON_TOTAL_LIMIT);
		int request = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.FOLDER_ICON_REUQEST_COUNT, 0);
		
		EmobLog.d(TAG, "checkFolderIconChannel, count check: total="+limit+", request="+request);
		if (request < limit) {	// 次数未达到Limit
			// 下次请求时间检查（时间间隔检查）
			long curTime = System.currentTimeMillis();
			long nextTime = SdkPreferences.getInstance(context).getLong(channel, SdkPreferences.FOLDER_ICON_NEXT_TIME, 0L);
			EmobLog.d(TAG, "checkFolderIconChannel, time check: curTime="+curTime+", nexttime="+nextTime);
			
			if (nextTime < 1L || curTime > nextTime) {
				EmobLog.d(TAG, "checkFolderIconChannel, it's my turn");
				ret = true;
			}
		}
		EmobLog.d(TAG, "checkFolderIconChannel end, ret="+ret);
		return ret;
	}
	
	public static boolean checkInterval(Context context) {
		boolean ret = false;
		EmobLog.d("", "#### checkInterval begin");
		long lasttime = SdkPreferences.getInstance(context).getLong(CommonDefine.DSP_GLOABL, SdkPreferences.SDK_SPOT_LASTTIME, 0L);
		long curtime = System.currentTimeMillis();
		if ((curtime-lasttime) > DefaultValues.DEFAULT_FREEQUENCY_INTERVAL * 1000) {
			EmobLog.d("", "#### checkInterval not freequency");
			SdkPreferences.getInstance(context).setLong(CommonDefine.DSP_GLOABL, SdkPreferences.SDK_SPOT_LASTTIME, curtime);
			ret = true;
		} else {
			EmobLog.d("", "#### checkInterval tiem too short");
		}
		
		EmobLog.d("", "#### checkInterval end");
		return ret;
	}
	
//	private static int[] getSdkChannels(Context context) {
//		String sdkChannels = AdsPreferences.getInstance(context).getString(AdsPreferences.SDK_CHANNELS, "");
//		if (TextUtils.isEmpty(sdkChannels)) {
//			return SDK_SPOT_CHANNELS;
//		}
//		
//		String[] recents = sdkChannels.split(",");
//		if (recents == null || recents.length < 1) {
//			return SDK_SPOT_CHANNELS;
//		}
//		
//		try {
//			int[] channels = new int[recents.length];
//			for (int i=0; i<recents.length; i++) {
//				String str = recents[i];
//				if (TextUtils.isEmpty(str)) {
//					continue;
//				}
//				int channel = Integer.valueOf(str);
//				channels[i] = channel;
//			}
//			EmobLog.e("channels:" + channels.toString());
//			return channels;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return SDK_SPOT_CHANNELS;
//		}
//	}
}
