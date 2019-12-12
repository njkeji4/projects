package com.shicha.yzmgt.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.shicha.yzmgt.aircb.MeterStatus;

@Entity(name="device")
@Table(indexes={
			@Index(name="deviceNo_Index",columnList="deviceNo")
		})
public class Device {

	public static int device_status_online = 0;
	public static int device_status_offline = 1;
	
	public static int device_switch_close = 0;  //0-合闸状态
	public static int device_switch_open = 1;	//其他值是拉闸状态
	
	
	@Id
	String deviceNo;

	String deviceName;
	
	@Column(nullable=false, columnDefinition="Double default 0")
	double groupactionEnergy;  //组合有功电能量
	
	@Column(nullable=false, columnDefinition="Double default 0")
    double actionEnergy;       //":65.21,       #正向有功电能量
	
	@Column(nullable=false, columnDefinition="Double default 0")
    double reactionEnergy;     //:219.3,     #反向有功电能量
	
	@Column(nullable=false, columnDefinition="Double default 0")
    double vol;               //":220.5,    #电压
	
	@Column(nullable=false, columnDefinition="Double default 0")
    double cur; //":1.503,    #电流
	
	@Column(nullable=false, columnDefinition="Double default 0")
    double actionPower; //":0.3254,   #有功功率
	
	@Column(nullable=false, columnDefinition="Double default 0")
    double freq;//":49.92,   #电网频率
	
	@Column(nullable=false, columnDefinition="Double default 0")
    double factor;//":0.953,     #功率因数
	
	@Column(nullable=false, columnDefinition="INT default 0")
    int switchStat;//":1,     #开关状态： 0-合闸， 其他--拉闸状态-合闸
		
    String dateTime;//": "2019-04-11 14:15:00" #数据时间
    
	@Column(nullable=false, columnDefinition="Double default 0")
    double threshValue; //
	
		
	@Column(nullable=false, columnDefinition="INT default 1")
	Integer status = 1;		//1 offline, 0 online
	
	String userName;
	String groupName;
	
	public void syncDevice(MeterStatus meter) {
		groupactionEnergy = meter.GroupactionEnergy;
		actionEnergy = meter.ActionEnergy;
		reactionEnergy= meter.ReactionEnergy;
		vol=meter.vol;
		cur=meter.cur;
		actionPower=meter.ActionPower;
		freq=meter.Freq;
		factor=meter.Factor;
		switchStat=meter.SwitchStat;
		dateTime=meter.DataTime;		
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

	public double getGroupactionEnergy() {
		return groupactionEnergy;
	}

	public void setGroupactionEnergy(double groupactionEnergy) {
		this.groupactionEnergy = groupactionEnergy;
	}

	public double getActionEnergy() {
		return actionEnergy;
	}

	public void setActionEnergy(double actionEnergy) {
		this.actionEnergy = actionEnergy;
	}

	public double getReactionEnergy() {
		return reactionEnergy;
	}

	public void setReactionEnergy(double reactionEnergy) {
		this.reactionEnergy = reactionEnergy;
	}

	public double getVol() {
		return vol;
	}

	public void setVol(double vol) {
		this.vol = vol;
	}

	public double getCur() {
		return cur;
	}

	public void setCur(double cur) {
		this.cur = cur;
	}

	public double getActionPower() {
		return actionPower;
	}

	public void setActionPower(double actionPower) {
		this.actionPower = actionPower;
	}

	public double getFreq() {
		return freq;
	}

	public void setFreq(double freq) {
		this.freq = freq;
	}

	public double getFactor() {
		return factor;
	}

	public void setFactor(double factor) {
		this.factor = factor;
	}

	public int getSwitchStat() {
		return switchStat;
	}

	public void setSwitchStat(int switchStat) {
		this.switchStat = switchStat;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public double getThreshValue() {
		return threshValue;
	}

	public void setThreshValue(double threshValue) {
		this.threshValue = threshValue;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	
	
}
