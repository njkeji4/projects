package com.shicha.yzmgt.bean.basestation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity(name="base_conf")
public class BaseConf {

	@Id
	@Column(name="id", nullable=false, length=36)
	@GenericGenerator(name="system-uuid", strategy="uuid2")
	@GeneratedValue(generator="system-uuid")
	String id;
	
	
	int duration = 1;		//连续几个小时
	int daysCount = 4;			//一周之中有几天满足
	double traffic = 10;
	double prbdown = 3;
	double prbup = 3;
	double activeUser = 0.5;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getTraffic() {
		return traffic;
	}

	public void setTraffic(double traffic) {
		this.traffic = traffic;
	}

	public double getPrbdown() {
		return prbdown;
	}

	public void setPrbdown(double prbdown) {
		this.prbdown = prbdown;
	}

	public double getPrbup() {
		return prbup;
	}

	public void setPrbup(double prbup) {
		this.prbup = prbup;
	}

	public double getActiveUser() {
		return activeUser;
	}

	public void setActiveUser(double activeUser) {
		this.activeUser = activeUser;
	}
	
}
