package com.emob.luck.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cmcm.adsdk.interstitial.InterstitialAdCallBack;
import com.cmcm.adsdk.interstitial.InterstitialAdManager;
import com.emob.lib.stats.StatsUtil;
import com.emob.luck.DspHelper;
import com.emob.luck.SdkHelper;
import com.emob.luck.SdkPreferences;
import com.emob.luck.common.CommonDefine;
import com.emob.luck.common.DefaultValues;
import com.emob.luck.common.Value;

/**
 * Created by Pengz on 16/6/22.
 */
public class CmActivity extends Activity{
    private static final String TAG = "CmActivity";
    private static InterstitialAdManager interstitialAdManager = null;
    private int mSdkChannel;
    private String mPackageName;
    private int triggerType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSdkChannel = CommonDefine.DSP_CHANNEL_CM;
        mPackageName = "";
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            mPackageName = intent.getExtras().getString(Value.INTENT_EXTRA_PKGNAME);
            mSdkChannel = intent.getExtras().getInt(Value.INTENT_EXTRA_CHANNEL, CommonDefine.DSP_CHANNEL_CM);
            triggerType = intent.getExtras().getInt(Value.AD_TRIGGER_TYPE);
        }

        Log.e(TAG, "#### CmActivity onCreate " + mSdkChannel + " " + mPackageName + " " + triggerType);

        String randomString = SdkHelper.getSdkSpotKeyString(getApplicationContext(), CommonDefine.DSP_CHANNEL_CM);
        interstitialAdManager = new InterstitialAdManager(this, randomString);
        interstitialAdManager.setInterstitialCallBack(callBack);
        interstitialAdManager.loadAd();
        StatsUtil.onEventEx(this, CommonDefine.DSP_CHANNEL_CM, triggerType, CommonDefine.AD_TYPE_SPOT, CommonDefine.AD_RESULT_REQUEST);
        finish();
    }

    InterstitialAdCallBack callBack = new InterstitialAdCallBack() {
        @Override
        public void onAdLoadFailed(int i) {
            Log.e(TAG, "#### onAdLoadFailed " + i);
            StatsUtil.onEventEx(CmActivity.this, CommonDefine.DSP_CHANNEL_CM, triggerType, CommonDefine.AD_TYPE_SPOT, CommonDefine.AD_RESULT_FAIL);
            SdkPreferences sdkPref = SdkPreferences.getInstance(CmActivity.this);
            int _num = sdkPref.getInt(CommonDefine.DSP_CHANNEL_CM, SdkPreferences.SDK_SITE_HAVE_TRIES_NUM, 0) + 1;
            Log.e("", "#### cmcm tries num is " + _num);
            sdkPref.setInt(CommonDefine.DSP_CHANNEL_CM, SdkPreferences.SDK_SITE_HAVE_TRIES_NUM, _num);
            if (_num >= sdkPref.getInt(CommonDefine.DSP_CHANNEL_CM, SdkPreferences.SDK_SITE_TRIES_NUM, DefaultValues.SDK_SITE_TRIES_NUM)){
                sdkPref.setBoolean(CommonDefine.DSP_CHANNEL_CM, SdkPreferences.SDK_SITE_TRIES_OVER, true);
                sdkPref.setLong(CommonDefine.DSP_CHANNEL_CM, SdkPreferences.SDK_SITE_TRIES_TIME, System.currentTimeMillis());
            }
        }

        @Override
        public void onAdLoaded() {
            showFolder();
            Log.e(TAG, "#### onAdLoaded ");
            DspHelper.updateSdkSpotCount(CmActivity.this, mSdkChannel);
            StatsUtil.onEventEx(CmActivity.this, CommonDefine.DSP_CHANNEL_CM, triggerType, CommonDefine.AD_TYPE_SPOT, CommonDefine.AD_RESULT_SUCCESS);
            interstitialAdManager.showAd();
        }

        @Override
        public void onAdClicked() {
            Log.e(TAG, "#### onAdClicked ");
            StatsUtil.onEventEx(CmActivity.this, CommonDefine.DSP_CHANNEL_CM, triggerType, CommonDefine.AD_TYPE_SPOT, CommonDefine.AD_RESULT_CLICK);
            if (UsedAppsListActivity.instance != null){
                UsedAppsListActivity.instance.finish();
                UsedAppsListActivity.instance = null;
            }
        }

        @Override
        public void onAdDisplayed() {
            Log.e(TAG, "#### onAdDisplayed ");
            StatsUtil.onEventEx(CmActivity.this, CommonDefine.DSP_CHANNEL_CM, triggerType, CommonDefine.AD_TYPE_SPOT, CommonDefine.AD_RESULT_SHOW);
        }

        @Override
        public void onAdDismissed() {
            Log.e(TAG, "#### onAdDismissed ");
            StatsUtil.onEventEx(CmActivity.this, CommonDefine.DSP_CHANNEL_CM, triggerType, CommonDefine.AD_TYPE_SPOT, CommonDefine.AD_RESULT_CLOSE);
            if (UsedAppsListActivity.instance != null){
                UsedAppsListActivity.instance.finish();
                UsedAppsListActivity.instance = null;
            }
        }
    };

    private void showFolder() {
        Log.d(TAG, "#### CmActivity.showFolder begin");
        try {
            Intent intent = new Intent();
            intent.setClass(this, UsedAppsListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            this.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "#### CmActivity.showFolder end");
    }
}
