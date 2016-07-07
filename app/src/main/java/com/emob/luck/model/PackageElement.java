package com.emob.luck.model;

import java.io.Serializable;

import com.emob.lib.util.Utils;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

/**
 * @author tianJT
 *
 */
public class PackageElement implements Serializable{
	private static final long serialVersionUID = -5560244455188916988L;
	//private static final long serialVersionUID = 1L;
	private PackageInfo mPackageInfo;
	private String mPackageName;
	private Drawable mIcon;
	private String mLabel;
	private boolean mIsNative;
	private String mUrl;
	private String mLandingUrl;
	private int index;
	
	public PackageElement() {}

	public PackageElement(String packageName, String label)
	{
		this.mPackageName = Utils.filterString(packageName);
		this.mLabel = Utils.filterString(label) ;
	}

	public Object getCompareField() {
		return mLabel;
	}
	
	public String getLabel() {
		return mLabel;
	}

	public void setLabel(String mLabel) {
		this.mLabel = mLabel;
	}
	
	public PackageInfo getmPackageInfo() {
		return mPackageInfo;
	}
	
	public void setmPackageInfo(PackageInfo mPackageInfo) {
		this.mPackageInfo = mPackageInfo;
	}
	
	public String getmPackageName() {
		return mPackageName;
	}
	
	public void setmPackageName(String mPackageName) {
		this.mPackageName = mPackageName;
	}
	
	public Drawable getmIcon() {
		return mIcon;
	}
	
	public void setmIcon(Drawable mIcon) {
		this.mIcon = mIcon;
	}

	public boolean ismIsNative() {
		return mIsNative;
	}

	public void setmIsNative(boolean mIsNative) {
		this.mIsNative = mIsNative;
	}

	public String getmUrl() {
		return mUrl;
	}

	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}

	public String getmLandingUrl() {
		return mLandingUrl;
	}

	public void setmLandingUrl(String mLandingUrl) {
		this.mLandingUrl = mLandingUrl;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
}
