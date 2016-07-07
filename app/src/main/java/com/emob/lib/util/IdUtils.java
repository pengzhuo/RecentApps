package com.emob.lib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class IdUtils {
	private static final String PREFS_FILE_NAME = StrUtils.deCrypt("ids_info");
	
	public static final String PREFS_KEY_GID = StrUtils.deCrypt("gid");
	public static final String PREFS_KEY_SID = StrUtils.deCrypt("sid");
	
	public static final String PREFS_KEY_UID = StrUtils.deCrypt("uid");
	
	
	public static String generateGid(Context context) {
		String gid = SysHelper.getHashCode("MD5", SysHelper.getRandomData(32));
		setPreference(context, PREFS_KEY_GID, gid);
		
		return gid;
	}
	
	public static String getGid(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE_NAME, 0);
		String gid = prefs.getString(PREFS_KEY_GID, "");
		if (gid == null || gid.length() == 0) {
			return generateGid(context);
		}
		
		return gid;
	}
	
	public static void setUid(Context context, String uid) {
		setPreference(context, PREFS_KEY_UID, uid);
	}
	
	public static String getUid(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE_NAME, 0);
		String sid = prefs.getString(PREFS_KEY_UID, "");
		
		return sid;
	}
	
	public static void setSid(Context context, String sid) {
		setPreference(context, PREFS_KEY_SID, sid);
	}
	
	public static String getSid(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE_NAME, 0);
		String gid = prefs.getString(PREFS_KEY_SID, "");
		
		return gid;
	}
	
	public static String getIid(Context context) {
		return Installation.id(context);
	}
	
	private static void setPreference(Context context, String key, String value) {
		if (TextUtils.isEmpty(key)) {
			return;
		}
		
		SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE_NAME, 0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

}
