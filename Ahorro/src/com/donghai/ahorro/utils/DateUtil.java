package com.donghai.ahorro.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.content.Context;
import android.util.Log;

/**
 * 时间操作类
 * 
 * @author Administrator
 * 
 */
public class DateUtil {

	private static Date date = new Date();
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
	
	// 获取当前的时间
	private static String getDateToDay() {

//		Calendar calendar = new GregorianCalendar(2016, 03, 01);

		Log.d("BUG", sdf.format(date));
		// 2016/02/22
//		return sdf.format(calendar.getTime());
		return sdf.format(date);
	}
	
	public static Date getDate(String pase)
	{
		Date date = null;
		try {
			date = sdf.parse(pase);
		} catch (ParseException e) {
			Log.d("BUG", "时间格式化错误，位置在DateUtil类中");
			date = new Date();
		}
		
		return date;
	}
	
	/**
	 * 写入当前时间
	 */
	public static void WriteDate(Context context) {
		String date = getDateToDay();

		// 如果当前的时间和保存的时间不一致的话，更新时间
		if (!date.equals(getDate(context)))
			CacheUtil.putString(context, "Date", date);
	}

	/**
	 * 日期往后走一天
	 */
	public static String dateRoll(int day) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, day);
		Date date = calendar.getTime();
		String dateFormat = sdf.format(date);
		//System.out.println(dateFormat);
		return dateFormat;
	}
	/**
	 * 
	 * @param day
	 * @param date 指定一个时间点滚动时间
	 * @return
	 */
	public static String dateRoll(int day,Date date) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, day);
		date = calendar.getTime();
		String dateFormat = sdf.format(date);
		return dateFormat;
	}

	/**
	 * 获取当前时间
	 */
	public static String getDate(Context context) {
		
		return CacheUtil.getString(context, "Date");
	}

	/**
	 * 这个地方的代码太臭了，需要改
	 * 
	 * @param mCalendar
	 * @return
	 */
	public static String fomart(Calendar mCalendar) {

		String year = mCalendar.get(Calendar.YEAR) + "";

		while (year.length() < 4) {
			year = 0 + year;
		}

		String monthOfYear = mCalendar.get(Calendar.MONTH) + 1 + "";

		while (monthOfYear.length() < 2) {
			monthOfYear = 0 + monthOfYear;
		}

		String dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH) + "";

		while (dayOfMonth.length() < 2) {
			dayOfMonth = 0 + dayOfMonth;
		}
		return year + "/" + monthOfYear + "/" + dayOfMonth;
	}

}
