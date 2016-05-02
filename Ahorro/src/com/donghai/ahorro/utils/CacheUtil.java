package com.donghai.ahorro.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * ���幤����
 * 
 * @author A
 * 
 */
public class CacheUtil {

	public static void putBoolean(Context context, String key, boolean value) {

		SharedPreferences sh = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		sh.edit().putBoolean(key, value).commit();
	}

	public static boolean getBoolean(Context context, String key) {

		return context.getSharedPreferences("config", Context.MODE_PRIVATE)
				.getBoolean(key, false);
	}

	public static int getInt(Context context, String key) {

		return context.getSharedPreferences("config", Context.MODE_PRIVATE)
				.getInt(key, 0);
	}

	public static void putString(Context context, String key, String value) {
		SharedPreferences sh = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		sh.edit().putString(key, value).commit();
	}

	public static String getString(Context context, String key) {

		return context.getSharedPreferences("config", Context.MODE_PRIVATE)
				.getString(key, "");
	}

	public static void putInt(Context context, String key, int value) {
		SharedPreferences sh = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		sh.edit().putInt(key, value).commit();
	}
}
