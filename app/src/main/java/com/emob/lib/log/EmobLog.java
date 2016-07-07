package com.emob.lib.log;


import com.emob.lib.util.StrUtils;
import com.emob.luck.SdkHelper;

import android.text.TextUtils;
import android.util.Log;

public final class EmobLog {
	public static boolean DEBUG_MARK = true;
	public static final String TAG = StrUtils.deCrypt("Milano");
    
	public static boolean isDebug() {
		return DEBUG_MARK;
	}
	
	public static void v(String msg) {
		v(TAG, msg);
	}
	
	public static void v(String tag, String msg) {
    	if(DEBUG_MARK) {
    		if(TextUtils.isEmpty(tag)) {
    			Log.v(TAG, msg);
    		} else {
    			Log.v(tag, msg);
    		}
    	}
    }
	
	public static void d(String msg) {
		d(TAG, msg);
	}
	
    public static void d(String tag, String msg) {
    	if(DEBUG_MARK) {
    		if(TextUtils.isEmpty(tag)) {
    			Log.d(TAG, msg);
    		} else {
    			Log.d(tag, msg);
    		}
    	}
    }
    
    public static void i(String msg) {
		i(TAG, msg);
	}
    
    public static void i(String tag, String msg) {
    	if(DEBUG_MARK) {
    		if(TextUtils.isEmpty(tag)) {
    			Log.i(TAG, msg);
    		} else {
    			Log.i(tag, msg);
    		}
    	}
    }
    
    public static void w(String msg) {
		w(TAG, msg);
	}
    
    public static void w(String tag, String msg) {
    	if(DEBUG_MARK) {
    		if(TextUtils.isEmpty(tag)) {
    			Log.w(TAG, msg);
    		} else {
    			Log.w(tag, msg);
    		}
    	}
    }
    
    public static void e(String msg) {
		e(TAG, msg);
	}
    
    public static void e(String tag, String msg) {
    	if(DEBUG_MARK) {
    		if(TextUtils.isEmpty(tag)) {
    			Log.e(TAG, msg);
    		} else {
    			Log.e(tag, msg);
    		}
    	}
    }
    
    public static void logDefaultThings() {
    	SdkHelper.logDefaultThings();
    }
}
