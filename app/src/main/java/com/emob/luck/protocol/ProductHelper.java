package com.emob.luck.protocol;

import com.emob.lib.util.DevicesUtils;
import com.emob.luck.common.CommonDefine;

import android.text.TextUtils;

public class ProductHelper {
	private static final boolean WO_PRODUCT = false;
	
	public static String getOid() {
		String oid = CommonDefine.APP_COOPERATION_ID;
		if (WO_PRODUCT) {
			String model = DevicesUtils.getWoModel();
			if (!TextUtils.isEmpty(model)) {
				String a[] = model.split("-");
				if (a != null && a.length > 2) {
					oid = a[0] + "-" + a[1] + "-" + a[2] ;
				} else {
					oid = model;
				}
			}
		}
		
		return oid;
	}
	
	public static String getChannelId() {
		String cid = CommonDefine.APP_CHANNEL_ID;
		if (WO_PRODUCT) {
			String model = DevicesUtils.getWoModel();
			if (!TextUtils.isEmpty(model)) {
				String a[] = model.split("-");
				if (a != null && a.length > 1) {
					cid = a[0] + "-" + a[1];
				} else {
					cid = model;
				}
			}
		}
		
		return cid;
	}
	
	public static String getProductId() {
		String pid = CommonDefine.APP_PRODUCT_ID;
		if (WO_PRODUCT) {
			String model = DevicesUtils.getWoModel();
			if (!TextUtils.isEmpty(model)) {
				String a[] = model.split("-");
				if (a != null  && a.length > 2) {
					pid = a[2];
				} else {
					pid = model;
				}
			}
		}
		
		return pid;
	}
	
	public static String getProtocol() {
		String model = CommonDefine.APP_PROTOCOL;
		if (WO_PRODUCT) {
			model = DevicesUtils.getWoModel();
		}
		
		return model;
	}

}
