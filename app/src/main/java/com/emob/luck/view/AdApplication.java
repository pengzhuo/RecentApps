package com.emob.luck.view;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.cmcm.adsdk.BitmapListener;
import com.cmcm.adsdk.CMAdManager;
import com.cmcm.adsdk.CMAdManagerFactory;
import com.cmcm.adsdk.ImageDownloadListener;
import com.emob.lib.util.SysHelper;
import com.emob.lib.util.VolleyUtil;
import com.emob.luck.common.CommonDefine;

/**
 * Created by Pengz on 16/7/8.
 */
public class AdApplication extends Application{
    private static final String TAG = "AdApplication";
    private static AdApplication instance;

    public static AdApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;

        initConfigInfo();

        //初始化SDK
        String mid = CommonDefine.SDK_KEY_CM.substring(0,4);
        CMAdManager.applicationInit(getApplicationContext(), mid, "");
        CMAdManagerFactory.setImageDownloadListener(new MyImageLoadListener());
        //是否允许打印日志
        CMAdManager.enableLog();
    }

    public void initConfigInfo(){
        CommonDefine.APP_VERSION 		= SysHelper.getManifestApplicationMetaData(this, "APP_VERSION");
        CommonDefine.APP_CHANNEL_ID		= SysHelper.getManifestApplicationMetaData(this, "APP_CHANNEL_ID");
        CommonDefine.APP_COOPERATION_ID	= SysHelper.getManifestApplicationMetaData(this, "APP_COOPERATION_ID");
        CommonDefine.APP_PRODUCT_ID		= SysHelper.getManifestApplicationMetaData(this, "APP_PRODUCT_ID");
        CommonDefine.APP_PROTOCOL		= SysHelper.getManifestApplicationMetaData(this, "APP_PROTOCOL");
        //facebook
        CommonDefine.SDK_KEY_FACEBOOK   = SysHelper.getManifestApplicationMetaData(this, "SDK_KEY_FACEBOOK");
        //猎豹cm
        CommonDefine.SDK_KEY_CM         = SysHelper.getManifestApplicationMetaData(this, "SDK_KEY_CM");
        CommonDefine.SDK_KEY_CM         = CommonDefine.SDK_KEY_CM.substring(0, CommonDefine.SDK_KEY_CM.length()-1);
        //admob
        CommonDefine.SDK_KEY_ADMOB 	    = SysHelper.getManifestApplicationMetaData(this, "SDK_KEY_ADMOB");
        Log.e(TAG, "#### APP_VERSION = " + CommonDefine.APP_VERSION);
        Log.e(TAG, "#### APP_CHANNEL_ID = " + CommonDefine.APP_CHANNEL_ID);

        Log.e(TAG, "#### APP_COOPERATION_ID = " + CommonDefine.APP_COOPERATION_ID);
        Log.e(TAG, "#### APP_PRODUCT_ID = " + CommonDefine.APP_PRODUCT_ID);

        Log.e(TAG, "#### APP_PROTOCOL = " + CommonDefine.APP_PROTOCOL);
        Log.e(TAG, "#### ASSETS_PATH = " + CommonDefine.ASSETS_PATH);

        Log.e(TAG, "#### SDK_KEY_FACEBOOK = " + CommonDefine.SDK_KEY_FACEBOOK);
        Log.e(TAG, "#### SDK_KEY_CM = " + CommonDefine.SDK_KEY_CM);
        Log.e(TAG, "#### SDK_KEY_ADMOB = " + CommonDefine.SDK_KEY_ADMOB);
    }

    /**
     * Image loader must setted  if you integrate interstitial Ads in your App.
     */
    class MyImageLoadListener implements ImageDownloadListener {

        @Override
        public void getBitmap(String url, final BitmapListener imageListener) {
            if(TextUtils.isEmpty(url)){
                if(imageListener != null) {
                    imageListener.onFailed("url is null");
                }
                return;
            }
            //You can use your own VolleyUtil for image loader
            VolleyUtil.loadImage(url, new ImageLoader.ImageListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (imageListener != null) {
                        imageListener.onFailed(volleyError.getMessage());
                    }
                }

                @Override
                public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                    if (imageContainer != null && imageContainer.getBitmap() != null) {
                        if (imageListener != null) {
                            imageListener.onSuccessed(imageContainer.getBitmap());
                        }
                    }
                }
            });
        }
    }
}
