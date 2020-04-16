package com.shicha.yzmgt.domain;

public class AutoOnOff {

	String addr;
	int branch;
	
	int[] times;
	
	public AutoOnOff() {}
	
	public AutoOnOff(String addr, int[]times) {
		this.addr = addr;
		this.times = times;
	}
	
	public AutoOnOff(String addr, int branch) {
		this.addr = addr;
		this.branch = branch;
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

	public int getBranch() {
		return branch;
	}

	public void setBranch(int branch) {
		this.branch = branch;
	}
	
	
	
	
}
