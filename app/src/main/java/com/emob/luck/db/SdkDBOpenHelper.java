package com.emob.luck.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *  本类主要执行对 数据库与表的创建和更新操作
 *  事件表（EVENT_TABLE_NAME）
 */
public class SdkDBOpenHelper extends SQLiteOpenHelper{
	//数据库信息
	private static final String DATABASE_NAME = "milano_db.db";
	private static final int DATABASE_VERSION = 1;
	
	//表名
	public static final String EVENT_TABLE_NAME  = "event_table";              // 事件表
	public static final String AD_TABLE_NAME  = "ad_table";     			   // 广告表
 
	//事件表
	public static final String ID = "_id";									    //id
	public static final String PACKAGENAME = "pkg";					//"lock"代表锁屏， "包名"代表topTen
	public static final String TIME = "time";								    //用户行为触发时间点
	public static final String ACTION = "action";							    //记录用户行为  1:showed; 2:clicked; 3:下载；4：安装；
																			   			  //5:closed；6:请求Inmobi; 7:请求Inmobi成功；8:请求Inmobi失败；
	public static final String CHANNEL = "channel";				
	public static final String POS		= "pos";
	
	public static final String ICON_URL = "url";                                //icon url
	public static final String NAME = "name";									//广告title name
	public static final String LANDING_URL = "landing_url";						//广告点击跳转url
	public static final String DISTINGUISH_ID = "distinguish_id";									//区分那个id
	//	public static final String STATUS = "status";                           //0 未上传     1，正在上传       2：已经上传

	 
	private static final String SQL_CREATE_EVENT_TABLE = new StringBuffer("create table if not exists  ")
																			.append(EVENT_TABLE_NAME).append("(")
																			.append(ID).append(" integer primary key ").append(",")
																		    .append(PACKAGENAME).append(" text ").append(",")
																			.append(TIME).append(" text ").append(",")
																		    .append(CHANNEL).append(" integer ").append(",")
																		    .append(POS).append(" integer ").append(",")
																		    .append(ACTION).append(" integer ")
																		    .append(");").toString();
	
	private static final String SQL_CREATE_AD_TABLE = new StringBuffer("create table if not exists  ")
																			.append(AD_TABLE_NAME).append("(")
																			.append(ID).append(" integer primary key ").append(",")
																			.append(DISTINGUISH_ID).append(" integer ").append(",")
																		    .append(NAME).append(" text ").append(",")
																			.append(ICON_URL).append(" text ").append(",")
																		    .append(LANDING_URL).append(" text ")
																		    .append(");").toString();
	//删除统计结果表的sql语句
	private static final String DELETE_EVENT_TABLE = "drop table if exists " + EVENT_TABLE_NAME + ";";
	private static final String DELETE_AD_TABLE = "drop table if exists " + AD_TABLE_NAME + ";";

	public SdkDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (null == db) {
			return;
		}
		db.execSQL(SQL_CREATE_EVENT_TABLE);
		db.execSQL(SQL_CREATE_AD_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (null == db || newVersion <= oldVersion) {
			return;
		}
		db.execSQL(DELETE_EVENT_TABLE);
		db.execSQL(DELETE_AD_TABLE);
		onCreate(db);
	}

}
