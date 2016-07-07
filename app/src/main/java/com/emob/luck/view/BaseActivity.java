package com.emob.luck.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.emob.lib.stats.FlurryUtil;
import com.emob.lib.stats.StatsDefines;
import com.emob.lib.stats.StatsUtil;
import com.emob.lib.util.DevicesUtils;
import com.emob.lib.util.Utils;
import com.emob.luck.ImageMemoryCache;
import com.emob.luck.SdkPreferences;
import com.emob.luck.TopSpotNativeHelper;
import com.emob.luck.common.CommonDefine;
import com.emob.luck.model.AdItem;
import com.emob.luck.model.AdItemList;
import com.emob.luck.model.EventItem;
import com.emob.luck.model.PackageElement;
import com.emob.luck.protocol.app.RecentTasksHelper;
import com.duduws.recent.R;

public class BaseActivity extends Activity implements OnItemClickListener {
//	private LinearLayout mLlVLayout;
	private ArrayList<PackageElement> mPkgList;
	private AppListAdapter mGVAdapter;
	private AdItemList mList;
	private Context mContext;
	private int mAdNum;
	
	private LinearLayout mLl;
	private GridView mGvFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.icon_file_activity);
		mContext = this.getApplicationContext();
		//注册友盟
		StatsUtil.onCreat(mContext);
		//		AnalyticsConfig.setAppkey("55ebf3d9e0f55aeae7000938");
		//		AnalyticsConfig.setChannel(CommonDefine.APP_VERSION);
		//		MobclickAgent.updateOnlineConfig(mContext);
		//		AnalyticsConfig.enableEncrypt(true);
		
		WindowManager.LayoutParams a = getWindow().getAttributes();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		a.gravity = Gravity.CENTER;
		a.dimAmount = 0.75f; // 设置遮罩透明度
		getWindow().setAttributes(a);
		
		mPkgList = new ArrayList<PackageElement>();
		mList = new AdItemList();
		mAdNum = 0;
		
		initView();
		initData();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		FlurryUtil.onStart(this, StatsDefines.APP_KEY_FLURRY);
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
		FlurryUtil.onStop(this);
		super.onStop();
	}
	
	private void initView() {
		mLl = (LinearLayout) findViewById(R.id.ll_bg);
//		FrameLayout.LayoutParams lP = new FrameLayout.LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		mLl.setOrientation(LinearLayout.VERTICAL);
//		mLl.setLayoutParams(lP);
//		mLl.setGravity(Gravity.CENTER_VERTICAL);
		//mTvTitle = (TextView) findViewById(R.id.tv_app_name);
		mGvFile = (GridView) findViewById(R.id.gv);
		mGvFile.setOnItemClickListener(this);
	}

	private void initData() {
		mPkgList = RecentTasksHelper.getRecentApps(mContext);
//		if(DevicesUtils.getSDKVersion() >= 11) {
//			int channel = DspHelper.getFolderIconChannel(getApplicationContext());
////		boolean isNativeEnabled = InmobiNativeHelper.getInstance(mContext).isNativeEnabled();
//			if (channel != CommonDefine.DSP_GLOABL) {
//				mList = AdTableDBHelper.getAds(Value.NATIVE_FOLDER_ICON, mContext);
//				if(null != mList) {
//					mAdNum = mList.size();
//				} 
//				
//				if(mAdNum > 0) {
//					InMobi.initialize(mContext, CommonDefine.FOLDER_ICON_KEY_INMOBI);
////					FolderIconNativeHelper.getInstance(getApplicationContext()).recodeShow(mLl);
//					PackageElement pkgItem = generateRecentItem();
//					if (mPkgList == null) {
//						mPkgList = new ArrayList<PackageElement>(1);
//						mPkgList.add(pkgItem);
//						EventTableDBHelper.insertData(mContext, EventItem.SHOW_TYPE_INMOBI_NATIVE_ICON, CommonDefine.DSP_CHANNEL_INMOBI,
//								CommonDefine.AD_POSITION_FOLDER_ICON, EventItem.EVENT_TYPE_SHOW);
//						Map<String, String> map = new HashMap<String, String>();
//						map.put("PCK", EventItem.SHOW_TYPE_INMOBI_NATIVE_ICON);
//						map.put("ACTION", EventItem.EVENT_TYPE_SHOW + "");
//						UmengUtils.onEvent(mContext, LogEvents.FOLDER_ICON_INMOBI, map);
//						FlurryUtil.onEvent(mContext, LogEvents.FOLDER_ICON_INMOBI, map);
//					}
//					else {
//						if(mPkgList.size() > 4) {
//							mPkgList.add(1, pkgItem);
//							EventTableDBHelper.insertData(mContext, EventItem.SHOW_TYPE_INMOBI_NATIVE_ICON, CommonDefine.DSP_CHANNEL_INMOBI,
//									CommonDefine.AD_POSITION_FOLDER_ICON, EventItem.EVENT_TYPE_SHOW);
//							Map<String, String> map = new HashMap<String, String>();
//							map.put("PCK", EventItem.SHOW_TYPE_INMOBI_NATIVE_ICON);
//							map.put("ACTION", EventItem.EVENT_TYPE_SHOW + "");
//							UmengUtils.onEvent(mContext, LogEvents.FOLDER_ICON_INMOBI, map);
//							FlurryUtil.onEvent(mContext, LogEvents.FOLDER_ICON_INMOBI, map);
//						} else {
//							mPkgList.add(1, pkgItem);
//							EventTableDBHelper.insertData(mContext, EventItem.SHOW_TYPE_INMOBI_NATIVE_ICON, CommonDefine.DSP_CHANNEL_INMOBI,
//									CommonDefine.AD_POSITION_FOLDER_ICON, EventItem.EVENT_TYPE_SHOW);
//							Map<String, String> map = new HashMap<String, String>();
//							map.put("PCK", EventItem.SHOW_TYPE_INMOBI_NATIVE_ICON);
//							map.put("ACTION", EventItem.EVENT_TYPE_SHOW + "");
//							UmengUtils.onEvent(mContext, LogEvents.FOLDER_ICON_INMOBI, map);
//							FlurryUtil.onEvent(mContext, LogEvents.FOLDER_ICON_INMOBI, map);
//						}
//					}
//				}
//			}
//		}
		mGVAdapter = new AppListAdapter(mContext, mPkgList);
		mGvFile.setAdapter(mGVAdapter);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(mPkgList.get(position).ismIsNative()) {
			//int index = mPkgList.get(position).getIndex();
//			InMobi.initialize(mContext, CommonDefine.FOLDER_ICON_KEY_INMOBI);
//			FolderIconNativeHelper.getInstance(getApplicationContext()).recodeClick();
//			EventTableDBHelper.insertData(mContext, EventItem.SHOW_TYPE_INMOBI_NATIVE_ICON, CommonDefine.DSP_CHANNEL_INMOBI,
//					CommonDefine.AD_POSITION_FOLDER_ICON, EventItem.EVENT_TYPE_CLICK);
//			
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("PCK", EventItem.SHOW_TYPE_INMOBI_NATIVE_ICON);
//			map.put("ACTION", EventItem.EVENT_TYPE_CLICK + "");
//			UmengUtils.onEvent(mContext, LogEvents.FOLDER_ICON_INMOBI, map);
//			FlurryUtil.onEvent(mContext, LogEvents.FOLDER_ICON_INMOBI, map);
//			Utils.openUrl(mContext, mPkgList.get(position).getmLandingUrl());
		} else {
			DevicesUtils.runApps(BaseActivity.this, mPkgList.get(position).getmPackageName());
			try {
//				EventTableDBHelper.insertData(mContext, mPkgList.get(position).getmPackageName(), 0,
//						0, EventItem.EVENT_TYPE_CLICK_APP);
				
				Map<String, String> map = new HashMap<String, String>();
				map.put("PCK", mPkgList.get(position).getmPackageName());
				map.put("ACTION", EventItem.EVENT_TYPE_CLICK_APP + "");
//				UmengUtils.onEvent(mContext, StatsDefines.FOLDER_ICON_APP, map);
//				FlurryUtil.onEvent(mContext, StatsDefines.FOLDER_ICON_APP, map);
//				StatsUtil.onEvent(mContext, StatsDefines.FOLDER_ICON_APP, map);
				
				StatsUtil.onEventBackground(mContext, StatsDefines.FOLDER_ICON_APP, mPkgList.get(position).getmPackageName());
				
			} catch (Exception e) {
				e.printStackTrace(); 
			}
		}
		finish();
	}
	
	@Override
	protected void onDestroy() {
		if(mAdNum > 0) {
//			FolderIconNativeHelper.getInstance(getApplicationContext()).clearNativeAd();
//			AdTableDBHelper.deleteAds(mList);
//			for (int i = 0; i < mList.size(); i++) {
//				ImageMemoryCache.getInstance(mContext).removeOneImg(mList.get(i).iconUrl);
//			}
		}

		// 更新广告
		if(DevicesUtils.getSDKVersion() >= 11) {
//			int folderIconOn = SdkPreferences.getInstance(mContext).getInt(CommonDefine.DSP_CHANNEL_INMOBI, SdkPreferences.FOLDER_ICON_ONOFF, 1);
			int topSpotOn = SdkPreferences.getInstance(mContext).getInt(CommonDefine.DSP_CHANNEL_INMOBI, SdkPreferences.TOP_SPOT_ONOFF, 1);
//			if(folderIconOn == 1) {
//				FolderIconNativeHelper.getInstance(mContext).loadFolderIconNative();
//
//				Map<String, String> map = new HashMap<String, String>();
//				map.put("PCK", EventItem.SHOW_TYPE_INMOBI_NATIVE_ICON);
//				map.put("ACTION", EventItem.EVENT_TYPE_REQUEST + "");
//				UmengUtils.onEvent(mContext, LogEvents.FOLDER_ICON_INMOBI, map);
//				FlurryUtil.onEvent(mContext, LogEvents.FOLDER_ICON_INMOBI, map);
//			}
			if(topSpotOn == 1) {
				TopSpotNativeHelper.getInstance(mContext).loadTopSpotNative();

//				Map<String, String> map = new HashMap<String, String>();
//				map.put("PCK", EventItem.SHOW_TYPE_INMOBI_NATIVE_SPOT);
//				map.put("ACTION", EventItem.EVENT_TYPE_REQUEST + "");
////				UmengUtils.onEvent(mContext, StatsDefines.TOP_SPOT_INMOBI, map);
////				FlurryUtil.onEvent(mContext, StatsDefines.TOP_SPOT_INMOBI, map);
//				StatsUtil.onEventBackground(mContext, StatsDefines.TOP_SPOT_INMOBI, map);


				StatsUtil.onEventOfferBackground(mContext,EventItem.SHOW_TYPE_INMOBI_NATIVE_SPOT,
						EventItem.EVENT_TYPE_REQUEST,
						CommonDefine.DSP_CHANNEL_INMOBI, CommonDefine.AD_POSITION_TOP_SPOT);

			}
		}
		super.onDestroy();
	}
	
	@SuppressWarnings("deprecation")
	protected PackageElement generateRecentItem() {
		if (mList != null && mList.size() > 0) {
			AdItem adItem = mList.get(0);
			if (adItem != null) {
				PackageElement item = new PackageElement();
				item.setIndex(adItem.getIndex());
				item.setLabel(adItem.getTitleName());
				item.setmIsNative(true);
				Bitmap bitmap = ImageMemoryCache
						.getInstance(mContext)
						.getBitmapFromCache(adItem.iconUrl);
//				Bitmap bitmap = null;
				if (bitmap == null) {
					//Resources res = getResources();
					Drawable d = Utils.getBitmapdDrawable("ic_ps.png");
					bitmap = ((BitmapDrawable)d).getBitmap();
				} 
				
				item.setmIcon(new BitmapDrawable(bitmap));
				item.setmUrl(adItem.getIconUrl());
				item.setmLandingUrl(adItem.getLandingUrl());
				
				return item;
			}
		}
		
		return null;
	}
}
