package com.emob.luck.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.emob.lib.stats.StatsDefines;
import com.emob.lib.stats.StatsUtil;
import com.emob.luck.DspHelper;
import com.emob.luck.SdkHelper;
import com.emob.luck.common.CommonDefine;
import com.emob.luck.common.Value;
import com.emob.luck.model.EventItem;
import com.facebook.FacebookSdk;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

/**
 * Created by Pengz on 16/6/22.
 */
public class FacebookActivity extends Activity implements InterstitialAdListener{
    private static final String TAG = "FacebookActivity";
    private InterstitialAd interstitialAd;
    private int mSdkChannel;
    private String mPackageName;
    private int triggerType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSdkChannel = CommonDefine.DSP_CHANNEL_FACEBOOK;
        mPackageName = "";
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            mPackageName = intent.getExtras().getString(Value.INTENT_EXTRA_PKGNAME);
            mSdkChannel = intent.getExtras().getInt(Value.INTENT_EXTRA_CHANNEL, CommonDefine.DSP_CHANNEL_FACEBOOK);
            triggerType = intent.getExtras().getInt(Value.AD_TRIGGER_TYPE);
        }

        Log.e(TAG, "#### FacebookActivity onCreate " + mSdkChannel + " " + mPackageName + " " + triggerType);
        //facebook SDK初始化
        FacebookSdk.sdkInitialize(getApplicationContext());
        String randomString = SdkHelper.getSdkSpotKeyString(getApplicationContext(), CommonDefine.DSP_CHANNEL_FACEBOOK);
        loadInterstitialAd(this, randomString);
    }

    private void loadInterstitialAd(Context context, String id){
        Log.e(TAG, "#### loadInterstitialAd " + id);
        interstitialAd = new InterstitialAd(context, id);
        interstitialAd.setAdListener(this);
        interstitialAd.loadAd();
        StatsUtil.onEventEx(this, CommonDefine.DSP_CHANNEL_FACEBOOK, triggerType, CommonDefine.AD_TYPE_SPOT, CommonDefine.AD_RESULT_REQUEST);
    }

    @Override
    public void onInterstitialDisplayed(Ad ad) {
        Log.e(TAG, "#### onInterstitialDisplayed " + ad.toString());
        // 记录展示行为
        try {
            StatsUtil.onEventEx(this, CommonDefine.DSP_CHANNEL_FACEBOOK, triggerType, CommonDefine.AD_TYPE_SPOT, CommonDefine.AD_RESULT_SHOW);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

    @Override
    public void onInterstitialDismissed(Ad ad) {
        Log.e(TAG, "#### onInterstitialDismissed " + ad.toString());
        // 记录关闭行为
        try {
            StatsUtil.onEventEx(this, CommonDefine.DSP_CHANNEL_FACEBOOK, triggerType, CommonDefine.AD_TYPE_SPOT, CommonDefine.AD_RESULT_CLOSE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

    @Override
    public void onError(Ad ad, AdError adError) {
        Log.e(TAG, "#### onError " + ad.toString() + " " + adError.getErrorCode() + " " + adError.getErrorMessage());
        // 记录请求广告失败行为
        try {
            StatsUtil.onEventEx(this, CommonDefine.DSP_CHANNEL_FACEBOOK, triggerType, CommonDefine.AD_TYPE_SPOT, CommonDefine.AD_RESULT_FAIL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

    @Override
    public void onAdLoaded(Ad ad) {
        showFolder();
        Log.e(TAG, "#### onAdLoaded " + ad.toString());
        DspHelper.updateSdkSpotCount(this, mSdkChannel);
        interstitialAd.show();
        StatsUtil.onEventEx(this, CommonDefine.DSP_CHANNEL_FACEBOOK, triggerType, CommonDefine.AD_TYPE_SPOT, CommonDefine.AD_RESULT_SUCCESS);
    }

    @Override
    public void onAdClicked(Ad ad) {
        Log.e(TAG, "#### onAdClicked " + ad.toString());
        // 记录点击行为
        try {
            StatsUtil.onEventEx(this, CommonDefine.DSP_CHANNEL_FACEBOOK, triggerType, CommonDefine.AD_TYPE_SPOT, CommonDefine.AD_RESULT_CLICK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy(){
        if (interstitialAd != null){
            Log.e(TAG, "#### onDestroy interstitialAd !");
            interstitialAd.destroy();
            interstitialAd = null;
        }
        super.onDestroy();
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

    private void showFolder() {
        Log.d(TAG, "#### FacebookActivity.showFolder begin");
        try {
            Intent intent = new Intent();
            intent.setClass(this, AmActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            this.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "#### FacebookActivity.showFolder end");
    }
}
