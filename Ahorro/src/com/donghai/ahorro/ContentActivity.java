package com.donghai.ahorro;

import java.util.Calendar;
import java.util.GregorianCalendar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.donghai.ahorro.fragment.ExpensesFragment;
import com.donghai.ahorro.utils.CacheUtil;
import com.donghai.ahorro.utils.DateUtil;
import com.donghai.ahorro.view.DrawerButton;

/**
 * 主界面
 * 
 * @author A
 * 
 */
public class ContentActivity extends FragmentActivity {

	private DrawerButton mDrawerBtn;
	private boolean isSwitch = true;
	private ImageView mAddRecord;
	private static final int ADD_RECORD = 5;
	public static  TextView mDateView;// 显示时间
	public static String mDate;
	private Calendar mCalendar;
	private int year;
	private int monthOfYear;
	private int dayOfMonth;
	private ImageView mCalendarView;
	private PopupWindow mpopupWindow;
	public boolean isUpdata;
	public static int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_context);
		
		mCalendar = Calendar.getInstance();
		year = mCalendar.get(Calendar.YEAR);
		monthOfYear = mCalendar.get(Calendar.MONTH);
		dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

		mCalendarView = (ImageView) findViewById(R.id.iv_date);
		mCalendarView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				initCaldener();
			}
		});

		mDate = DateUtil.getDate(this);

		mDateView = (TextView) findViewById(R.id.tv_date);
		mDateView.setText(mDate);
		
		mDrawerBtn = (DrawerButton) findViewById(R.id.view_drawer);
		mDrawerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ImageView iv_drawer = (ImageView) mDrawerBtn.findViewById(R.id.iv_drawer);

				TranslateAnimation translateAnim = isSwitch == true ? new TranslateAnimation(0, -10, 0, 0)
						: new TranslateAnimation(-10, 0, 0, 0);

				isSwitch = !isSwitch;
				translateAnim.setDuration(200);
				translateAnim.setFillAfter(true);

				iv_drawer.startAnimation(translateAnim);
				translateAnim = null;
			}
		});

		mAddRecord = (ImageView) findViewById(R.id.iv_add);
		mAddRecord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				ExpensesFragment fragment = (ExpensesFragment) getSupportFragmentManager().findFragmentByTag(EX_FRAGMENT_TAG);
				int position = fragment.getCurrentPositon();
				CacheUtil.putInt(ContentActivity.this, "Position", position);
				startActivityForResult(new Intent(ContentActivity.this, ExpeneseActivity.class), ADD_RECORD);
			}
		});

		initView();
		
	}

	/** 接收返回过来的数据 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case ADD_RECORD:

			// 说明添加成功了数据，需要更新界面
			if (resultCode == Activity.RESULT_OK) {

				isUpdata = true;
				Log.i("Updata", "用户添加了数据，需要更新数据");
			} else {
				isUpdata = false;
				Log.i("Updata", "用户没有做任何操作");
			}
			break;
		}

	}

	
	public static final String 	EX_FRAGMENT_TAG = "EXTag";
	public void initView() {

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		// 替换Fragment
//		transaction.replace(R.id.fl_content, new ExpensesFragment());
		transaction.replace(R.id.fl_content, new ExpensesFragment(), EX_FRAGMENT_TAG);
		transaction.commit();
	}

	/**
	 * 初始化日期对话框
	 */
	public void initCaldener() {

		final View view = View.inflate(this, R.layout.datepick_dialog, null);

		DatePicker dp = (DatePicker) view.findViewById(R.id.datePick);
		// 禁止弹出键盘进行编辑
		dp.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

		dp.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {

			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

				mCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
			}
		});

		if (mpopupWindow == null) {

			mpopupWindow = new PopupWindow(this);
			mpopupWindow.setWidth(LayoutParams.WRAP_CONTENT);
			mpopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
			// false 物理返回健会退出当前的Activity false 物理返回键退出当前的popWindow
			mpopupWindow.setFocusable(true);
			mpopupWindow.setOutsideTouchable(false);
		}

		mpopupWindow.setContentView(view);
		mpopupWindow.showAtLocation(mCalendarView, Gravity.CENTER_HORIZONTAL, 0, 250);
		mpopupWindow.update();

	}

	//  日期对话框取消按钮ß
	public void datePickerChancel(View v) {

		mpopupWindow.dismiss();
	}

	// 日期对话框确定按钮
	public void datePickerOk(View v) {

		String date = DateUtil.fomart(mCalendar);
		mDateView.setText(date);
		// 指定日期进行数据库查询操作
		/*
		 * 根据当前日期向前查找30，向后查找30 当前日现实在当前的ViewPager上 具体如何查找呢？
		 */
		ExpensesFragment fragment = (ExpensesFragment) getSupportFragmentManager().findFragmentByTag(EX_FRAGMENT_TAG);
		fragment.reInitData(DateUtil.getDate(date));
		fragment.updataView();
		/*
		 * 查询当天往前3天，往后3天共7天记录存放到mDataCaChe中 index决定pager
		 */
		mpopupWindow.dismiss();
	}


	/**
	 * 设置日期文本视图
	 */
	public void setDateView(String date)
	{
		mDate = date;
		mDateView.setText(date);
	}
	
	/**
	 * 获取日期
	 * @return
	 */
	public String getDate() {
		return mDateView.getText().toString();
	}

}
