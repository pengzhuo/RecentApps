package com.emob.luck.db;

public class BaseDB {
	
	protected DBWrapper mDbWrapper;
	
	/**
	 * void
	 * Michael
	 * @comment 关闭数据库 
	 */
	public void closeDB(){
		if (null != mDbWrapper) {
			mDbWrapper.close();
		}
	}
	
	/**
	 * 
	 * 
	 * void
	 * Michael
	 * @comment 打开事务
	 */
	public void beginTransaction(){
		if (null != mDbWrapper) {
			mDbWrapper.beginTransaction();
		}
	}
	
	/**
	 * 
	 * 
	 * void
	 * Michael
	 * @comment 事务成功
	 */
	public void setTransactionSuccessful(){
		if (null != mDbWrapper) {
			mDbWrapper.setTransactionSuccessful();
		}
	}
	
	/**
	 * 
	 * 
	 * void
	 * Michael
	 * @comment 事务结束
	 */
	public void endTransaction(){
		if (null != mDbWrapper) {
			mDbWrapper.endTransaction();
		}
	}
	

}
