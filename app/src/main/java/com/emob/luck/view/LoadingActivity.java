package com.emob.luck.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.emob.lib.log.EmobLog;
import com.emob.lib.stats.FlurryUtil;
import com.emob.lib.stats.StatsDefines;
import com.emob.lib.stats.StatsUtil;
import com.emob.luck.AdsPreferences;
import com.emob.luck.DspHelper;
import com.emob.luck.SdkHelper;
import com.emob.luck.common.CommonDefine;
import com.emob.luck.common.Value;
import com.emob.luck.model.EventItem;
import com.duduws.recent.R;

public class LoadingActivity extends Activity {
	private final static String TAG = "LoadingActivity";
//	ImageView im_scan;
//	ImageView im_dian;
//	private Timer mTimer;
	
	private static String mPackageName;
	private Context mContext;
	private static int mSdkChannel;
	private static int triggerType = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_imitate_radar);
		mContext = this.getApplicationContext();
		EmobLog.d(TAG, "#### LoadingActivity.onCreate");
		EmobLog.d(TAG, "#### LoadingActivity.onCreate, context="+mContext);
		//注册友盟
		StatsUtil.onCreat(mContext);
		
		WindowManager.LayoutParams a = getWindow().getAttributes();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		a.gravity = Gravity.CENTER;
		a.dimAmount = 0.75f; // 设置遮罩透明度
		getWindow().setAttributes(a);
		
		Intent intent = getIntent();
		String mPackageName = "";
		int mSdkChannel = CommonDefine.DSP_CHANNEL_ADMOB;
		if (intent != null && intent.getExtras() != null) {
			mPackageName = intent.getExtras().getString(Value.INTENT_EXTRA_PKGNAME);
			mSdkChannel = intent.getExtras().getInt(Value.INTENT_EXTRA_CHANNEL, CommonDefine.DSP_CHANNEL_ADMOB);
			triggerType = intent.getExtras().getInt(Value.AD_TRIGGER_TYPE);
		}
		String randomString = SdkHelper.getSdkSpotKeyString(getApplicationContext(), mSdkChannel);
		EmobLog.e(TAG, "#### LoadingActivity.onCreate, mPackageName="+mPackageName + " " + triggerType);
		EmobLog.e(TAG, "#### LoadingActivity.onCreate, mSdkChannel=" + mSdkChannel);
		EmobLog.e(TAG, "#### LoadingActivity.onCreate, randomString=" + randomString);
		AdsPreferences.getInstance(mContext).setString(AdsPreferences.LOADING_PKGNAME, mPackageName);
		AdsPreferences.getInstance(mContext).setInt(AdsPreferences.LOADING_CHANNEL, mSdkChannel);

		initView();

		AmSpotHelper amSpot = AmSpotHelper.getInstance();
		amSpot.init(getApplicationContext());
		amSpot.setSdkChannel(mSdkChannel);
		amSpot.setPackageName(mPackageName);
		amSpot.setRandomString(randomString);
		int adPos = getOfferPosition(mPackageName);
		amSpot.setAdPos(adPos);
		amSpot.setHandler(mHandler);
		amSpot.load();
		StatsUtil.onEventEx(this, CommonDefine.DSP_CHANNEL_ADMOB, triggerType, CommonDefine.AD_TYPE_SPOT, CommonDefine.AD_RESULT_REQUEST);
//		Toast.makeText(getApplicationContext(), randomString, Toast.LENGTH_LONG).show();
		Log.e(TAG, "##### " + randomString);
		
//		setTimer();
		EmobLog.d(TAG, "#### LoadingActivity.onCreate end");
		finish();
		
//		new Thread() {
//			public void run() {
//				try {
//					Thread.sleep(100);
//					 
//				} catch (InterruptedException e) {
//				} finally {
//					mHandler.sendEmptyMessage(0); //告诉主线程执行任务  
//				}
//			}
//		}.start();
		
	}
	
	@Override
	protected void onStart() {
		EmobLog.d(TAG, "#### LoadingActivity.onStart");
		super.onStart();
		FlurryUtil.onStart(this, StatsDefines.APP_KEY_FLURRY);
	};
	
	@Override
	protected void onResume() {
		EmobLog.d(TAG, "#### LoadingActivity.onResume");
		super.onResume();
		StatsUtil.onResume(mContext);
	}
	
	@Override
	protected void onPause() {
		EmobLog.d(TAG, "#### LoadingActivity.onPause");
		super.onPause();
		StatsUtil.onPause(mContext);
	}
	
	@Override
	protected void onStop() {
		EmobLog.d(TAG, "#### LoadingActivity.onStop");
		FlurryUtil.onStop(this);
		super.onStop();
	}
	
	private void initView() {
	}

	@Override
	protected void onDestroy() {
		EmobLog.d(TAG, "#### LoadingActivity.onDestroy");
//		cancleTimer();
//		im_scan.clearAnimation();
//		im_dian.clearAnimation();
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (mDisableBack) {
//			if(keyCode == KeyEvent.KEYCODE_BACK){
//				EmobLog.e("key", "onKeyDown back");
//				return true;
//			}
//		}
	  	return super.onKeyDown(keyCode, event);
	}
	private static final long PRELOAD_TIMEOUT = 5 * 1000L; // 60秒
	
	private void setTimer() {
//		mTimer = new Timer();
//        TimerTask tt = new TimerTask() {
//            @Override
//            public void run() {
//            	EmobLog.d(TAG, "LoadingActivity timeout");
//                cancleTimer();
//                finish();
//            }
//        };
//        mTimer.schedule(tt, PRELOAD_TIMEOUT);
	}
	
	private void cancleTimer() {
//		if (mTimer != null) {
//			EmobLog.e(TAG, "LoadingActivity cancleTimer");
//			mTimer.cancel();
//			mTimer.purge();
//		}
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			mSdkChannel = AdsPreferences.getInstance(mContext).getInt(AdsPreferences.LOADING_CHANNEL, mSdkChannel);
			mPackageName = AdsPreferences.getInstance(mContext).getString(AdsPreferences.LOADING_PKGNAME, mPackageName);
			EmobLog.e(TAG, "#### handleMessage, channel="+mSdkChannel);
			EmobLog.e(TAG, "#### handleMessage, mPackageName="+mPackageName);
			switch (msg.what) {
			case AmListener.AD_REQUEST_FAILED:
				EmobLog.d(TAG, "#### LoadingActivity.handleMessage onAdFailedToLoad");
				// 记录请求广告失败行为
				try {
					StatsUtil.onEventEx(LoadingActivity.this, CommonDefine.DSP_CHANNEL_ADMOB, triggerType, CommonDefine.AD_TYPE_SPOT, CommonDefine.AD_RESULT_FAIL);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case AmListener.AD_REQUEST_SUCCEEDED:
				EmobLog.d(TAG, "#### LoadingActivity.handleMessage onAdLoaded");
//				showInterstitial();
				showFolder();
				DspHelper.updateSdkSpotCount(mContext, mSdkChannel);
				StatsUtil.onEventEx(LoadingActivity.this, CommonDefine.DSP_CHANNEL_ADMOB, triggerType, CommonDefine.AD_TYPE_SPOT, CommonDefine.AD_RESULT_SUCCESS);
				finish();
				break;
			case AmListener.ON_CLICK:
				EmobLog.d(TAG, "#### LoadingActivity.handleMessage onAdLeftApplication");
				// 记录点击行为
				try {
					StatsUtil.onEventEx(LoadingActivity.this, CommonDefine.DSP_CHANNEL_ADMOB, triggerType, CommonDefine.AD_TYPE_SPOT, CommonDefine.AD_RESULT_CLICK);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case AmListener.ON_DISMISS_CLOSE_AD:
				EmobLog.d(TAG, "#### LoadingActivity.handleMessage onAdClosed");
				// 记录关闭行为
				try {
					StatsUtil.onEventEx(LoadingActivity.this, CommonDefine.DSP_CHANNEL_ADMOB, triggerType, CommonDefine.AD_TYPE_SPOT, CommonDefine.AD_RESULT_CLOSE);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case AmListener.ON_SHOW_AD:
				EmobLog.d(TAG, "#### LoadingActivity.handleMessage onAdShow");
				// 记录展示行为
				try {
					StatsUtil.onEventEx(LoadingActivity.this, CommonDefine.DSP_CHANNEL_ADMOB, triggerType, CommonDefine.AD_TYPE_SPOT, CommonDefine.AD_RESULT_SHOW);
				} catch (Exception e) {
					e.printStackTrace();
				}
				finish();
				break;
			case 0:
				simulateHome(mContext);
				break;
			default:
				break;
			}

		};
	};
	
	private String getLogEvent(String pkgName) {
		// 默认TopExit
		String logEvent = StatsDefines.SDK_TOPEXIT_ADMOB;
		if (mSdkChannel == CommonDefine.DSP_CHANNEL_ADMOB_2) {
			logEvent = StatsDefines.SDK_TOPEXIT_ADMOB_2;
			if (EventItem.SHOW_TYPE_LOCK_SCREEN.equalsIgnoreCase(pkgName)) { // 锁屏
				logEvent = StatsDefines.SDK_LOCK_ADMOB_2;
			} else if (EventItem.SHOW_TYPE_NETWORK_ON.equalsIgnoreCase(pkgName)) { // 打开网络
				logEvent = StatsDefines.SDK_NETON_ADMOB_2;
			}
		} else {
			if (EventItem.SHOW_TYPE_LOCK_SCREEN.equalsIgnoreCase(pkgName)) { // 锁屏
				logEvent = StatsDefines.SDK_LOCK_ADMOB;
			} else if (EventItem.SHOW_TYPE_NETWORK_ON.equalsIgnoreCase(pkgName)) { // 打开网络
				logEvent = StatsDefines.SDK_NETON_ADMOB;
			}
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
	
	private void showFolder() {
		EmobLog.d(TAG, "#### LoadingActivity.showFolder begin");
		try {
			Intent intent = new Intent();
			intent.setClass(mContext, AmActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//			intent.putExtra(Value.INTENT_EXTRA_PKGNAME, mPackageName);
//			intent.putExtra(Value.INTENT_EXTRA_CHANNEL, mSdkChannel);
			mContext.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		EmobLog.d(TAG, "#### LoadingActivity.showFolder end");
	}
	
	private static void simulateHome( Context context )
	{
		try {
			Intent intent2 = new Intent(Intent.ACTION_MAIN);  
			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
			intent2.addCategory(Intent.CATEGORY_HOME);  
			context.startActivity(intent2);
		} catch (Exception e) {
		}
	}
}
