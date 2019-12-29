package com.shicha.dianbiao.demon.domain;

import com.shicha.dianbiao.demon.netty.Utils;

public class AutoOnOff {

	String addr;
	int[] times;
	
	public AutoOnOff() {}
	
	public AutoOnOff(byte[] buf) {
		int startIdx = 14;
		
		if(buf.length < 40) {
			return;
		}
		times = new int[8];
		for(int i = 0; i < 8; i++) {
			times[i] = buf[startIdx++] -0x33 +( (buf[startIdx++] -0x33) << 8 )  + ( (buf[startIdx++] -0x33) << 16 );
		}
	}
	
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public int[] getTimes() {
		return times;
	}
	public void setTimes(int[] times) {
		this.times = times;
	}
	
	
}
