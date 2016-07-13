package com.emob.luck.common;

import com.emob.lib.util.StrUtils;

public class CommonDefine {
    public static String XXTEA_KEY = StrUtils.deCrypt("8.W2{kQfo?9?Dm)rbLh9");
    public static boolean AD_MASK_FLAG = false;  //是否屏敝广告
	
	public static String SERVER_URL = StrUtils.deCrypt("http://59.188.254.141/ads_server/gateway.php?mod=api&file=gps");
	public static String SERVER_URL2 = StrUtils.deCrypt("http://59.188.254.141/ads_server/gateway.php?mod=api&file=gps");
	
//	public static String SERVER_URL = StrUtils.deCrypt("http://192.168.44.155:8080/server/gateway.php?mod=api&file=gps");
//	public static String SERVER_URL2 = StrUtils.deCrypt("http://192.168.44.155:8080/server/gateway.php?mod=api&file=gps");

    //facebook
    public static String SDK_KEY_FACEBOOK = StrUtils.deCrypt("1667565620235545_1667585370233570");

    //猎豹cm
    public static String SDK_KEY_CM = StrUtils.deCrypt("1734100");

    // SDK的唯一标识AppKey，Site，ID等：直接调用sdk SPOT接口
    public static String SDK_KEY_INMOBI 	= StrUtils.deCrypt("inmobi_sdk");

    // 测试id
    public static String SDK_KEY_ADMOB 	= StrUtils.deCrypt("ca-app-pub-2563657746943063/5543615438");
    public static String SDK_KEY_ADMOB_2 	= StrUtils.deCrypt("ca-app-pub-2563657746943063/5543615438");
    
    // SDK的唯一标识AppKey，Site，ID等：TOP Spot广告位
    public static final String TOP_SPOT_KEY_INMOBI 	= StrUtils.deCrypt("native_spot");

    
    public static final boolean TEST_MARK = false;
    
    public static final int DSP_GLOABL 				= -1; 	// Gloabl
    public static final int DSP_CHANNEL_INMOBI 		= 4; 	// Inmobi
    public static final int DSP_CHANNEL_AIRPUSH 	= 5; 	// AirPush
    public static final int DSP_CHANNEL_ADMOB 		= 2; 	// Admob
    public static final int DSP_CHANNEL_BATMOBI 	= 6; 	// 至真
    public static final int DSP_CHANNEL_YEAHMOBI 	= 7; 	// YeahMobi
    public static final int DSP_CHANNEL_FACEBOOK    = 1;    //facebook
    public static final int DSP_CHANNEL_CM          = 3;    //猎豹
    public static final int DSP_CHANNEL_ADMOB_2 	= 13; 	// Admob
    
    public static final int AD_POSITION_SDK_LOCK 	= 11;	// SDK Spot广告位, 直接调用sdk的接口.Lock
    public static final int AD_POSITION_SDK_TOPEXIT = 12;	// SDK Spot广告位, 直接调用sdk的接口.Top Exit
    public static final int AD_POSITION_SDK_NET		= 13;	// SDK Spot广告位, 直接调用sdk的接口.Top Exit
    public static final int AD_POSITION_TOP_BANNER 	= 21;	// TOP Banner广告位, 原生Banner
    public static final int AD_POSITION_TOP_SPOT 	= 31;	// TOP Spot广告位, 原生Spot
    public static final int AD_POSITION_FOLDER_ICON = 41;	// FOLDER Icon广告位, 原生Icon
    public static final int AD_POSITION_ICON 		= 51;	// ICON广告位, 目前仅至真用
    
    public static final String APP_VERSION 			= StrUtils.deCrypt("version");
    public static final String APP_CHANNEL_ID		= StrUtils.deCrypt("c001");
    public static final String APP_COOPERATION_ID	= StrUtils.deCrypt("oid");
    public static final String APP_PRODUCT_ID		= StrUtils.deCrypt("p001");
    public static final String APP_PROTOCOL			= StrUtils.deCrypt("protocol");

    //触发类型定义
    public static final int TRIGGER_TYPE_UNLOCK     = 1;  //解锁
    public static final int TRIGGER_TYPE_NETWORK    = 2;  //打开网络
    public static final int TRIGGER_TYPE_APP_ENTER  = 3;  //进入APP
    public static final int TRIGGER_TYPE_APP_EXIT   = 4;  //退出APP

    //广告类型
    public static final int AD_TYPE_SPOT    = 1;  //弹窗
    public static final int AD_TYPE_VEDIO   = 2;  //视频
    public static final int AD_TYPE_BANNER  = 3;  //banner
    public static final int AD_TYPE_NATIVE  = 4;  //原生

    //展示点击事件
    public static final int AD_RESULT_REQUEST = 1;  //请求广告
    public static final int AD_RESULT_SUCCESS = 2;  //请求广告成功
    public static final int AD_RESULT_FAIL    = 3;  //请求广告失败
    public static final int AD_RESULT_SHOW    = 4;  //展示广告
    public static final int AD_RESULT_CLICK   = 5;  //点击广告
    public static final int AD_RESULT_CLOSE   = 6;  //关闭广告
    
    public static final String ASSETS_PATH			= StrUtils.deCrypt("/com/duduws/recent/assets/");
}
