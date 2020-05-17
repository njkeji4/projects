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
	
	String roomName;
	String roomId;
	
	String deviceName;
	String deviceNo;
	String groupName;
	
	double energy;
	
	double aEnergy;
	double bEnergy;
	double cEnergy;
	double dEnergy;
	
	long aOntime;
	long bOntime;
	long cOntime;
	long dOntime;
	
	long statDate;
	
	
	public DeviceStat() {}
	
	
	public DeviceStat(
			String deviceNo, String deviceName, 
			String groupName, String roomName, String roomId, 
			long statDate, 
			double energy, double a, double b, double c, double d, 
			long aOntime, long bOntime, long cOntime, long dOntime ) {
		
		this.deviceName = deviceName;
		this.groupName = groupName;		
		this.deviceNo = deviceNo;
		this.roomName = roomName;
		this.roomId = roomId;
		
		this.statDate = statDate;		
		
		this.energy = (double)Math.round(energy * 100) /100;
		this.aEnergy = (double)Math.round(a * 100) /100;
		this.bEnergy = (double)Math.round(b * 100) /100;
		this.cEnergy = (double)Math.round(c * 100) /100;
		this.dEnergy = (double)Math.round(d * 100) /100;
		
		this.aOntime = aOntime;
		this.bOntime = bOntime;
		this.cOntime = cOntime;
		this.dOntime = dOntime;		
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


	public String getRoomName() {
		return roomName;
	}


	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}


	public String getRoomId() {
		return roomId;
	}


	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}


	public String getDeviceName() {
		return deviceName;
	}


	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}


	public String getDeviceNo() {
		return deviceNo;
	}


	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}


	public String getGroupName() {
		return groupName;
	}


	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	public double getEnergy() {
		return energy;
	}


	public void setEnergy(double energy) {
		this.energy = energy;
	}


	public double getaEnergy() {
		return aEnergy;
	}


	public void setaEnergy(double aEnergy) {
		this.aEnergy = aEnergy;
	}


	public double getbEnergy() {
		return bEnergy;
	}


	public void setbEnergy(double bEnergy) {
		this.bEnergy = bEnergy;
	}


	public double getcEnergy() {
		return cEnergy;
	}


	public void setcEnergy(double cEnergy) {
		this.cEnergy = cEnergy;
	}


	public double getdEnergy() {
		return dEnergy;
	}


	public void setdEnergy(double dEnergy) {
		this.dEnergy = dEnergy;
	}


	public long getaOntime() {
		return aOntime;
	}


	public void setaOntime(long aOntime) {
		this.aOntime = aOntime;
	}


	public long getbOntime() {
		return bOntime;
	}


	public void setbOntime(long bOntime) {
		this.bOntime = bOntime;
	}


	public long getcOntime() {
		return cOntime;
	}


	public void setcOntime(long cOntime) {
		this.cOntime = cOntime;
	}


	public long getdOntime() {
		return dOntime;
	}


	public void setdOntime(long dOntime) {
		this.dOntime = dOntime;
	}


	public long getStatDate() {
		return statDate;
	}


	public void setStatDate(long statDate) {
		this.statDate = statDate;
	}	
}
