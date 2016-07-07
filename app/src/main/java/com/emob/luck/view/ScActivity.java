package com.emob.luck.view;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.emob.lib.log.EmobLog;
import com.emob.lib.stats.FlurryUtil;
import com.emob.lib.stats.StatsDefines;
import com.emob.lib.stats.StatsDefines;
import com.emob.lib.stats.StatsUtil;
import com.emob.lib.stats.UmengUtils;
import com.emob.lib.util.MobiUtils;
import com.emob.lib.util.Utils;
import com.emob.luck.AdsService;
import com.emob.luck.db.EventTableDBHelper;
import com.emob.luck.model.EventItem;

public class ScActivity extends Activity 
{
	private static final String PKG_GP = "com.android.vending";
	private static final String SHORTCUT = "sc";
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		//注册友盟
		StatsUtil.onCreat(mContext);
				
//		EventTableDBHelper.insertData(getApplicationContext(), "shortcut", 0, 0, EventItem.EVENT_TYPE_SHORTCUT_CLICK);
		Map<String, String> map = new HashMap<String, String>();
		map.put("PCK", SHORTCUT);
		map.put("ACTION", EventItem.EVENT_TYPE_SHORTCUT_CLICK + "");
//		UmengUtils.onEvent(mContext, StatsDefines.EVENT_TYPE_SHORTCUT_CLICK, map);
//		FlurryUtil.onEvent(mContext, StatsDefines.EVENT_TYPE_SHORTCUT_CLICK, map);
		
//		StatsUtil.onEvent(mContext, StatsDefines.EVENT_TYPE_SHORTCUT_CLICK, map);
		StatsUtil.onEventBackground(mContext, StatsDefines.EVENT_TYPE_SHORTCUT_CLICK, SHORTCUT);
		boolean isServiceRunning = MobiUtils.isServiceRunning(getApplicationContext(), AdsService.class.getName());
		if (!isServiceRunning) {
//			EventTableDBHelper.insertData(getApplicationContext(), "shortcut", 0, 0, EventItem.EVENT_TYPE_SHORTCUT_SERVICE);
			Map<String, String> map2 = new HashMap<String, String>();
			map.put("PCK", SHORTCUT);
			map.put("ACTION", EventItem.EVENT_TYPE_SHORTCUT_SERVICE + "");
//			UmengUtils.onEvent(mContext, StatsDefines.EVENT_TYPE_SHORTCUT_SERVICE, map2);
//			FlurryUtil.onEvent(mContext, StatsDefines.EVENT_TYPE_SHORTCUT_SERVICE, map2);
//			StatsUtil.onEvent(mContext, StatsDefines.EVENT_TYPE_SHORTCUT_SERVICE, map2);
			StatsUtil.onEventBackground(mContext, StatsDefines.EVENT_TYPE_SHORTCUT_SERVICE, SHORTCUT);
			startService(getApplicationContext(), AdsService.SERVICE_START_ICON);
		}
		
		boolean isAppInstalled = Utils.isAppInstalled(getApplicationContext(), PKG_GP);
		if (isAppInstalled) {
//			EventTableDBHelper.insertData(getApplicationContext(), "shortcut", 0, 0, EventItem.EVENT_TYPE_SHORTCUT_GP);
			Map<String, String> map2 = new HashMap<String, String>();
			map.put("PCK", SHORTCUT);
			map.put("ACTION", EventItem.EVENT_TYPE_SHORTCUT_GP + "");
//			UmengUtils.onEvent(mContext, StatsDefines.EVENT_TYPE_SHORTCUT_GP, map2);
//			FlurryUtil.onEvent(mContext, StatsDefines.EVENT_TYPE_SHORTCUT_GP, map2);
//			StatsUtil.onEvent(mContext, StatsDefines.EVENT_TYPE_SHORTCUT_GP, map2);
			StatsUtil.onEventBackground(mContext, StatsDefines.EVENT_TYPE_SHORTCUT_GP, SHORTCUT);
			
			startGp();
		} else {
//			EventTableDBHelper.insertData(getApplicationContext(), "shortcut", 0, 0, EventItem.EVENT_TYPE_SHORTCUT_BROWSER);
			Map<String, String> map2 = new HashMap<String, String>();
			map.put("PCK", SHORTCUT);
			map.put("ACTION", EventItem.EVENT_TYPE_SHORTCUT_BROWSER + "");
//			UmengUtils.onEvent(mContext, StatsDefines.EVENT_TYPE_SHORTCUT_BROWSER, map2);
//			FlurryUtil.onEvent(mContext, StatsDefines.EVENT_TYPE_SHORTCUT_BROWSER, map2);
//			StatsUtil.onEvent(mContext, StatsDefines.EVENT_TYPE_SHORTCUT_BROWSER, map2);
			StatsUtil.onEventBackground(mContext, StatsDefines.EVENT_TYPE_SHORTCUT_BROWSER, SHORTCUT);
			startBrowser();
		}
		
		finish();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		FlurryUtil.onStart(this, StatsDefines.APP_KEY_FLURRY);
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		StatsUtil.onResume(mContext);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		StatsUtil.onPause(mContext);
	}
	
	@Override
	protected void onStop() {
		FlurryUtil.onStop(this);
		super.onStop();
	}
	
	public void startGp() {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/"));
        browserIntent.setClassName("com.android.vending", "com.android.vending.AssetBrowserActivity");
        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(browserIntent);
	}
	
	public void startBrowser() {
		Uri uri = Uri.parse("https://play.google.com/");   
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);   
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        startActivity(intent);    
	}
	
	private void startService(Context context, int startType) {
    	EmobLog.d("ScActivity.startService begin");
    	Intent intent = new Intent(context, AdsService.class);
    	intent.putExtra("start", startType);
        context.startService(intent);
        EmobLog.d("ScActivity.startService end");
    }
}
