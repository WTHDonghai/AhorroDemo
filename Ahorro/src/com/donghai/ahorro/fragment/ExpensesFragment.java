package com.donghai.ahorro.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import com.donghai.ahorro.ContentActivity;
import com.donghai.ahorro.R;
import com.donghai.ahorro.fragment.base.BaseFragment;
import com.donghai.ahorro.utils.CacheUtil;
import com.donghai.ahorro.utils.DateUtil;
import com.donghai.ahorro.view.ExpesenPagerItem;
import com.donghai.dao.Accounting;
import com.donghai.dao.Dao;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

/**
 * 支出的Fragment
 * 
 * @author A
 *
 */
public class ExpensesFragment extends BaseFragment {

	/**
	 * 如何查找数据？？
	 * 
	 * 开启程序，首先查找出当天数据。
	 *
	 * 其次查找前一天和后一天的数据
	 * 
	 */
	private View view;
	private android.support.v4.view.ViewPager mContentPager;
	public LinkedList<List<Accounting>> mDataCache;
	private ArrayList<ExpesenPagerItem> recordHolder;
	private ArrayList<String> dateList;// 日期的列表
	private int mCurrentPage = 506;// 当前的页数
	private int middlePage = 500;

	@Override
	public View initView() {

		view = View.inflate(getActivity(), R.layout.fragemtn_expenese, null);
		mContentPager = (ViewPager) view.findViewById(R.id.cont_pager);

		return view;
	}

	public int getCurrentPositon() {
		return mContentPager.getCurrentItem();
	}

	/**
	 * 更新界面
	 */
	public void updataView() {

		// 设置ViewPager适配器
		mContentPager.setAdapter(new ViwPagerAdapter());
		mContentPager.setCurrentItem(middlePage);
		mContentPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// 更新当天的日期
				String curDate = dateList.get((mCurrentPage + position) % dateList.size());
				((ContentActivity) getActivity()).setDateView(curDate);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int position) {
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();

		// 判断用户是否更添加了数据
		boolean isUpdata = ((ContentActivity) getActivity()).isUpdata;
		if (isUpdata) {
			upData();
		}
		int pagePosition = CacheUtil.getInt(getActivity(), "Position");

		if (pagePosition != 0) {

			middlePage = pagePosition;
			System.out.println(pagePosition);
		} else {
			middlePage = 500;
		}

		mContentPager.setCurrentItem(middlePage);
	}

	/**
	 * 更新数据
	 */
	public void upData() {

		/*
		 * 遍历用户添加的记录在当前缓存的记录当中么？ 如果在获取当前缓冲记录的那天天，重新查找那一天的记录
		 * 如果不再，则缓冲清空，重新查找当前日历所处的位置的前三天和后三天的位置（一共7天）
		 */
		initDate();
		mContentPager.setAdapter(new ViwPagerAdapter());
	}

	/**
	 * 初始化数据
	 */
	@Override
	public void initDate() {

		// 每次进来都先初始化数据
		mDataCache = new LinkedList<>();
		recordHolder = new ArrayList<>();
		dateList = new ArrayList<>();
		
		/*
		 * 查询当天往前3天，往后3天共7天记录存放到mDataCaChe中 index决定pager
		 */
		for (int i = -30, index = 0; i <= 30; i++, index++) {

			String date = DateUtil.dateRoll(i);
			// 决定List的条目
			mDataCache.add(Dao.curSearch(date));
			ExpesenPagerItem epi = new ExpesenPagerItem(getContext(), index, mDataCache);
			recordHolder.add(epi);
			dateList.add(date);
		}
		
	}
	
	public void reInitData(Date date)
	{
		// 每次进来都先初始化数据
		mDataCache.clear();;
		recordHolder.clear();
		dateList.clear();
		/*
		 * 查询当天往前3天，往后3天共7天记录存放到mDataCaChe中 index决定pager
		 */
		for (int i = -30, index = 0; i <= 30; i++, index++) {

			String dateRecord = DateUtil.dateRoll(i,date);
			// 决定List的条目
			mDataCache.add(Dao.curSearch(dateRecord));
			ExpesenPagerItem epi = new ExpesenPagerItem(getContext(), index, mDataCache);
			recordHolder.add(epi);
			dateList.add(dateRecord);
		}
	}

	class ViwPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {

			return 1000;
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

			container.addView((recordHolder.get((position + mCurrentPage) % recordHolder.size())));
			return (recordHolder.get((position + mCurrentPage) % recordHolder.size()));
		}
	}

}
