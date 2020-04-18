package com.shicha.yzmgt.bean.basestation;

public class BaseConf {

	int duration = 1;		//连续几个小时
	
	int daysCount = 4;			//一周之中有几天满足

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDaysCount() {
		return daysCount;
	}

	public void setDaysCount(int daysCount) {
		this.daysCount = daysCount;
	}
	
}
