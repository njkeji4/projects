package com.shicha.yzmgt.aircb;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MeterPeriod {

	int HH=10;
	int MM;
	int SS;
	
	@JsonProperty("HH")
	public int getHH() {
		return HH;
	}
	
	@JsonProperty("HH")
	public void setHH(int hH) {
		HH = hH;
	}
	
	@JsonProperty("MM")
	public int getMM() {
		return MM;
	}
	@JsonProperty("MM")
	public void setMM(int mM) {
		MM = mM;
	}
	
	@JsonProperty("SS")
	public int getSS() {
		return SS;
	}
	@JsonProperty("SS")
	public void setSS(int sS) {
		SS = sS;
	}
	
	
		
}
