package com.emob.luck.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class EventItemList extends ArrayList<EventItem> {
	public static final String EVENT_DATA = "eventData";
	private static final long serialVersionUID = 1L;

	public static EventItemList newInstance(List<EventItem> items) {
		EventItemList sEventList = new EventItemList();
		sEventList.addAll(items);
		return sEventList;
	}

	public JSONArray getJsonArray() throws JSONException {
		JSONArray array = new JSONArray();
		// add data
		for (int i = 0; i < size();i++) {
			Object obj = get(i);
			if (obj instanceof EventItem) {
				array.put(((EventItem) get(i)).getJsonObject());
			}
		}
		return array;
	}
	
}
