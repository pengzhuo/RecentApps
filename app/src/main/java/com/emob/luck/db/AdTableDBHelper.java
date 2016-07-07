package com.emob.luck.db;

import com.emob.luck.model.AdItem;
import com.emob.luck.model.AdItemList;

import android.content.Context;

public class AdTableDBHelper {
	private static AdTableDB mAdTableDB = null;
	
	private static void ensureEventDB(Context context) {
		if (mAdTableDB == null) {
			mAdTableDB = new AdTableDB(context);
		}
	}

	/**
	 * 插入一条数据
	 * @param context
	 * @param name
	 * @param url
	 * @param landing_url
	 */
	public static void insertData(Context context, int distinguish_id, String name, String url, String landing_url) {
		ensureEventDB(context);
		try {
			mAdTableDB.insertAd(distinguish_id, name, url, landing_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 插入一条数据
	 * @param context
	 * @param item
	 */
	public static void insertData(Context context, AdItem item) {
		ensureEventDB(context);
		try {
			mAdTableDB.insertAdByObj(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取一条数据
	 * @param type
	 * @param context
	 * @return
	 */
	public static AdItem queryData (int type, Context context) {
		try {
			ensureEventDB(context);
			return mAdTableDB.queryAd(type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取数据条数
	 * @param context
	 * @return
	 */
	public static int getAdCounts(int type, Context context) {
		ensureEventDB(context);
		int count = 0;
		try {
			count = mAdTableDB.queryAdCount(type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * 获取有限数据
	 * @param context
	 * @return
	 */
	public static AdItemList getAds(int type, Context context) {
		ensureEventDB(context);
		int count = getAdCounts(type, context);
		if (count != 0 && count <= 3) {
			try {
				return mAdTableDB.queryAds(type, String.valueOf(count));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (count > 3) {
			try {
				return mAdTableDB.queryAds(type, String.valueOf(3));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 获取所有数据
	 * @param context
	 * @return
	 */
	public static AdItemList getAllAds(Context context) {
		try {
			return mAdTableDB.queryAllAd();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void deleteOneAd(String url) {
		try {
			mAdTableDB.deleteOneAd(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void deleteAll() {
		try {
			mAdTableDB.deleteAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/**
	 * 删除广告
	 * @param itemList
	 */
	public static void deleteAds(AdItemList itemList) {
		try {
			mAdTableDB.deleteAds(itemList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
