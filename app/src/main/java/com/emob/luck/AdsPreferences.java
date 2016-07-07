package com.emob.luck;

import com.emob.lib.util.StrUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

// 保存相关的配置信息
public class AdsPreferences {
    
//	public static final String SDK_CHANNEL 				= StrUtils.deCrypt("sdk_channel");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String SDK_AD_TYPE 				= StrUtils.deCrypt("sdk_ad_type");	// 广告类型：1-插屏，2-banner
//	public static final String SDK_UI_TYPE 				= StrUtils.deCrypt("sdk_ui_type");	// UI类型：1-文件夹，2-提示框，仅限TOP APP的弹出界面
//	public static final String SDK_RANDOM_INT 			= StrUtils.deCrypt("random_int");	// SDK渠道：1-inmobi，2-airpush，3-admob
//	public static final String SDK_RANDOM_STRING 		= StrUtils.deCrypt("random_string");// 广告类型：1-插屏，2-banner
	
	public static final String NATIVE_SDK_SWITCH 		= StrUtils.deCrypt("na_switch");		// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String NATIVE_SDK_CHANNEL 		= StrUtils.deCrypt("na_sdk_channel");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String NATIVE_SDK_AD_TYPE 		= StrUtils.deCrypt("na_sdk_ad_type");	// 广告类型：1-插屏，2-banner
	public static final String NATIVE_SDK_UI_TYPE 		= StrUtils.deCrypt("na_sdk_ui_type");	// UI类型：1-文件夹，2-提示框，仅限TOP APP的弹出界面
	public static final String NATIVE_SDK_RANDOM_INT 	= StrUtils.deCrypt("na_random_int");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String NATIVE_SDK_RANDOM_STRING = StrUtils.deCrypt("na_random_string");	// 广告类型：1-插屏，2-banner
	
	
	public static final String UPLOAS_RANDOM_STRING 	= StrUtils.deCrypt("upload_random_string");// 广告类型：1-插屏，2-banner
	public static final String UPLOAS_NATIVE_RANDOM_STRING = StrUtils.deCrypt("na_upload_random_string");// 广告类型：1-插屏，2-banner
	
//	public static final String TOTAL_LIMIT				= StrUtils.deCrypt("total_show_limit");  // topApp次数+锁屏次数  = 总次数
//	public static final String SHOW_LIMIT 				= StrUtils.deCrypt("show_limit");	// 广告弹出次数。仅限解锁之后的弹出限制。Top10退出，不受限制。
//	public static final String TOPAPP_SHOW_LIMIT        = StrUtils.deCrypt("topapp_show_limit"); // topApp弹出广告的限制次数
	public static final String NEXT_CONNECT_TIME 		= StrUtils.deCrypt("next_connect_time");	// 服务器下次联网时间
	public static final String NEXT_HEART_TIME 			= StrUtils.deCrypt("next_heart_time");	// 服务器下次心跳时间
//	public static final String SHOW_INTERVAL 			= StrUtils.deCrypt("show_inteval");	// 广告弹出间隔。仅限解锁之后的弹出限制。Top10退出，不受限制。
	public static final String TOPAPP_ACTION 			= StrUtils.deCrypt("topapp_action");	// 展示时机，0-退出时展示，1-启动时展示
//	public static final String TOPAPP_SHOW_INTERVAL 	= StrUtils.deCrypt("topapp_show_inteval");	// 广告弹出间隔。仅限TOPAPP的弹出限制。。
	public static final String RECENTAPP_INTEVAL 		= StrUtils.deCrypt("recent_app_interval");
	public static final String RECENTAPP_NEXTTIME		= StrUtils.deCrypt("recent_nexttime");
	public static final String SHORTCUT_NEXTTIME		= StrUtils.deCrypt("sc_nexttime");
	
	public static final String PREFS_KEY_EXTEND 		= StrUtils.deCrypt("extend");
	
//	public static final String SPOT_LOCK_ENABLED 		= StrUtils.deCrypt("lock_enabled");	//spot已经展示的次数
	
//	public static final String SPOT_NEXT_SHOW_TIME    	= StrUtils.deCrypt("next_show_time");	//保存下次展示时间（插屏）
//	public static final String SPOT_IMPRESSIONS_COUNT 	= StrUtils.deCrypt("impression_count");	//spot已经展示的次数
//	public static final String SPOT_IMPRESSIONS_TIME  	= StrUtils.deCrypt("impression_time");	//spot已展示的时间
	public static final String SPOT_TOP_PKGNAME       	= StrUtils.deCrypt("top_pkgname");	//保存处于栈顶并且包含于top_ten中的应用
	public static final String SPOT_TOP_OPENED			= StrUtils.deCrypt("top_opened");  //是否有TOP APP启动过
////	public static final String SPOT_TOP_IMPRESSIONS_TIME= StrUtils.deCrypt("top_impressions_time");  //top_spot已展示的时间
////	public static final String SPOT_TOP_NEXT_SHOW_TIME  = StrUtils.deCrypt("top_next_show_time");	//保存下次展示时间（插屏）
//	public static final String TOPAPP_IMPRESSIONS_COUNT = StrUtils.deCrypt("top_impression_count"); //topApp已经展示的次数
	
	public static final String TOP_NO_NETWORK_TIME		= StrUtils.deCrypt("top_next_record_nonet_time");	//保存满足TopApp弹窗时间，但无网络的时间间隔
	public static final String LOCK_NO_NETWORK_TIME		= StrUtils.deCrypt("lock_next_record_nonet_time");	//保存满足锁屏弹窗时间，但无网络的时间间隔
	
	public static final String SERVICE_RESTART  		= StrUtils.deCrypt("service_restart");	//保存Service是否重启
//	public static final String NEXT_RESTART_CONNECT_TIME= StrUtils.deCrypt("next_restart_connect_time");	// Service重启后的下次联网时间
	
	public static final String BB_LIST_STRING			= StrUtils.deCrypt("bb_list_string");		// 服务器下发的blockList，这里面的应用
	public static final String WHITE_LIST_STRING 		= StrUtils.deCrypt("white_list_string");  //白名单
	
	public static final String BACKUP_URL_SWITCH		= StrUtils.deCrypt("backup_url_switch"); 	// 服务器下发的备用域名开关
	public static final String BACKUP_URL				= StrUtils.deCrypt("backup_urls");			// 服务器下发的备用域名
	
	public static final String CONNECT_FAILED			= StrUtils.deCrypt("connect_failed");		// 联网失败过
	public static final String LOCK_COUNT				= StrUtils.deCrypt("lock_count");
	public static final String UNLOCK_COUNT				= StrUtils.deCrypt("unlock_count");
	public static final String NET_ON_COUNT				= StrUtils.deCrypt("neton_count");
	public static final String NET_OFF_COUNT			= StrUtils.deCrypt("netoff_count");
	
	public static final String SNOT_BBLIST				= StrUtils.deCrypt("snot_bbl");
	public static final String SNOT_DSP					= StrUtils.deCrypt("snot_d");
	public static final String SNOT_GLOABL				= StrUtils.deCrypt("snot_glo");
	public static final String SNOT_TOPAPP				= StrUtils.deCrypt("snot_ta");
	
	public static final String STATS_OFF				= StrUtils.deCrypt("stats_off");
	
	public static final String SDK_CHANNELS				= StrUtils.deCrypt("s_channels");	
	
	public static final String LOADING_PKGNAME				= StrUtils.deCrypt("l_pkg");	
	public static final String LOADING_CHANNEL				= StrUtils.deCrypt("l_channel");

	public static final String AD_MASK_FLAG 			= StrUtils.deCrypt("AD_MASK_FLAG");
	
	private SharedPreferences mPref;
	private Editor mEditor;
	private static AdsPreferences mPreferences;

	public static synchronized AdsPreferences getInstance(Context context) {
		if (mPreferences == null) {
			mPreferences = new AdsPreferences(context);
		}
		return mPreferences;
	}

	public AdsPreferences(Context context) {
		mPref = context.getSharedPreferences("emob_config", 0);
		mEditor = mPref.edit();
	}

	public void setString(String key,String value) {
		if (mEditor!=null) {
			mEditor.putString(key, value).commit();
		}
	}

	public String getString(String key,String defValue) {
		return (null!=mPref)?mPref.getString(key, defValue):"";
	}
	
    public <T> void setLong(T key, long value) {
        if (mEditor == null) {
            return;
        }
        mEditor.putLong(key.toString(), value);
        mEditor.commit();
    }
	
	public <T> long getLong(T key,long defValue)
	{
		if (mPref==null) {
			return 0;
		}
		return mPref.getLong((String) key, defValue);
	}
	
	public <T> void setInt(T key, int value) {
		  if (mEditor == null) {
	            return;
	        }
	        mEditor.putInt(key.toString(), value);
	        mEditor.commit();
	}
	
	public <T> int getInt(T key, int defValue)
	{
		if (mPref==null) {
			return 0;
		}
		return mPref.getInt((String) key, defValue);
	}

	public <T> void setBoolean(T key, boolean value) {
		if (mEditor == null) {
			return;
		}
		mEditor.putBoolean(key.toString(), value);
		mEditor.commit();
	}

	public <T> boolean getBoolean(T key, boolean defValue)
	{
		if (mPref==null) {
			return false;
		}
		return mPref.getBoolean((String) key, defValue);
	}
}
