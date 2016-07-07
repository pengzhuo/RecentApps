package com.emob.luck;

import android.graphics.Bitmap;

public interface NativeListener {
	public void onSpotNativeSucceed(String landingUrl, Bitmap bitmap);   
	public void onBannerNativeSucceed(String landingUrl, Bitmap bitmap);   
};
   
