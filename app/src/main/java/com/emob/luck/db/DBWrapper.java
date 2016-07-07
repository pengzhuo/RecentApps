package com.emob.luck.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBWrapper {

	private static Object lockObj = new Object();

	private SQLiteDatabase mDatabase;

	private String mTable;
	private SQLiteOpenHelper dbHelper;

	/**
	 * 针对本地数据库的构造方法
	 * 
	 * @param context
	 *            Context对象
	 * @param table
	 *            数据库表名
	 * @param helper
	 *            SQLiteOpenHelper对象
	 */
	public DBWrapper(Context context, SQLiteOpenHelper helper, String table) {
		this.dbHelper = helper;
		this.mTable = table;
		if (null != this.dbHelper) {
			checkDBOpen();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		mDatabase.close();
		mDatabase = null;
		dbHelper = null;
	}

	/**
	 * Query the given table, returning a Cursor over the result set.
	 * 
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 * @throws SQLException
	 */
	public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) throws SQLException {
		checkDBOpen();
		return mDatabase.query(mTable, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}

	/**
	 * 
	 * @param nullColumnHack
	 * @param values
	 * @return
	 * @throws SQLException
	 *            
	 * @comment 数据库插入数据的方法; 使用了InsertHelper提高批量插入的速度 大约 在每秒525行
	 */
	public long insert(String nullColumnHack, ContentValues values) throws SQLException {
		checkDBOpen();
		synchronized (lockObj) {
//			InsertHelper ih = new InsertHelper(mDatabase, mTable);
//			ih.prepareForInsert();
//			long id = ih.insert(values);
		    long res = -1;
		    res = mDatabase.insert(mTable, nullColumnHack, values);
			return res;
		}
	}

	/**
	 * Convenience method for updating rows in the database.
	 * 
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return the number of rows affected
	 * @throws SQLException
	 */
	public int update(ContentValues values, String whereClause, String[] whereArgs) throws SQLException {
		checkDBOpen();
		synchronized (lockObj) {
			int res = -1;
			res = mDatabase.update(mTable, values, whereClause, whereArgs);
			return res;
		}
	}

	/**
	 * Convenience method for deleting rows in the database.
	 * 
	 * @param whereClause
	 * @param whereArgs
	 * @return the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause.
	 * @throws SQLException
	 */
	public int delete(String whereClause, String[] whereArgs) throws SQLException {
		checkDBOpen();
		synchronized (lockObj) {
			int res = -1;
			res = mDatabase.delete(mTable, whereClause, whereArgs);
			return res;
		}
	}

	/**
	 * Execute a single SQL statement that is not a query. For example, CREATE TABLE, DELETE, INSERT, etc. Multiple statements separated by semicolons
	 * are not supported. Takes a write lock.
	 * 
	 * @param sql
	 * @param bindArgs
	 *            only byte[], String, Long and Double are supported in bindArgs
	 * @throws SQLException
	 *             if the SQL string is invalid
	 */
	public void execSQL(String sql, Object[] bindArgs) throws SQLException {
		checkDBOpen();
		synchronized (lockObj) {
			mDatabase.execSQL(sql, bindArgs);
		}
	}

	/**
	 * Runs the provided SQL and returns a Cursor over the result set.
	 * 
	 * @param sql
	 * @param selectionArgs
	 * @return A Cursor object, which is positioned before the first entry. Note that Cursors are not synchronized, see the documentation for more
	 *         details.
	 * @throws SQLException
	 */
	public Cursor rawQuery(String sql, String[] selectionArgs) throws SQLException {
		checkDBOpen();
		synchronized (lockObj) {
			return mDatabase.rawQuery(sql, selectionArgs);
		}
	}

	/**
	 * 
	 *
	 * 
	 * @comment 关闭数据库
	 */
	public void close() {
		if (null != mDatabase && mDatabase.isOpen()) {
			mDatabase.close();
		}
	}

	/**
	 * 
	 * 
	 * @comment 检查数据库是否打开
	 */
	private void checkDBOpen() {
		if (null == mDatabase || !mDatabase.isOpen()) {
			mDatabase = this.dbHelper.getWritableDatabase();
		}
	}

	/**
	 * 
	 * 
	 * @comment 打开事务
	 */
	public void beginTransaction() {
		if (null != mDatabase) {
			mDatabase.beginTransaction();
		}
	}

	/**
	 * 
	 * 
	 * 
	 * @comment 事务成功
	 */
	public void setTransactionSuccessful() {
		if (null != mDatabase) {
			mDatabase.setTransactionSuccessful();
		}
	}

	/**
	 * 
	 * 
	 * @comment 事务结束
	 */
	public void endTransaction() {
		if (null != mDatabase) {
			mDatabase.endTransaction();
		}
	}
}
