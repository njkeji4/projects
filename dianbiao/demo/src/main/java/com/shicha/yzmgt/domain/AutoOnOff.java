package com.shicha.yzmgt.domain;

public class AutoOnOff {

	String addr;
	int branch;
	
	Integer[] times;
	
	public AutoOnOff() {}
	
	
	public AutoOnOff(String addr, int branch, Integer[]times) {
		this.addr = addr;
		this.times = times;
		this.branch = branch;
	}
	
	public AutoOnOff(String addr, int branch) {
		this.addr=addr;
		this.branch = branch;
	}
	
	
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	

	public int getBranch() {
		return branch;
	}

	public void setBranch(int branch) {
		this.branch = branch;
	}


	public Integer[] getTimes() {
		return times;
	}


	public void setTimes(Integer[] times) {
		this.times = times;
	}
	
	public String toString() {
		String time = "";
		
		if(times.length > 0 && times[0] == -1) {
			time="清除设置";
		}
			
		for(int i = 0; i < times.length; i++) {
			if(times[i] == -1)
				continue;
			
			time +=  (i % 2 == 0) ? "[" :  " ";
			time+= ( (times[i] >> 16) & 0xff) + ":" + ( (times[i] >> 8) & 0xff);			
			time +=  (i % 2 == 0) ? "," :  "]";
		}
		return branch + "路" + ":" + time;
	}
}
