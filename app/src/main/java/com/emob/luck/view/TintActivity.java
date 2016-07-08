package com.emob.luck.view;

import java.util.HashMap;
import java.util.Map;

import com.emob.lib.log.EmobLog;
import com.emob.lib.stats.StatsUtil;
import com.emob.lib.util.Utils;
import com.emob.luck.ImageMemoryCache;
import com.emob.luck.common.CommonDefine;
import com.emob.luck.db.AdTableDBHelper;
import com.emob.luck.model.AdItem;
import com.emob.luck.model.EventItem;
import com.duduws.recent.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class TintActivity extends Activity implements OnClickListener{
	public RelativeLayout mRlView;
	private ImageView mIvDelete;
	private ImageView mAdView;
	private Context mContext;
	private Bitmap mAdBitmap;
	private String mLandingURl;
	private String mUrl;
	private static final String TAG = "TextActivty";

	public static final int NATIVE_SUCCEED_TOP_SPOT 	= 101;
	private AdItem mAdItem;
	private String mPckName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_spot_view);
		mContext = getApplicationContext();

		// 注册友盟
		StatsUtil.onCreat(mContext);
				  
		mAdItem = (AdItem) getIntent().getSerializableExtra("item");
		mPckName = getIntent().getStringExtra("pck");
		if(TextUtils.isEmpty(mPckName)) {
			mPckName = EventItem.SHOW_TYPE_INMOBI_NATIVE_SPOT;
		}
		EmobLog.d("tian", "pck=" + mPckName);
		EmobLog.d("RecentService", "intent=" + mAdItem);
		if (mAdItem != null) {
			mLandingURl = mAdItem.getLandingUrl();
			mAdBitmap = ImageMemoryCache.getInstance(mContext).getBitmap(mAdItem.getIconUrl());
			mUrl = mAdItem.getIconUrl();
			EmobLog.d(TAG, "mLandingURl=" + mLandingURl);
			EmobLog.d(TAG, "mAdBitmap=" + mAdBitmap);
		}
		
		initView();
		FrameLayout.LayoutParams lParams = new FrameLayout.LayoutParams(
				Utils.dip2px(mContext, 320), 
				Utils.dip2px(mContext, 480));
		lParams.gravity = Gravity.CENTER;
		mRlView.setLayoutParams(lParams);
		mRlView.setGravity(Gravity.CENTER);
		initData();
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		StatsUtil.onResume(mContext);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		StatsUtil.onPause(mContext);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	private void initView() {
		mRlView = (RelativeLayout) findViewById(R.id.rl_view);
		mIvDelete = (ImageView) findViewById(R.id.iv_delete);
		mAdView = (ImageView)findViewById(R.id.iv_view);
		mIvDelete.setOnClickListener(this);
		mAdView.setOnClickListener(this);
	}
	
	@SuppressWarnings("deprecation")
	private void initData() {
		mAdView.setBackgroundDrawable(new BitmapDrawable(mAdBitmap));
		
//		EventTableDBHelper.insertData(mContext,
//				EventItem.SHOW_TYPE_INMOBI_NATIVE_SPOT,
//				CommonDefine.DSP_CHANNEL_INMOBI,
//				CommonDefine.AD_POSITION_TOP_SPOT, EventItem.EVENT_TYPE_SHOW);
		
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("PCK", mPckName);
//		map.put("ACTION", EventItem.EVENT_TYPE_SHOW + "");
//		UmengUtils.onEvent(mContext, StatsDefines.TOP_SPOT_INMOBI, map);
//		FlurryUtil.onEvent(mContext, StatsDefines.TOP_SPOT_INMOBI, map);
		
//		StatsUtil.onEvent(mContext, StatsDefines.TOP_SPOT_INMOBI, map);
		StatsUtil.onEventOfferBackground(mContext, mPckName, EventItem.EVENT_TYPE_SHOW, 
				CommonDefine.DSP_CHANNEL_INMOBI, CommonDefine.AD_POSITION_TOP_SPOT);

	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.iv_delete) {
			EmobLog.d(TAG, "removeSpotView");
//			EventTableDBHelper.insertData(mContext,
//					EventItem.SHOW_TYPE_INMOBI_NATIVE_SPOT, CommonDefine.DSP_CHANNEL_INMOBI,
//					CommonDefine.AD_POSITION_TOP_SPOT, EventItem.EVENT_TYPE_CLOSE);
			
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("PCK", mPckName);
//			map.put("ACTION", EventItem.EVENT_TYPE_CLOSE + "");
//			UmengUtils.onEvent(mContext, StatsDefines.TOP_SPOT_INMOBI, map);
//			FlurryUtil.onEvent(mContext, StatsDefines.TOP_SPOT_INMOBI, map);
//			StatsUtil.onEvent(mContext, StatsDefines.TOP_SPOT_INMOBI, map);
			StatsUtil.onEventOfferBackground(mContext, mPckName, EventItem.EVENT_TYPE_CLOSE, 
					CommonDefine.DSP_CHANNEL_INMOBI, CommonDefine.AD_POSITION_TOP_SPOT);

			finish();
		}
		
		if (id == R.id.iv_view) {
//				EventTableDBHelper.insertData(mContext,
//						EventItem.SHOW_TYPE_INMOBI_NATIVE_SPOT, CommonDefine.DSP_CHANNEL_INMOBI,
//						CommonDefine.AD_POSITION_TOP_SPOT, EventItem.EVENT_TYPE_CLICK);
				
				Map<String, String> map = new HashMap<String, String>();
				map.put("PCK", mPckName);
				map.put("ACTION", EventItem.EVENT_TYPE_CLICK + "");
//				UmengUtils.onEvent(mContext, StatsDefines.TOP_SPOT_INMOBI, map);
//				FlurryUtil.onEvent(mContext, StatsDefines.TOP_SPOT_INMOBI, map);
				StatsUtil.onEventOfferBackground(mContext, mPckName, EventItem.EVENT_TYPE_CLICK, 
						CommonDefine.DSP_CHANNEL_INMOBI, CommonDefine.AD_POSITION_TOP_SPOT);

				//InMobi.setLogLevel(LOG_LEVEL.VERBOSE);
				Utils.openUrl(mContext, mLandingURl);
				EmobLog.d(TAG, "removeSpotView");
//				EventTableDBHelper.insertData(mContext,
//						EventItem.SHOW_TYPE_INMOBI_NATIVE_SPOT, CommonDefine.DSP_CHANNEL_INMOBI,
//						CommonDefine.AD_POSITION_TOP_SPOT, EventItem.EVENT_TYPE_CLOSE);
				finish();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		AdTableDBHelper.deleteOneAd(mUrl);
		ImageMemoryCache.getInstance(mContext).removeOneImg(mUrl);
		super.onDestroy();
	}
}
