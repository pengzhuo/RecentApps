package com.emob.lib.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
	public static final int HOURS_24_MAKE = 0; //24小时制
	public static final int HOURS_12_MAKE = 1; //12小时制
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE    = new SimpleDateFormat("yyyy-MM-dd");

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     * 
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }
    
    private static String getTimeStr(int hour, int minute, boolean is12) {
		StringBuilder temp = new StringBuilder();
		if (is12) {
			temp.append(hour);
		} else {
			temp.append(toTwoDigits(hour));
		}
		temp.append(":");
		temp.append(toTwoDigits(minute));
		return temp.toString();
	}
    
    /**
	 * 对小于10的数补0显示，如04
	 * 
	 * @param digit
	 * @return
	 */
	private static String toTwoDigits(int digit) {
		if (digit >= 10) {
			return "" + digit;
		} else {
			return "0" + digit;
		}
	}
	
	/**
	 * 得到24小时制时间，如07:30，14:12
	 * 
	 * @param time
	 * @return
	 */
	private static String getTimeOf24(long time) {
		return getTimeStr(getHourOf24(time), getMinute(time), false);
	}
	
	/**
	 * 得到12小时制时间，如7:30 AM，2:12 PM
	 * 
	 * @param time
	 * @return
	 */
	private static String getTimeOf12(long time) {
		return getAmPm(time) + " " + getTimeStr(getHourOf12(time), getMinute(time), true);
	}
	
	/**
	 * 
	 * @param time
	 * @return
	 * int
	 * @comment 获取时，24小时制时间
	 */
	private static int getHourOf24(long time) {
		return currentDateTime(time, Calendar.AM_PM);
	}

	/**
	 * 
	 * @param time
	 * @return
	 * int
	 * @comment 获取时，12小时制时间
	 */
	private static int getHourOf12(long time) {
		int hour = currentDateTime(time, Calendar.HOUR);
		if (hour == 0)
			hour = 12;
		return hour;
	}
	
	/**
	 * 获得AM/PM
	 * 
	 * @param time
	 * @return
	 */
	private static String getAmPm(long time) {
		SimpleDateFormat sf = new SimpleDateFormat("aa");
		return sf.format(new Date(time));
		// return getAmPmStr(time) == 0 ? mContext.getString(AM) : mContext.getString(PM);
	}
	
	/**
	 * 
	 * @param time
	 * @return
	 * int
	 * @comment 获取分
	 */
	private static int getMinute(long time) {
		return currentDateTime(time, Calendar.MINUTE);
	}

	private static int currentDateTime(long time, int type) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return calendar.get(type);
	}
	
	public static String getTime(long time, int timeFormat) {
		return timeFormat == HOURS_24_MAKE ? getTimeOf24(time) : getTimeOf12(time);
	}
	
	public static String getFormattedTime(long timeInMillis) {
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss", Locale.US);
//		SimpleDateFormat formatter = new SimpleDateFormat ("MM-dd", Locale.US);
		Date curDate = new Date(timeInMillis);
		String str = formatter.format(curDate); 
		
		//Date date = new Date(timeInMillis);
		//DateFormat df = new DateFormat();
		//return df.format("yyyy-MM-dd hh:mm:ss", date);
		return str.toString();
	}
	
}
