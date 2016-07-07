package com.emob.luck;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.jaredrummler.android.processes.AndroidProcesses;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

public class AppTaskTimer {
	
	private Context mContext;
	private Timer scanAppTimer;
	private static AppTaskTimer mAppTaskTimer;
	private ScreenOnOffRecvier mScreenOnOffRecvier;
	private static String oldPackname = "";
	private ActivityManager mActivityManager;
	
	private static final long INTERVAL = 2000L;
	private static ArrayList<AppTaskTimer.CheckPackageObserver> observers = new ArrayList<AppTaskTimer.CheckPackageObserver>();
	
	public interface CheckPackageObserver{
		int getPriority();
		public void checkPackageName(String packageName);
	};
	
	public synchronized void addObserver(CheckPackageObserver ob){
			if (!observers.contains(ob)) {
				boolean isAdded = false;
				for (int i = 0; i < observers.size(); i++) {
					if (observers.get(i).getPriority() < ob.getPriority()) {
						observers.add(i, ob);
						isAdded = true;
						break;
					}
				}
				
				if (!isAdded) {
					observers.add(ob);
				}
			}
	}
	
	public synchronized void removeObserver(CheckPackageObserver ob) {
			observers.remove(ob);
	}
	
	private AppTaskTimer(Context context){
		this.mContext = context;
		mActivityManager  = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
//		mScreenOnOffRecvier = new ScreenOnOffRecvier();
//		mContext.registerReceiver(mScreenOnOffRecvier, filter);
		
	}
	
	public static AppTaskTimer getInstance(Context context){
		if (null == mAppTaskTimer) {
			mAppTaskTimer = new AppTaskTimer(context);
		}
		return mAppTaskTimer;
	}
	
	public void start(){
		if (null == scanAppTimer) {
			scanAppTimer = new Timer();
			scanAppTimer.schedule(new AppTimerTask(), 0, INTERVAL);
		}
	}
	
	public void pause(){
		if (null != scanAppTimer) {
			scanAppTimer.cancel();
			scanAppTimer = null;
		}
	}
	
	public void destroy(){
		if (null != scanAppTimer) {
			scanAppTimer.cancel();
			scanAppTimer = null;
		}
		if (null != mScreenOnOffRecvier) {
			mContext.unregisterReceiver(mScreenOnOffRecvier);
		}
		mAppTaskTimer = null;
		oldPackname = "";
	}
	
	private class AppTimerTask extends TimerTask{
		@Override
		public void run() {
			String packageName = appInFront(mContext);
//			Log.e("AppTaskTimer", "#### pkg: " + packageName + " old: " + oldPackname);
			if (!TextUtils.equals(oldPackname, packageName)) {
				synchronized (AppTaskTimer.this) {
					for (CheckPackageObserver ob : observers) {
						ob.checkPackageName(packageName);
					}
				}
			}
			oldPackname = packageName;
		}
		
	};
	
	private class ScreenOnOffRecvier extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (TextUtils.equals(action, Intent.ACTION_SCREEN_ON)) {
				if (null != mAppTaskTimer) {
					mAppTaskTimer.start();
				}
			}else if (TextUtils.equals(action, Intent.ACTION_SCREEN_OFF)) {
				if (null != mAppTaskTimer) {
					mAppTaskTimer.pause();
				}
			}
			
		}
		
	};
	
	public String appInFront(Context context){
		List<ActivityManager.RunningTaskInfo> rtList = mActivityManager.getRunningTasks(1);
		if( rtList == null ||
				rtList.size() == 0 ){
			return "" ;
		}
		ActivityManager.RunningTaskInfo taskInfo = rtList.get(0);
		if (null != taskInfo) {
			String packageName = taskInfo.topActivity.getPackageName();
			return packageName;
		}
		return "";
	}

	public String getTopPkgName(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
			Field field = null;

			try {
				field = RunningAppProcessInfo.class
						.getDeclaredField("processState");
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
			List<ActivityManager.RunningAppProcessInfo> processInfos = am
					.getRunningAppProcesses();
			for (RunningAppProcessInfo app : processInfos) {
				if (app.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
						&& app.importanceReasonCode == 0) {
					Integer state = null;
					try {
						state = field.getInt(app);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
					if (state != null && state == 2) {
						if (app.pkgList.length > 0) {
							Log.d("", "#### ---L getTopPkgName: " + app.pkgList[0]);
							return app.pkgList[0];
						}
					}
				}
			}
			return "";
		} else {
			List<ActivityManager.RunningTaskInfo> rtList = mActivityManager.getRunningTasks(1);
			if( rtList == null ||
					rtList.size() == 0 ){
				return "" ;
			}
			ActivityManager.RunningTaskInfo taskInfo = rtList.get(0);
			if (null != taskInfo) {
				String packageName = taskInfo.topActivity.getPackageName();
				return packageName;
			}
			return "";
		}
	}

}
