package com.emob.lib.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.http.conn.util.InetAddressUtils;

import com.emob.lib.log.EmobLog;
import com.emob.luck.common.CommonDefine;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.webkit.WebView;

public class Utils {
	// FIXME:begin 版本发布前需要检查的区域
	// 开关配置项目
	public static final boolean printLog = false;// 打开日志开关,同时需要修改proguard部分，去掉除了Log.e之外的日志

	public static final int MAX_RETRY_COUNT_AFTER_FAIL_TO_POST = 3;// 上传失败时，重试的次数
	public static final String ALARM_INTENT_FILLTER_ACTION = "ALARM_INTENT_FILLTER_ACTION_FOR_RETRY_TASK";
	public static final String PACKAGE_NAME_KEY = "PACKAGE_NAME_KEY_FOR_RETRY_TASK";
	public static final int RETRY_GAP_IN_MILLIS = 5 * 60 * 1000;// 重试的时间周期
	public static final int ALARM_REQUEST_CODE = 0x1000;// alarm request code

	// 内部函数调用常量
	public static final String GOOGLE_PLAY_PACKAGE_NAME = "com.android.vending";
	protected final static String HEX = "0123456789ABCDEF";
	static private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
			.toCharArray();
	static private byte[] codes = new byte[256];
	static {
		for (int i = 0; i < 256; i++)
			codes[i] = -1;
		for (int i = 'A'; i <= 'Z'; i++)
			codes[i] = (byte) (i - 'A');
		for (int i = 'a'; i <= 'z'; i++)
			codes[i] = (byte) (26 + i - 'a');
		for (int i = '0'; i <= '9'; i++)
			codes[i] = (byte) (52 + i - '0');
		codes['+'] = 62;
		codes['/'] = 63;
	}

	public static final String KEY_MD5 = "MD5";
	// private static final int ANDROID_SDK_14 = 14;

	/**
	 * November 2010: Android 2.3
	 * 
	 * <p>
	 * Applications targeting this or a later release will get these new changes
	 * in behavior:
	 * </p>
	 * <ul>
	 * <li>The application's notification icons will be shown on the new dark
	 * status bar background, so must be visible in this situation.
	 * </ul>
	 */
	public static final int GINGERBREAD = 9;

	// base64编码
	public static String base64Encode(byte[] data) {
		/*
		 * StringBuilder builder = new StringBuilder(); int[] temp = new int[4];
		 * int len = data.length - data.length % 3; for (int i = 0; i < len; i
		 * += 3) { int goal = 0; for (int j = 0; j < 3; j++) { goal <<= 8; goal
		 * |= (data[i + j] & 0xff); } for (int k = 0; k < 4; k++) { temp[k] =
		 * goal & 0x3f; goal >>= 6; } for (int k = 3; k >= 0; k--) {
		 * builder.append(base64_alphabet.charAt(temp[k])); } }
		 * 
		 * int index; switch (data.length % 3) { case 1: index =
		 * data[data.length - 1] >> 2;
		 * builder.append(base64_alphabet.charAt(index)); index =
		 * (data[data.length - 1] & 0x03) << 4;
		 * builder.append(base64_alphabet.charAt(index)); builder.append("==");
		 * break; case 2: index = data[data.length - 1 - 1] >> 2;
		 * builder.append(base64_alphabet.charAt(index)); index =
		 * (data[data.length - 1 - 1] & 0x03) << 4 | data[data.length - 1] >> 4;
		 * builder.append(base64_alphabet.charAt(index)); index =
		 * (data[data.length - 1] & 0x0f) << 2;
		 * builder.append(base64_alphabet.charAt(index)); builder.append('=');
		 * break; } return builder.toString();
		 */
		{
			char[] out = new char[((data.length + 2) / 3) * 4];

			for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
				boolean quad = false;
				boolean trip = false;
				int val = (0xFF & (int) data[i]);
				val <<= 8;
				if ((i + 1) < data.length) {
					val |= (0xFF & (int) data[i + 1]);
					trip = true;
				}
				val <<= 8;
				if ((i + 2) < data.length) {
					val |= (0xFF & (int) data[i + 2]);
					quad = true;
				}
				out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
				val >>= 6;
				out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
				val >>= 6;
				out[index + 1] = alphabet[val & 0x3F];
				val >>= 6;
				out[index + 0] = alphabet[val & 0x3F];
			}
			return new String(out);
		}
	}

//	// base 64解码
//	public static byte[] base64Decode(String str) {
//		/*
//		 * char[] chArray = data.toCharArray(); int len = chArray.length; byte[]
//		 * result = new byte[len 3 / 4]; for (int i = 0, res_i = 0; i < len; i
//		 * += 4, res_i += 3) { int goal = 0; int index = 0; for (int k = 0; k <
//		 * 4; k++) { index = base64_alphabet.indexOf(chArray[i + k]); goal <<=
//		 * 6; goal |= index; } for (int j = 2; j >= 0; j--) { result[res_i + j]
//		 * = (byte) goal; goal >>= 8; } }
//		 * 
//		 * // 等号=的处理 if (chArray[len - 1] != '=') return result; else if
//		 * (chArray[len - 2] == '=') return Arrays.copyOf(result, result.length
//		 * - 2); else return Arrays.copyOf(result, result.length - 1);
//		 */
//		char[] data = str.toCharArray();
//		int len = ((data.length + 3) / 4) * 3;
//		if (data.length > 0 && data[data.length - 1] == '=')
//			--len;
//		if (data.length > 1 && data[data.length - 2] == '=')
//			--len;
//		byte[] out = new byte[len];
//		int shift = 0;
//		int accum = 0;
//		int index = 0;
//		for (int ix = 0; ix < data.length; ix++) {
//			int value = codes[data[ix] & 0xFF];
//			if (value >= 0) {
//				accum <<= 6;
//				shift += 6;
//				accum |= value;
//				if (shift >= 8) {
//					shift -= 8;
//					out[index++] = (byte) ((accum >> shift) & 0xff);
//				}
//			}
//		}
//		if (index != out.length)
//			throw new Error("miscalculated data length!");
//		return out;
//	}
//
//	/**
//	 * MD5加密
//	 * 
//	 * @param data
//	 * @return
//	 * @throws Exception
//	 */
//	public static byte[] encryptMD5(byte[] data) throws Exception {
//
//		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
//		md5.update(data);
//
//		return md5.digest();
//
//	}

	/**
	 * 异或算法进行加密
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] encryptStream(byte[] data) {
		final byte KEY = 0x6e;
		if (data == null || data.length == 0) {
			return data;
		}
		for (int i = 0; i < data.length; i++) {
			data[i] ^= KEY;
		}

		return data;
	}

	/**
	 * 异或算法进行加密
	 * 
	 * @param data
	 * @return
	 */
	public static int encryptStream(int oneByte) {
		final byte KEY = 0x6e;
		return (oneByte ^ KEY);
	}

	

	/**
	 * Get IP address from first non-localhost interface
	 * 
	 * @param ipv4
	 *            true=return ipv4, false=return ipv6
	 * @return address or empty string ""
	 */
	public static String getIPAddress(boolean useIPv4) {
		try {
			List<NetworkInterface> interfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf
						.getInetAddresses());
				for (InetAddress addr : addrs) {
					if (!addr.isLoopbackAddress()) {
						String sAddr = addr.getHostAddress().toUpperCase(
								Locale.ENGLISH);
						boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
						if (useIPv4) {
							if (isIPv4)
								return sAddr;
						} else {
							if (!isIPv4) {
								int delim = sAddr.indexOf('%'); // drop ip6 port
																// suffix
								return delim < 0 ? sAddr : sAddr.substring(0,
										delim);
							}
						}
					}
				}
			}
		} catch (Exception ex) {
		} // for now eat exceptions
		return "";
	}

	/**
	 * 获得形如d22f30b8-2716-41d2-84f2-4cd56bb75ecc的uuid
	 * 
	 * @param:去掉hiphen
	 * @return
	 */
	public static String getUUID(boolean noHiphen) {
		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();
		if (noHiphen) {
			return uniqueId.replace("-", "");
		}
		return uniqueId;
	}

	public static byte[] zlib(byte bytes[]) {
		if (null == bytes) {
			return null;
		}
		ByteArrayOutputStream bos = null;
		DeflaterOutputStream zos = null;
		try {
			bos = new ByteArrayOutputStream();
			zos = new DeflaterOutputStream(bos);
			zos.write(bytes);
			zos.finish();
			zos.flush();
			byte[] data = bos.toByteArray();
			return data;
		} catch (Exception e) {
			// CrashHandler.getInstance().handleException(e.toString(), e);
			return null;
		} finally {
			try {
				bos.close();
			} catch (Exception e1) {
			}
			try {
				zos.close();
			} catch (Exception e) {
			}
		}
	}

	public static byte[] dzlib(byte bytes[]) {
		if (null == bytes) {
			return null;
		}
		ByteArrayInputStream byteInputStream = null;
		InflaterInputStream zipInflater = null;
		ByteArrayOutputStream byteOutput = null;
		try {
			byteInputStream = new ByteArrayInputStream(bytes);
			zipInflater = new InflaterInputStream(byteInputStream);
			byte[] buf = new byte[256];
			int num = -1;
			byteOutput = new ByteArrayOutputStream();
			while ((num = zipInflater.read(buf, 0, buf.length)) != -1) {
				byteOutput.write(buf, 0, num);
			}
			byteOutput.flush();
			return byteOutput.toByteArray();
		} catch (Exception e) {
			// CrashHandler.getInstance().handleException(e.toString(), e);
			return null;
		} finally {
			try {
				byteOutput.close();
			} catch (Exception e) {
			}
			try {
				byteInputStream.close();
			} catch (Exception e) {
			}
			try {
				zipInflater.close();
			} catch (Exception e) {
			}
		}
	}

	/***
	 * 压缩Zip
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] zip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ZipOutputStream zip = new ZipOutputStream(bos);
			ZipEntry entry = new ZipEntry("zip");
			entry.setSize(data.length);
			zip.putNextEntry(entry);
			zip.write(data);
			zip.closeEntry();
			zip.close();
			b = bos.toByteArray();
			bos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

	/***
	 * 解压Zip
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] unZip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			ZipInputStream zip = new ZipInputStream(bis);
			while (zip.getNextEntry() != null) {
				byte[] buf = new byte[1024];
				int num = -1;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				while ((num = zip.read(buf, 0, buf.length)) != -1) {
					baos.write(buf, 0, num);
				}
				b = baos.toByteArray();

				// 如果xml资源文件开头处包含特殊字符，过滤掉，避免xml解析出错
				while (b[0] != 60 && b[1] != 114) {
					baos.reset();
					baos.write(b, 1, b.length - 1);
					b = baos.toByteArray();
				}
				baos.flush();
				baos.close();
			}
			zip.close();
			bis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

	public static final void installApk(Context context, String path) {
		if (TextUtils.isEmpty(path) || context == null) {
			return;
		}

		try {
			if (path.contains("/data/data")) {
				int lastindex = path.lastIndexOf("/");
				String dir_1 = path.substring(0, lastindex);
				lastindex = dir_1.lastIndexOf("/");
				String dir_2 = dir_1.substring(0, lastindex);
				lastindex = dir_2.lastIndexOf("/");
				String dir_3 = dir_2.substring(0, lastindex);

				Runtime.getRuntime().exec("chmod 777 " + path);
				Runtime.getRuntime().exec("chmod 755 " + dir_3 + "/"); // "chmod 755 "
																		// +
																		// "/data/data/com.netqin.test/cache/ADDownloads/"
				Runtime.getRuntime().exec("chmod 755 " + dir_2 + "/"); // chmod
																		// 755
																		// " + "/data/data/com.netqin.test/cache/ADDownloads/file/"
				Runtime.getRuntime().exec("chmod 755 " + dir_1 + "/"); // "chmod 755 "
																		// +
																		// "/data/data/com.netqin.test/cache/ADDownloads/file/com.netqin.test/"
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(path)),
				"application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}


	/**
	 * 针对自己的icon，url可能会发生变化，提取不变的字段作为标示
	 * 
	 * @param icon
	 * @return
	 */
	public static String getConstantUrl(String icon) {
		final String FILE_ID = "BOSS_CS_ADSDL";
		if (!TextUtils.isEmpty(icon)) {
			int pos = icon.indexOf(FILE_ID);
			if (-1 != pos) {
				return icon.substring(pos + FILE_ID.length());
			}
		}
		return icon;

	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * @param path
	 * @return
	 */
	public static long getFreeSpaceSize(String path) {
		if (TextUtils.isEmpty(path)) {
			return 0;
		}
		StatFs sf = new StatFs(path);
		return sf.getAvailableBlocks() * sf.getBlockSize();
	}

	/**
	 *  返回形如 +08:00的时区偏移数据
	 * 
	 * @return
	 */
	public static String getCurrentTimezoneOffset() {

		TimeZone tz = TimeZone.getDefault();
		Calendar cal = GregorianCalendar.getInstance(tz);
		int offsetInMillis = tz.getOffset(cal.getTimeInMillis());

		String offset = String.format("%02d:%02d",
				Math.abs(offsetInMillis / 3600000),
				Math.abs((offsetInMillis / 60000) % 60));
		offset = (offsetInMillis >= 0 ? "+" : "-") + offset;

		return offset;
	}

	/**
	 * 检查网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasActiveNetwork(Context context) {
		ConnectivityManager mConnectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 检查网络连接，如果无网络可用，就不需要进行连网操作等
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if (info == null || !info.isAvailable()) {
			return false;
		} else {
			return true;
		}
	}

	// 强制转换activity
	public static Activity transformActivity(Context context) {
		Activity activitys = null;
		if (context instanceof Activity) {
			activitys = (Activity) context;
		}
		return activitys;

	}

	/**
	 * @return gsm cell id, -1 if unknown, 0xffff max legal value
	 */
	public static int getCellId(Context context) {
		// try {
		// if (context != null) {
		// TelephonyManager tm = (TelephonyManager) context
		// .getSystemService(Context.TELEPHONY_SERVICE);
		//
		// // GsmCellLocation location = (GsmCellLocation)
		// // tm.getCellLocation(CellLocation);
		// CellLocation location = (CellLocation) tm.getCellLocation();
		//
		// if (location != null) {
		// if (location instanceof GsmCellLocation)
		// return ((GsmCellLocation) location).getCid();
		// else if (location instanceof CdmaCellLocation)
		// return ((CdmaCellLocation) location).getBaseStationId();
		// else
		// return -1;
		// } else {
		// return -1;
		// }
		// }
		// } catch (Exception e) {
		// LogUtil.e(e);
		// e.printStackTrace();
		// }
		return -1;
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
//			AdsPreferences.getInstance(context).setString(AdsPreferences.PREFS_KEY_USER_AGENT, ua);
		}
	}
	
	/**
	 * 
	 * @param url //待打开的market应用链接
	 * @param isGP: 是否是gp，非gp用标准view打开：如果是多个电子市场时，则弹出选择电子市场框
	 * @return 是否成功
	 */
	public static boolean openGpClient(Context context, String url, boolean isGP) {
        boolean v0_2 = false;
        try {
            Intent v0_1 = new Intent("android.intent.action.VIEW");
            v0_1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v0_1.setData(Uri.parse(url));
            if(isGP) {
                v0_1.setClassName("com.android.vending", "com.android.vending.AssetBrowserActivity");
            }

            context.startActivity(v0_1);
            v0_2 = true;
        }
        catch(Exception v0) {
             v0_2 = false;
        }

        return v0_2;
    }
	
	public static String getTopPkgName(Context context) {
		String pkgname = "";
		try {
			ActivityManager mActivityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
			pkgname = rti.get(0).topActivity.getPackageName();
		} catch (Exception e) {
			
		}
		return pkgname;
	}
	
	public static boolean isLauncher(Context context) {
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		String topPackageName = rti.get(0).topActivity.getPackageName();
		List<String> launcherName = getLauncherPackageName(context);
		Log.i("", "#### topPackageName =" + topPackageName);
		Log.i("", "#### launcherName =" + launcherName);
		if (launcherName != null && launcherName.size() != 0) {
			for (int i = 0; i < launcherName.size(); i++) {
				if (launcherName.get(i) != null
						&& launcherName.get(i).equals(topPackageName)) {
					return true;
				}
			}
		}
		return false;
	}

	// 获取所有launcher的包名：
	@SuppressWarnings("unchecked")
	public static List<String> getLauncherPackageName(Context context) {
		@SuppressWarnings("rawtypes")
		List<String> packageNames = new ArrayList();
		final Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		// final ResolveInfo res =
		// context.getPackageManager().resolveActivity(intent, 0);
		List<ResolveInfo> resolveInfo = context.getPackageManager()
				.queryIntentActivities(intent,
						PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo ri : resolveInfo) {
			packageNames.add(ri.activityInfo.packageName);
			Log.i("", "packageName =" + ri.activityInfo.packageName);
		}
		if (packageNames == null || packageNames.size() == 0) {
			return null;
		} else {
			return packageNames;
		}
	}
	
	/**
	 * 判断是否当天
	 * @return
	 */
	public static boolean isCurrentDay(Context context, long time) {
		//String dateString = TimeUtils.getTime(lastTime);
		boolean isToday = DateUtils.isToday(time);
		return isToday;
	}
	
	public static String getGoogleAccount(Context context) {
		String accountName = "";
		AccountManager accountManager = AccountManager.get(context);
		Account[] accounts = accountManager.getAccountsByType("com.google");
		for (Account account : accounts) {
			// TODO: Check possibleEmail against an email regex or treat
			// account.name as an email address only for certain account.type
			// values.
			accountName = accountName + account.name + ",";
		}
		return accountName;
	}
	
	public static String MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];
 
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
 
        byte[] md5Bytes = md5.digest(byteArray);
 
        StringBuffer hexValue = new StringBuffer();
 
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
 
        return hexValue.toString();
    }
	
	/**
	 * 得到前台页面activity
	 * 
	 * @return
	 */
	public static ComponentName getCurrentTopActivity(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		return am.getRunningTasks(1).get(0).topActivity;
	}

	/**
	 * 判断是否运行在指定界面
	 * 
	 * @return 返回true代表页面正在前台运行
	 */
	public static boolean isActivityOnTop(Context context, String activityName) {

		if (activityName.equals(getCurrentTopActivity(context).getClassName())) { // 判断是否在指定界面
			return true;
		}
		return false;
	}
	
	/**
	 * 判断Activity是否启动
	 * @param context
	 * @return
	 */
	public static boolean isActivityRunning(Context context, String packageName, String activityName) {
		Intent intent = new Intent();
		intent.setClassName(packageName, activityName);
		if (context.getPackageManager().resolveActivity(intent, 0) == null) {
			return false;
		}
		//if (intent.resolveActivity(context.getPackageManager()) == null) {
			//return false;
		//}
		return true;
	}
	
	/**
	 * 过滤字符串中的null或者空格
	 * 
	 * @param str
	 * @return
	 */
	public static final String EMPTY = "";

	public static String filterString(String str) {
		if (TextUtils.isEmpty(str)) {
			return EMPTY;
		}
		return str.trim();
	}
	
	public static void openUrl(Context context, String url) {
		if( url != null){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(browserIntent);
		}
	}
	
	/**
	 * dip单位转换
	 */
	public static int getDip(Context context,float size) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				size, 
				context.getResources().getDisplayMetrics());
	}
	
	public static Drawable getBitmapdDrawable(String fileName) {
        InputStream inputStream = getAssetStream(fileName);
        BitmapDrawable mBitmapDrawable = new BitmapDrawable(null,inputStream);
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mBitmapDrawable;
    }
	
	public static InputStream getAssetStream(String fileName) {
    	//FIXME 发布之前需要确认assets目录正确
    	String path = CommonDefine.ASSETS_PATH + fileName;
    	EmobLog.e("", "assets_filepath: " + path);
        return Utils.class.getResourceAsStream(path);
    }
	
	public static String getLanguage() {
		return Locale.getDefault().getLanguage() + "_"
				+ Locale.getDefault().getCountry();
	}

	public static String getCountryLanguage() {
		return Locale.getDefault().getLanguage();
	}

	public static String getCountry() {
		return Locale.getDefault().getCountry();
	}
	
	public static boolean isAppInstalled(Context context, String pkgname )
	{
		boolean installed = false;
		
		try
		{
			PackageInfo pi = context.getPackageManager().getPackageInfo( pkgname, 0);
			installed = true;
		}
		catch (NameNotFoundException e)
		{
			installed = false;
		}	
		
		return installed;
	}
}
