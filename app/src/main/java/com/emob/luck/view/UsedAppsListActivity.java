package com.emob.luck.view;

import android.os.Bundle;
import android.util.Log;

/**
 * Created by Pengz on 16/7/8.
 */
public class UsedAppsListActivity extends BaseActivity{
    private static final String TAG = "UsedAppsListActivity";
    public static UsedAppsListActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "#### Used Apps Activity onCreate");
        instance = this;
    }

    @Override
    protected void onDestroy(){
        Log.i(TAG, "#### Used Apps Activity onDestroy");
        super.onDestroy();
    }
}
