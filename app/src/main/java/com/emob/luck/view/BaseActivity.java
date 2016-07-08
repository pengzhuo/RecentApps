package com.emob.luck.view;

import java.util.ArrayList;

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

import com.emob.lib.stats.StatsDefines;
import com.emob.lib.stats.StatsUtil;
import com.emob.lib.util.DevicesUtils;
import com.emob.lib.util.Utils;
import com.emob.luck.ImageMemoryCache;
import com.emob.luck.SdkPreferences;
import com.emob.luck.common.CommonDefine;
import com.emob.luck.model.AdItem;
import com.emob.luck.model.AdItemList;
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
		mLl = (LinearLayout) findViewById(R.id.ll_bg);
		mGvFile = (GridView) findViewById(R.id.gv);
		mGvFile.setOnItemClickListener(this);
	}

	private void initData() {
		mPkgList = RecentTasksHelper.getRecentApps(mContext);
		mGVAdapter = new AppListAdapter(mContext, mPkgList);
		mGvFile.setAdapter(mGVAdapter);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(mPkgList.get(position).ismIsNative()) {

		} else {
			DevicesUtils.runApps(BaseActivity.this, mPkgList.get(position).getmPackageName());
		}
		finish();
	}
	
	@Override
	protected void onDestroy() {
		// 更新广告
		if(DevicesUtils.getSDKVersion() >= 11) {
			int topSpotOn = SdkPreferences.getInstance(mContext).getInt(CommonDefine.DSP_CHANNEL_INMOBI, SdkPreferences.TOP_SPOT_ONOFF, 1);

			if(topSpotOn == 1) {
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
