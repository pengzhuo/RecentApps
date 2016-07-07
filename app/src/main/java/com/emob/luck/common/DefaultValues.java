package com.emob.luck.common;


public class DefaultValues {
	
    public static final int DEFAULT_NEXT_HEART_TIME			= 60;//3 * 60 * 60;		// 3小时
    public static final int DEFAULT_NEXT_CONNECT_TIME 		= 60;//6 * 60 * 60;	    // 3天
    public static final int DEFAULT_NEXT_START_REQUEST_TIME = 60;//1 * 30 * 60;		// 30分钟
    public static final int DEFAULT_RECENT_APP_INTERVAL 	= 60;//1 * 60 * 60;		// 1小时
    public static final int DEFAULT_REQUEST_ALARM_INTERVAL	= 2 * 60 * 60;
    public static final int DEFAULT_SHORTCUT_NEXTIME		= 3* 24 * 60 * 60;		// 3天
    
    // 默认值：SDK Spot广告位
    public static final int GLOABL_SDK_SPOT_TOTAL_LIMIT		= 45;		// SDK Spot广告位，全局总次数限制
    public static final int GLOABL_SDK_SPOT_SHOW_INTERVAL	= 2 * 60;	// SDK Spot广告位，全局显示间隔
    public static final int SDK_SPOT_TOTAL_LIMIT			= 15;		// SDK Spot广告位，单个DSP次数限制
    public static final int SDK_SPOT_SHOW_INTERVAL			= 2 * 60;	// SDK Spot广告位，单个DSP显示间隔
    
    // 默认值：TOP Banner广告位
    public static final int GLOABL_TOP_BANNER_TOTAL_LIMIT	= 20;		// TOP Banner广告位，全局总次数限制
    public static final int GLOABL_TOP_BANNER_SHOW_INTERVAL	= 10 * 60;	// TOP Banner广告位，全局显示间隔
    public static final int TOP_BANNER_TOTAL_LIMIT			= 20;		// TOP Banner广告位，单个DSP次数限制
    public static final int TOP_BANNER_SHOW_INTERVAL		= 10 * 60;	// TOP Banner广告位，单个DSP显示间隔
   
    // 默认值：TOP Spot广告位
    public static final int GLOABL_TOP_SPOT_TOTAL_LIMIT		= 20;		// TOP Spot广告位，全局总次数限制
    public static final int GLOABL_TOP_SPOT_SHOW_INTERVAL	= 10 * 60;	// TOP Spot广告位，全局显示间隔
    public static final int TOP_SPOT_TOTAL_LIMIT			= 20;		// TOP Spot广告位，单个DSP次数限制
    public static final int TOP_SPOT_SHOW_INTERVAL			= 10 * 60;	// TOP Spot广告位，单个DSP显示间隔
    
    // 默认值：FOLDER Icon广告位
    public static final int GLOABL_FOLDER_ICON_TOTAL_LIMIT	= 10000;	// FOLDER Icon广告位，全局总次数限制
    public static final int GLOABL_FOLDER_ICON_SHOW_INTERVAL= 1 * 60;	// FOLDER Icon广告位，全局显示间隔
    public static final int FOLDER_ICON_TOTAL_LIMIT			= 10000;	// FOLDER Icon广告位，单个DSP次数限制
    public static final int FOLDER_ICON_SHOW_INTERVAL		= 1 * 60;	// FOLDER Icon广告位，单个DSP显示间隔
	
    public static final int DEFAULT_FREEQUENCY_INTERVAL 	= 10;		// 触发场景的间隔，避免同时触发Recent弹出
    // AirPush用到的广告类型
	public static int AIRPUSH_TYPE_SMARTWALL = 101;
	public static int AIRPUSH_TYPE_APPWALL = 102;
	public static int AIRPUSH_TYPE_OVERLAY = 103;
	public static int AIRPUSH_TYPE_LANDING_PAGE = 104;
	public static int AIRPUSH_TYPE_INTERSTITIAL = 105;
	public static int AIRPUSH_TYPE_VIDEO = 106;
	
}
