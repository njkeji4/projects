package com.shicha.yzmgt.bean;

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
	
	
	public DeviceStat() {}
	
	public DeviceStat(String deviceNo, long statDate, double energy, long ontime, String deviceName, String groupName) {
		this.deviceNo = deviceNo;
		this.statDate = statDate;
		this.energy = energy;
		this.ontime = ontime;
		this.deviceName = deviceName;
		this.groupName = groupName;
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
	
}
