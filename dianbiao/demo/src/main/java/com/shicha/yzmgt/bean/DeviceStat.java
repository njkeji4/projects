package com.shicha.yzmgt.bean;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity(name="device_stat")
public class DeviceStat {

	@Id
	@Column(name="id", nullable=false, length=36)
	@GenericGenerator(name="system-uuid", strategy="uuid2")
	@GeneratedValue(generator="system-uuid")
	String id;
	
	String deviceName;
	String deviceNo;
	String groupName;
	
	long statDate;
	double energy;
	long ontime;
	
	long month;
	
	public DeviceStat() {}
	
	public DeviceStat(double energy, long ontime, long statDate) {
		this.energy = energy;
		this.ontime = ontime;
		this.statDate = statDate;
	}
	
	public DeviceStat(String deviceNo, long statDate, double energy, long ontime, String deviceName, String groupName) {
		this.deviceNo = deviceNo;
		this.statDate = statDate;
		this.energy = energy;
		this.ontime = ontime;
		this.deviceName = deviceName;
		this.groupName = groupName;		
		
		this.month = caclMonth(statDate);
	}
	
	public static long caclMonth(long date) {
		Calendar c =  Calendar.getInstance();
		c.setTimeInMillis(date);
		
		c.set(Calendar.DAY_OF_MONTH, 1);	
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		return c.getTimeInMillis();
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public long getStatDate() {
		return statDate;
	}

	public void setStatDate(long statDate) {
		this.statDate = statDate;
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public long getOntime() {
		return ontime;
	}

	public void setOntime(long ontime) {
		this.ontime = ontime;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public long getMonth() {
		return month;
	}

	public void setMonth(long month) {
		this.month = month;
	}
	
}
