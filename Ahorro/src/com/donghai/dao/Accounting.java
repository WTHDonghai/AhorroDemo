package com.donghai.dao;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "Accounting")
public class Accounting {

	// 记录ID
	@Column(name = "id", isId = true, autoGen = true)
	private int id;

	// 条目标题
	@Column(name = "title")
	private String title;

	// 条目名
	@Column(name = "sum")
	private String sum;

	// 日期
	@Column(name = "date")
	private String date;

	// 描述
	@Column(name = "description")
	private String description;

	@Column(name = "datePick")
	private String datePick;

	// 账户
	@Column(name = "account")
	private String account;

	// 固定支出
	@Column(name = "autoExp")
	private String autoExp;

	//图标资源的ID
	@Column(name = "iconId")
	private String iconId;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDatePick() {
		return datePick;
	}

	public void setDatePick(String datePick) {
		this.datePick = datePick;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAutoExp() {
		return autoExp;
	}

	public void setAutoExp(String autoExp) {
		this.autoExp = autoExp;
	}

	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("日期：");
		sb.append(date);
		
		return sb.toString();
	}

	public void setIconId(String iconId)
	{
		this.iconId = iconId;
	}
	public int getIconId() {
		
		return Integer.parseInt(iconId);
	}

}
