package com.shicha.dianbiao.demon.domain;

import com.shicha.dianbiao.demon.netty.Utils;

public class MeterPeriod extends Meter{

	int minute;
	
	public MeterPeriod() {}
	
	public MeterPeriod(byte[] buf) {
		
		int startIdx = 14;
		
		minute = Utils.bcd(buf[15]) * 10 + Utils.bcd(buf[14]);
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}	
}
