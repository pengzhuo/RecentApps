package com.emob.luck.view;


import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emob.lib.log.EmobLog;
import com.emob.lib.util.Utils;
import com.emob.luck.TopSpotNativeHelper;
import com.emob.luck.common.CommonDefine;
import com.emob.luck.db.EventTableDBHelper;
import com.emob.luck.model.EventItem;


public class FloatView extends View implements OnClickListener {
	private WindowManager mWManager; 					// WindowManager
	private WindowManager.LayoutParams mSpotParams; 	// WindowManager参数
	private WindowManager.LayoutParams mBannerParams; 	// WindowManager参数
	public RelativeLayout mSpotView;
	public RelativeLayout mBannerView;
	private Context mContext;
	private ImageView mIvDelete;
	private ImageView mIvAd;
	private LinearLayout mLlSpotView;
	private ImageView mBannerIv;
	private ImageView mBannerDel;
	private int mWidth;
	private String mSpotUrl; 
	private String mBannerUrl;
	private TextView mSpotTvTimer;
	private TextView mBannerTvTimer;
	private static final int SPOT_DELETE = 102;
	private static final int SPOT_AD = 103;
	private static final int BANNER_DELETE = 202;
	private static final int BANNER_AD = 201;
	
	public static Map<String, Object> mapView = new HashMap<String, Object>();
	public static final String SPOT_VIEW = "spotview";
	private Timer mSpotTimer; 
	private Timer mBannertimer; 
	private TimerTask mBannerTimerTask;
	private TimerTask mSpotTimerTask;
	private int mSpotRecLen ;  
	private int mBannerRecLen ;  
	private final int SPOT_MSG = 1;
	private final int BANNER_MSG = 2;
	private static final String TAG = "FloatView";
	
	//倒计时显示
	private Handler handler = new Handler(){ 
        @Override 
        public void handleMessage(Message msg){ 
            switch (msg.what) { 
            case SPOT_MSG: 
            	mSpotTvTimer.setText("" + mSpotRecLen); 
                if(mSpotRecLen < 0){ 
                	mSpotTvTimer.setVisibility(View.GONE); 
                	EmobLog.d(TAG, "mSpotRecLen < 0");
                    removeSpotView();
                    cancelSpotTimer();
                } 
                break;
            case BANNER_MSG: 
            	mBannerTvTimer.setText("" + mBannerRecLen); 
            	if(mBannerRecLen < 0){ 
            		mBannerTvTimer.setVisibility(View.GONE); 
            		removeBannerView();
            		cancelBannerTimer();
            	} 
            	break;
            } 
            
        } 
    };  
    
	public FloatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mWManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		
		//获取屏幕像素相关信息
		DisplayMetrics dm = new DisplayMetrics();
		mWManager.getDefaultDisplay().getMetrics(dm);
		mWidth = dm.widthPixels;
		mSpotView = new RelativeLayout(mContext);
	}

	/**
	 * 绘制界面，显示spot广告
	 * @param landingUrl 点击的url
	 * @param url 广告图片url
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	public void showSpotView(String landingUrl, Bitmap bitmap) {
		if (bitmap == null) {
			return;
		}
		
		if (TextUtils.isEmpty(landingUrl)) {
			return;
		}
		
		mSpotUrl = landingUrl;
		RelativeLayout.LayoutParams spotRlLp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mSpotView.setLayoutParams(spotRlLp);
		mSpotView.setBackgroundColor(0xff542456);
		
		mSpotParams = new WindowManager.LayoutParams();
		mSpotParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 系统提示window
		mSpotParams.format = PixelFormat.TRANSLUCENT;// 支持透明
		// mParams.format = PixelFormat.RGBA_8888;
		mSpotParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 焦点
		mSpotParams.width = dip2px(mContext, 300) + dip2px(mContext, 20);// 窗口的宽和高
		mSpotParams.height = dip2px(mContext, 250) + dip2px(mContext, 20);
		mSpotParams.x = 0;// 窗口位置的偏移量
		mSpotParams.y = 0;
		mSpotParams.gravity = Gravity.CENTER;
		// mParams.alpha = 0.45f;//窗口的透明度
		
		mLlSpotView = new LinearLayout(mContext);
		LinearLayout.LayoutParams llSpotLp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		llSpotLp.setMargins(0, Utils.getDip(mContext, 8), 0, 0);
		mLlSpotView.setLayoutParams(llSpotLp);
		mLlSpotView.setPadding(Utils.getDip(mContext, 2), Utils.getDip(mContext, 2), 
				Utils.getDip(mContext, 2), Utils.getDip(mContext, 2));
		
		mIvAd = new ImageView(mContext);
		LinearLayout.LayoutParams ivAdLp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		ivAdLp.setMargins(Utils.getDip(mContext, 2), 
				Utils.getDip(mContext, 2), 
				Utils.getDip(mContext, 2), 
				Utils.getDip(mContext, 2));
		mIvAd.setLayoutParams(ivAdLp);
		mIvAd.setId(SPOT_AD);
		if(bitmap != null) {
			mIvAd.setBackgroundDrawable(new BitmapDrawable(bitmap));
		} else {
			return;
		}
		//		mIvAd.setBackground(new BitmapDrawable(ImageMemoryCache
		//				.getInstance(mContext)
		//				.getBitmapFromCache(url)));
		mIvAd.setOnClickListener(this);
		mLlSpotView.addView(mIvAd);
		mSpotView.addView(mLlSpotView);
		
		mIvDelete = new ImageView(mContext);
		RelativeLayout.LayoutParams deleteLp = new RelativeLayout.LayoutParams(
				Utils.getDip(mContext, 25), Utils.getDip(mContext, 25));
		deleteLp.setMargins(0, 0, 0, 0);
		deleteLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		mIvDelete.setLayoutParams(deleteLp);
		mIvDelete.setBackgroundDrawable(Utils.getBitmapdDrawable("icon_delete.png"));
		mIvDelete.setOnClickListener(this);
		mIvDelete.setId(SPOT_DELETE);
		mSpotView.addView(mIvDelete);
		
		mSpotTvTimer = new TextView(mContext);
		RelativeLayout.LayoutParams tvTimerLp = new RelativeLayout.LayoutParams(
				Utils.getDip(mContext, 35), Utils.getDip(mContext, 35));
		tvTimerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		tvTimerLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		tvTimerLp.setMargins(0, 0, Utils.getDip(mContext, 13), Utils.getDip(mContext, 13));
		mSpotTvTimer.setLayoutParams(tvTimerLp);
		mSpotTvTimer.setTextColor(0xffffffff);
		mSpotTvTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
		mSpotTvTimer.setBackgroundDrawable(Utils.getBitmapdDrawable("eyu_icon_notice.png"));
		
		mSpotTvTimer.setPadding(0, 0, 0, Utils.getDip(mContext, 5));
		mSpotTvTimer.setGravity(Gravity.CENTER);
		mSpotView.addView(mSpotTvTimer);
		
		mSpotView.setFocusable(true);//comment by danielinbiti,设置view能够接听事件，标注1    
		mSpotView.setFocusableInTouchMode(true); //comment by danielinbiti,设置view能够接听事件 标注2  
		mSpotView.clearFocus();
		
		if (!mSpotView.isShown()) {
			EmobLog.d("TopSpot", "mSpotView.isShown()");
			EventTableDBHelper.insertData(mContext,
					EventItem.SHOW_TYPE_INMOBI_NATIVE_SPOT, CommonDefine.DSP_CHANNEL_INMOBI,
					CommonDefine.AD_POSITION_TOP_SPOT, EventItem.EVENT_TYPE_SHOW);
			mapView.put("SpotView", mSpotView);
			mWManager.addView(mSpotView, mSpotParams);
			TopSpotNativeHelper.getInstance(mContext).recodeShow(mSpotView);
//			DspHelper.updateTopSpotRecord(mContext, CommonDefine.DSP_CHANNEL_INMOBI);
			if(mSpotTimer == null ) {
				mSpotTimer = new Timer(); 
			} 
			mSpotRecLen = 10;
			mSpotTimerTask = new TimerTask() {
				@Override
				public void run() {
					mSpotRecLen--;
					Message message = new Message();
					message.what = SPOT_MSG;
					handler.sendMessage(message);
				}
			};
			mSpotTimer.schedule(mSpotTimerTask, 1000, 1000);
		}
		
	}

    
    /**
     * 绘banner界面，显示广告
     * @param landingUrl
     * @param url
     */
	@SuppressWarnings("deprecation")
	public void showBannerView(String landingUrl, Bitmap bitmap) {
		if (bitmap == null) {
			return;
		}
		
		if (TextUtils.isEmpty(landingUrl)) {
			return;
		}
		
		mBannerUrl = landingUrl;
		mBannerView = new RelativeLayout(mContext);
		RelativeLayout.LayoutParams bannerRlLp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		bannerRlLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		mBannerView.setLayoutParams(bannerRlLp);
		
		//mBannerView = mBannerInflater.inflate(R.layout.add_banner_view, null);
		mBannerParams = new WindowManager.LayoutParams();
		mBannerParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 系统提示window
		mBannerParams.format = PixelFormat.TRANSLUCENT;// 支持透明
		// mParams.format = PixelFormat.RGBA_8888;
		mBannerParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 焦点
		mBannerParams.width = mWidth;// 窗口的宽和高
		mBannerParams.height = mWidth*50/320;
		mBannerParams.x = 0;// 窗口位置的偏移量
		mBannerParams.y = 0;
		mBannerParams.gravity = Gravity.BOTTOM;
		// mParams.alpha = 0.45f;//窗口的透明度
		mBannerIv = new ImageView(mContext);
		RelativeLayout.LayoutParams adRlLp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mBannerIv.setLayoutParams(adRlLp);
		//mBannerIv.setBackgroundColor(0xff467896);
		if(bitmap != null) {
			mBannerIv.setBackgroundDrawable(new BitmapDrawable(bitmap));
		} else {
			return;
		}
		mBannerIv.setId(BANNER_AD);
		mBannerIv.setOnClickListener(this);
		mBannerView.addView(mBannerIv);
		
		mBannerDel = new ImageView(mContext);
		RelativeLayout.LayoutParams bannerDelRlLp = new RelativeLayout.LayoutParams(
				Utils.getDip(mContext, 20), Utils.getDip(mContext, 20));
		bannerDelRlLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		bannerDelRlLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		mBannerDel.setLayoutParams(bannerDelRlLp);
		mBannerDel.setId(BANNER_DELETE);
		mBannerDel.setOnClickListener(this);
		mBannerDel.setBackgroundDrawable(Utils.getBitmapdDrawable("icon_delete.png"));
		mBannerView.addView(mBannerDel);
		
		mBannerTvTimer = new TextView(mContext);
		RelativeLayout.LayoutParams tvTimerLp = new RelativeLayout.LayoutParams(
				Utils.getDip(mContext, 35), Utils.getDip(mContext, 35));
		tvTimerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		tvTimerLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		tvTimerLp.setMargins(0, 0, Utils.getDip(mContext, 8), Utils.getDip(mContext, 8));
		mBannerTvTimer.setLayoutParams(tvTimerLp);
		mBannerTvTimer.setTextColor(0xffffffff);
		mBannerTvTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
		mBannerTvTimer.setPadding(0, 0, 0, Utils.getDip(mContext, 5));
		mBannerTvTimer.setBackgroundDrawable(Utils.getBitmapdDrawable("eyu_icon_notice.png"));
		mBannerTvTimer.setGravity(Gravity.CENTER);
		mBannerView.addView(mBannerTvTimer);
		
		mBannerView.setFocusable(true);//comment by danielinbiti,设置view能够接听事件，标注1    
		mBannerView.setFocusableInTouchMode(true); //comment by danielinbiti,设置view能够接听事件 标注2  
		
		
		if(!mBannerView.isActivated()) {
			EventTableDBHelper.insertData(mContext,
					EventItem.SHOW_TYPE_INMOBI_NATIVE_BANNER, CommonDefine.DSP_CHANNEL_INMOBI,
					CommonDefine.AD_POSITION_TOP_BANNER,
					EventItem.EVENT_TYPE_SHOW);
			mWManager.addView(mBannerView, mBannerParams);
//			TopBannerNativeHelper.getInstance(mContext).recodeShow(mBannerView);
//			DspHelper.updateTopBannerRecord(mContext, CommonDefine.DSP_CHANNEL_INMOBI);
			if (mBannertimer == null) {
				mBannertimer = new Timer();
			}
			mBannerRecLen = 10;
			mBannerTimerTask = new TimerTask() {
				@Override
				public void run() {
					mBannerRecLen--;
					Message message = new Message();
					message.what = BANNER_MSG;
					handler.sendMessage(message);
				}
			};
			
			mBannertimer.schedule(mBannerTimerTask, 1000, 1000);
		}
	}
	
	    
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	private int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == SPOT_DELETE) {
			if (mSpotView.isShown()) {
				cancelSpotTimer();
				removeSpotView();
			}
		}
		
		if (id == SPOT_AD) {
			if (mSpotView.isShown()) {
				cancelSpotTimer();
				EventTableDBHelper.insertData(mContext,
						EventItem.SHOW_TYPE_INMOBI_NATIVE_SPOT, CommonDefine.DSP_CHANNEL_INMOBI,
						CommonDefine.AD_POSITION_TOP_SPOT, EventItem.EVENT_TYPE_CLICK);
				TopSpotNativeHelper.getInstance(mContext).recodeClick();
				Utils.openUrl(mContext, mSpotUrl);
				removeSpotView();
			}
		}
		
		if(id == BANNER_AD) {
			if(mBannerView.isShown()) {
				cancelBannerTimer();
				EventTableDBHelper.insertData(mContext,
						EventItem.SHOW_TYPE_INMOBI_NATIVE_BANNER, CommonDefine.DSP_CHANNEL_INMOBI,
						CommonDefine.AD_POSITION_TOP_BANNER, EventItem.EVENT_TYPE_CLICK);
//				TopBannerNativeHelper.getInstance(mContext).recodeClick();
				Utils.openUrl(mContext, mBannerUrl);
				removeBannerView();
			}
		}
		
		if(id == BANNER_DELETE) {
			if(mBannerView.isShown()) {
				cancelBannerTimer();
				removeBannerView();
			}
		}
	}
	
	/**
	 * 移除spot view, 删除旧数据, 请求新数据
	 */
	public void removeSpotView() {
		EmobLog.d(TAG, "removeSpotView");
		if (mWManager != null) {
			mWManager.removeView(mSpotView);
			EmobLog.d(TAG, "mWManager != null");
		}
		EventTableDBHelper.insertData(mContext,
				EventItem.SHOW_TYPE_INMOBI_NATIVE_SPOT, CommonDefine.DSP_CHANNEL_INMOBI,
				CommonDefine.AD_POSITION_TOP_SPOT, EventItem.EVENT_TYPE_CLOSE);
		TopSpotNativeHelper.getInstance(mContext).clearNativeAd();
//		TopSpotNativeHelper.getInstance(mContext).loadTopSpotNative();
	}
	
	/**
	 * 移除banner view, 删除旧数据, 请求新数据
	 */
	public void removeBannerView() {
		if(mBannerView != null) {
			mWManager.removeView(mBannerView);
		}
		EventTableDBHelper.insertData(mContext,
				EventItem.SHOW_TYPE_INMOBI_NATIVE_BANNER, CommonDefine.DSP_CHANNEL_INMOBI,
				CommonDefine.AD_POSITION_TOP_BANNER, EventItem.EVENT_TYPE_CLOSE);
//		TopBannerNativeHelper.getInstance(mContext).clearNativeAd();
//		TopBannerNativeHelper.getInstance(mContext).loadTopBannerNative();
	}
	
	private void cancelBannerTimer() {
		if(mBannertimer != null){
			mBannertimer.cancel();
			mBannertimer = null;
		}
		if (mBannerTimerTask != null) {
			mBannerTimerTask.cancel();
			mBannerTimerTask = null;
		}
	}
	
	private void cancelSpotTimer() {
		if(mSpotTimer != null){
			mSpotTimer.cancel();
			mSpotTimer = null;
		}
		if (mSpotTimerTask != null) {
			mSpotTimerTask.cancel();
			mSpotTimerTask = null;
		}
	}
}
