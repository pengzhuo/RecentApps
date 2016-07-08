package com.emob.lib.stats;

import com.emob.lib.util.StrUtils;

public class StatsDefines {
	// Umeng的key
	public static final String APP_KEY_UMENG = StrUtils.deCrypt("577f497967e58eb2390012ed");
		
	// Flurry的key
	public static final String APP_KEY_FLURRY = StrUtils.deCrypt("42TCTCKX8NYGPP38825M");
	
	public final static String SDK_LOCK_INMOBI			= "11_01";	// SDK直接弹广告-锁屏场景-Inmobi
	public final static String SDK_LOCK_AIRPUSH			= "11_02";	// SDK直接弹广告-锁屏场景-Airpush
	public final static String SDK_LOCK_ADMOB			= "11_03";	// SDK直接弹广告-锁屏场景-Admob
	public final static String SDK_LOCK_ADMOB_2			= "11_13";	// SDK直接弹广告-锁屏场景-Admob2
	public final static String SDK_TOPEXIT_INMOBI		= "12_01";	// SDK直接弹广告-TopApp退出-Inmobi
	public final static String SDK_TOPEXIT_AIRPUSH		= "12_02";	// SDK直接弹广告-TopApp退出-Airpush
	public final static String SDK_TOPEXIT_ADMOB		= "12_03";	// SDK直接弹广告-TopApp退出-Admob
	public final static String SDK_TOPEXIT_ADMOB_2		= "12_13";	// SDK直接弹广告-TopApp退出-Admob2
	public final static String SDK_NETON_INMOBI			= "13_01";	// SDK直接弹广告-网络打开-Inmobi
	public final static String SDK_NETON_AIRPUSH		= "13_02";	// SDK直接弹广告-网络打开-Airpush
	public final static String SDK_NETON_ADMOB			= "13_03";	// SDK直接弹广告-网络打开-Admob
	public final static String SDK_NETON_ADMOB_2		= "13_13";	// SDK直接弹广告-网络打开-Admob2
	
	public final static String TOP_BANNER_INMOBI		= "21_01";	// 原生广告-进入应用Banner
	public final static String TOP_SPOT_INMOBI			= "31_01";	// 原生广告-进入应用Spot插屏
	public final static String FOLDER_ICON_INMOBI		= "41_01";	// 原生逛到-文件夹Icon
	
	
	public final static String FOLDER_ICON_APP			= "30";		// 点击了Recent文件夹中的应用


	public static String EVENT_TYPE_SHORTCUT_CLICK = "81"; 			// 点击了快捷方式
	public static String EVENT_TYPE_SHORTCUT_SERVICE = "82"; 		// 通过快捷方式，启动了Service
	public static String EVENT_TYPE_SHORTCUT_GP = "83"; 			// 通过快捷方式，打开了GooglePlay
	public static String EVENT_TYPE_SHORTCUT_BROWSER = "84"; 		// 通过快捷方式，因为没有GooglePlay，用浏览器GooglePlay首页
	
	
	public static final String EVENT_TYPE_GET_APPID_FAILED 	= "20";	// 获取appId失败
    public static final String EVENT_TYPE_LOCK_DISABLE 		= "40";	// 全局锁屏开关关闭
    public static final String EVENT_TYPE_LOCK		 		= "41";	// 锁屏发生，但不满足弹窗时机
    public static final String EVENT_TYPE_NETON		 		= "42";	// 网络打开场景发生，但不满足弹窗时机
    public static final String EVENT_TYPE_LAUNCHER_ENABLE	= "50";	// 锁屏场景。Launcher开关开启，限制为桌面才能弹窗
    public static final String EVENT_TYPE_LAUNCHER_ENABLE_NETON	= "51";	// 网络打开触发场景。Launcher开关开启，限制为桌面才能弹窗,
    public static final String EVENT_TYPE_NETON_DISABLE		= "60";	// Launcher开关开启，限制为桌面才能弹窗
    public static final String EVENT_TYPE_NO_NETWORK 		= "70";	// 到锁屏弹窗时间，但无网络
    public static final String EVENT_TYPE_NO_NETWORK_REQ	= "71";	// 到请求服务器时间，但无网络
    public static final String EVENT_TYPE_NO_NETWORK_ICON	= "72";	// 到更新文件夹icon广告时间，但无网络
    public static final String EVENT_TYPE_NO_NETWORK_TOPEXIT= "73";	// 到TopExit弹窗时间，但无网络
    public static final String EVENT_TYPE_REBOOT			= "91";	// 手机重启时间
    
    public static final String EVENT_TYPE_SERVICE_RESTART	= "99";
    
    public static final String EVENT_TYPE_SCREEN_ON		= "scr_on";	// 解锁事件-ScreenOn
    public static final String EVENT_TYPE_SCREEN_OFF	= "scr_off";// 锁屏事件-ScreenOff
    
    public static final String EVENT_TYPE_NETWORK_ON	= "ne_on";	// 网络打开事件
    public static final String EVENT_TYPE_NETWORK_OFF	= "ne_off";	// 网络关闭事件
    
    public static final String EVENT_TYPE_ALARM_REQUEST	= "al_req";	// 记录通过闹钟定时触发联网签到请求事件，后续是否成功签到，取决于是否有网络。
    public static final String EVENT_TYPE_ALARM_HEART	= "al_hrt";	// 记录通过闹钟定时触发联网签到请求事件，后续是否成功签到，取决于是否有网络。
    public static final String EVENT_TYPE_ALARM_RECENT	= "al_upd";	// 记录通过闹钟定时更新Recents应用事件
    
    public static final String EVENT_TYPE_SELF			= "self";

    public static final String EVENT_TYPE_REQ = "req";     			// 请求广告
    public static final String EVENT_TYPE_CLK = "clk";     			// 广告点击
    public static final String EVENT_TYPE_IMP = "imp";     			// 广告展示
}
