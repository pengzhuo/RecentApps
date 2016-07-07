package com.emob.luck.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class AdItemList extends ArrayList<AdItem> implements Serializable {

	private static final long serialVersionUID = -2648083020925581298L;
	private List<AdItem> adsList = new ArrayList<AdItem>();
	public AdItemList() {
		super();
	}
	public List<AdItem> getAdsList() {
		return adsList;
	}
	public void setAdsList(AdItemList list) {
		if (list==null)
		{
			return;
		}
		adsList.clear();
		adsList.addAll(list.getItems());
	}
	
	public List<AdItem> getItems() {
	    return (adsList.size()!=0)?adsList:new ArrayList<AdItem>();
	}
	
	public static AdItemList getAdListByJson(JSONArray objs) throws Exception {
		if (objs == null) {
			return null;
		}
		AdItemList mAdList = new AdItemList();
		AdItem item = null;
		for (int i = 0; i < objs.length(); i++) {
			Object object = objs.get(i);
			if (object instanceof JSONObject) {
				item = new AdItem((JSONObject) object);
				mAdList.add(item);
			}
		}
		return mAdList;
	}
	
}
