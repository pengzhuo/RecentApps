package com.emob.luck.view;

import android.app.Application;
import android.os.Handler;
import android.os.Message;

import com.cmcm.adsdk.CMAdManager;

/**
 * Created by Pengz on 16/7/8.
 */
public class AdApplication extends Application{
    @Override
    public void onCreate(){
        super.onCreate();
        //初始化SDK
        CMAdManager.applicationInit(getApplicationContext(), "1", "1");
        //是否允许打印日志
        CMAdManager.enableLog();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            //初始化SDK
            CMAdManager.applicationInit(getApplicationContext(), "", "");
            //是否允许打印日志
            CMAdManager.enableLog();
        }
    };
}
