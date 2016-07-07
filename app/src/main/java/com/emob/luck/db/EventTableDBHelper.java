package com.emob.luck.db;

import org.json.JSONArray;

import com.emob.lib.util.TimeUtils;
import com.emob.luck.model.EventItemList;

import android.content.Context;

public class EventTableDBHelper {
	private static EventTableDB mEventTableDB = null;
	public static void insertData(Context context, String packageName, int channel, int pos, int action, String time) {
		ensureEventDB(context);
		try {
			mEventTableDB.insertEvent(packageName, channel, pos, action, time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void insertData(Context context, String pkgName, int channel, int pos, int action) {
		String time = TimeUtils.getFormattedTime(System.currentTimeMillis()).toString();
//		String time = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.HOURS_24_MAKE);
		insertData(context, pkgName, channel, pos, action, time);
	}
	
	public static void deleteAll(Context context) {
		ensureEventDB(context);
		try {
			mEventTableDB.deleteAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static JSONArray getEvents(Context context) {
		ensureEventDB(context);
		JSONArray eventArray = null;
		try {
			EventItemList eventItemList = mEventTableDB.getuploadEventItems();
			if (null != eventItemList && eventItemList.size() > 0) {
				eventArray = eventItemList.getJsonArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return eventArray;
	}
	
	private static void ensureEventDB(Context context) {
		if (mEventTableDB == null) {
			mEventTableDB = new EventTableDB(context);
		}
	}
}