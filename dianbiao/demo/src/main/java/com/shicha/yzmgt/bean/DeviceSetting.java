package com.shicha.yzmgt.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;


@Entity(name="device_setting")
public class DeviceSetting {

	public static int action_close = 0;
	public static int action_open = 1;
	
	
	@Id
	@Column(name="id", nullable=false, length=36)
	@GenericGenerator(name="system-uuid", strategy="uuid2")
	@GeneratedValue(generator="system-uuid")
	String id;
	
	String deviceNo;
	String deviceName;
	
	long offTime;
	long onTime;	
	int branch;
	
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
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public long getOffTime() {
		return offTime;
	}
	public void setOffTime(long offTime) {
		this.offTime = offTime;
	}
	public long getOnTime() {
		return onTime;
	}
	public void setOnTime(long onTime) {
		this.onTime = onTime;
	}
	public int getBranch() {
		return branch;
	}
	public void setBranch(int branch) {
		this.branch = branch;
	}	
}
