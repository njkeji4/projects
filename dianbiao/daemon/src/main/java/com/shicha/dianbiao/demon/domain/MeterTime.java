package com.shicha.dianbiao.demon.domain;

import com.shicha.dianbiao.demon.netty.Utils;

public class MeterTime extends Meter{

	int hour;
	int minute;
	int second;
	
	public MeterTime() {}
	
	public MeterTime(byte[] buf) {
		int startIdx = 14;
		
		if(buf.length < 19) {
			return;
		}
		
		second =  Utils.bcd(buf[startIdx++]);
		minute =  Utils.bcd(buf[startIdx++]);
		hour = Utils.bcd(buf[startIdx]);
	}
	
	
	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}
	
	
}
