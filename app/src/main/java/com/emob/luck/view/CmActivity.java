package com.emob.luck.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.emob.luck.SdkHelper;
import com.emob.luck.common.CommonDefine;

/**
 * Created by Pengz on 16/6/22.
 */
public class CmActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String randomString = SdkHelper.getSdkSpotKeyString(getApplicationContext(), CommonDefine.DSP_CHANNEL_CM);
        Toast.makeText(getApplicationContext(), randomString, Toast.LENGTH_LONG).show();
        finish();
    }
}
