package com.emob.luck.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.emob.lib.http.HTTPRequest;
import com.emob.lib.log.EmobLog;
import com.emob.lib.util.Base64;
import com.emob.lib.util.DESUtil;
import com.emob.lib.util.IdUtils;
import com.emob.lib.util.XXTea;
import com.emob.luck.AdsPreferences;
import com.emob.luck.AlarmMgrHelper;
import com.emob.luck.SdkPreferences;
import com.emob.luck.common.CommonDefine;
import com.emob.luck.common.DefaultValues;
import com.emob.luck.common.Value;
import com.emob.luck.db.EventTableDBHelper;
import com.emob.luck.protocol.app.RecentTasksHelper;

public class ProtocolManager
{
	private static final String TAG = "ProtocolManager";
	private Context mContext = null;
	public static String mServerUrl = CommonDefine.SERVER_URL;
	
	private int mRequestType;
	
	public ProtocolManager( Context context)
	{
		mContext = context;
	}

	public void start(int requestType)
	{
		mRequestType = requestType;
		requestToServer();
//		new Thread() {
//			public void run() {
//				requestToServer();
//			}
//		}.start();
	}
	
	public void startHeart() {
		mRequestType = Value.REQUEST_TYPE_HEART;
		heartToServer();
//		new Thread() {
//			public void run() {
//				heartToServer();
//			}
//		}.start();
	}
	
	public void heartToServer() {
		JSONObject request = buildRequestBody();
		
		try {
			String body = request.toString();//DESUtil.encrypt(request.toString());
			body = new String(Base64.encode(XXTea.encrypt(body.getBytes(), CommonDefine.XXTEA_KEY.getBytes())));
			String[] urls = getUrls();
			EmobLog.e("", "urls: " + urls[0] + ", "+urls[1]);
			EmobLog.e("", "body: " + body);
			String response = HTTPRequest.httpRequest(mContext, urls, body.getBytes("UTF-8"), "dontcompress");
			if (!TextUtils.isEmpty(response)) {
				response = new String(XXTea.decrypt(Base64.decode(response.toCharArray()), CommonDefine.XXTEA_KEY.getBytes()));
				EmobLog.e("response: " + response);
				parseResponse(response);
			} else {
				long nextConnectTime = System.currentTimeMillis() + DefaultValues.DEFAULT_NEXT_HEART_TIME * 1000L;
				AdsPreferences.getInstance(mContext).setLong(AdsPreferences.NEXT_HEART_TIME, nextConnectTime);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void requestToServer() {
		JSONObject request = buildRequestBody();
		try {
			String body = request.toString();
			body = new String(Base64.encode(XXTea.encrypt(body.getBytes(), CommonDefine.XXTEA_KEY.getBytes())));
			String[] urls = getUrls();
			
//			EmobLog.e("", "#### urls: " + urls[0] + ", "+urls[1]);
//			EmobLog.e("", "#### body: " + body);
			
			String response = HTTPRequest.httpRequest(mContext, urls, body.getBytes("UTF-8"), "dontcompress");
			if (!TextUtils.isEmpty(response)) {
				response = new String(XXTea.decrypt(Base64.decode(response.toCharArray()), CommonDefine.XXTEA_KEY.getBytes()));
				EmobLog.e("response: " + response);
				parseResponse(response);
			} else {
				long nextConnectTime = System.currentTimeMillis() + DefaultValues.DEFAULT_NEXT_CONNECT_TIME * 1000L;
				AdsPreferences.getInstance(mContext).setLong(AdsPreferences.NEXT_CONNECT_TIME, nextConnectTime);
				
				AdsPreferences.getInstance(mContext).setInt(AdsPreferences.CONNECT_FAILED, 1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String[] getUrls() {
		String[] urls = {CommonDefine.SERVER_URL, CommonDefine.SERVER_URL2};
		
		int backup_url_switch = AdsPreferences.getInstance(mContext).getInt(AdsPreferences.BACKUP_URL_SWITCH, 0);
		int connect_failed = AdsPreferences.getInstance(mContext).getInt(AdsPreferences.CONNECT_FAILED, 0);
		if (backup_url_switch == 1 || connect_failed == 1) {
			String bArray = AdsPreferences.getInstance(mContext).getString(AdsPreferences.BACKUP_URL, "");
			if (!TextUtils.isEmpty(bArray)) {
				String[] recents = bArray.split(",");
				if (recents != null && recents.length >0 ) {
					return recents;
				}
			}
		} 
		
		return urls;
	}
	
	private JSONObject buildRequestBody() {
		
		PacketHelper packetHelper = new PacketHelper(mContext);
		JSONObject requJsonObject = null;
		try {
			requJsonObject = packetHelper.getRequestPacket(mRequestType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return requJsonObject;
	}
	
	private void parseResponse(String response) {
		JSONObject resultJSON = null;
		try {
			resultJSON = new JSONObject(response);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    if (resultJSON == null) {
	    	return;
	    }

		int resCode = resultJSON.optInt("code");
		if (resCode == Value.SUCCESS){
			CommonDefine.AD_MASK_FLAG = false;
			AdsPreferences.getInstance(mContext).setBoolean(AdsPreferences.AD_MASK_FLAG, false);
			//服务器返回成功
			JSONObject extendObj = null;
			try {
				extendObj = resultJSON.getJSONObject("extend");
			}catch (Exception e){}
			AdsPreferences prefs = AdsPreferences.getInstance(mContext);
			// 心跳请求回应
			if (mRequestType == Value.REQUEST_TYPE_HEART) {
				int nextinteval = (extendObj == null) ? extendObj.optInt("net_haart_interval") : 0;
				if (nextinteval < 1) {
					nextinteval = DefaultValues.DEFAULT_NEXT_HEART_TIME;
				}
				prefs.setLong(AdsPreferences.NEXT_HEART_TIME, (System.currentTimeMillis() + nextinteval * 1000));
				AlarmMgrHelper.setHeartAlarm(mContext, nextinteval*1000L);
				return;
			}

			// 联网成功后，要reset。确保开关为0时，优先走本地url
			AdsPreferences.getInstance(mContext).setInt(AdsPreferences.CONNECT_FAILED, 0);
			resetStatCount();

			// 清空数据
			EventTableDBHelper.deleteAll(mContext);

			String uid = resultJSON.optString(Value.UID, "");
			if (!TextUtils.isEmpty(uid)) {
				IdUtils.setUid(mContext, uid);
			}

			String sid = resultJSON.optString(Value.SID, "");
			if (!TextUtils.isEmpty(sid)){
				IdUtils.setSid(mContext, sid);
			}

			int statsOff = resultJSON.optInt(Value.STATS_OFF, 0);
			prefs.setInt(AdsPreferences.STATS_OFF, statsOff);

			// 保存下次联网时间
			int nextinteval = (extendObj == null) ? extendObj.optInt("net_con_interval") : 0;
			if (nextinteval < 1) {
				nextinteval = DefaultValues.DEFAULT_NEXT_CONNECT_TIME;
			}
			prefs.setLong(AdsPreferences.NEXT_CONNECT_TIME, (System.currentTimeMillis() + nextinteval * 1000));
			AlarmMgrHelper.setAlram(mContext, nextinteval*1000L);
			SdkPreferences sdkPrefs = SdkPreferences.getInstance(mContext);

			JSONObject snotObj = resultJSON.optJSONObject(Value.SNOT);
			if (snotObj != null) {
				String bblist = snotObj.optString(Value.SNOT_BBLIST, "");
				if (!TextUtils.isEmpty(bblist)) {
					prefs.setString(AdsPreferences.SNOT_BBLIST, bblist);
				}
				String dsp = snotObj.optString(Value.SNOT_DSP, "");
				if (!TextUtils.isEmpty(dsp)) {
					prefs.setString(AdsPreferences.SNOT_DSP, dsp);
				}
				String gloabl = snotObj.optString(Value.SNOT_GLOABL, "");
				if (!TextUtils.isEmpty(gloabl)) {
					prefs.setString(AdsPreferences.SNOT_GLOABL, gloabl);
				}
				String topapp = snotObj.optString(Value.SNOT_TOPAPP, "");
				if (!TextUtils.isEmpty(topapp)) {
					prefs.setString(AdsPreferences.SNOT_TOPAPP, topapp);
				}
			}

			// 解析全局参数
			JSONObject gloablObject = resultJSON.optJSONObject("product");
			if (gloablObject != null) {
				int netOnOff	= (Integer) gloablObject.optInt("net_action", 1);
				int sdkLauncher = (Integer) gloablObject.optInt("launch_action", 0);
				int sdkLockOn = (Integer) gloablObject.optInt("lock_action", 1);
				int sdkTopExitOn = (Integer) gloablObject.optInt("topapp_exit_action", 1);
				int sdkTotalLimit = (Integer) gloablObject.optInt("app_count", 0);
				if (sdkTotalLimit < 1) {
					sdkTotalLimit = DefaultValues.SDK_SPOT_TOTAL_LIMIT;
				}
				int sdkShowInterval = (Integer) gloablObject.optInt("app_interval", 0);
				if (sdkShowInterval < 1) {
					sdkShowInterval = DefaultValues.GLOABL_SDK_SPOT_SHOW_INTERVAL;
				}

				int channel = -1; // gloabl

				sdkPrefs.setInt(channel, SdkPreferences.SDK_SPOT_NETWORK_ONOFF, netOnOff);
				sdkPrefs.setInt(channel, SdkPreferences.SDK_SPOT_LAUNCHER, sdkLauncher);
				sdkPrefs.setInt(channel, SdkPreferences.SDK_SPOT_LOCK_ONOFF, sdkLockOn);
				sdkPrefs.setInt(channel, SdkPreferences.SDK_SPOT_TOP_EXIT_ONOFF, sdkTopExitOn);
				sdkPrefs.setInt(channel, SdkPreferences.SDK_SPOT_TOTAL_COUNT, sdkTotalLimit);
				sdkPrefs.setInt(channel, SdkPreferences.SDK_SPOT_SHOW_INTERVAL, sdkShowInterval);
				Log.e("#### ProtocolManager", "gloabl " + netOnOff + "," + sdkLauncher + "," + sdkLockOn + "," +sdkTopExitOn + "," + sdkTotalLimit + "," + sdkShowInterval);
			}

			JSONArray dspArray = resultJSON.optJSONArray("site");
			if (dspArray != null) {
				for (int i=0; i< dspArray.length(); i++) {
					JSONObject object = dspArray.optJSONObject(i);
					if (object == null) {
						continue;
					}
					if (object.optInt("status") == 0){
						continue;
					}
					String sdkName = object.optString("sdk_name");
					int channel = CommonDefine.DSP_GLOABL;
					if (sdkName.equals("admob")){
						channel = CommonDefine.DSP_CHANNEL_ADMOB;
						CommonDefine.SDK_KEY_ADMOB = object.optString("site");
					}else if (sdkName.equals("facebook")){
						channel = CommonDefine.DSP_CHANNEL_FACEBOOK;
						CommonDefine.SDK_KEY_FACEBOOK = object.optString("site");
					}else if (sdkName.equals("cm")){
						channel = CommonDefine.DSP_CHANNEL_CM;
						CommonDefine.SDK_KEY_CM = object.optString("site");
					}
					int onoff = object.optInt("lock_action");
					int onoff2 = object.optInt("topapp_exit_action");
					int randomInt = channel;//(Integer) object.optInt(Value.DSP_ID_INT);
					int dspTotalLimt = object.optInt("app_count");
					int dspShowInterval = object.optInt("app_interval");
					int tries_num = object.optInt("tries_num");
					int reset_day_num = object.optInt("reset_day_num");

					sdkPrefs.setInt(channel, SdkPreferences.SDK_SPOT_LOCK_ONOFF, onoff);
					sdkPrefs.setInt(channel, SdkPreferences.SDK_SPOT_TOP_EXIT_ONOFF, onoff2);
					sdkPrefs.setInt(channel, SdkPreferences.SDK_SPOT_TOTAL_COUNT, dspTotalLimt);
					sdkPrefs.setInt(channel, SdkPreferences.SDK_SPOT_SHOW_INTERVAL, dspShowInterval);
					sdkPrefs.setInt(channel, SdkPreferences.SDK_SITE_TRIES_NUM, tries_num);
					sdkPrefs.setInt(channel, SdkPreferences.SDK_SITE_RESET_DAY_NUM, reset_day_num);
					Log.e("#### ProtocolManager", "site " + channel + "," + onoff + "," + onoff2 + "," + dspTotalLimt + "," +
														dspShowInterval + "," + randomInt + "," + tries_num + "," + reset_day_num);
				}
			}

			int backup_url_switch = resultJSON.optInt(Value.BACKUP_URL_SWITCH, 0);
			prefs.setInt(AdsPreferences.BACKUP_URL_SWITCH, backup_url_switch);

			JSONArray bArray = resultJSON.optJSONArray(Value.BACKUP_URLS);
			if (bArray != null) {
				String bblistString = "";
				for (int i=0; i< bArray.length(); i++) {

					String str = bArray.optString(i);;
					if (!TextUtils.isEmpty(str)) {
						bblistString += str + ",";
					}
				}
				if (!TextUtils.isEmpty(bblistString)) {
					AdsPreferences.getInstance(mContext).setString(AdsPreferences.BACKUP_URL, bblistString);
				}
			}

			//黑名单
			JSONArray pkgArray = resultJSON.optJSONArray("blackList");
			if (pkgArray != null) {
				String bblistString = "";
				for (int i=0; i< pkgArray.length(); i++) {
					try {
						JSONObject bl = pkgArray.getJSONObject(i);
						String pkgname = bl.optString("pkg");
						bblistString += pkgname + ", ";
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				AdsPreferences.getInstance(mContext).setString(AdsPreferences.BB_LIST_STRING, bblistString);
			}
			//白名单
			JSONObject whiteApps = resultJSON.optJSONObject("whiteList");
			if (whiteApps != null) {
				int action = (Integer) whiteApps.optInt("action");
				prefs.setInt(AdsPreferences.TOPAPP_ACTION, action);
				EmobLog.e("HTTP", "action is " + action);
				JSONArray whiteArr = whiteApps.optJSONArray("apps");
				if (whiteArr != null) {
					String recentApp = RecentTasksHelper.getRecentAppString(mContext);
					EmobLog.d("HTTP", "recentApp: " + recentApp);
					if (recentApp == null) {
						recentApp = "";
					}
					for (int i=0; i< whiteArr.length(); i++) {
						JSONObject object = whiteArr.optJSONObject(i);
						if (object != null) {
							String pkgname = object.optString("pkg");
							EmobLog.d("HTTP", "pkgname: " + pkgname);
							if (!TextUtils.isEmpty(pkgname)) {
								if (!recentApp.contains(pkgname)) {
									recentApp += pkgname + ", ";
									EmobLog.d("HTTP", "added recentApp: " + recentApp);
								}
							}
						}
					}
					EmobLog.d("HTTP", "neorecentApp: " + recentApp);
					RecentTasksHelper.setRecentAppString(mContext, recentApp);
				}else{
					EmobLog.d("HTTP", "apps is null!");
				}
			}else{
				EmobLog.d("HTTP", "whiteList is null!");
			}
		}else if(resCode == Value.DEVICE_BE_MASK ||
				resCode == Value.CHANNEL_BE_MASK){
			CommonDefine.AD_MASK_FLAG = true;
			AdsPreferences.getInstance(mContext).setBoolean(AdsPreferences.AD_MASK_FLAG, true);
			Log.e(TAG, "be mask " + resCode);
			return;
		}else{
			Log.e(TAG, "server return error! [" + resCode + "]");
		}
	}
	
	private void resetStatCount() {
		AdsPreferences.getInstance(mContext).setInt(AdsPreferences.LOCK_COUNT, 0);
		AdsPreferences.getInstance(mContext).setInt(AdsPreferences.UNLOCK_COUNT, 0);
		AdsPreferences.getInstance(mContext).setInt(AdsPreferences.NET_ON_COUNT, 0);
		AdsPreferences.getInstance(mContext).setInt(AdsPreferences.NET_OFF_COUNT, 0);
	}
}

