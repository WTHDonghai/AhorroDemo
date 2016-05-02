package com.donghai.ahorro;

import java.util.ArrayList;
import java.util.LinkedList;
import org.xutils.ex.DbException;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.donghai.ahorro.utils.CacheUtil;
import com.donghai.ahorro.view.DrawerButton;
import com.donghai.ahorro.view.ExpeneseListItem;
import com.donghai.dao.Accounting;
import com.donghai.dao.Dao;

public class ExpeneseActivity extends Activity {

	private DrawerButton mBackButton;
	private TextView mSaveButton;
	private ArrayList<TextView> mExpenseIcon;
	private ViewPager mViewPager;
	private GridView mGridView;
	private int mPage;// PagerView的页数
	private int mItemCount;// GridView的Item数
	private ArrayList<GridView> mViewHolder;
	private LinearLayout pointRoot;
	private View point;
	private int pointWidth;// 小圆点间的距离
	private ListView mMenuListView;
	private ExpeneseListItem item;
	private PopupWindow mpopupWindow;
	private RadioGroup mOperator_rg;
	private TextView numDisplay;
	private RadioButton[] rbs;// 保存单选按钮的数组
	private boolean isChecked;
	private String oper;// 存放运算符
	private StringBuilder text;
	public LinkedList<String> mExpress = new LinkedList<>();
	private boolean isDouble;// 小数标志位
	private String str;
	private RadioGroup mItemRg;
	private RelativeLayout root;
	private int[] iconIds;
	private int[] iconIdWs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expenese);

		root = (RelativeLayout) findViewById(R.id.edt_background);
		// 通过点击金额编辑框也可以弹出键盘
		root.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (mpopupWindow == null || !mpopupWindow.isShowing()) {
					initKeyBord();
				}
			}
		});

		mItemRg = new RadioGroup(this);

		numDisplay = (TextView) findViewById(R.id.tv_limit);

		pointRoot = (LinearLayout) findViewById(R.id.ex_point_root);
		point = findViewById(R.id.point);

		// 缓存每一页GridView
		mViewHolder = new ArrayList<GridView>();

		mBackButton = (DrawerButton) (findViewById(R.id.ic_back));
		mBackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 返回按钮的逻辑实现
				finish();
			}
		});

		mSaveButton = (TextView) findViewById(R.id.tv_saveButton);
		mSaveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 储存按钮的逻辑实现

				RadioButton rb = null;

				if (numDisplay.getText().equals("0")) {
					// 弹出对话框
					Toast.makeText(ExpeneseActivity.this, "请输入金额", Toast.LENGTH_SHORT).show();
					return;
				} else {
					// 获取当前的Item名和金额数
					rb = (RadioButton) (findViewById(mItemRg.getCheckedRadioButtonId()));
					if (rb == null) {
						Toast.makeText(ExpeneseActivity.this, "请选择类别", Toast.LENGTH_SHORT).show();
						return;
					}
				}

				// 写入数据库。。
				CharSequence cs = rb.getText();

			    //按钮的下标和图片数组的下标向对应，考虑将该下标写进数据库中，在查询数据的时候根据下标匹配图片数组
//				System.out.println("第"+mItemRg.getCheckedRadioButtonId()+"被选中了");
				Accounting ac = new Accounting();

				/**
				 * 首先获取时间，拿时间去时间表进行查询，如果有查询结果，说明该次记录不是第一次,从本地获取DatePick
				 * 否则获取查询结果，将该查询结果写入datePick中
				 */
				String date = ContentActivity.mDate;

				com.donghai.dao.Date d = null;
				try {
					d = SplashActivity.db.selector(com.donghai.dao.Date.class).where("date", "=", date).findFirst();

					if (d != null) {
						ac.setDatePick(d.getDatePick());
					} else {

						d = new com.donghai.dao.Date();

						int datePick = CacheUtil.getInt(ExpeneseActivity.this, "DATE_PICK");

						ac.setDatePick(String.valueOf(++datePick));

						CacheUtil.putInt(ExpeneseActivity.this, "DATE_PICK", datePick);

						Log.i("DATE_PICK", datePick + "");
						d.setDatePick(datePick + "");
						d.setDate(date);
					}

				} catch (DbException e) {
					Log.e("DONG", "查询出现了异常");
					e.printStackTrace();
				}

				ac.setDate(date);
				ac.setTitle(cs.toString());
				ac.setSum(numDisplay.getText().toString());
				ac.setIconId(String.valueOf(iconIdWs[mItemRg.getCheckedRadioButtonId()]));

				try {
					
					// 写入日期表
					Dao.save(d);
					Dao.save(ac);
					setResult(RESULT_OK);

				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 弹出对话框
				Toast.makeText(ExpeneseActivity.this, cs + ":" + numDisplay.getText(), Toast.LENGTH_SHORT).show();
				finish();
			}
		});

		// 加载图标文件
		mExpenseIcon = new ArrayList<TextView>();

		TypedArray ar = ExpeneseActivity.this.getResources().obtainTypedArray(R.array.expeneseItemAry);
		TypedArray ar_w = ExpeneseActivity.this.getResources().obtainTypedArray(R.array.expenese_W_ItemAry);

		int len = ar.length();
		iconIds = new int[len];//存放图标id的数组
		iconIdWs = new int[len];

		String[] iconNames = { "早餐", "午餐", "晚餐", "饮料", "零食", "交通", "日常用品", "娱乐", "社交", "衣物", "购物", "房租", "礼物", "礼金",
				"医疗", "电话费", "投资", "信用卡", "转账", "其他" };

		for (int i = 0; i < iconIds.length; i++) {

			iconIds[i] = ar.getResourceId(i, -1);
			iconIdWs[i] = ar_w.getResourceId(i, -1);
			RadioButton icon_rb = new RadioButton(this);
			icon_rb.setButtonDrawable(null);
			icon_rb.setBackgroundDrawable(null);
			icon_rb.setGravity(Gravity.CENTER);
			Drawable nav_up = getResources().getDrawable(iconIds[i]);
			nav_up.setBounds(0, 0, nav_up.getIntrinsicWidth(), nav_up.getIntrinsicHeight());
			icon_rb.setCompoundDrawables(null, nav_up, null, null);
			icon_rb.setText(iconNames[i]);
			icon_rb.setId(i);

			icon_rb.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					if (numDisplay.getText().toString().equals("0")) {
						// 弹出键盘输入对话框
						initKeyBord();
					}
				}
			});
			mItemRg.addView(icon_rb);
			mExpenseIcon.add(icon_rb);
		}

		ar.recycle();
		ar_w.recycle();
		mItemCount = mExpenseIcon.size();

		mViewPager = (ViewPager) findViewById(R.id.vp_expense_item);

		mViewPager.setAdapter(new MviewPagerAdapter());

		// 获取视图树观察者
		point.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {

				point.getViewTreeObserver().removeOnGlobalLayoutListener(this);

				pointWidth = pointRoot.getChildAt(1).getLeft() - pointRoot.getChildAt(0).getLeft();
			}
		});

		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
			}

			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {

				int moveLength = (int) (pointWidth * (position + arg1));

				RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) point
						.getLayoutParams();
				params.leftMargin = moveLength;
				point.setLayoutParams(params);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		final int[] menuIcons = { R.drawable.icon_op_description, R.drawable.icon_op_account,
				R.drawable.icon_op_repeat };
		final int[] menuNames = { R.string.ic_op_desc, R.string.ic_op_acc, R.string.ic_op_repeat };
		mMenuListView = (ListView) findViewById(R.id.lv_expense_menu);
		mMenuListView.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {

				item = new ExpeneseListItem(ExpeneseActivity.this);
				item.setImage(menuIcons[arg0]);
				item.setText(menuNames[arg0]);
				return item;
			}

			@Override
			public long getItemId(int arg0) {
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				return null;
			}

			@Override
			public int getCount() {
				return 3;
			}
		});

	}

	// ----------------------------
	public String getResult(String y, String oper) {

		double X = Double.valueOf(mExpress.getFirst());
		double Y = Double.valueOf(y);
		double result = 0;
		switch (oper) {

		case "+":

			result = X + Y;

			break;
		case "-":
			result = X - Y;
			break;
		case "×":
			result = X * Y;
			break;
		case "÷":
			result = X / Y;
			break;
		}

		mExpress.clear();

		int re = (int) result;

		// 小数整数处理
		if (result - re == 0)
			return re + "";
		else
			return result + "";
	}

	/**
	 * 需要拼接操作符和操作 操作符不许要显示在屏幕上
	 */
	public void onChecked(View view) {

		for (RadioButton rb : rbs) {
			if (rb == view) {
				isChecked = rb.isChecked();
				rb.setChecked(isChecked);
				oper = rb.getText().toString();

				// 如果显示有运算数
				if (!TextUtils.isEmpty(numDisplay.getText()) && text != null) {

					mExpress.add(numDisplay.getText().toString());
					text.setLength(0);
					isDouble = false;
				}
			}
		}
	}

	/**
	 * 弹出键盘输入对话框
	 */
	public void initKeyBord() {// 弹出式对话框

		final View view = View.inflate(this, R.layout.view_key_borde, null);

		view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));

		// 运算符的单选按钮组
		mOperator_rg = (RadioGroup) view.findViewById(R.id.operator);
		text = new StringBuilder();
		// 显示和拼接字符可以分离
		str = "";

		rbs = new RadioButton[mOperator_rg.getChildCount()];
		for (int i = 0; i < mOperator_rg.getChildCount(); i++) {
			rbs[i] = (RadioButton) mOperator_rg.getChildAt(i);
		}

		if (mpopupWindow == null) {

			mpopupWindow = new PopupWindow(this);
			mpopupWindow.setWidth(LayoutParams.MATCH_PARENT);

			mpopupWindow.setHeight(LayoutParams.WRAP_CONTENT);

			mpopupWindow.setBackgroundDrawable(new BitmapDrawable());

			mpopupWindow.setFocusable(true);
			mpopupWindow.setOutsideTouchable(false);
		}

		mpopupWindow.setContentView(view);
		mpopupWindow.showAtLocation(mSaveButton, Gravity.BOTTOM, 0, 0);
		mpopupWindow.update();

	}

	public void click(View view) {

		TextView tv = (TextView) view;

		switch (tv.getId()) {

		case R.id.number_del:// 删除
			if (text.length() > 0)
				// 删除一段字符。
				text.delete(text.length() - 1, text.length());
			break;
		case R.id.number_ac:// 清空

			mOperator_rg.clearCheck();
			text.setLength(0);
			mExpress.clear();

			break;

		case R.id.number_eq:// 等于

			if (!mExpress.isEmpty() && text.length() != 0) {
				mExpress.add(text.toString());

				str = getResult(text.toString(), oper);
				text.setLength(0);
				numDisplay.setText(str);
				oper = "";
				mOperator_rg.clearCheck();

				return;

			} else {
				// 推出键盘，获取当前的结果

			}
			mOperator_rg.clearCheck();
			break;

		case R.id.number_doc:// 小数点的处理

			// 注意:最末尾的输入可以带小数点，在做操作数处理的时候,要检查末尾是否存在小数点。
			if (!isDouble) {

				if (text.length() == 0)
					text.append("0");

				text.append(".");
				isDouble = true;
			}

			break;

		default:

			text.append(tv.getText().toString());

			// 整数首字符为0的处理
			if (text.charAt(0) == '0' & !isDouble)
				text.deleteCharAt(0);

			break;
		}

		if (text.length() == 0) {
			// 重置可以输入小数
			isDouble = false;
			str = "0";
		} else
			str = text.toString();

		numDisplay.setText(str);

	}

	/**
	 * 初始化小圆点
	 */
	public void initPoint(int count) {

		for (int i = 0; i < count; i++) {

			View point = new View(ExpeneseActivity.this);
			point.setBackgroundResource(R.drawable.point_normal_shape);
			LayoutParams params = new LayoutParams(12, 12);

			if (i != 0) {
				params.leftMargin = 12;
			}
			point.setLayoutParams(params);
			pointRoot.addView(point);
		}
	}

	class MviewPagerAdapter extends PagerAdapter {

		int pageNum = 0;// ViewPager的页数
		boolean isDone = false;

		@Override
		public int getCount() {

			mPage = mExpenseIcon.size();
			pageNum = mPage % 10 == 0 ? mPage / 10 : mPage / 10 + 1;

			if (!isDone) {
				isDone = true;
				initPoint(pageNum);
			}

			return pageNum;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			mGridView = (GridView) View.inflate(ExpeneseActivity.this, R.layout.expenese_item, null);

			int itemNum = 0;
			// 每一页每一个Grid的Item数量
			if (mItemCount / 10 >= 1) {
				mItemCount -= 10;
				itemNum = 10;
			} else {
				itemNum = mItemCount;
			}

			if (mViewHolder.size() < pageNum) {
				int len = position * 10;
				mGridView.setAdapter(new ImageGrideAdapter(itemNum, len));
				mViewHolder.add(mGridView);
			}

			container.addView(mViewHolder.get(position));

			return mViewHolder.get(position);
		}

	}

	class ImageGrideAdapter extends BaseAdapter {

		int itemNum;
		int len;

		public ImageGrideAdapter(int itemNum, int len) {

			this.itemNum = itemNum;
			this.len = len;
		}

		@Override
		public int getCount() {

			return itemNum;
		}

		@Override
		public Object getItem(int position) {

			return mExpenseIcon.get(position + len);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			TextView textView = null;

			if (convertView == null) {
				textView = mExpenseIcon.get(position + len);
				textView.setLayoutParams(new GridView.LayoutParams(110, 120));
			} else {
				textView = (TextView) convertView;
			}

			return textView;
		}
	}

	/**
	 * 添加记录
	 */
	public void addRecord(String account, String autoExp, String datePick, String Description, String title,
			String sum) {

		Accounting ac = new Accounting();

		ac.setDate(ContentActivity.mDate);
		ac.setAccount(account);
		ac.setAutoExp(autoExp);
		ac.setDatePick(datePick);
		ac.setDescription(null);
		ac.setTitle(title);
		ac.setSum(sum);

		try {
			Dao.save(ac);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}