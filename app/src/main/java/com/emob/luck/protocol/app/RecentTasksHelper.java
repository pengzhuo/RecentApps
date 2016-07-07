package com.emob.luck.protocol.app;

import java.util.ArrayList;
import java.util.List;

import com.emob.lib.log.EmobLog;
import com.emob.lib.util.StrUtils;
import com.emob.luck.AdsPreferences;
import com.emob.luck.model.PackageElement;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

public class RecentTasksHelper
{
	private static final String PKGNAME_BROWSER_SYS = StrUtils.deCrypt("com.android.browser");
	private static final String PREFS_FILE_NAME = StrUtils.deCrypt("recent_apps");
	private static final String PREFS_KEY_RECENT = StrUtils.deCrypt("recent");
	private static final int TOTAL_RECENT_APPS = 13;
	
	public static String getRecentAppString(Context context) {
		EmobLog.d("RecentTasksHelper.getRecentAppString begin");
//		String recentApp = updateRecentApp(context);
		SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE_NAME, 0);		
    	String recentApp = prefs.getString(PREFS_KEY_RECENT, "");
    	EmobLog.d("RecentTasksHelper.getRecentAppString recentApp: " + recentApp);
    	if (TextUtils.isEmpty(recentApp)) {
    		EmobLog.d("RecentTasksHelper.getRecentAppString to Update Recent Apps.");
    		recentApp = updateRecentApp(context);
    		EmobLog.d("RecentTasksHelper.getRecentAppString updated recentApp: " + recentApp);
    	}
    	
    	return recentApp;
	}
	
	public static void setRecentAppString(Context context, String recentApp) {
		
		SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE_NAME, 0);		
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PREFS_KEY_RECENT, recentApp);
		editor.commit();
	}
	
	public static String updateRecentApp(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE_NAME, 0);		
		
    	String recentApp = prefs.getString(PREFS_KEY_RECENT, "");
    	EmobLog.d("RecentTasksHelper.updateRecentApp begin, recentApp: "+recentApp);
        
    	List<String> recentList = getRecentTaskList(context);
    	if (recentList != null && recentList.size() > 0) {
    		EmobLog.d("AdsReceiver.updateRecentApp recentList.size : "+recentList.size() + ", toString: " + recentList.toString());

    		String bbList = AdsPreferences.getInstance(context).getString(AdsPreferences.BB_LIST_STRING, "");
    		EmobLog.d("AdsReceiver.updateRecentApp bbList: "+bbList);
    		for (int i=0; i< recentList.size(); i++) {
    			String pkgname = recentList.get(i);
    			EmobLog.d("AdsReceiver.updateRecentApp pkgName: "+pkgname);
    			if (bbList.contains(pkgname)) {
    				EmobLog.d("AdsReceiver.updateRecentApp bblist contains: "+pkgname);
    				if (recentApp.contains(pkgname)) {
    					EmobLog.d("AdsReceiver.updateRecentApp recentApp contains: "+pkgname+",recentApp: "+recentApp);
    					recentApp = recentApp.replace(pkgname, "");
    					EmobLog.d("AdsReceiver.updateRecentApp recentApp replaced: "+recentApp);
    				}
    				continue;
    			}
    			if (TextUtils.isEmpty(pkgname) || recentApp.contains(pkgname)) {
    				EmobLog.d("AdsReceiver.updateRecentApp pkgname empty or exists");
    				continue;
    			}
    			recentApp += pkgname + ", ";
    			
        	}
    		
			EmobLog.d("AdsReceiver.updateRecentApp neoRecentPkgNameList: "+recentApp + ", total: " + recentApp);
			
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(PREFS_KEY_RECENT, recentApp);
			editor.commit();
    	}
    	
    	EmobLog.d("AdsReceiver.updateRecentApp end");
    	return recentApp;
    }
	
	public static List<String> getRecentTaskList( Context context )
	{
		EmobLog.d("RecentTasksHelper.getRecentTaskList begin");
		List<String> rts = new ArrayList<String>();
		
		try
		{
			EmobLog.d("RecentTasksHelper.getRecentTaskList 1");
			ActivityManager am = (ActivityManager) context.getSystemService( Context.ACTIVITY_SERVICE );
			List<RecentTaskInfo> list = am.getRecentTasks( 64, 0 );
			EmobLog.d("RecentTasksHelper.getRecentTaskList 2");
			PackageManager pm  = context.getPackageManager();
			String pkgName = "";
			for(RecentTaskInfo task : list )
			{
				EmobLog.d("recent list.size: "+list.size());
				ComponentName cn = task.origActivity;
				if( cn != null )
				{
					pkgName = cn.getPackageName();
					
					EmobLog.d("RecentTasksHelper.getRecentTaskList cn.getPackageName: " + pkgName);
					if (!TextUtils.isEmpty(pkgName) && isUserApplication(context, pkgName)) {
						EmobLog.d("RecentTasksHelper.getRecentTaskList userapp, add it");
						rts.add(pkgName);
						continue;
					}
				}
				
				Intent baseIntent = task.baseIntent;
				ResolveInfo ri = pm.resolveActivity( baseIntent, 0 );
				if( ri != null )
				{
					pkgName = ri.activityInfo.packageName;
					EmobLog.d("RecentTasksHelper.getRecentTaskList ri.activityInfo.packageName: " + pkgName);
					if (!TextUtils.isEmpty(pkgName) && isUserApplication(context, pkgName)) {
						EmobLog.d("RecentTasksHelper.getRecentTaskList userapp, add it 222");
						rts.add(pkgName);
						continue;
					}
				}
			}
		} catch (SecurityException e)
		{
		}
		EmobLog.d("RecentTasksHelper.getRecentTaskList end");
		return rts;
	}
	
	public static ArrayList<PackageElement> getRecentApps(Context context)
	{
		EmobLog.d("RecentTasksHelper.getRecentApps begin");
		
		String recentApp = updateRecentApp(context);
		EmobLog.d("RecentTasksHelper.getRecentApps recentApp: " + recentApp);
		if (TextUtils.isEmpty(recentApp)) {
			return null;
		}
		
		String[] recents = recentApp.split(",");
		if (recents == null || recents.length < 1) {
			return null;
		}
		
		EmobLog.d("RecentTasksHelper.getRecentApps recentApp: " + recents.toString());
		
		int total = 0;
		PackageManager pm = context.getPackageManager();  
		ArrayList<PackageElement> rts = new ArrayList<PackageElement>();
		for (int i=recents.length-1; i>=0; i--) {
			String recent = recents[i];
			if (TextUtils.isEmpty(recent)) {
				continue;
			}
			
			if (total >= TOTAL_RECENT_APPS) {
				break;
			}
			
			String pkgname = recent.trim();
			
			try { 
				PackageElement pe = new PackageElement();
	             ApplicationInfo info = pm.getApplicationInfo(pkgname, 0);     
	             String name = pm.getApplicationInfo(pkgname, 0).loadLabel(pm).toString();     
                 pe.setLabel(name);
                 pe.setmIcon(info.loadIcon(pm));
                 pe.setmPackageName(pkgname);
                 pe.setmIsNative(false);
                 rts.add(pe);
                 total++;
	        } catch (Exception e) {    
	              
	        }    
		}

		EmobLog.d("RecentTasksHelper.getRecentTaskList end");
		return rts;
	}
	
	private static boolean isUserApplication(Context context, String pkgName) {
		EmobLog.d("isUserApplication, pkgname: " + pkgName);
		if (!TextUtils.isEmpty(pkgName)) {
			try {
				if (pkgName.equalsIgnoreCase(context.getPackageName())) {
					return false;
				}
				// skip SYSTEM BROWSER
				if (pkgName.equalsIgnoreCase(PKGNAME_BROWSER_SYS)) {
					return true;
				}
				
				PackageInfo pInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
				if (pInfo != null) {
					boolean isUserApp = isUserApp(pInfo);
					EmobLog.d("isUserApplication, isUserApp: " + isUserApp);
					return isUserApp;
				}
			} catch (Exception e) {
				return false;
			}
		}
		
		return false;
	}
	
	private static boolean isSystemApp(PackageInfo pInfo) {  
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);  
    }  
  
    public static boolean isSystemUpdateApp(PackageInfo pInfo) {  
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);  
    }  
  
    public static boolean isUserApp(PackageInfo pInfo) {  
        return (!isSystemApp(pInfo) && !isSystemUpdateApp(pInfo));  
    }  
}
