package com.emob.lib.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.emob.lib.common.LibValues;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.webkit.WebView;

public class DevicesUtils {

	private static final String PREFS_FILE_NAME = StrUtils.deCrypt("device_info");

	public static final String PREFS_KEY_UA = StrUtils.deCrypt("ua");
	public static final String PREFS_KEY_ADVERTISE_ID = StrUtils.deCrypt("adv_id");

	public static String getDeviceId(Context context) {
		TelephonyManager telMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telMgr.getDeviceId();

		return imei == null ? "" : imei;
	}

	public static String getIMSI(Context context) {
		TelephonyManager telMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		String imsi = telMgr.getSubscriberId();

		return imsi == null ? "" : imsi;
	}

	public static int getScreenOrientation(Context context) {
		return context.getResources().getConfiguration().orientation;
	}

	public static String getOsVersion() {
		if (DevicesUtils.enableDebugData) {
			String addr = LocalTestData.getInstance().getElementData(
					LocalTestData.typeOsVersion);
			if (!TextUtils.isEmpty(addr)) {
				return addr;
			}
		}
		return Build.VERSION.RELEASE;
	}

	public static String getLocalMacAddress(Context context) {
		if (DevicesUtils.enableDebugData) {
			String addr = LocalTestData.getInstance().getElementData(
					LocalTestData.typeWifiMacAddr);
			if (!TextUtils.isEmpty(addr)) {
				return addr.replace(":", "");
			}
		}

		String mac = "";
		try {
			WifiManager wifi = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			info.getMacAddress().replace(":", "");
		} catch (Exception e) {
			mac = "";
		}

		return mac;
	}

	/**
	 * 得到手机的IMEI号，需要context参数,获取不到时，返回“000000000000000”
	 */
	public static String getIMEI(Context context) {
		String imei = "";
		try {
			if (DevicesUtils.enableDebugData) {
				imei = LocalTestData.getInstance().getElementData(
						LocalTestData.typeImei);
				if (!TextUtils.isEmpty(imei)) {
					return imei;
				}
			}
			if (context != null) {
				TelephonyManager mTelephonyMgr = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				imei = mTelephonyMgr.getDeviceId();
			}
		} catch (Exception e) {
		}

		return TextUtils.isEmpty(imei) ? "000000000000000" : imei;
	}

	/**
	 * 得到androidID
	 * 
	 * @param context
	 * @return the corresponding value, or "" if not present
	 */
	public static String getAndroidId(Context context) {
		String androidid = "";
		if (DevicesUtils.enableDebugData) {
			androidid = LocalTestData.getInstance().getElementData(
					LocalTestData.typeAndroidId);
			if (!TextUtils.isEmpty(androidid)) {
				return androidid;
			}
		}
		if (context != null) {
			try {
				androidid = Settings.Secure.getString(
						context.getContentResolver(),
						Settings.Secure.ANDROID_ID);
			} catch (Exception e) {
				androidid = "";
			}
		}

		return TextUtils.isEmpty(androidid) ? "" : androidid;
	}

	/**
	 * 是否在模拟器上运行
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isEmulator(Context context) {
		String android_id = getAndroidId(context);
		boolean emulator = TextUtils.isEmpty(android_id)
				|| "google_sdk".equals(Build.PRODUCT)
				|| "sdk".equals(Build.PRODUCT);
		return emulator;
	}

	/**
	 * 得到手机上面当前默认使用的接入点名称(4.0以下用，4.0以上GOOGLE关闭了读写APN权限)
	 * 
	 * @param context
	 * @return
	 */
	private static final int ANDROID_SDK_14 = 14;

	public static String getCurrentApnName(Context context) {
		String name = "Unknown";
		if (android.os.Build.VERSION.SDK_INT < ANDROID_SDK_14) {
			Cursor cursor = null;
			try {
				cursor = context.getContentResolver().query(
						Uri.parse("content://telephony/carriers/preferapn"),
						null, null, null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (cursor != null) {
				if (cursor.moveToNext()) {
					name = cursor.getString(cursor.getColumnIndex("name"));
				}
				cursor.close();
			}
		} else {
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = manager.getActiveNetworkInfo();
			if (info != null) {
				if (ConnectivityManager.TYPE_WIFI == info.getType()) {
					name = info.getTypeName();
					if (name == null) {
						name = "wifi";
					}
				} else {
					name = info.getExtraInfo();
					if (name == null) {
						name = "mobile";
					}
				}
			}
		}
		return name;
	}

	/**
	 * 获取程序 图标
	 */
	public static Drawable getAppIcon(Context context, String packname) {
		try {
			PackageManager pm = context.getPackageManager();
			ApplicationInfo info = pm.getApplicationInfo(packname, 0);
			return info.loadIcon(pm);
		} catch (NameNotFoundException e) {
			e.printStackTrace();

		}
		return null;
	}

	/**
	 * 通过包名获取应用程序的名称。
	 * 
	 * @param context
	 *            Context对象。
	 * @param packageName
	 *            包名。
	 * @return 返回包名所对应的应用程序的名称。
	 */
	public static String getProgramNameByPackageName(Context context,
			String packageName) {
		PackageManager pm = context.getPackageManager();
		String name = null;
		try {
			name = pm.getApplicationLabel(
					pm.getApplicationInfo(packageName,
							PackageManager.GET_META_DATA)).toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 根据包名，在手机上查找packageInfo
	 * 
	 * @param context
	 * @param packageName
	 * @param systemAppInclusive
	 * @return null或者packageinfo
	 */
	public static PackageInfo getPackageInfoOnPhone(Context context,
			String packageName, boolean systemAppInclusive) {
		List<PackageInfo> packageInfos = getAppsOnPhone(context,
				systemAppInclusive);
		for (PackageInfo packageInfo : packageInfos) {
			if (packageInfo != null) {
				if (TextUtils.equals(packageInfo.packageName, packageName)) {
					return packageInfo;
				}
			}
		}
		return null;
	}

	/**
	 * 根据包名，在手机上查找packageInfo
	 * 
	 * @param context
	 * @param packageName
	 * @param systemAppInclusive
	 * @return null或者packageinfo
	 */
	public static PackageInfo getPackageInfoOnPhone(Context context,
			String packageName, List<PackageInfo> packageInfos) {
		for (PackageInfo packageInfo : packageInfos) {
			if (packageInfo != null) {
				if (TextUtils.equals(packageInfo.packageName, packageName)) {
					return packageInfo;
				}
			}
		}
		return null;
	}

	/**
	 * 得到手机上安装应用的信息
	 * 
	 * @param context
	 * @param systemAppInclusive
	 *            是否包含系统内置应用
	 * @return
	 */
	public static List<PackageInfo> getAppsOnPhone(Context context,
			boolean systemAppInclusive) {

		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		// 获取手机内所有应用
		for (PackageInfo pak : safeGetInstallPackages(context)) {
			// 判断是否为非系统预装的应用程序
			// 这里还可以添加系统自带的，这里就先不添加了，如果有需要可以自己添加
			// if()里的值如果<=0则为自己装的程序，否则为系统工程自带
			if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				// 添加自己已经安装的应用程序
				apps.add(pak);
			} else if (systemAppInclusive) {
				apps.add(pak);
			}

		}
		return apps;
	}

	public static List<PackageInfo> mPkgInfos = null;

	public static List<PackageInfo> safeGetInstallPackages(Context context) {
		if (mPkgInfos == null) {
			try {
				mPkgInfos = context.getPackageManager().getInstalledPackages(0);
			} catch (Exception e) {
				mPkgInfos = new ArrayList<PackageInfo>();
			}
		}

		return mPkgInfos;
	}

	/**
	 * 判断软件是否已安装
	 * 
	 * @param cnt
	 * @param pkgName
	 * @return
	 */
	public static boolean isPackageAlreadyInstalled(Context cnt, String pkgName) {
		List<PackageInfo> installedList = safeGetInstallPackages(cnt);
		int installedListSize = installedList.size();
		for (int i = 0; i < installedListSize; i++) {
			PackageInfo tmp = installedList.get(i);
			if (pkgName.equalsIgnoreCase(tmp.packageName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否安装了gp
	 * 
	 * @return
	 */
	public static boolean isInstalledGpInSystem(Context context) {
		return isPackageAlreadyInstalled(context, "com.android.vending");
	}

	/**
	 * 判断应用程序是否安装
	 * 
	 * @param context
	 * @param packagename
	 * @return
	 */
	public static boolean isApkAvailable(Context context, String packagename) {
		if (context == null) {
			return false;
		}
		PackageInfo packageInfo;

		try {
			packageInfo = context.getPackageManager().getPackageInfo(
					packagename, 0);
		} catch (Exception e) {
			packageInfo = null;
		}
		return (packageInfo != null);
	}

	/**
	 * 打开GP
	 * 
	 * @param context
	 * @param url
	 */
	public static void openGP(Context context, String url) {
		openBrowser(context, url);
	}

	/**
	 * 打开系统浏览器
	 * 
	 * @param context
	 * @param url
	 */
	public static void openBrowser(Context context, String url) {
		if (TextUtils.isEmpty(url) || context == null) {
			return;
		}
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 启动指定应用
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void runApps(Context context, String packageName) {
		if (null == packageName || packageName.length() < 1) {
			return;
		}

		PackageManager packageManager = context.getPackageManager();
		Intent intent = packageManager.getLaunchIntentForPackage(packageName);
		context.startActivity(intent);
	}

	/**
	 * 获取SIM卡所在的国家
	 * 
	 * @param context
	 * @return 当前手机sim卡所在的国家，如果没有sim卡，取本地语言代表的国家
	 */
	public static String getSimCountry(Context context) {
		String ret = null;
		Locale locale = Locale.getDefault();
		try {
			TelephonyManager telManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			ret = telManager.getSimCountryIso();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if (TextUtils.isEmpty(ret)) {
			ret = locale.getCountry().toLowerCase();
		}
		return null == ret ? "error" : ret;
	}

	/**
	 * 获取语言和国家地区的方法 格式: SIM卡方式： cn 系统语言方式： zh-CN
	 * 
	 * @return
	 */
	public static String getLanguage(Context context) {
		String ret = null;
		Locale locale = Locale.getDefault();
		try {
			TelephonyManager telManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			ret = telManager.getSimCountryIso();
			if (!TextUtils.isEmpty(ret)) {
				ret = String.format("%s_%s",
						locale.getLanguage().toLowerCase(), ret.toLowerCase());
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if (TextUtils.isEmpty(ret)) {
			ret = String.format("%s_%s", locale.getLanguage().toLowerCase(),
					locale.getCountry().toLowerCase());
		}
		return null == ret ? "error" : ret;
	}

	/**
	 * 启动设置界面
	 */
	public static void startSetting(Context context) {
		Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
		ComponentName cName = new ComponentName("com.android.phone",
				"com.android.phone.Settings");
		intent.setComponent(cName);
		Utils.transformActivity(context).startActivity(intent);
	}

	/**
	 * 启动拨打电话界面
	 */
	public static void startPhone(Context context, String num) {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 系统默认的action，用来打开默认的电话界面
		intent.setAction(Intent.ACTION_DIAL);
		// 需要拨打的号码
		intent.setData(Uri.parse("tel:" + num));
		context.startActivity(intent);
	}

	/**
	 * 获取WifiMac地址 1.从sp中获取。 2.从api中直接取。去到后保存到sp中。
	 * 
	 * @param context
	 * @return
	 */
	public static String getWiFiMacAddr(Context context) {
		String wifiMac = null;

		WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		if (wm.isWifiEnabled()) {
			WifiInfo info = wm.getConnectionInfo();

			wifiMac = info.getMacAddress();

		}

		return wifiMac == null ? "" : wifiMac;
	}

	/**
	 * 判断手机是否root
	 * 
	 * @return
	 */
	public static boolean isRootSystem() {
		// 把字符串替换掉
		String a = LibValues.PATH_SYS_BIN;
		String b = LibValues.PATH_SYS_XBIN;
		String c = LibValues.PATH_SYS_SBIN;
		String d = LibValues.PATH_SBIN;
		String e = LibValues.PATH_VEN_BIN;
		String su = LibValues.PATH_SU;

		// final String kSuSearchPaths[] = { "/system/bin/", "/system/xbin/",
		// "/system/sbin/", "/sbin/", "/vendor/bin/" };
		final String kSuSearchPaths[] = { a, b, c, d, e };

		File f = null;
		try {
			for (int i = 0; i < kSuSearchPaths.length; i++) {
				f = new File(kSuSearchPaths[i] + su);
				if (f != null && f.exists()) {
					return true;
				}
			}
		} catch (Exception ex) {
		}

		return false;
	}

	// /**
	// * 获取当前网络速度
	// */
	// private long showNetSpeed(Context context) {
	// long lastTotalRxBytes = 0;
	// long lastTimeStamp = 0;
	// long nowTotalRxBytes =
	// TrafficStats.getUidRxBytes(context.getApplicationInfo().uid) ==
	// TrafficStats.UNSUPPORTED ? 0
	// :(TrafficStats.getTotalRxBytes()/1024);//转为KB
	// long nowTimeStamp = System.currentTimeMillis();
	// long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp
	// - lastTimeStamp));//毫秒转换
	//
	// lastTimeStamp = nowTimeStamp;
	// return speed;
	// }

	// // 获取用户UA
	// public String getUserAgent(Context context) {
	// WebView wv = new WebView(context);
	// String ua = wv.getSettings().getUserAgentString();
	// return ua;
	// }

	public static String getAdvertiseId(Context context) {
		String advId = null;
		try {
			advId = AdvertisingIdUtil.getAdvertisingIdInfo(context).getId();
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return advId;
	}

	public static String getUserAgent(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE_NAME,
				0);
		String ua = prefs.getString(PREFS_KEY_UA, "");
		if (!TextUtils.isEmpty(ua)) {
			return ua;
		}

		try {
			WebView wv = new WebView(context);
			ua = wv.getSettings().getUserAgentString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ua;
	}

	public static void initUserAgent(Context context) {
		String ua = null;
		try {
			WebView wv = new WebView(context);
			ua = wv.getSettings().getUserAgentString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!TextUtils.isEmpty(ua)) {
			setPreference(context, PREFS_KEY_UA, ua);
		}
	}

	private static void setPreference(Context context, String key, String value) {
		if (TextUtils.isEmpty(key)) {
			return;
		}

		SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE_NAME,
				0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
     * 得到手机SDK版本号
     * @return
     */
    public static int getSDKVersion() {
    	return Build.VERSION.SDK_INT;
    }

	public static final boolean enableDebugData = false;// 启用测试数据

	/**
	 * 仅沃特沃德专用
	 * @return
	 */
	public static String getWoModel() {
		String ret = "";
		try {
			Class<?> mClass = Class.forName("android.os.SystemProperties");
			java.lang.reflect.Method method = mClass.getMethod("get", String.class, String.class);
			ret = (String) method.invoke(mClass, "ro.antos.model", "NotFound");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
}
