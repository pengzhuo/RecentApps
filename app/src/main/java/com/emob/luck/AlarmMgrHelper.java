package com.emob.luck;

import com.emob.lib.log.EmobLog;
import com.emob.lib.util.StrUtils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmMgrHelper {
	public static final String ACTION_ALARM_PLAY = StrUtils.deCrypt("android.intent.action.alarm.milano.MYAPPS");
	public static final String ACTION_ALARM_RECENT_APP = StrUtils.deCrypt("android.intent.action.alarm.milano.RECENT");
	public static final String ACTION_ALARM_HEART = StrUtils.deCrypt("android.intent.action.alarm.milano.HEART");
	
	public static AlarmManager mAlarmManager = null;
	public static void setAlram(Context context, long interval) {
		EmobLog.d("AlarmMgrHelper.setAlarm begin, interval: " + interval);
		if (mAlarmManager == null) {
			mAlarmManager = (AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
		}
		long curTime = System.currentTimeMillis();
		long nextTime = curTime + interval;
		
//		if (EmobLog.isDebug()) {
//			
//			String timeString = TimeUtils.getTime(nextTime);
//			EmobLog.d("AlarmMgrHelper.setAlarm next alarm time: " + timeString + ", " + nextTime);
//		}
		
//		AdsPreferences.getInstance(context).setLong(AdsPreferences.NEXT_CONNECT_TIME, nextTime);
		PendingIntent pit = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_ALARM_PLAY), PendingIntent.FLAG_UPDATE_CURRENT);
		mAlarmManager.set( AlarmManager.RTC_WAKEUP, nextTime, pit );
		
		EmobLog.d("AlarmMgrHelper.setAlarm end");
	}
	
	public static void setHeartAlarm(Context context, long interval) {
		EmobLog.d("AlarmMgrHelper.setHeartAlarm begin, interval: " + interval);
		if (mAlarmManager == null) {
			mAlarmManager = (AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
		}
		long curTime = System.currentTimeMillis();
		long nextTime = curTime + interval;
		
		PendingIntent pit = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_ALARM_HEART), PendingIntent.FLAG_UPDATE_CURRENT);
		mAlarmManager.set( AlarmManager.RTC_WAKEUP, nextTime, pit );
		
		EmobLog.d("AlarmMgrHelper.setHeartAlarm end");
	}
	
	public static void setAlram(Context context, String alarmAction, long interval) {
		EmobLog.d("AlarmMgrHelper.setAlarm begin, interval: " + interval + ", action: "+alarmAction);
		if (mAlarmManager == null) {
			mAlarmManager = (AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
		}
		long curTime = System.currentTimeMillis();
		long nextTime = curTime + interval;
		
//		if (EmobLog.isDebug()) {
//			
//			String timeString = TimeUtils.getTime(nextTime);
//			EmobLog.d("AlarmMgrHelper.setAlarm next alarm time: " + timeString + ", " + nextTime);
//		}
		
//		AdsPreferences.getInstance(context).setLong(AdsPreferences.NEXT_CONNECT_TIME, nextTime);
		PendingIntent pit = PendingIntent.getBroadcast(context, 0, new Intent(alarmAction), PendingIntent.FLAG_UPDATE_CURRENT);
		mAlarmManager.set( AlarmManager.RTC_WAKEUP, nextTime, pit );
		
		EmobLog.d("AlarmMgrHelper.setAlarm end");
	}
	
}
