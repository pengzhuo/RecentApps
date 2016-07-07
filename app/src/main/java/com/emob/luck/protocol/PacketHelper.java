package com.emob.luck.protocol;

import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.emob.lib.log.EmobLog;
import com.emob.lib.util.DevicesUtils;
import com.emob.lib.util.IdUtils;
import com.emob.lib.util.SysHelper;
import com.emob.lib.util.Utils;
import com.emob.luck.AdsPreferences;
import com.emob.luck.common.CommonDefine;
import com.emob.luck.common.Value;
import com.emob.luck.db.EventTableDBHelper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class PacketHelper {
	private Context mContext;

	public PacketHelper(Context context) {
		mContext = context;
	}

	public JSONObject getRequestPacket(int requestType) throws JSONException {
		JSONObject requestObject = new JSONObject();
		
		try {
			requestObject.put(Value.PROTOCOL_TYPE, ProductHelper.getProtocol());
		} catch (Exception e1) {
		}
		
		try {
			requestObject.put(Value.REQUEST_TYPE, requestType);
		} catch (Exception e1) {
		}
		
		try {
			requestObject.put(Value.App_VERSION,  CommonDefine.APP_VERSION);
		} catch (Exception e1) {
		}
		
		try {
			requestObject.put(Value.APP_OPERATION_ID,  ProductHelper.getOid());
		} catch (Exception e1) {
		}
		
		try {
			requestObject.put(Value.APP_PRODUCT_ID,  ProductHelper.getProductId());
		} catch (Exception e1) {
		}
		
		try {
			requestObject.put(Value.APP_CHANNEL_ID,  ProductHelper.getChannelId());
		} catch (Exception e1) {
		}
		
		try {
			String extend = AdsPreferences.getInstance(mContext).getString(AdsPreferences.PREFS_KEY_EXTEND, "");
			requestObject.put(Value.EXTEND, extend);
		} catch (Exception e1) {
		}
		
		if (requestType == Value.REQUEST_TYPE_HEART) {
			JSONObject heartObj = getHeartsInfo();
			requestObject.put("heart", heartObj);
			/*附带用户已安装应用的信息*/
			try{
				requestObject.put(Value.INSTALL_APPS, getInstallAppsInfo().toString());
			}catch(Exception e){
				EmobLog.d("PacketHelper", e.toString());
			}
			EmobLog.e("RequestData: " + requestObject.toString());
			return requestObject;
		}

		try {
			String barray = AdsPreferences.getInstance(mContext).getString(AdsPreferences.BACKUP_URL, "");
			int baflag = 0;
			if (!TextUtils.isEmpty(barray)) {
				baflag = 1;
			}
			requestObject.put(Value.BACKUP_URL_FLAG, baflag);
		} catch (Exception e1) {
		}
		
		int serviceRestart = AdsPreferences.getInstance(mContext).getInt(AdsPreferences.SERVICE_RESTART, 0);
		AdsPreferences.getInstance(mContext).setInt(AdsPreferences.SERVICE_RESTART, 0);
		try {
			requestObject.put(Value.SERVICE_RESTART, serviceRestart);
		} catch (Exception e1) {
		}
		
		try {
			String lAddr = CommonDefine.SERVER_URL + "," + CommonDefine.SERVER_URL2;
			String encode = URLEncoder.encode(lAddr, "UTF-8");
			requestObject.put(Value.LOCAL_ADDRESS, encode);
		} catch (Exception e1) {
		}
		
		try {
			if(DevicesUtils.isInstalledGpInSystem(mContext)) {
				requestObject.put(Value.ACCOUNTS, getAccountsInfo());
			}
		} catch (Exception e) {
		}
		// 必须放在generateGid之前，否则就是新gid了
		if (requestType == Value.REQUEST_TYPE_LUCK) {
			try {
				int statsOff = AdsPreferences.getInstance(mContext).getInt(AdsPreferences.STATS_OFF, 0);
				if (statsOff <=0 ) {
					JSONArray eventArray = EventTableDBHelper.getEvents(mContext);
					if (eventArray != null) {
						String gid = IdUtils.getGid(mContext);
						JSONObject resultObject = new JSONObject();
						resultObject.put(Value.GID, gid);
						resultObject.put(Value.ITEMS, eventArray);
						requestObject.put(Value.RESULTS, resultObject);
					} 
				}
			} catch (Exception e) {
			}
		} 
		
		try {
			requestObject.put(Value.IDS, getIdsInfo(requestType));
		} catch (Exception e) {
		}
		
		try {
			requestObject.put(Value.SNOT, getSnotInfo());
		} catch (Exception e) {
		}
		
		try {
			
		} catch (Exception e) {
			
		}
		
		try {
			requestObject.put(Value.DEVICE, getDeviceInfo());
//			requestObject.put("dev", getDeviceInfo());
//			requestObject.put("dev1", getDeviceInfo());
//			requestObject.put("dev2", getDeviceInfo());
//			requestObject.put("dev3", getDeviceInfo());
//			requestObject.put("dev4", getDeviceInfo());
//			requestObject.put("dev5", getDeviceInfo());
//			requestObject.put("dev6", getDeviceInfo());
//			requestObject.put("dev7", getDeviceInfo());
//			requestObject.put("dev8", getDeviceInfo());
//			requestObject.put("dev9", getDeviceInfo());
//			requestObject.put("dev10", getDeviceInfo());
//			requestObject.put("dev11", getDeviceInfo());
//			requestObject.put("dev12", getDeviceInfo());
//			requestObject.put("dev13", getDeviceInfo());
//			requestObject.put("dev14", getDeviceInfo());
//			requestObject.put("dev15", getDeviceInfo());
//			requestObject.put("dev16", getDeviceInfo());
//			requestObject.put("dev17", getDeviceInfo());
//			requestObject.put("dev18", getDeviceInfo());
//			requestObject.put("dev19", getDeviceInfo());
		} catch (Exception e) {
		}
		
		try {
			requestObject.put(Value.DEVICESTATUS, getDeviceStatusInfo());
		} catch (Exception e) {
		}

		JSONObject netObj = getNetInfo();
		try {
			requestObject.put(Value.NET, netObj);
		} catch (Exception e) {
		}

		//拼装基本信息
		JSONObject baseInfo = new JSONObject();
		baseInfo.put("imei", DevicesUtils.getIMEI(mContext));
		baseInfo.put("cid", ProductHelper.getChannelId());
		baseInfo.put("version", CommonDefine.APP_VERSION);
		baseInfo.put("model", Build.MODEL);
		baseInfo.put("pid", ProductHelper.getProductId());
		baseInfo.put("area", Locale.getDefault().getCountry());
		try{
			requestObject.put("base", baseInfo);
		}catch (Exception e){
		}
		EmobLog.e("RequestData: " + requestObject.toString());
		return requestObject;
	}
	
	@SuppressLint("NewApi")
	public JSONObject getIdsInfo(int reqType) throws JSONException {
		JSONObject idsObject = new JSONObject();
		
		String uid = IdUtils.getUid(mContext);
		idsObject.put(Value.UID, uid);
		String gid = IdUtils.generateGid(mContext);
		idsObject.put(Value.GID, gid);
		idsObject.put(Value.SID, gid);
		String aid = Settings.Secure.getString(mContext.getContentResolver(),Settings.Secure.ANDROID_ID );
		idsObject.put(Value.AID, aid == null ? "" : aid);
		idsObject.put(Value.IID, IdUtils.getIid(mContext));
		if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD ) {
			idsObject.put(Value.DID, Build.SERIAL);
		} else {
			idsObject.put(Value.DID, "");
		}
		
		return idsObject;
	}

	public JSONObject getDeviceInfo() throws JSONException {
		JSONObject deviceObject = new JSONObject();
		deviceObject.put(Value.MODEL, Build.MODEL);
		deviceObject.put(Value.HARDWARE, Build.HARDWARE);
		deviceObject.put(Value.IMEI, DevicesUtils.getIMEI(mContext));
		deviceObject.put(Value.ABI, Build.CPU_ABI);
		deviceObject.put(Value.DISPLAY, Build.DISPLAY);
		deviceObject.put(Value.MAC, DevicesUtils.getWiFiMacAddr(mContext));
		deviceObject.put(Value.PRODUCT, Build.PRODUCT);
		deviceObject.put(Value.DEVICE, Build.DEVICE);
		deviceObject.put(Value.BTMAC, "");
		deviceObject.put(Value.UA, DevicesUtils.getUserAgent(mContext));
		deviceObject.put(Value.API, Build.VERSION.SDK_INT);
		deviceObject.put(Value.IMSI, DevicesUtils.getIMSI(mContext));
		deviceObject.put(Value.FGP, Build.FINGERPRINT);
		deviceObject.put(Value.ADVID, DevicesUtils.getAdvertiseId(mContext));
		deviceObject.put(Value.RELEASE, Build.VERSION.RELEASE);

		JSONObject screenObject = getScreenInfo();
		deviceObject.put(Value.SCREEN, screenObject);

		deviceObject.put(Value.LOCAL, DevicesUtils.getSimCountry(mContext));
		deviceObject.put(Value.LANGUAGE, DevicesUtils.getLanguage(mContext));
		deviceObject.put(Value.TIMEZONE, Utils.getCurrentTimezoneOffset());
		return deviceObject;
	}

	public JSONObject getDeviceStatusInfo() throws JSONException {
		JSONObject deviceStatusObject = new JSONObject();
		
		deviceStatusObject.put(Value.ORIENTATION, DevicesUtils.getScreenOrientation(mContext));
		deviceStatusObject.put(Value.ISLOCK, SysHelper.isLockScreen(mContext));
		deviceStatusObject.put(Value.SU, DevicesUtils.isRootSystem());

		return deviceStatusObject;
	}

	public JSONObject getNetInfo() throws JSONException {
		JSONObject netObject = new JSONObject();

		JSONObject networkObject = getNetworkInfo();
		netObject.put(Value.NETWORK, networkObject);

		/*因为网络状态不好抛错，暂时屏敝*/
//		JSONObject ipInfoObject = getIpInfo();
//		netObject.put(Value.IPINFO, ipInfoObject);

		return netObject;
	}

	public JSONArray getAccountsInfo() {
		JSONArray appsArray = AccountsListHelper.getAccountsList(mContext);
		return appsArray;
	}
	
//	public JSONArray getAppsInfo() throws JSONException {
//		JSONArray appsArray = AppListHelper.getAppList(mContext);
//		return appsArray;
//	}

	private JSONObject getNetworkInfo() throws JSONException {
		JSONObject screenObject = new JSONObject();

		String type = "Unknown";
		String subtype = "Unknown";
		String apn = "Unknown";
		ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = connMgr.getActiveNetworkInfo();
		if (ni != null) {
			type = "" + ni.getTypeName();
			subtype = "" + ni.getSubtypeName();
			apn = "" + ni.getExtraInfo();
		} 
		
		screenObject.put(Value.APN, apn);
		screenObject.put(Value.TYPE, type);
		screenObject.put(Value.SUBTYPE, subtype);

		return screenObject;
	}

	private JSONObject getIpInfo() throws JSONException {
		JSONObject ipInfoObject = IpInfoHelper.buildIpInfo(mContext);
		return ipInfoObject;
	}

	private JSONObject getScreenInfo() throws JSONException {
		JSONObject screenObject = new JSONObject();

		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();

		screenObject.put(Value.HEIGHTPIXELS, metrics.heightPixels);
		screenObject.put(Value.WIDTHPIXELS, metrics.widthPixels);
		screenObject.put(Value.DENISITY, metrics.density);
		screenObject.put(Value.SCALEDDENSITY, metrics.scaledDensity);
		screenObject.put(Value.DENSITYDPI, metrics.densityDpi);
		screenObject.put(Value.XDIP, metrics.xdpi);
		screenObject.put(Value.YDIP, metrics.ydpi);

		return screenObject;
	}
	
	private JSONObject getSnotInfo() throws JSONException {
		JSONObject snotObject = new JSONObject();
		
		String snotBBList = AdsPreferences.getInstance(mContext).getString(AdsPreferences.SNOT_BBLIST, "");
		String snotDsp = AdsPreferences.getInstance(mContext).getString(AdsPreferences.SNOT_DSP, "");
		String snotGloabl = AdsPreferences.getInstance(mContext).getString(AdsPreferences.SNOT_GLOABL, "");
		String snotTopApp = AdsPreferences.getInstance(mContext).getString(AdsPreferences.SNOT_TOPAPP, "");
		
		snotObject.put(Value.SNOT_BBLIST, snotBBList);
		snotObject.put(Value.SNOT_DSP, snotDsp);
		snotObject.put(Value.SNOT_GLOABL, snotGloabl);
		snotObject.put(Value.SNOT_TOPAPP, snotTopApp);
		
		return snotObject;
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public JSONObject getHeartsInfo() {
		JSONObject idsObject = new JSONObject();
		
		try {
			String uid = IdUtils.getUid(mContext);
			idsObject.put(Value.UID, uid);
			String gid = SysHelper.getHashCode("MD5", SysHelper.getRandomData(32));
			idsObject.put(Value.GID, gid);
			idsObject.put(Value.SID, gid);
		} catch (Exception e) {
		}
		
		try {
			String aid = Settings.Secure.getString(mContext.getContentResolver(),Settings.Secure.ANDROID_ID );
			idsObject.put(Value.AID, aid == null ? "" : aid);
			idsObject.put(Value.IID, IdUtils.getIid(mContext));
			if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD ) {
				idsObject.put(Value.DID, Build.SERIAL);
			} else {
				idsObject.put(Value.DID, "");
			}
		} catch (Exception e) {
		}
		
		try {
			idsObject.put(Value.IMEI, DevicesUtils.getIMEI(mContext));
			idsObject.put(Value.IMSI, DevicesUtils.getIMSI(mContext));
		} catch (Exception e) {
		}
		
		try {
			idsObject.put(Value.MODEL, Build.MODEL);
			idsObject.put(Value.MAC, DevicesUtils.getWiFiMacAddr(mContext));
			idsObject.put(Value.API, Build.VERSION.SDK_INT);
			idsObject.put(Value.LANGUAGE, DevicesUtils.getLanguage(mContext));
			idsObject.put(Value.LOCAL, DevicesUtils.getSimCountry(mContext));
			
			idsObject.put(Value.HARDWARE, Build.HARDWARE);
			idsObject.put(Value.ABI, Build.CPU_ABI);
			idsObject.put(Value.DISPLAY, Build.DISPLAY);
			idsObject.put(Value.PRODUCT, Build.PRODUCT);
			idsObject.put(Value.DEVICE, Build.DEVICE);
			idsObject.put(Value.BTMAC, "");
			idsObject.put(Value.ADVID, DevicesUtils.getAdvertiseId(mContext));
			idsObject.put(Value.RELEASE, Build.VERSION.RELEASE);
		} catch (Exception e) {
		}
		
		return idsObject;
	}
	
	public JSONObject getInstallAppsInfo(){
		JSONObject jsonObj = new JSONObject();
		List<PackageInfo> list = DevicesUtils.getAppsOnPhone(mContext, false);
		try {
			for (PackageInfo packageInfo : list) {
				JSONObject json = new JSONObject();
				json.put("appName", packageInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString());
				json.put("packageName", packageInfo.packageName);
				json.put("versionCode", packageInfo.versionCode);
				json.put("versionName", packageInfo.versionName);
//				json.put("icon", packageInfo.applicationInfo.loadIcon(mContext.getPackageManager()));
				jsonObj.put(packageInfo.packageName, json);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}
}
