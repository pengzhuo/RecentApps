package com.emob.luck.db;

import com.emob.luck.model.AdItem;
import com.emob.luck.model.AdItemList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class AdTableDB extends BaseDB {
	//private Context mContext;
	public AdTableDB(Context mContext) {
		super();
		//this.mContext = mContext;
		mDbWrapper = new DBWrapper(mContext, 
				new SdkDBOpenHelper(mContext), 
				SdkDBOpenHelper.AD_TABLE_NAME);
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		mDbWrapper = null;
	}
	
	/**
	 * 关闭数据库
	 */
	public void close() {
		if (null != mDbWrapper) {
			mDbWrapper.close();
			mDbWrapper = null;
		}
	}
	
	/**
	 * 插入一条数据
	 * @param titleName
	 * @param url
	 * @param landing_url
	 * @return
	 * @throws Exception
	 */
	public long insertAd(int index, String titleName, String url, String landing_url)
            throws Exception {
        ContentValues values = new ContentValues();
        values.put(SdkDBOpenHelper.DISTINGUISH_ID, index);
        values.put(SdkDBOpenHelper.NAME, titleName);
        values.put(SdkDBOpenHelper.ICON_URL, url);
        values.put(SdkDBOpenHelper.LANDING_URL, landing_url);
        return mDbWrapper.insert(null, values);
    }
	
	/**
	 * 插入多条数据
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public int insertAdList(AdItemList list) throws Exception {
		for (AdItem item : list) {
			insertAdByObj(item);
		}
		return 0;
	}
	
	/**
	 * 采用面向对象的方式插入一条数据
	 * @param adItme
	 * @return
	 * @throws Exception
	 */
	public long insertAdByObj(AdItem adItme) throws Exception {
		ContentValues values = new ContentValues();
		values.put(SdkDBOpenHelper.DISTINGUISH_ID, adItme.getIndex());
		values.put(SdkDBOpenHelper.NAME, adItme.getTitleName());
		values.put(SdkDBOpenHelper.ICON_URL, adItme.getIconUrl());
		values.put(SdkDBOpenHelper.LANDING_URL, adItme.getLandingUrl());
		return mDbWrapper.insert(null, values);

	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 * @comment 删除全部记录
	 */
	public int deleteAll() throws Exception {
		return mDbWrapper.delete(null, null);
	}
	
	/**
	 * 根据url删除一条数据
	 * @param url
	 * @return
	 */
	public int deleteOneAd(String url) {
		String whereClause = SdkDBOpenHelper.ICON_URL + "= ?";
		String whereArgs[] = { url };
		return mDbWrapper.delete(whereClause, whereArgs);
	}
	
	/**
	 * 删除一条数据
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public int deleteOneCampaign(AdItem item) throws Exception {
		String whereClause = SdkDBOpenHelper.ICON_URL + "= ?";
		String whereArgs[] = { item.getIconUrl() };
		return mDbWrapper.delete(whereClause, whereArgs);
	}
	
	/**
	 * 删除多条广告
	 * 
	 * @throws Exception
	 */
	public void deleteAds(AdItemList adList) throws Exception {
		for (AdItem item : adList) {
			deleteOneCampaign(item);
		}
	}
	
	/**
	 * 获取所有广告
	 * 
	 * @return
	 * @throws Exception
	 */
	public AdItemList queryAllAd() throws Exception {
		AdItemList adsLists = new AdItemList();
		Cursor cursor = null;
		try {
			cursor = mDbWrapper.query(null, null, null, null,
					null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					adsLists = new AdItemList();
					do {
						AdItem adItem = new AdItem();
						adItem.setIndex(cursor.getInt(cursor.getColumnIndex(SdkDBOpenHelper.DISTINGUISH_ID)));
						adItem.setTitleName(cursor.getString(cursor
								.getColumnIndex(SdkDBOpenHelper.NAME)));
						adItem.setIconUrl(cursor.getString(cursor
								.getColumnIndex(SdkDBOpenHelper.ICON_URL)));
						adItem.setLandingUrl(cursor.getString(cursor
								.getColumnIndex(SdkDBOpenHelper.LANDING_URL)));
						adsLists.add(adItem);

					} while (cursor.moveToNext());
				}
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return adsLists;
	}
	
	/**
	 * 根据类型查询一条数据
	 * @param index
	 * @return
	 */
	public AdItem queryAd(int index) {
		String selection = SdkDBOpenHelper.DISTINGUISH_ID + "= ?";
		String[] selectionArgs = { String.valueOf(index) };
		Cursor cursor = null;
		AdItem adItem = null;
		try {
			cursor = mDbWrapper.query(null, selection, selectionArgs, null,
					null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					adItem = new AdItem();
					adItem.setIndex(cursor.getInt(cursor
							.getColumnIndex(SdkDBOpenHelper.DISTINGUISH_ID)));
					adItem.setTitleName(cursor.getString(cursor
							.getColumnIndex(SdkDBOpenHelper.NAME)));
					adItem.setIconUrl(cursor.getString(cursor
							.getColumnIndex(SdkDBOpenHelper.ICON_URL)));
					adItem.setLandingUrl(cursor.getString(cursor
							.getColumnIndex(SdkDBOpenHelper.LANDING_URL)));

				}
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return adItem;
		
	}
	
	/**
	 * 获取几条广告
	 * @param type
	 * @param limit
	 * @return
	 */
	public AdItemList queryAds(int type, String limit) {
		String selection = SdkDBOpenHelper.DISTINGUISH_ID + "= ?";
		String[] selectionArgs = { String.valueOf(type) };
		AdItemList adsLists = new AdItemList();
		Cursor cursor = null;
		try {
			cursor = mDbWrapper.query(null, selection, selectionArgs, null,
					null, null, limit);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					adsLists = new AdItemList();
					do {
						AdItem adItem = new AdItem();
						adItem.setIndex(cursor.getInt(cursor.getColumnIndex(SdkDBOpenHelper.DISTINGUISH_ID)));
						adItem.setTitleName(cursor.getString(cursor
								.getColumnIndex(SdkDBOpenHelper.NAME)));
						adItem.setIconUrl(cursor.getString(cursor
								.getColumnIndex(SdkDBOpenHelper.ICON_URL)));
						adItem.setLandingUrl(cursor.getString(cursor
								.getColumnIndex(SdkDBOpenHelper.LANDING_URL)));
						adsLists.add(adItem);

					} while (cursor.moveToNext());
				}
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return adsLists;
	}
	
	/**
	 * 查询数据总条数
	 * @return
	 * @throws Exception
	 */
	public int queryAdCount(int type) throws Exception{
		String selection = SdkDBOpenHelper.DISTINGUISH_ID + "= ?";
		String[] selectionArgs = { String.valueOf(type) };
		int count = 0;
		Cursor cursor = null;
		try {
			cursor = mDbWrapper.query(null, selection, selectionArgs, null, null, null, null);
			if (null == cursor){
				return count;
			}
			if (cursor.moveToFirst()) {
				count = cursor.getCount();
			}
		} finally {
			if(cursor != null) {
				cursor.close();
			}
		}
		return count;
	}
}
