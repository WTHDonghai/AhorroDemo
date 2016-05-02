package com.donghai.ahorro.view;

import com.donghai.ahorro.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class DrawerButton extends RelativeLayout {

	private ImageView mDrawer;

	public DrawerButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public DrawerButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public DrawerButton(Context context) {
		super(context);
		initView();
	}

	public void initView() {
		
		View view = View.inflate(getContext(), R.layout.view_drawer_button, this);
		mDrawer = (ImageView) view.findViewById(R.id.iv_drawer);
	}
	
	public void setImageForResource(int resourceId)
	{
		mDrawer.setImageResource(resourceId);
	}
	
}
