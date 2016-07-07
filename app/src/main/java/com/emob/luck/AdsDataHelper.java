package com.emob.luck;

import com.emob.luck.protocol.ProtocolManager;

import android.content.Context;

public class AdsDataHelper {
	
	public static void request4Ads(Context context, int type) {
		Context ctx = context.getApplicationContext();
		
		ProtocolManager pManager = new ProtocolManager(ctx);
		pManager.start(type);
	}
	
	public static void request4Heart(Context context) {
		Context ctx = context.getApplicationContext();
		ProtocolManager pManager = new ProtocolManager(ctx);
		pManager.startHeart();
	}
}
