package com.mobi.fork;

import android.content.Context;
import android.os.Environment;

public class GuardHelper {

	public static void startDaemon(final Context context, final String clsName) {
		
		String executable = "libhelper.so";
		String aliasfile = "helper";
		NativeRuntime.getInstance().RunExecutable(context.getPackageName(), executable, aliasfile, context.getPackageName() + "/" + clsName);

		(new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					NativeRuntime.getInstance().startService(context.getPackageName() + "/" + clsName, createRootPath(context));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		})).start();
		
	}

	public static boolean isSdCardAvailable() {
		return Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	public static String createRootPath(Context context) {
		String rootPath = "";
		if (isSdCardAvailable()) {
			// /sdcard/Android/data/<application package>/cache
			rootPath = context.getExternalCacheDir()
					.getPath();
		} else {
			// /data/data/<application package>/cache
			rootPath = context.getCacheDir().getPath();
		}
		return rootPath;
	}
}
