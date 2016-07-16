package com.emob.luck;

import com.emob.lib.util.StrUtils;
import com.emob.luck.common.CommonDefine;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

// 保存相关的配置信息
public class SdkPreferences {
	public static final String SDK_SPOT_NETWORK_ONOFF 		= StrUtils.deCrypt("sdk_network_on");	// SDK渠道：1-inmobi，2-airpush，3-admob
	
	public static final String SDK_SPOT_LASTTIME 			= StrUtils.deCrypt("sdk_lasttime");
	
	public static final String SDK_SPOT_LAUNCHER			= StrUtils.deCrypt("sdk_launcher");;
	public static final String SDK_SPOT_LOCK_ONOFF 			= StrUtils.deCrypt("sdk_lock_on");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String SDK_SPOT_TOP_EXIT_ONOFF 		= StrUtils.deCrypt("sdk_texit_on");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String SDK_SPOT_TOTAL_COUNT 		= StrUtils.deCrypt("sdk_tcount");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String SDK_SPOT_SHOW_INTERVAL 		= StrUtils.deCrypt("sdk_sint");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String SDK_SPOT_DSP_KEY_STRING 		= StrUtils.deCrypt("sdk_random_string");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String SDK_SPOT_DSP_KEY_INT 		= StrUtils.deCrypt("sdk_random_int");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String SDK_SPOT_REUQEST_COUNT		= StrUtils.deCrypt("sdk_req_count");	
	public static final String SDK_SPOT_SHOW_COUNT			= StrUtils.deCrypt("sdk_show_count");	
	public static final String SDK_SPOT_NEXT_TIME			= StrUtils.deCrypt("sdk_next_time");	
	
	public static final String TOP_BANNER_ONOFF 			= StrUtils.deCrypt("tban_on");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String TOP_BANNER_TOTAL_COUNT 		= StrUtils.deCrypt("tban_tcount");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String TOP_BANNER_SHOW_INTERVAL 	= StrUtils.deCrypt("tban_sint");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String TOP_BANNER_DSP_KEY_STRING 	= StrUtils.deCrypt("tban_random_string");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String TOP_BANNER_DSP_KEY_INT 		= StrUtils.deCrypt("tban_random_int");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String TOP_BANNER_REUQEST_COUNT		= StrUtils.deCrypt("tban_req_count");	
	public static final String TOP_BANNER_NEXT_TIME			= StrUtils.deCrypt("tban_next_time");
	
	public static final String TOP_SPOT_ONOFF 				= StrUtils.deCrypt("tint_on");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String TOP_SPOT_TOTAL_COUNT 		= StrUtils.deCrypt("tint_tcount");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String TOP_SPOT_SHOW_INTERVAL 		= StrUtils.deCrypt("tint_sint");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String TOP_SPOT_DSP_KEY_STRING 		= StrUtils.deCrypt("tint_random_string");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String TOP_SPOT_DSP_KEY_INT 		= StrUtils.deCrypt("tint_random_int");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String TOP_SPOT_REUQEST_COUNT		= StrUtils.deCrypt("tint_req_count");	
	public static final String TOP_SPOT_NEXT_TIME			= StrUtils.deCrypt("tint_next_time");
	
	public static final String FOLDER_ICON_ONOFF 			= StrUtils.deCrypt("ficon_on");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String FOLDER_ICON_TOTAL_COUNT 		= StrUtils.deCrypt("ficon_tcount");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String FOLDER_ICON_SHOW_INTERVAL	= StrUtils.deCrypt("ficon_sint");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String FOLDER_ICON_DSP_KEY_STRING 	= StrUtils.deCrypt("ficon_random_string");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String FOLDER_ICON_DSP_KEY_INT 		= StrUtils.deCrypt("ficon_random_int");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String FOLDER_ICON_REUQEST_COUNT	= StrUtils.deCrypt("ficon_req_count");	
	public static final String FOLDER_ICON_NEXT_TIME		= StrUtils.deCrypt("ficon_next_time");
	
	public static final String ICON_ONOFF 					= StrUtils.deCrypt("icon_on");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String ICON_TOTAL_COUNT 			= StrUtils.deCrypt("icon_tcount");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String ICON_SHOW_INTERVAL			= StrUtils.deCrypt("icon_sint");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String ICON_DSP_KEY_STRING 			= StrUtils.deCrypt("icon_random_string");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String ICON_DSP_KEY_INT 			= StrUtils.deCrypt("icon_random_int");	// SDK渠道：1-inmobi，2-airpush，3-admob
	public static final String ICON_REUQEST_COUNT			= StrUtils.deCrypt("icon_req_count");	
	public static final String ICON_NEXT_TIME				= StrUtils.deCrypt("icon_next_time");

	public static final String SDK_SITE_TRIES_NUM 			= StrUtils.deCrypt("SDK_SITE_TRIES_NUM");  //尝试次数
	public static final String SDK_SITE_RESET_DAY_NUM 		= StrUtils.deCrypt("SDK_SITE_RESET_DAY_NUM");  //尝试次数清零周期
	public static final String SDK_SITE_HAVE_TRIES_NUM		= StrUtils.deCrypt("SDK_SITE_HAVE_TRIES_NUM");  //已经尝试的次数
	public static final String SDK_SITE_TRIES_TIME 			= StrUtils.deCrypt("SDK_SITE_TRIES_TIME");  //尝试次数全部用完的时间
	public static final String SDK_SITE_REQUEST_FAIL 		= StrUtils.deCrypt("SDK_SITE_REQUEST_FAIL");  //上次尝试失败的渠道
	public static final String SDK_SITE_TRIES_OVER 			= StrUtils.deCrypt("SDK_SITE_TRIES_OVER");  //尝试次数用户完标志
	
	private static SdkPreferences mPreferences;
	private Context mContext;
	
	private SharedPreferences mPrefGloabl;
	private Editor mEditorGloabl;
	
	private SharedPreferences mPrefAdmob;
	private Editor mEditorAdmob;
	
	private SharedPreferences mPrefAdmob2;
	private Editor mEditorAdmob2;
	
	private SharedPreferences mPrefInmobi;
	private Editor mEditorInmobi;
	
	private SharedPreferences mPrefAirpush;
	private Editor mEditorAirpush;
	
	private SharedPreferences mPrefBatmobi;
	private Editor mEditorBatmobi;
	
	private SharedPreferences mPrefYeahmobi;
	private Editor mEditorYeahmobi;

	private SharedPreferences mPrefFacebook;
	private Editor mEditorFacebook;

	private SharedPreferences mPrefCM;
	private Editor mEditorCM;
	
	public static synchronized SdkPreferences getInstance(Context context) {
		if (mPreferences == null) {
			mPreferences = new SdkPreferences(context);
		}
		return mPreferences;
	}

	public SdkPreferences(Context context) {
		mContext = context;
	}

	public void setString(int channel, String key, String value) {
		Editor editor = getEditor(channel); 
		if (editor != null) {
			editor.putString(key, value).commit();
		}
	}

	public String getString(int channel, String key, String defValue) {
		SharedPreferences prefs = getPrefs(channel);
		return (null != prefs) ? prefs.getString(key, defValue) : "";
	}
	
    public <T> void setLong(int channel, T key, long value) {
    	Editor editor = getEditor(channel); 
        if (editor == null) {
            return;
        }
        editor.putLong(key.toString(), value);
        editor.commit();
    }
	
	public <T> long getLong(int channel, T key,long defValue)
	{
		SharedPreferences prefs = getPrefs(channel);
		if (prefs==null) {
			return 0;
		}
		return prefs.getLong((String) key, defValue);
	}
	
	public <T> void setInt(int channel, T key, int value) {
		Editor editor = getEditor(channel); 
		if (editor == null) {
	    	return;
	  	}
		editor.putInt(key.toString(), value);
		editor.commit();
	}
	
	public <T> int getInt(int channel, T key, int defValue)
	{
		SharedPreferences prefs = getPrefs(channel);
		if (prefs==null) {
			return 0;
		}
		return prefs.getInt((String) key, defValue);
	}

	public <T> void setBoolean(int channel, T key, boolean value) {
		Editor editor = getEditor(channel);
		if (editor == null) {
			return;
		}
		editor.putBoolean(key.toString(), value);
		editor.commit();
	}

	public <T> boolean getBoolean(int channel, T key, boolean defValue)
	{
		SharedPreferences prefs = getPrefs(channel);
		if (prefs==null) {
			return false;
		}
		return prefs.getBoolean(key.toString(), defValue);
	}
	
	private void ensurePrefs(int channel) {
		if (channel == CommonDefine.DSP_GLOABL ) {
			if (mPrefGloabl == null) {
				mPrefGloabl = mContext.getSharedPreferences("d_gloabl", 0);
			}
			if (mEditorGloabl == null) {
				mEditorGloabl = mPrefGloabl.edit();
			}
		} else if (channel == CommonDefine.DSP_CHANNEL_ADMOB) {
			if (mPrefAdmob == null) {
				mPrefAdmob = mContext.getSharedPreferences("d_am", 0);
			}
			if (mEditorAdmob == null) {
				mEditorAdmob = mPrefAdmob.edit();
			}
		} else if (channel == CommonDefine.DSP_CHANNEL_ADMOB_2) {
			if (mPrefAdmob2 == null) {
				mPrefAdmob2 = mContext.getSharedPreferences("d_am_2", 0);
			}
			if (mEditorAdmob2 == null) {
				mEditorAdmob2 = mPrefAdmob2.edit();
			}
		} else if (channel == CommonDefine.DSP_CHANNEL_INMOBI) {
			if (mPrefInmobi == null) {
				mPrefInmobi = mContext.getSharedPreferences("d_in", 0);
			}
			if (mEditorInmobi == null) {
				mEditorInmobi = mPrefInmobi.edit();
			}
		} else if (channel == CommonDefine.DSP_CHANNEL_AIRPUSH) {
			if (mPrefAirpush == null) {
				mPrefAirpush = mContext.getSharedPreferences("d_ai", 0);
			}
			if (mEditorAirpush == null) {
				mEditorAirpush = mPrefAirpush.edit();
			}
		} else if (channel == CommonDefine.DSP_CHANNEL_BATMOBI) {
			
			if (mPrefBatmobi == null) {
				mPrefBatmobi = mContext.getSharedPreferences("d_bat", 0);
			}
			if (mEditorBatmobi == null) {
				mEditorBatmobi = mPrefBatmobi.edit();
			}
		} else if (channel == CommonDefine.DSP_CHANNEL_YEAHMOBI) {
			if (mPrefYeahmobi == null) {
				mPrefYeahmobi = mContext.getSharedPreferences("d_ym", 0);
			}
			if (mEditorYeahmobi == null) {
				mEditorYeahmobi = mPrefYeahmobi.edit();
			}
		} else if (channel == CommonDefine.DSP_CHANNEL_FACEBOOK){
			if (mPrefFacebook == null) {
				mPrefFacebook = mContext.getSharedPreferences("d_fb", 0);
			}
			if (mEditorFacebook == null) {
				mEditorFacebook = mPrefFacebook.edit();
			}
		} else if (channel == CommonDefine.DSP_CHANNEL_CM){
			if (mPrefCM == null) {
				mPrefCM = mContext.getSharedPreferences("d_cm", 0);
			}
			if (mEditorCM == null) {
				mEditorCM = mPrefCM.edit();
			}
		}
		
	}
	
	private Editor getEditor(int channel) {
		ensurePrefs(channel);
		if (channel == CommonDefine.DSP_GLOABL) {
			return mEditorGloabl;
		} else if (channel == CommonDefine.DSP_CHANNEL_ADMOB) {
			return mEditorAdmob;
		} else if (channel == CommonDefine.DSP_CHANNEL_ADMOB_2) {
			return mEditorAdmob2;
		} else if (channel == CommonDefine.DSP_CHANNEL_INMOBI) {
			return mEditorInmobi;
		} else if (channel == CommonDefine.DSP_CHANNEL_AIRPUSH) {
			return mEditorAirpush;
		} else if (channel == CommonDefine.DSP_CHANNEL_BATMOBI) {
			return mEditorBatmobi;
		} else if (channel == CommonDefine.DSP_CHANNEL_YEAHMOBI) {
			return mEditorYeahmobi;
		} else if (channel == CommonDefine.DSP_CHANNEL_FACEBOOK){
			return mEditorFacebook;
		} else if (channel == CommonDefine.DSP_CHANNEL_CM){
			return mEditorCM;
		}
		return null;
	}
	
	private SharedPreferences getPrefs(int channel) {
		ensurePrefs(channel);
		if (channel == CommonDefine.DSP_GLOABL ) {
			return mPrefGloabl;
		} else if (channel == CommonDefine.DSP_CHANNEL_ADMOB) {
			return mPrefAdmob;
		} else if (channel == CommonDefine.DSP_CHANNEL_ADMOB_2) {
			return mPrefAdmob2;
		} else if (channel == CommonDefine.DSP_CHANNEL_INMOBI) {
			return mPrefInmobi;
		} else if (channel == CommonDefine.DSP_CHANNEL_AIRPUSH) {
			return mPrefAirpush;
		} else if (channel == CommonDefine.DSP_CHANNEL_BATMOBI) {
			return mPrefBatmobi;
		} else if (channel == CommonDefine.DSP_CHANNEL_YEAHMOBI) {
			return mPrefYeahmobi;
		} else if (channel == CommonDefine.DSP_CHANNEL_FACEBOOK){
			return mPrefFacebook;
		} else if (channel == CommonDefine.DSP_CHANNEL_CM){
			return mPrefCM;
		}
		return null;
	}
}
