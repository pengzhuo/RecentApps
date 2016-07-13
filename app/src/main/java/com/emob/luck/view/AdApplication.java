package com.emob.luck.view;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.cmcm.adsdk.BitmapListener;
import com.cmcm.adsdk.CMAdManager;
import com.cmcm.adsdk.CMAdManagerFactory;
import com.cmcm.adsdk.ImageDownloadListener;
import com.emob.lib.util.VolleyUtil;
import com.emob.luck.common.CommonDefine;

/**
 * Created by Pengz on 16/7/8.
 */
public class AdApplication extends Application{
    @Override
    public void onCreate(){
        super.onCreate();
        //初始化SDK
        String mid = CommonDefine.SDK_KEY_CM.substring(0,4);
        CMAdManager.applicationInit(getApplicationContext(), mid, "");
        CMAdManagerFactory.setImageDownloadListener(new MyImageLoadListener());
        //是否允许打印日志
        CMAdManager.enableLog();
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
