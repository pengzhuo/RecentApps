package com.emob.luck;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RelativeLayout;

import com.emob.lib.log.EmobLog;
import com.emob.lib.stats.StatsDefines;
import com.emob.lib.stats.StatsUtil;
import com.emob.luck.common.CommonDefine;
import com.emob.luck.common.Value;
import com.emob.luck.db.AdTableDBHelper;
import com.emob.luck.model.EventItem;
import com.inmobi.monetization.IMErrorCode;
import com.inmobi.monetization.IMNative;
import com.inmobi.monetization.IMNativeListener;

public class TopSpotNativeHelper implements IMNativeListener {
	
	private static final String TAG = "TopSpot";
	public static final int DEFAULT_NATIVE_ENABLED = 1;
	private static TopSpotNativeHelper mNativeHelper;
	
	private NativeListener mListener;
	
	protected IMNative mImNative;
	protected Context mContext;
	private Bitmap mBitmap;
	private String mLandingUrl;
	private String mImageUrl;

	/**
	 * 记录展示
	 * 
	 * @param ll
	 */
	public void recodeShow(RelativeLayout ll) {
		if (mImNative != null) {
			mImNative.attachToView(ll);
		}
	}

	/**
	 * 记录点击
	 */
	public void recodeClick() {
		if (mImNative != null) {
			mImNative.handleClick(null);
		}
	}

	/**
	 * 将原生广告从填充的View中移除
	 */
	public void clearNativeAd() {
		if (mImNative != null) {
			mImNative.detachFromView();
		}
	}
	public static synchronized TopSpotNativeHelper getInstance(Context context) {
		if (mNativeHelper == null) {
			mNativeHelper = new TopSpotNativeHelper(context);
		}
		return mNativeHelper;

	}
	
	public TopSpotNativeHelper(Context context) {
		mContext = context;
	}

	public void setListener(NativeListener listener) {
		mListener = listener;
	}
	
	public void loadTopSpotNative() {
		EmobLog.d(TAG, "loadTopSpotNative");
		int channel = DspHelper.getTopSpotChannel(mContext);
		if (channel == CommonDefine.DSP_GLOABL) {
			EmobLog.d(TAG, "loadTopSpotNative channel check failed");
			return;
		}
		// String key = "3582e49efe99459fbef34c5d81f86fd0";
		String key = SdkHelper.getTopSpotKeyString(mContext, channel);
		EmobLog.d(TAG, "loadTopSpotNative channel="+channel);
		EmobLog.d(TAG, "loadTopSpotNative, key="+key);
		
		if (channel == CommonDefine.DSP_CHANNEL_INMOBI) {
//			EventTableDBHelper.insertData(mContext,
//					EventItem.SHOW_TYPE_INMOBI_NATIVE_SPOT, CommonDefine.DSP_CHANNEL_INMOBI,
//					CommonDefine.AD_POSITION_TOP_SPOT, EventItem.EVENT_TYPE_REQUEST);
			StatsUtil.onEventBackground(mContext, EventItem.SHOW_TYPE_INMOBI_NATIVE_SPOT, EventItem.EVENT_TYPE_REQUEST);
			mImNative = new IMNative(key, this);
			mImNative.loadAd();
		} 
	}
	
	/**
	 * 下载icon图片
	 * 
	 * @param url
	 */
	protected void downloadImage(final String url) {
		new Thread() {
			public void run() {
				mBitmap = ImageGetFromHttp.downloadBitmap(mImageUrl);
				if (mListener != null) {
					mListener.onSpotNativeSucceed(mLandingUrl, mBitmap);
				}
			}
		}.start();
	}
	
	@Override
	public void onNativeRequestSucceeded(IMNative arg0) {
		// TODO: record event
		try {
			JSONObject resultJSON = new JSONObject(arg0.getContent());
			String title = resultJSON.optString("title");
			String landingUrl = resultJSON.optString("landingURL");
			JSONObject iconObj = resultJSON.getJSONObject("screenshots");
			String url = iconObj.optString("url");
			mImageUrl = url;
			mLandingUrl = landingUrl;
			AdTableDBHelper.insertData(mContext, Value.NATIVE_TOP_SPOT, title, url, landingUrl);
			EmobLog.d(TAG, "TopSpotNativeHelper.onNativeRequestSucceeded, title = " + title+"\nurl="+url+"\nlandingUrl = " + landingUrl);
			downloadImage(url);
		} catch (JSONException e) {
			e.printStackTrace(); 
		}
	}
	
	@Override
	public void onNativeRequestFailed(IMErrorCode arg0) {
		EmobLog.d(TAG, "TopSpotNativeHelper.onNativeRequestFailed");
//		EventTableDBHelper.insertData(mContext,
//				EventItem.SHOW_TYPE_INMOBI_NATIVE_SPOT, CommonDefine.DSP_CHANNEL_INMOBI,
//				CommonDefine.AD_POSITION_TOP_SPOT, EventItem.EVENT_TYPE_REQUEST_FAILED);
	
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("PCK", EventItem.SHOW_TYPE_INMOBI_NATIVE_SPOT);
//		map.put("ACTION", EventItem.EVENT_TYPE_REQUEST_FAILED + "");
////		UmengUtils.onEvent(mContext, StatsDefines.TOP_SPOT_INMOBI, map);
//		StatsUtil.onEventBackground(mContext, StatsDefines.TOP_SPOT_INMOBI, map);
		
		StatsUtil.onEventOfferBackground(mContext, EventItem.SHOW_TYPE_INMOBI_NATIVE_SPOT,
				EventItem.EVENT_TYPE_REQUEST_FAILED,
				CommonDefine.DSP_CHANNEL_INMOBI, CommonDefine.AD_POSITION_TOP_SPOT);
	}
}
