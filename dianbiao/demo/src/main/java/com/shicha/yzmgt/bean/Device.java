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
	double GroupactionEnergy;  //组合有功电能量
	
	@Column(nullable=false, columnDefinition="Double default 0")
    double ActionEnergy;       //":65.21,       #正向有功电能量
	
	@Column(nullable=false, columnDefinition="Double default 0")
    double ReactionEnergy;     //:219.3,     #反向有功电能量
	
	@Column(nullable=false, columnDefinition="Double default 0")
    double vol;               //":220.5,    #电压
	
	@Column(nullable=false, columnDefinition="Double default 0")
    double cur; //":1.503,    #电流
	
	@Column(nullable=false, columnDefinition="Double default 0")
    double ActionPower; //":0.3254,   #有功功率
	
	@Column(nullable=false, columnDefinition="Double default 0")
    double Freq;//":49.92,   #电网频率
	
	@Column(nullable=false, columnDefinition="Double default 0")
    double Factor;//":0.953,     #功率因数
	
	@Column(nullable=false, columnDefinition="INT default 0")
    int SwitchStat;//":1,     #开关状态： 0-合闸， 其他--拉闸状态-合闸
		
    String DateTime;//": "2019-04-11 14:15:00" #数据时间
    
	@Column(nullable=false, columnDefinition="Double default 0")
    double threshValue; //
	
		
	@Column(nullable=false, columnDefinition="INT default 1")
	Integer status = 1;		//1 offline, 0 online
	
	String userName;
	String groupName;
	
	public void syncDevice(MeterStatus meter) {
		GroupactionEnergy = meter.GroupactionEnergy;
		ActionEnergy = meter.ActionEnergy;
		ReactionEnergy= meter.ReactionEnergy;
		vol=meter.vol;
		cur=meter.cur;
		ActionPower=meter.ActionPower;
		Freq=meter.Freq;
		Factor=meter.Factor;
		SwitchStat=meter.SwitchStat;
		DateTime=meter.DataTime;		
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Device() {}
	
	
	
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}


	public double getGroupactionEnergy() {
		return GroupactionEnergy;
	}

	public void setGroupactionEnergy(double groupactionEnergy) {
		GroupactionEnergy = groupactionEnergy;
	}

	public double getActionEnergy() {
		return ActionEnergy;
	}

	public void setActionEnergy(double actionEnergy) {
		ActionEnergy = actionEnergy;
	}

	public double getReactionEnergy() {
		return ReactionEnergy;
	}

	public void setReactionEnergy(double reactionEnergy) {
		ReactionEnergy = reactionEnergy;
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
		return ActionPower;
	}

	public void setActionPower(double actionPower) {
		ActionPower = actionPower;
	}

	public double getFreq() {
		return Freq;
	}

	public void setFreq(double freq) {
		Freq = freq;
	}

	public double getFactor() {
		return Factor;
	}

	public void setFactor(double factor) {
		Factor = factor;
	}

	public int getSwitchStat() {
		return SwitchStat;
	}

	public void setSwitchStat(int switchStat) {
		SwitchStat = switchStat;
	}

	public String getDateTime() {
		return DateTime;
	}

	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}

	public double getThreshValue() {
		return threshValue;
	}

	public void setThreshValue(double threshValue) {
		this.threshValue = threshValue;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
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
