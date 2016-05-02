package com.donghai.ahorro.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class DisplayUtil {

	/**
	 * densityDpi 每英寸多少个像素点, density = densityDpi /160
	 * APP根据densityDpi的大小决定调用哪个文件夹下的图片，关系如下： drawable-ldpi 底密度，通常是指120
	 * drawable-mdpi 中等密度，通常是指160 drawable-xhdpi 超高密度，通常是指320
	 * 
	 * px = dip*density 在绘制矩形时，可以首先获得屏幕宽度的dip，根据dip决定边长D，然后用文中的方法确定其px坐标
	 */
	private String tag = "TAG";

	public void getDivceScreen(Context con) {
		DisplayMetrics dm = con.getResources().getDisplayMetrics();
		int w_screen = dm.widthPixels;// 像素
		int h_screen = dm.heightPixels;// 像素
		Log.i(tag, "屏幕尺寸2：宽度 = " + w_screen + "高度 = " + h_screen + "密度 = " + dm.densityDpi);
	}

	/**dip转像素
	 *  像素转化成dip
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	
	
}
