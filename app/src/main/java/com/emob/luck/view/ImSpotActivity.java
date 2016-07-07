package com.emob.luck.view;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.emob.lib.log.EmobLog;
import com.emob.lib.stats.StatsDefines;
import com.emob.lib.stats.StatsUtil;
import com.emob.luck.DspHelper;
import com.emob.luck.SdkHelper;
import com.emob.luck.common.CommonDefine;
import com.emob.luck.common.Value;
import com.emob.luck.model.EventItem;
import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMInterstitial;

public class ImSpotActivity extends BaseActivity {

	private ImSpotListener mListener;
	private IMInterstitial mInterstitial;
	
	public static final int AD_REQUEST_SUCCEEDED_NOTREADY = 107;
	public static final int AD_REQUEST_SUCCEEDED = 101;
	public static final int AD_REQUEST_FAILED = 102;
	public static final int ON_SHOW_MODAL_AD = 103;
	public static final int ON_DISMISS_MODAL_AD = 104;
	public static final int ON_LEAVE_APP = 105;
	public static final int ON_CLICK = 106;
	
	private String mPackageName;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		Intent intent = getIntent();
		mPackageName = "";
		if (intent != null && intent.getExtras() != null) {
			mPackageName = intent.getExtras().getString(Value.INTENT_EXTRA_PKGNAME);
		}
		
		String randomString = SdkHelper.getSdkSpotKeyString(getApplicationContext(), CommonDefine.DSP_CHANNEL_INMOBI);
		if(TextUtils.isEmpty(randomString)){
			//记录获取appId失败行为
			try {
//				EventTableDBHelper.insertData(getApplicationContext(), mPackageName, CommonDefine.DSP_CHANNEL_INMOBI,
//						CommonDefine.AD_POSITION_SDK_SPOT, EventItem.EVENT_TYPE_GET_APPID_FAILED);
				
				Map<String, String> map = new HashMap<String, String>();
				map.put("PCK", mPackageName);
				map.put("ACTION", EventItem.EVENT_TYPE_GET_APPID_FAILED + "");
				String logEvent = getLogEvent(mPackageName);
//				UmengUtils.onEvent(mContext, logEvent, map);
//				FlurryUtil.onEvent(mContext, logEvent, map);
				
				StatsUtil.onEventBackground(mContext, logEvent, map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			InMobi.initialize(this, randomString);
			mListener = new ImSpotListener(getApplicationContext(), handler);
			mInterstitial = new IMInterstitial(ImSpotActivity.this,
					randomString);
			mInterstitial.setIMInterstitialListener(mListener);
			
			mInterstitial.loadInterstitial();
			
			try {
//				EventTableDBHelper.insertData(getApplicationContext(), mPackageName, CommonDefine.DSP_CHANNEL_INMOBI,
//						CommonDefine.AD_POSITION_SDK_SPOT, EventItem.EVENT_TYPE_REQUEST);
//				
//				Map<String, String> map = new HashMap<String, String>();
//				map.put("PCK", mPackageName);
//				map.put("ACTION", EventItem.EVENT_TYPE_REQUEST + "");
//				String logEvent = getLogEvent(mPackageName);
//				UmengUtils.onEvent(mContext, logEvent, map);
//				FlurryUtil.onEvent(mContext, logEvent, map);
				
//				UmengUtils.onEvent(mContext, StatsDefines.EVENT_TYPE_REQ);
//				FlurryUtil.onEvent(StatsDefines.EVENT_TYPE_REQ);
				
				int adPos = getOfferPosition(mPackageName);
				StatsUtil.onEventOffer(mContext, mPackageName, EventItem.EVENT_TYPE_REQUEST, 
						CommonDefine.DSP_CHANNEL_INMOBI, adPos);
				StatsUtil.onEventOffer(mContext, StatsDefines.EVENT_TYPE_REQ);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(CommonDefine.TEST_MARK) {
				Toast.makeText(getApplicationContext(), "Inmobi Ad request", Toast.LENGTH_SHORT).show();
			}
			EmobLog.d("AppName", "InmobiActivity");
		}
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AD_REQUEST_SUCCEEDED_NOTREADY:
				try {
//					EventTableDBHelper.insertData(getApplicationContext(), mPackageName, CommonDefine.DSP_CHANNEL_INMOBI,
//							CommonDefine.AD_POSITION_SDK_SPOT,EventItem.EVENT_TYPE_NOTREDAY);
					
					Map<String, String> map = new HashMap<String, String>();
					map.put("PCK", mPackageName);
					map.put("ACTION", EventItem.EVENT_TYPE_NOTREDAY + "");
					String logEvent = getLogEvent(mPackageName);
//					UmengUtils.onEvent(mContext, logEvent, map);
//					FlurryUtil.onEvent(mContext, logEvent, map);
					
					StatsUtil.onEventBackground(mContext, logEvent, map);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
				
			case AD_REQUEST_SUCCEEDED:
				//记录显示行为
				try {
					DspHelper.updateSdkSpotCount(ImSpotActivity.this, CommonDefine.DSP_CHANNEL_INMOBI); 
					
//					EventTableDBHelper.insertData(getApplicationContext(), mPackageName, CommonDefine.DSP_CHANNEL_INMOBI,
//							CommonDefine.AD_POSITION_SDK_SPOT,EventItem.EVENT_TYPE_SHOW);
//					
//					Map<String, String> map = new HashMap<String, String>();
//					map.put("PCK", mPackageName);
//					map.put("ACTION", EventItem.EVENT_TYPE_SHOW + "");
//					String logEvent = getLogEvent(mPackageName);
//					UmengUtils.onEvent(mContext, logEvent, map);
//					FlurryUtil.onEvent(mContext, logEvent, map);
//					
//					UmengUtils.onEvent(mContext, StatsDefines.EVENT_TYPE_IMP);
//					FlurryUtil.onEvent(StatsDefines.EVENT_TYPE_IMP);
//					
					int adPos = getOfferPosition(mPackageName);
					StatsUtil.onEventOffer(mContext, mPackageName, EventItem.EVENT_TYPE_SHOW, 
							CommonDefine.DSP_CHANNEL_INMOBI, adPos);
					StatsUtil.onEventOffer(mContext, StatsDefines.EVENT_TYPE_IMP);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
				
			case AD_REQUEST_FAILED:
				//记录请求广告失败行为
				try {
//					EventTableDBHelper.insertData(getApplicationContext(), mPackageName, CommonDefine.DSP_CHANNEL_INMOBI,
//							CommonDefine.AD_POSITION_SDK_SPOT, EventItem.EVENT_TYPE_REQUEST_FAILED);
//					
//					Map<String, String> map = new HashMap<String, String>();
//					map.put("PCK", mPackageName);
//					map.put("ACTION", EventItem.EVENT_TYPE_REQUEST_FAILED + "");
//					String logEvent = getLogEvent(mPackageName);
//					UmengUtils.onEvent(mContext, logEvent, map);
//					FlurryUtil.onEvent(mContext, logEvent, map);
					int adPos = getOfferPosition(mPackageName);
					StatsUtil.onEventOfferBackground(mContext, mPackageName, EventItem.EVENT_TYPE_REQUEST_FAILED, 
							CommonDefine.DSP_CHANNEL_INMOBI, adPos);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
				
			case ON_SHOW_MODAL_AD:
				break;
				
			case ON_DISMISS_MODAL_AD:
				//记录关闭行为
				try {
//					EventTableDBHelper.insertData(getApplicationContext(), mPackageName, CommonDefine.DSP_CHANNEL_INMOBI,
//							CommonDefine.AD_POSITION_SDK_SPOT,EventItem.EVENT_TYPE_CLOSE);
					
//					Map<String, String> map = new HashMap<String, String>();
//					map.put("PCK", mPackageName);
//					map.put("ACTION", EventItem.EVENT_TYPE_CLOSE + "");
//					String logEvent = getLogEvent(mPackageName);
//					UmengUtils.onEvent(mContext, logEvent, map);
//					FlurryUtil.onEvent(mContext, logEvent, map);
					
					int adPos = getOfferPosition(mPackageName);
					StatsUtil.onEventOfferBackground(mContext, mPackageName, EventItem.EVENT_TYPE_CLOSE, 
							CommonDefine.DSP_CHANNEL_INMOBI, adPos);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
//				InmobiActivity.this.finish();
				break;
				
			case ON_LEAVE_APP:
				//记录点击行为
				try {
//					EventTableDBHelper.insertData(getApplicationContext(), mPackageName, CommonDefine.DSP_CHANNEL_INMOBI,
//							CommonDefine.AD_POSITION_SDK_SPOT,EventItem.EVENT_TYPE_CLICK);
//					
//					Map<String, String> map = new HashMap<String, String>();
//					map.put("PCK", mPackageName);
//					map.put("ACTION", EventItem.EVENT_TYPE_CLICK + "");
//					String logEvent = getLogEvent(mPackageName);
//					UmengUtils.onEvent(mContext, logEvent, map);
//					FlurryUtil.onEvent(mContext, logEvent, map);
//					
//					UmengUtils.onEvent(mContext, StatsDefines.EVENT_TYPE_CLK);
//					FlurryUtil.onEvent(StatsDefines.EVENT_TYPE_CLK);
					
					int adPos = getOfferPosition(mPackageName);
					StatsUtil.onEventOffer(mContext, mPackageName, EventItem.EVENT_TYPE_CLICK, 
							CommonDefine.DSP_CHANNEL_INMOBI, adPos);
					StatsUtil.onEventOffer(mContext, StatsDefines.EVENT_TYPE_CLK);
				} catch (Exception e) {
					e.printStackTrace();
				}
//				InmobiActivity.this.finish();
				break;
				
			case ON_CLICK:
				break;
				
			}
			super.handleMessage(msg);
		}
	};
	
	private String getLogEvent(String pkgName) {
		// 默认TopExit
		String logEvent = StatsDefines.SDK_TOPEXIT_INMOBI;
		if (EventItem.SHOW_TYPE_LOCK_SCREEN.equalsIgnoreCase(pkgName)) { // 锁屏
			logEvent = StatsDefines.SDK_LOCK_INMOBI;
		} else if (EventItem.SHOW_TYPE_NETWORK_ON.equalsIgnoreCase(pkgName)) { // 打开网络
			logEvent = StatsDefines.SDK_NETON_INMOBI;
		}
		return logEvent;
	}
	
	private int getOfferPosition(String pkgName) {
		// 默认TopExit
		int logEvent = CommonDefine.AD_POSITION_SDK_TOPEXIT;
		if (EventItem.SHOW_TYPE_LOCK_SCREEN.equalsIgnoreCase(pkgName)) { // 锁屏
			logEvent = CommonDefine.AD_POSITION_SDK_LOCK;
		} else if (EventItem.SHOW_TYPE_NETWORK_ON.equalsIgnoreCase(pkgName)) { // 打开网络
			logEvent = CommonDefine.AD_POSITION_SDK_NET;
		}
		return logEvent;
	}
	
}
