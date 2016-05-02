package com.donghai.ahorro;

/**
 * 闪屏界面
 */
import org.xutils.DbManager;
import org.xutils.DbManager.DaoConfig;
import org.xutils.x;
import com.donghai.ahorro.utils.CacheUtil;
import com.donghai.ahorro.utils.DateUtil;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {

	public static final String IS_GO_GUIDE = "com.donghai.SplashActivity";
	private static final int DB_VERSION = 1;// 数据库版本号
	private boolean isGuide;
	private DaoConfig daoConfig;// 数据库配置对象
	public static DbManager db;// 数据库管理器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		x.Ext.init((Application) this.getApplicationContext());
		// 初始化数据库
		initDB();
		initData();

		isGuide = CacheUtil.getBoolean(this, IS_GO_GUIDE);

		// 动画透明
		AlphaAnimation alphAnim = new AlphaAnimation(0f, 1f);
		alphAnim.setDuration(60);
		alphAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

				if (isGuide) {
					// 进入主界面
					startActivity(new Intent(SplashActivity.this,
							ContentActivity.class));
				} else {

					// 进入引导界面
					startActivity(new Intent(SplashActivity.this,
							GuideActivity.class));
				}

				finish();
			}
		});

		RelativeLayout spashRoot = (RelativeLayout) findViewById(R.id.splash);
		spashRoot.startAnimation(alphAnim);

	}

	/**
	 * 初始化数据库
	 */
	public void initDB() {

		if (daoConfig == null) {

			daoConfig = new DbManager.DaoConfig().setDbName("Expenese") // 设置数据库名称
					.setDbVersion(DB_VERSION) // 数据库版本
					.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
						@Override
						public void onUpgrade(DbManager db, int oldVersion,
								int newVersion) {
							// TODO: ...
							// db.addColumn(...);
							// db.dropTable(...);
						}
					});

			db = x.getDb(daoConfig);
		}
	}

	/**
	 * 初始化数据
	 */
	public void initData() {

		//写入时间
		DateUtil.WriteDate(this);
		//清空数据
		CacheUtil.putInt(this, "Position", 0);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
