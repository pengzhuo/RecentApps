package com.emob.luck.db;

import java.util.List;

import com.emob.luck.model.EventItem;
import com.emob.luck.model.EventItemList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

/**
 * 用户行为表的操作
 */
public class EventTableDB extends BaseDB {
//    public static final int NOT_UPLOAD = 0;                       //没有上传的行为
//    public static final int UPLOAD_ING = 1;                       //正在上传的行为
//    public static final int INSTALLED_UPLOAD = 1;                 //广告被安装主动上传行为
//    public static final int NOT_INSTALLED_UPLOAD = 0;             //被动跟着协议携带上传行为

    public EventTableDB(Context contextParam) {
        mDbWrapper = new DBWrapper(contextParam, new SdkDBOpenHelper(
                contextParam), SdkDBOpenHelper.EVENT_TABLE_NAME);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        mDbWrapper = null;
    }

    public void close() {
        if (null != mDbWrapper) {
            mDbWrapper.close();
            mDbWrapper = null;
        }
    }

    /*
     * status: 0-未上传； 1-正在上传； 
     */
    public int queryCountByStatus() throws Exception {
        int count = 0;
        String[] columns = { SdkDBOpenHelper.ID };
        Cursor cursor = null;
        try {
            cursor = mDbWrapper.query(columns, null, null, null,
                    null, null, null);
            if (null == cursor) {
                return count;
            }
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    /**
     * 
     * @comment 通过status获取事件集合
     */
    public EventItemList queryEventsByStatus() throws Exception {
        EventItemList eventsLists = new EventItemList();
        Cursor cursor = null;
        try {
            cursor = mDbWrapper.query(null, null, null, null,
                    null, null, null);
            if (null == cursor) {
                return eventsLists;
            }
            if (cursor.moveToFirst()) {
                do {
                    EventItem eventObj = new EventItem();
                    eventObj.setPackageName(cursor.getString(cursor
                    		.getColumnIndex(SdkDBOpenHelper.PACKAGENAME)));
                    eventObj.setChannel(cursor.getInt(cursor
                    		.getColumnIndex(SdkDBOpenHelper.CHANNEL)));
                    eventObj.setPos(cursor.getInt(cursor
                    		.getColumnIndex(SdkDBOpenHelper.POS)));
                    eventObj.setAction(cursor.getInt(cursor
                    		.getColumnIndex(SdkDBOpenHelper.ACTION)));
                    eventObj.setTime(cursor.getString(cursor
                    		.getColumnIndex(SdkDBOpenHelper.TIME)));
                    eventsLists.add(eventObj);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return eventsLists;
    }

    /**
     * @return 获取上传服务器的Event
     * @throws Exception
     */
    public EventItemList getuploadEventItems() throws Exception {
    	EventItemList eventsLists = null; 
    	eventsLists = queryEventsByStatus();
//        for (EventItem eventItem : eventsLists) {
//            eventItem.setStatus(UPLOAD_ING);
//        }
        updateEvents(eventsLists);
        return eventsLists;
    }

    public int updateEvent(EventItem event) throws Exception {
        if (null == event) {
            throw new NullPointerException(" campaignId  event is null");
        }
        // 号码不能为空
        int id = event.getId();
        if (TextUtils.isEmpty(String.valueOf(id))) {
            throw new NullPointerException(" campaignId Parameter is null");
        }

        ContentValues values = new ContentValues();
        values.put(SdkDBOpenHelper.PACKAGENAME, event.getPackageName());
        values.put(SdkDBOpenHelper.CHANNEL, event.getChannel());
        values.put(SdkDBOpenHelper.POS, event.getPackageName());
        values.put(SdkDBOpenHelper.ACTION, event.getAction());
        values.put(SdkDBOpenHelper.TIME, event.getTime());

        String where = SdkDBOpenHelper.ID + " = ?";
        String whereArgs[] = { event.getId()+""};
        return mDbWrapper.update(values, where, whereArgs);
    }

    public void updateEvents(EventItemList list) throws Exception {
        for (EventItem eventItem : list) {
            updateEvent(eventItem);
        }
    }

    public long insertEvent(String packageName, int channel, int pos, int action, String time)
            throws Exception {
        ContentValues values = new ContentValues();
        values.put(SdkDBOpenHelper.PACKAGENAME, packageName);
        values.put(SdkDBOpenHelper.CHANNEL, channel);
        values.put(SdkDBOpenHelper.POS, pos);
        values.put(SdkDBOpenHelper.ACTION, action);
        values.put(SdkDBOpenHelper.TIME, time);
        return mDbWrapper.insert(null, values);
    }

    public int deleteOneEvent(int id) throws Exception {
        String whereClause = SdkDBOpenHelper.ID + "= ?";
        String whereArgs[] = { String.valueOf(id) };
        return mDbWrapper.delete(whereClause, whereArgs);

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
     * @param mContext
     * @return 是否有需要上传的事件 
     */
    public boolean needUnloadEvents() {
      // 查找未上传的事件。
      try {
          List<EventItem> notUpoadEvents = queryEventsByStatus();
          if (notUpoadEvents.size()==0) {
              return false;
          } 
      } catch (Exception e) {
          e.printStackTrace();
          return false;
      }
      return true;
    }
}
