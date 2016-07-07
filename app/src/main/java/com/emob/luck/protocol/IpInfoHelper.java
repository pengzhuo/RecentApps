package com.emob.luck.protocol;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.emob.lib.util.StrUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.text.format.Time;

public class IpInfoHelper {
	public static final String PREFS_KEY_TAOBAO = "0001";
	public static final String PREFS_KEY_SINA = "0002";
	public static final String PREFS_KEY_TELIZE = "0003";
	public static final String PREFS_KEY_WEEK = "week";
	
	public static final String TAOBAO = "tb";
	public static final String SINA = "sa";
	public static final String TELIZE = "ipi";
	
	//proguard StringUtils 里面的东西会自动处理
	private static final String URL_TAOBAO = StrUtils.deCrypt("http://ip.taobao.com/service/getIpInfo2.php?ip=myip");
	//proguard StringUtils 里面的东西会自动处理
	private static final String URL_SINA = StrUtils.deCrypt("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=");
	//proguard StringUtils 里面的东西会自动处理
	private static final String URL_TELIZE = StrUtils.deCrypt("http://ipinfo.io/json");
//	private static final String URL_IP138 = "http://1111.ip138.com/ic.asp";

	public static JSONObject buildIpInfo(Context context) {
		
		JSONObject ipAddr = null;
		try {
			ipAddr = new JSONObject();
			String taobao = null;
			String sina = null;
			String tlz = null;
			boolean need = true;
			SharedPreferences prefs = context.getSharedPreferences("ip_info", 0);				
			int week = prefs.getInt(PREFS_KEY_WEEK, 0);
			Time time = new Time();
			time.setToNow();
			int weekNow = time.getWeekNumber();
			if(weekNow == week)
			{
				taobao = prefs.getString(PREFS_KEY_TAOBAO, "");
				sina = prefs.getString(PREFS_KEY_SINA, "");
				tlz = prefs.getString(PREFS_KEY_TELIZE, "");
				
				if(taobao == null || taobao.equals("") ||
						sina == null || sina.equals("") ||
						tlz == null || tlz.equals(""))
				{
					need = true;
				}
				else{
					need = false;
				}
	
			}
			
			if(need)
			{
				try {				
					taobao = getIpByTaobao(context);
				} catch (Exception e) {
					taobao = "";
					setPreference(context, PREFS_KEY_TAOBAO, taobao);
				}
				try {				
					sina = getIpBySina(context);
				} catch (Exception e) {
					sina = "";
					setPreference(context, PREFS_KEY_SINA, sina);
				}
				try {				
					tlz = getIpByTelize(context);
				} catch (Exception e) {
					tlz = "";
					setPreference(context, PREFS_KEY_TELIZE, tlz);
				}
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt(PREFS_KEY_WEEK, weekNow);
				editor.commit();
			}
			
			try {
				
				JSONObject tbObj = new JSONObject(taobao);
				ipAddr.put(TAOBAO, tbObj);
			} catch (Exception e) {
				
			}
			try {
				JSONObject saObj = new JSONObject(sina);
				ipAddr.put(SINA, saObj);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				JSONObject tlzObj = new JSONObject(tlz);
				ipAddr.put(TELIZE, tlzObj);
			} catch (Exception e) {
				e.printStackTrace();
			}			
		} catch (Exception e) {
//			e.printStackTrace();
		}
		
		
		return ipAddr;
	}

	private static String getIpByTaobao(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("ip_info", 0);
		String taobao = prefs.getString(PREFS_KEY_TAOBAO, "");
		if (TextUtils.isEmpty(taobao)) {
			if (isNetworkEnabled(context)) {
				taobao = doHttpGet(URL_TAOBAO);
				if (!TextUtils.isEmpty(taobao)) {
					setPreference(context, PREFS_KEY_TAOBAO, taobao);
				}
			}
		}
		
		return taobao;
	}
	
	private static String getIpBySina(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("ip_info", 0);
		String sina = prefs.getString(PREFS_KEY_SINA, "");
		if (TextUtils.isEmpty(sina)) {
			if (isNetworkEnabled(context)) {
				sina = doHttpGet(URL_SINA);
				if (!TextUtils.isEmpty(sina)) {
					setPreference(context, PREFS_KEY_SINA, sina);
				}
			}
		}
		return sina;
	}
	

	private static String getIpByTelize(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("ip_info", 0);
		String ip138 = prefs.getString(PREFS_KEY_TELIZE, "");
		if (TextUtils.isEmpty(ip138)) {
			if (isNetworkEnabled(context)) {
				ip138 = doHttpGet(URL_TELIZE);
				if (!TextUtils.isEmpty(ip138)) {
					setPreference(context, PREFS_KEY_TELIZE, ip138);
				}
			}
		}
		return ip138;
	}
	
	public static String doHttpGet(String url) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		String response = null;
		try {
			ArrayList<NameValuePair> headerList = new ArrayList<NameValuePair>();  
            headerList.add(new BasicNameValuePair("Content-Type", "text/html; charset=GBK")); 
			HttpGet httpGet = new HttpGet(url);
			for (int i = 0; i < headerList.size(); i++) {  
				httpGet.addHeader(headerList.get(i).getName(), 
						headerList.get(i).getValue());  
            }  
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				response = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			
		}
		return response;
	}
	
	private static void setPreference(Context context, String key, String value) {
		if (TextUtils.isEmpty(key)) {
			return;
		}
		
		SharedPreferences prefs = context.getSharedPreferences("ip_info", 0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	private static boolean isNetworkEnabled( Context context )
	{
		ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService( Context.CONNECTIVITY_SERVICE );
		
		return ( connMgr.getActiveNetworkInfo() != null );
	}
}
