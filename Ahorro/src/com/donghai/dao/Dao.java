package com.donghai.dao;

import java.util.ArrayList;
import java.util.List;

import org.xutils.ex.DbException;

import com.donghai.ahorro.SplashActivity;

/**
 * 数据库类
 * 
 * @author donghai
 *
 */
public class Dao {

	/**
	 * 根据指定id删除记录
	 */
	public static void deleteById(String id)
	{
		try {
			SplashActivity.db.deleteById(Accounting.class, id);;
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查找指定日期的数据
	 * 
	 * @param date
	 */
	public static List<Accounting> curSearch(String date) {

		List<Accounting> allData = null;
		try {
			Date d = SplashActivity.db.selector(Date.class).where("date", "=", date).findFirst();

			String datePick = null;
			if (d != null) {

				datePick = d.getDatePick();
			}
			allData = SplashActivity.db.selector(Accounting.class).where("datePick", "=", datePick).findAll();
			
			//防止第一次启动时数据库中没有任何数据
			if(allData == null)
			{
				allData = new ArrayList<>();
			}

			/*
			 * test
			 */
			// for (Accounting ac : allData) {
			// System.out.println(ac.getDate());
			// System.out.println(ac.getTitle());
			// System.out.println(ac.getSum());
			// }

		} catch (DbException e) {
			e.printStackTrace();
		}
		return allData;
	}

	/**
	 * 保持日期
	 * 
	 * @param date
	 * @throws DbException
	 */
	public static void save(Date date) throws DbException 
	{
		SplashActivity.db.save(date);
	}

	/**
	 * 保存消费记录
	 * @param accounting
	 * @throws DbException
	 */
	public static void save(Accounting accounting) throws DbException 
	{
		SplashActivity.db.save(accounting);
	}

	public static List<Accounting> selectAllAccouting() throws DbException {

		return SplashActivity.db.findAll(Accounting.class);
	}
	
	/**
	 * 查找所有的日期数据
	 * 
	 * @return
	 * @throws DbException
	 */
	public static List<Date> selectAllDate() throws DbException {

		return SplashActivity.db.findAll(Date.class);
	}
}
