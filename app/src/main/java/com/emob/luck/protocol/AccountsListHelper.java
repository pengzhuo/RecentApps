package com.emob.luck.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.emob.lib.util.StrUtils;
import com.emob.lib.util.Utils;
import com.emob.luck.common.Value;

import android.content.Context;

public class AccountsListHelper {
	public static final String NAME = StrUtils.deCrypt("name");
	public static final String VALUE = StrUtils.deCrypt("value");
	static public JSONArray getAccountsList(Context context)
	{
		JSONArray appsArray = new JSONArray();
		JSONObject appObject = new JSONObject();
		try {
			appObject.put(NAME, Value.GOOGLE_ACCOUNT);
			appObject.put(VALUE, Utils.getGoogleAccount(context));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		appsArray.put(appObject);
		return appsArray;
	}
}
