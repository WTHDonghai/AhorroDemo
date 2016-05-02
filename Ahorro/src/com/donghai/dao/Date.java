package com.donghai.dao;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "Date")
public class Date {

	@Column(name = "id",isId = true,autoGen = true)
	private String id;
	
	
	@Column(name = "date")
	private String date;
	
	@Column(name = "datePick")
	private String datePick;
	
	public String getDatePick() {
		
		return datePick;
	}
	
	public void setDatePick(String datePick)
	{
		this.datePick = datePick;
	}

	public void setDate(String date) {
		
		this.date = date;
	}
	
}
