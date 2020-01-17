package com.shicha.dianbiao.demon.domain;

public class MeterValue extends Meter{

	//for interger and float
	double value;
	
	public MeterValue() {}
	
	//sublen - 小数位数
	public MeterValue(byte[] buf, int len, int sublen) {
		int startIdx = 14;
		
		if(sublen == 0) {
			value = MeterData.parseInt(buf, startIdx, len);
		}else {
			value = MeterData.parseDouble(buf, startIdx, len, sublen);
		}
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
}
