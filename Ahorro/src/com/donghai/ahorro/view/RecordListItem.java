package com.donghai.ahorro.view;

import java.util.List;

import com.donghai.ahorro.R;
import com.donghai.dao.Accounting;
import com.donghai.dao.Dao;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 记录的列表视图
 * 
 * @author donghai
 *
 */
public class RecordListItem extends RelativeLayout {
	private View root;
	private ImageView categoryIcon;// 类别图标
	private TextView titleTextView;// 列别名
	private TextView priceTextView;// 单价
	private boolean isDelectShown;// 是否显示删除图标
	public ImageView delectIcon;// 删除的图标
	private AnimatorSet setIn;
	private AnimatorSet setOut;
	private String recordId;// 删除记录的ID
	private int count;// 当前为第几条条目纪录
	private List<Accounting> dayList;
	public int flag = -1;

	public RecordListItem(Context context) {
		super(context);
		initView();
	}

	/**
	 * 视图初始化
	 */
	private void initView() {
		isDelectShown = false;
		finidView();
		setUpAnimation();
	}

	private void setUpAnimation() {

		setIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_right_in);
		setIn.setTarget(delectIcon);
		setIn.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				delectIcon.setVisibility(View.VISIBLE);
				flag = count;
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});

		setOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_right_out);
		// 设置翻转动画
		setOut.setTarget(delectIcon);
		setOut.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				delectIcon.setVisibility(View.GONE);
				flag = -1;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		
		RotateAnimation rotaAni = new RotateAnimation(0, -100);
		this.setAnimation(rotaAni);
	}

	private void finidView() {
		root = View.inflate(getContext(), R.layout.content_list_item, this);
		categoryIcon = (ImageView) root.findViewById(R.id.icon);
		titleTextView = (TextView) root.findViewById(R.id.title);
		priceTextView = (TextView) root.findViewById(R.id.price);
		delectIcon = (ImageView) root.findViewById(R.id.delete);

		delectIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				 Dao.deleteById(recordId);
				// 刷新父容器界面
				 ListView parent = (ListView) RecordListItem.this.getParent();
				 dayList.remove(count);
				 ((BaseAdapter)parent.getAdapter()).notifyDataSetChanged();
				 delectIcon.setVisibility(View.GONE);
			}
		});
	}

	public void isShowDeleteIcon(boolean isShow) {
		isDelectShown = isShow;

		if (isDelectShown) {
			setIn.start();
		} else {
			setOut.start();
		}
	}

	/**
	 * 获取显示的状态
	 */
	public boolean getShown() {
		return isDelectShown;
	}

	/**
	 * 根据资源ID设置类别图标
	 * 
	 * @param resid
	 */
	public void setCategoryIcon(Drawable drawable) {
		categoryIcon.setImageDrawable(drawable);
	}

	/**
	 * 设置类别名称
	 */
	public void setTitle(String title) {
		titleTextView.setText(title);
	}

	/**
	 * 设置单价
	 * 
	 * @param price
	 */
	public void setPrice(String price) {
		priceTextView.setText(price);
	}

	/**
	 * 获取消费单价
	 */
	public String getPrice() {
		return priceTextView.getText().toString();
	}

	/**
	 * 设置ID
	 */
	public void setRecordID(String recordID) {
		this.recordId = recordID;
	}

	/**
	 * 设置当前的条目数
	 * @param count
	 */
	public void setCount(int count) 
	{
		this.count = count;
	}

	/**
	 * 方便移除list中的元素
	 * @param dayList
	 */
	public void setList(List<Accounting> dayList) 
	{
		this.dayList = dayList;
	}

}