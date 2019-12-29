package com.shicha.dianbiao.demon.domain;

import com.shicha.dianbiao.demon.netty.Utils;

public class MeterDate {

	int week;
	int day;
	int month;
	int year;
	
	public MeterDate() {}
	
	public MeterDate(byte[] buf) {
		int startIdx = 14;
		
		if(buf.length < 20) {
			return;
		}
		
		week =  Utils.bcd(buf[startIdx++]);
		day =  Utils.bcd(buf[startIdx++]);
		month = Utils.bcd(buf[startIdx++]);
		year = Utils.bcd(buf[startIdx]);
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
}
