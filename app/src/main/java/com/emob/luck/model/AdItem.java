package com.emob.luck.model;

import java.io.Serializable;

import org.json.JSONObject;

import com.emob.luck.common.Value;

public class AdItem implements Serializable{
	private static final long serialVersionUID = 1L;
	public int index;
	public String titleName;
	public String iconUrl;
	public String landingUrl;
	
	public AdItem(int index, String titleName, String iconUrl, String landingUrl) {
		super();
		this.index = index;
		this.titleName = titleName;
		this.iconUrl = iconUrl;
		this.landingUrl = landingUrl;
	}
	
	public AdItem() {
		super();
	}

	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getLandingUrl() {
		return landingUrl;
	}

	public void setLandingUrl(String landingUrl) {
		this.landingUrl = landingUrl;
	}
	
	public AdItem(JSONObject obj) throws Exception {
		if (obj == null || obj.length() == 0) {
			return;
		}
		if (obj.has(Value.TITLENAME)) {
			titleName = obj.getString(Value.TITLENAME);
		}

		if (obj.has(Value.ICONURL)) {
			iconUrl = obj.getString(Value.ICONURL);
		}

		if (obj.has(Value.LANDINGURL)) {
			landingUrl = obj.getString(Value.LANDINGURL);
		}

	}

}
