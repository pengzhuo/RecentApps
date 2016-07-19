package com.emob.luck;

import android.content.Context;
import android.text.TextUtils;

import com.emob.lib.log.EmobLog;
import com.emob.lib.util.SiteUtil;
import com.emob.luck.common.CommonDefine;

public class SdkHelper {
	private static final String TAG = "dsp";
	
	public static String getSdkSpotKeyString(Context context, int channel) {
		String randomString = SdkPreferences.getInstance(context).getString(channel, SdkPreferences.SDK_SPOT_DSP_KEY_STRING, "");
		String ret = decryptRandomString(randomString);
		EmobLog.e(TAG, "getSdkSpotKeyString, channel="+channel +", KeyInPrefs="+ret);
		// 如果ret为空，说明randomString为空或者randomList里不包含randomString
		//if (TextUtils.isEmpty(ret)){
			if (channel == CommonDefine.DSP_CHANNEL_ADMOB) {
				ret = CommonDefine.SDK_KEY_ADMOB;
			} else if (channel == CommonDefine.DSP_CHANNEL_INMOBI) {
				ret = CommonDefine.SDK_KEY_INMOBI;
			} else if (channel == CommonDefine.DSP_CHANNEL_FACEBOOK) {
				ret = CommonDefine.SDK_KEY_FACEBOOK;
			} else if (channel == CommonDefine.DSP_CHANNEL_CM) {
				ret = CommonDefine.SDK_KEY_CM;
			} else if (channel == CommonDefine.DSP_CHANNEL_ADMOB_2) {
				ret = CommonDefine.SDK_KEY_ADMOB_2;
			}
		//}
		
		EmobLog.e(TAG, "getSdkSpotKeyString, channel="+channel +", string="+ret);
		return ret;
	}

//	public static int getSdkSpotKeyInt(Context context, int channel) {
//		int randomInt = SdkPreferences.getInstance(context).getInt(channel, SdkPreferences.SDK_SPOT_DSP_KEY_INT, 0);
//		if (randomInt < 1) {
//			// TODO:目前只有airpush用到int
//			randomInt = CommonDefine.SDK_INT_AIRPUSH;
//		}
//		
//		return randomInt;
//	}
	
//	public static String getTopBannerKeyString(Context context, int channel) {
//		String randomString = SdkPreferences.getInstance(context).getString(channel, SdkPreferences.TOP_BANNER_DSP_KEY_STRING, "");
//		String ret = decryptRandomString(randomString);
//		EmobLog.d(TAG, "getTopBannerKeyString, channel="+channel +", KeyInPrefs="+ret);
//		// 如果ret为空，说明randomString为空或者randomList里不包含randomString
//		if (TextUtils.isEmpty(ret)){
//			ret = CommonDefine.TOP_BANNER_KEY_INMOBI;
////			if (channel == CommonDefine.DSP_CHANNEL_ADMOB) {
////				ret = CommonDefine.TOP_BANNER_KEY_ADMOB;
////			} else if (channel == CommonDefine.DSP_CHANNEL_INMOBI) {
////				ret = CommonDefine.TOP_BANNER_KEY_INMOBI;
////			} else if (channel == CommonDefine.DSP_CHANNEL_AIRPUSH) {
////				ret = CommonDefine.TOP_BANNER_KEY_AIRPUSH;
////			} else if (channel == CommonDefine.DSP_CHANNEL_BATMOBI) {
////				ret = CommonDefine.TOP_BANNER_KEY_BATMOBI;
//////			} else if (channel == CommonDefine.SDK_CHANNEL_YEAHMOBI) {
////				
////			}
//		}
//		EmobLog.d(TAG, "getTopBannerKeyString, channel="+channel +", returnKey="+ret);
//		return ret;
//	}
	
	public static String getTopSpotKeyString(Context context, int channel) {
		String randomString = SdkPreferences.getInstance(context).getString(channel, SdkPreferences.TOP_SPOT_DSP_KEY_STRING, "");
		String ret = decryptRandomString(randomString);
		EmobLog.e(TAG, "getTopSpotKeyString, channel="+channel +", KeyInPrefs="+ret);
		// 如果ret为空，说明randomString为空或者randomList里不包含randomString
		if (TextUtils.isEmpty(ret)){
			ret = CommonDefine.TOP_SPOT_KEY_INMOBI;
//			if (channel == CommonDefine.DSP_CHANNEL_ADMOB) {
//				ret = CommonDefine.TOP_SPOT_KEY_ADMOB;
//			} else if (channel == CommonDefine.DSP_CHANNEL_INMOBI) {
//				ret = CommonDefine.TOP_SPOT_KEY_INMOBI;
//			} else if (channel == CommonDefine.DSP_CHANNEL_AIRPUSH) {
//				ret = CommonDefine.TOP_SPOT_KEY_AIRPUSH;
//			} else if (channel == CommonDefine.DSP_CHANNEL_BATMOBI) {
//				ret = CommonDefine.TOP_SPOT_KEY_BATMOBI;
////			} else if (channel == CommonDefine.SDK_CHANNEL_YEAHMOBI) {
//				
//			}
		}
		EmobLog.e(TAG, "getTopSpotKeyString, channel="+channel +", returnKey="+ret);
		return ret;
	}
	
//	public static String getFolderIconKeyString(Context context, int channel) {
//		String randomString = SdkPreferences.getInstance(context).getString(channel, SdkPreferences.FOLDER_ICON_DSP_KEY_STRING, "");
//		String ret = decryptRandomString(randomString);
//		EmobLog.e(TAG, "getFolderIconKeyString, channel="+channel +", KeyInPrefs="+ret);
//		// 如果ret为空，说明randomString为空或者randomList里不包含randomString
//		if (TextUtils.isEmpty(ret)){
//			ret = CommonDefine.FOLDER_ICON_KEY_INMOBI;
////			if (channel == CommonDefine.DSP_CHANNEL_ADMOB) {
////				ret = CommonDefine.FOLDER_ICON_KEY_ADMOB;
////			} else if (channel == CommonDefine.DSP_CHANNEL_INMOBI) {
////				ret = CommonDefine.FOLDER_ICON_KEY_INMOBI;
////			} else if (channel == CommonDefine.DSP_CHANNEL_AIRPUSH) {
////				ret = CommonDefine.FOLDER_ICON_KEY_AIRPUSH;
////			} else if (channel == CommonDefine.DSP_CHANNEL_BATMOBI) {
////				ret = CommonDefine.FOLDER_ICON_KEY_BATMOBI;
//////			} else if (channel == CommonDefine.SDK_CHANNEL_YEAHMOBI) {
////				
////			}
//		}
//		EmobLog.e(TAG, "getFolderIconKeyString, channel="+channel +", returnKey="+ret);
//		return ret;
//	}
	
	public static String decryptRandomString(String randomString) {
		String ret = null;
		if (!TextUtils.isEmpty(randomString)) {
			String keyString = SiteUtil.decrypt(randomString);
			ret =  keyString;
		} 
		return ret;
	}
	
	public static void logDefaultThings() {
		EmobLog.e(TAG, "#### ServerUrl = " + CommonDefine.SERVER_URL);
		EmobLog.e(TAG, "#### ServerUrl2 = " + CommonDefine.SERVER_URL2);
	}
}
