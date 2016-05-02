package com.donghai.ahorro.view;

import java.util.LinkedList;
import java.util.List;
import com.donghai.ahorro.R;
import com.donghai.dao.Accounting;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 将listView中的item封装 RecordListItem类 20160417 16:30
 * 
 * @author donghai
 *
 */
public class ExpesenPagerItem extends LinearLayout {

	public LinkedList<List<Accounting>> mDataCache;
	public ListView listV;
	public List<Accounting> dayList;// 随着列表的动态删除，list中存放的数据是可变的
	private TextView sumTv;
	public int visiAbleCunt = 0;

	public ExpesenPagerItem(Context context, int position, LinkedList<List<Accounting>> mDataCache) {
		super(context);
		this.mDataCache = mDataCache;
		initView(position);
	}

	private void initView(int position) {

		View view = View.inflate(getContext(), R.layout.day_of_record_view, this);
		view.setFocusable(false);

		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setDelectIconGone();
			}
		});

		listV = (ListView) view.findViewById(R.id.recordList);
		listV.setFocusable(false);
		sumTv = (TextView) view.findViewById(R.id.sumText);

		listV.setAdapter(new DayOfList(position));
		listV.setVerticalScrollBarEnabled(false);// 去掉滚动条
		listV.setDivider(new ColorDrawable(0x00ffffff));// 设置间距的颜色为透明
		listV.setDividerHeight(8);// 设置每项的间距

		// 长按删除事件处理
		listV.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

				setDelectIconGone();
				RecordListItem recordItem = (RecordListItem) listV.getChildAt(position);
				recordItem.isShowDeleteIcon(true);
				visiAbleCunt = position;
				listV.deferNotifyDataSetChanged();
				// 我日，之前做的逻辑判断到底是为了啥？？false表示不拦截事件，事件继续传递，最后会触发单击事件
				return true;
			}
		});

		listV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				setDelectIconGone();
			}

		});

		// 设置数据设置观察者
		listV.getAdapter().registerDataSetObserver(new DataSetObserver() {

			// 监听list中的数据的该表
			@Override
			public void onChanged() {
				super.onChanged();
				// 重新计算总金额
				setSumOfDay();
			}

		});
		listV.deferNotifyDataSetChanged();

	}

	/**
	 * 设置一天的消费总金额
	 */
	public void setSumOfDay() {
		
		double sum = 0;

		for (Accounting ac : dayList)
			sum += Double.valueOf(ac.getSum());

		if ((int) sum - sum == 0)
			sumTv.setText((int) sum + "");
		else
			sumTv.setText(sum + "");
	}

	/**
	 * 设置所有的删除图标消失
	 */
	private void setDelectIconGone() {

		RecordListItem recordItem = (RecordListItem) listV.getChildAt(visiAbleCunt);
		if (recordItem != null && recordItem.getShown()) {
			recordItem.isShowDeleteIcon(false);
			visiAbleCunt = 0;
		}

		// for (int i = 0; i < dayList.size(); i++) {
		//
		// RecordListItem recordItem = (RecordListItem) listV.getChildAt(i);
		//
		// if(recordItem.getShown())
		// {
		// recordItem.isShowDeleteIcon(false);
		// }
		// }
	}

	/**
	 * listAdapter
	 * 
	 * @author donghai
	 *
	 */
	class DayOfList extends BaseAdapter {

		public DayOfList(int pagerNum) {

			dayList = mDataCache.get(pagerNum);
			setSumOfDay();
		}

		@Override
		public int getCount() {
			// 当天记录的条数
			return dayList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			// 视图缓存
			RecordListItem recordItem = null;
			ListItemHolder holder = null;

			if (convertView == null) {

				recordItem = new RecordListItem(getContext());
				convertView = recordItem;
				holder = new ListItemHolder();
				holder.re = recordItem;
				convertView.setTag(holder);

			} else {
				holder = (ListItemHolder) convertView.getTag();
			}

			// 如果当天的有记录
			if (dayList.size() != 0) {

				holder.re.setTitle(dayList.get(position).getTitle());
				holder.re.setPrice(dayList.get(position).getSum());
				holder.re.setCategoryIcon(getResources().getDrawable(dayList.get(position).getIconId()));
				holder.re.setRecordID(dayList.get(position).getId() + "");
				holder.re.setCount(position);
				holder.re.setList(dayList);
			}

			return convertView;
		}

		class ListItemHolder {
			RecordListItem re;
		}

	}

}
