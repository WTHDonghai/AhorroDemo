package com.donghai.ahorro;

import com.donghai.ahorro.utils.CacheUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class GuideActivity extends Activity {

	private ViewPager mViewPager;
	//引导界面图
	private int[] mImageId = new int[] { R.drawable.introduction_1_en, R.drawable.introduction_2_en,
			R.drawable.introduction_3_en, R.drawable.introduction_4_en, R.drawable.introduction_5_en,
			R.drawable.introduction_6_en, R.drawable.introduction_7_en };

	private View mPagerRoot;
	private Button goBtn;
	private AlphaAnimation alphAnim;
	private View scPoint;//�ƶ��ĵ�
	private LinearLayout pointRoot;
	private int pointWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		
		pointRoot = (LinearLayout) findViewById(R.id.point_root);
		scPoint = findViewById(R.id.v_scPoint);
		
		scPoint.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				
				//获取视图树观察者
				scPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				
				pointWidth = pointRoot.getChildAt(1).getLeft()-pointRoot.getChildAt(0).getLeft();
			}
		});
		
		//添加小灰点
		for(int i = 0; i < mImageId.length;i++)
		{
			View point = new View(this);
			point.setBackgroundResource(R.drawable.point_normal_shape);
			LayoutParams params = new LayoutParams(20, 20);
			
			if(i != 0)
			{
				params.leftMargin = 20;
			}
			point.setLayoutParams(params);
			pointRoot.addView(point);
		}
		
		
		alphAnim = new AlphaAnimation(0, 1);
		alphAnim.setDuration(800);

		mViewPager = (ViewPager) findViewById(R.id.guideImage);

		mViewPager.setAdapter(new GuidePagerAdapter());

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			/**
			 * @param position
			 */
			@Override
			public void onPageSelected(int position) {

					if (position == mImageId.length - 1) {
						goBtn.startAnimation(alphAnim);
					}
			}

			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {
				
				int moveLength = (int) (pointWidth*(position+arg1));
				
				RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) scPoint.getLayoutParams();
				params.leftMargin = moveLength;
				scPoint.setLayoutParams(params);
			}

			@Override
			public void onPageScrollStateChanged(int position) {

			}
		});

	}

	class GuidePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mImageId.length;
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

			mPagerRoot = View.inflate(GuideActivity.this, R.layout.view_pager_guide, null);

			mPagerRoot.setBackgroundResource(mImageId[position]);

			goBtn = (Button) mPagerRoot.findViewById(R.id.btn_go);
			goBtn.setOnClickListener(new GoContext());
			
				if (position == mImageId.length - 1) {
					
					goBtn.setVisibility(View.VISIBLE);
					goBtn.setClickable(true);
				}

			container.addView(mPagerRoot);

			return mPagerRoot;
		}
	}

	class GoContext implements OnClickListener {
		@Override
		public void onClick(View v) {

			// 不用再进入引导界面
			CacheUtil.putBoolean(GuideActivity.this, SplashActivity.IS_GO_GUIDE, true);
			
			startActivity(new Intent(GuideActivity.this, ContentActivity.class));
			finish();
		}

	}
	
	

}
