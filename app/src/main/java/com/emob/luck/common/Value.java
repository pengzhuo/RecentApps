package com.emob.luck.common;

import com.emob.lib.util.StrUtils;

public class Value {
	
    public static final int REQUEST_TYPE_LUCK 	= 301; //定义的获取campaign task类型
    public static final int REQUEST_TYPE_HEART 	= 310; //心跳请求，默认3小时，由服务器控制
    
    public static final String INSTALL_APPS = StrUtils.deCrypt("install_apps");
    
    public static final String MODEL 		= StrUtils.deCrypt("model");
    public static final String RELEASE 		= StrUtils.deCrypt("release");
	public static final String HARDWARE 	= StrUtils.deCrypt("hardware");
	public static final String IMEI 		= StrUtils.deCrypt("imei");
	public static final String ABI 			= StrUtils.deCrypt("abi");
	public static final String DISPLAY 		= StrUtils.deCrypt("display");
	public static final String MAC 			= StrUtils.deCrypt("mac");
	public static final String PRODUCT 		= StrUtils.deCrypt("product");
	public static final String DEVICE 		= StrUtils.deCrypt("device");
	public static final String BTMAC 		= StrUtils.deCrypt("btmac");
	public static final String UA 			= StrUtils.deCrypt("ua");
	//TODO 
	public static final String API 			= StrUtils.deCrypt("api");
	public static final String IMSI 		= StrUtils.deCrypt("imsi");
	public static final String FGP 			= StrUtils.deCrypt("fgp");
	public static final String LOCAL 		= StrUtils.deCrypt("local");
	public static final String LANGUAGE 	= StrUtils.deCrypt("lang");
	public static final String TIMEZONE 	= StrUtils.deCrypt("timezone");
	public static final String ADVID 		= StrUtils.deCrypt("adv_id");
	
	//请求手机屏幕信息字段定义
	public static final String PROTOCOL_TYPE 	= StrUtils.deCrypt("protocol_type");
	public static final String REQUEST_TYPE 	= StrUtils.deCrypt("request_type");
	public static final String App_VERSION 		= StrUtils.deCrypt("app_version");
	public static final String ACCOUNTS 		= StrUtils.deCrypt("accounts");
	public static final String GOOGLE_ACCOUNT 	= StrUtils.deCrypt("google");
	
	public static final String APP_CHANNEL_ID 	= StrUtils.deCrypt("cid");		// 合作方渠道ID：cid
    public static final String APP_OPERATION_ID = StrUtils.deCrypt("oid"); 		// 合作方ID：oid
    public static final String APP_PRODUCT_ID 	= StrUtils.deCrypt("pid");		// 合作方产品ID：pid
    public static final String WO_MODEL		 	= StrUtils.deCrypt("wo_model");		// 合作方产品ID：pid
	//请求手机屏幕信息字段定义
	public static final String SCREEN 			= StrUtils.deCrypt("screen");
	public static final String HEIGHTPIXELS 	= StrUtils.deCrypt("heightPixels");
	public static final String WIDTHPIXELS 		= StrUtils.deCrypt("widthPixels");
	public static final String DENISITY 		= StrUtils.deCrypt("denisity");
	public static final String SCALEDDENSITY 	= StrUtils.deCrypt("scaledDensity");
	public static final String DENSITYDPI 		= StrUtils.deCrypt("densityDpi");
	public static final String XDIP 			= StrUtils.deCrypt("xdip"); 
	public static final String YDIP 			= StrUtils.deCrypt("ydip");
	
	//请求ids信息字段定义
	public static final String IDS 				= StrUtils.deCrypt("ids");
	public static final String UID 				= StrUtils.deCrypt("uid");
	public static final String GID 				= StrUtils.deCrypt("gid");
	public static final String SID 				= StrUtils.deCrypt("sid");
	public static final String AID 				= StrUtils.deCrypt("aid");
	public static final String IID 				= StrUtils.deCrypt("iid");
	public static final String DID 				= StrUtils.deCrypt("did");
	
	//请求手机状态信息字段定义
	public static final String DEVICESTATUS 	= StrUtils.deCrypt("devicestatus");
	public static final String ORIENTATION 		= StrUtils.deCrypt("orientation");
	public static final String ISLOCK 			= StrUtils.deCrypt("islock");
	public static final String SU 				= StrUtils.deCrypt("su");
	
	//请求手机状态信息字段定义
	public static final String STATS 			= StrUtils.deCrypt("stats");
	public static final String NET_ON		 	= StrUtils.deCrypt("neton");
	public static final String NET_OFF	 		= StrUtils.deCrypt("netoff");
	public static final String LOCK 			= StrUtils.deCrypt("lock");
	public static final String UNLOCK 			= StrUtils.deCrypt("unlock");
		
	//请求手机网络信息字段定义
	public static final String NET 				= StrUtils.deCrypt("net");
	public static final String NETWORK 			= StrUtils.deCrypt("network");
	public static final String APN 				= StrUtils.deCrypt("apn");
	public static final String SUBTYPE 			= StrUtils.deCrypt("subtype");
	public static final String IPINFO 			= StrUtils.deCrypt("ipinfo");
	
	//未展示过的广告
	public static final String RESULTS 			= StrUtils.deCrypt("results");
	public static final String ITEMS 			= StrUtils.deCrypt("items");
    public static final String TYPE 			= StrUtils.deCrypt("type");
    public static final String PACKAGE_NAME 	= StrUtils.deCrypt("pkgname");					        // 包名
	
    //上传的campaign Event事件字段
    public static final String ACTIONS 			= StrUtils.deCrypt("action");
    public static final String PACKAGENAME 		= StrUtils.deCrypt("package_name");
    public static final String ACTION_TIME 		= StrUtils.deCrypt("time");                                //行为时间
    public static final String POS				= StrUtils.deCrypt("pos");
    //请求inmobi广告字段
    public static final String TITLENAME		= StrUtils.deCrypt("title_name");
    public static final String ICONURL 			= StrUtils.deCrypt("icon_url");
    public static final String LANDINGURL 		= StrUtils.deCrypt("landing_url");
    
    public static final String BACKUP_URL_SWITCH 	= StrUtils.deCrypt("baswitch");        //TWITTER
    public static final String BACKUP_URLS 			= StrUtils.deCrypt("barray");        //TWITTER
    
    
    public static final String SC 							= StrUtils.deCrypt("sc");
    public static final String EXTEND 						= StrUtils.deCrypt("extend");
    
	public static final String SDK 							= StrUtils.deCrypt("sdk");
	public static final String AD_TYPE 						= StrUtils.deCrypt("adtype");
	public static final String UI_TYPE 						= StrUtils.deCrypt("uitype");
	public static final String RANDOM_INT 					= StrUtils.deCrypt("random_int");
	public static final String RANDOM_STRING 				= StrUtils.deCrypt("random_string");
	public static final String RANDOM_LIST 					= StrUtils.deCrypt("random_list");
	
	public static final String NATIVE_SDK 					= StrUtils.deCrypt("nasdk");
	public static final String NATIVE_CHANNEL 				= StrUtils.deCrypt("na_channel");
	public static final String NATIVE_AD_TYPE 				= StrUtils.deCrypt("na_adtype");
	public static final String NATIVE_UI_TYPE 				= StrUtils.deCrypt("na_uitype");
	public static final String NATIVE_RANDOM_INT 			= StrUtils.deCrypt("na_random_int");
	public static final String NATIVE_RANDOM_STRING 		= StrUtils.deCrypt("na_random_string");
	public static final String NATIVE_SWITCH 				= StrUtils.deCrypt("naswitch");
	public static final String NATIVE_UPLOAD_RANDOM_STRING 	= StrUtils.deCrypt("u_na_random_string");
	
	
	public static final String TOPAPP 						= StrUtils.deCrypt("topapp");
	public static final String BBLIST 						= StrUtils.deCrypt("bblist");
	public static final String ACTION 						= StrUtils.deCrypt("action");
	
	public static final String SERVICE_RESTART 				= StrUtils.deCrypt("servicerestart");
	public static final String BACKUP_URL_FLAG 				= StrUtils.deCrypt("baflag");
	public static final String LOCAL_ADDRESS 				= StrUtils.deCrypt("laddr");
	
	public static final String SERVER_NEXT_INTERVAL 		= StrUtils.deCrypt("nextinterval");
	public static final String HEART_NEXT_INTERVAL			= StrUtils.deCrypt("heartinterval");
	public static final String RESPONSE_CODE_SUCCESS 		= StrUtils.deCrypt("200, 501, 502, 503, 504, 505, 506");
	
	
	public static final String CHNN							= StrUtils.deCrypt("chnn");
	
	// ============================ GLOABL =================================================
	
	public static final String GLOABL 						= StrUtils.deCrypt("gloabl");
	
	public static final String SDK_SPOT_NETWORK_ONOFF		= StrUtils.deCrypt("net_onoff");
	public static final String SDK_SPOT_LAUNCHER_ONOFF		= StrUtils.deCrypt("sdk_launcher");
	public static final String SDK_SPOT_LOCK_ONOFF 			= StrUtils.deCrypt("lock_onoff");
	public static final String SDK_SPOT_TOP_EXIT_ONOFF 		= StrUtils.deCrypt("texit_onoff");
	public static final String SDK_SPOT_TOTAL_LIMIT 		= StrUtils.deCrypt("sdk_tcount");
	public static final String SDK_SPOT_SHOW_INTERVAL 		= StrUtils.deCrypt("sdk_sint");
	
	public static final String TOP_BANNER_ONOFF 			= StrUtils.deCrypt("tban_onoff");
	public static final String TOP_BANNER_TOTAL_LIMIT 		= StrUtils.deCrypt("tban_tcount");
	public static final String TOP_BANNER_SHOW_INTERVAL 	= StrUtils.deCrypt("tban_sint");
	
	public static final String TOP_SPOT_ONOFF 				= StrUtils.deCrypt("tint_onoff");
	public static final String TOP_SPOT_TOTAL_LIMIT 		= StrUtils.deCrypt("tint_tcount");
	public static final String TOP_SPOT_SHOW_INTERVAL 		= StrUtils.deCrypt("tint_sint");
	
	public static final String FOLDER_ICON_ONOFF 			= StrUtils.deCrypt("ficon_onoff");
	public static final String FOLDER_ICON_TOTAL_LIMIT 		= StrUtils.deCrypt("ficon_tcount");
	public static final String FOLDER_ICON_SHOW_INTERVAL 	= StrUtils.deCrypt("ficon_sint");
	
	public static final String ICON_ONOFF 					= StrUtils.deCrypt("icon_onoff");
	public static final String ICON_TOTAL_LIMIT 			= StrUtils.deCrypt("icon_count");
	public static final String ICON_SHOW_INTERVAL 			= StrUtils.deCrypt("icon_interval");
	
	// ============================ DSP =================================================
	
	public static final String DSP 							= StrUtils.deCrypt("dsp");
	
	public static final String CHANNEL 						= StrUtils.deCrypt("channel");
	public static final String ONOFF 						= StrUtils.deCrypt("onoff");
	public static final String ONOFF2 						= StrUtils.deCrypt("onoff2");
	public static final String AD_POSITION 					= StrUtils.deCrypt("pos");
	public static final String DSP_ID_INT 					= StrUtils.deCrypt("random_int");
	public static final String DSP_KEY_STRING 				= StrUtils.deCrypt("random_string");
	public static final String DSP_TOTAL_LIMIT 				= StrUtils.deCrypt("tcount");
	public static final String DSP_SHOW_INTERVAL 			= StrUtils.deCrypt("sint");
	
	public static final String INTENT_EXTRA_PKGNAME			= StrUtils.deCrypt("pkgname");
	public static final String INTENT_EXTRA_CHANNEL			= StrUtils.deCrypt("channel");
	
	// ============================ SNOT =================================================
	public static final String SNOT 						= StrUtils.deCrypt("snot");
	
	public static final String SNOT_BBLIST 					= StrUtils.deCrypt("bbl");
	public static final String SNOT_DSP 					= StrUtils.deCrypt("d");
	public static final String SNOT_GLOABL 					= StrUtils.deCrypt("glo");
	public static final String SNOT_TOPAPP 					= StrUtils.deCrypt("ta");
	
	public static final String STATS_OFF 					= StrUtils.deCrypt("offla");
	
	public static int AIRPUSH_TYPE_SMARTWALL 				= 101;
	public static int AIRPUSH_TYPE_APPWALL 					= 102;
	public static int AIRPUSH_TYPE_OVERLAY 					= 103;
	public static int AIRPUSH_TYPE_LANDING_PAGE 			= 104;
	public static int AIRPUSH_TYPE_INTERSTITIAL 			= 105;
	public static int AIRPUSH_TYPE_VIDEO 					= 106;
	
	
	public static final int NATIVE_FOLDER_ICON 	= 1;
	public static final int NATIVE_TOP_BANNER 	= 2;
	public static final int NATIVE_TOP_SPOT 	= 3;

	//触发类型
	public static final String AD_TRIGGER_TYPE = StrUtils.deCrypt("ad_trigger_type");


	//服务器错误码定义
	public static final int SUCCESS = 1000;  //成功
	public static final int DEVICE_BE_MASK =  1002;  //设备在被屏敝列表中
	public static final int CHANNEL_BE_MASK = 1003;  //渠道屏敝
	
}
