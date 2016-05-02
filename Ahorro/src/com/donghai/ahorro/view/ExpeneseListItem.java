package com.donghai.ahorro.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.donghai.ahorro.R;

public class ExpeneseListItem extends RelativeLayout{

	private ImageView itemIv;
	private TextView itemTx;
	
	public ExpeneseListItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public ExpeneseListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public ExpeneseListItem(Context context) {
		super(context);
		initView();
	}

	
	public void initView() {

		View view = View.inflate(getContext(), R.layout.view_expenese_list_item, this);
		itemIv = (ImageView) view.findViewById(R.id.itemImage);
		itemTx = (TextView)view.findViewById(R.id.itemText);
	}
	
	public void setImage(int resourceId)
	{
		itemIv.setImageResource(resourceId);
	}
	
	public void setText(int resourceId)
	{
		itemTx.setText(resourceId);
	}

}
