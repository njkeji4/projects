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
	@Column(nullable=false, columnDefinition="INT default 1")
	Integer status = 1;		//1 offline, 0 online	
	
	@Column(nullable=false, columnDefinition="BIGINT default 0")
	Long lastHeartBeatTime;
	
	String userName;
	String groupName;	
	
	String cmdId;		//一个设备一次只能执行一条命令，必须等待返回或者超时
	Long cmdTime;		//发送命令时间
	
	
	//////data field
	@Column(nullable=false, columnDefinition="Double default 0")
	double allEnergy;  //组合有功总电量		
	@Column(nullable=false, columnDefinition="Double default 0")
	double allJianEnvery;  //组合有功尖费率电量		
	@Column(nullable=false, columnDefinition="Double default 0")
	double allFengEnvery;  //组合有功峰费率电量		
	@Column(nullable=false, columnDefinition="Double default 0")
	double allPingEnvery;  //组合有功平费率电量
	@Column(nullable=false, columnDefinition="Double default 0")
	double allGuEnvery; //组合有功谷费率电量/
	@Column(nullable=false, columnDefinition="Double default 0")
	double avol;
	@Column(nullable=false, columnDefinition="Double default 0")
	double bvol;
	@Column(nullable=false, columnDefinition="Double default 0")
	double cvol;
	@Column(nullable=false, columnDefinition="Double default 0")
	double acur;
	@Column(nullable=false, columnDefinition="Double default 0")
	double bcur;
	@Column(nullable=false, columnDefinition="Double default 0")
	double ccur;
	
	@Column(nullable=false, columnDefinition="Double default 0")
	double aActionPower;
	@Column(nullable=false, columnDefinition="Double default 0")
	double bActionPower;
	@Column(nullable=false, columnDefinition="Double default 0")
	double cActionPower;
	
	@Column(nullable=false, columnDefinition="Double default 0")
	double afactor;//":0.953,     #功率因数
	@Column(nullable=false, columnDefinition="Double default 0")
	double bfactor;//":0.953,     #功率因数
	@Column(nullable=false, columnDefinition="Double default 0")
	double cfactor;//":0.953,     #功率因数
	 
	
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
    int switchStat;//":1,     #开关状态： 0-合闸， 其他--拉闸状态
		
    String dateTime;//": "2019-04-11 14:15:00" #数据时间
    
	@Column(nullable=false, columnDefinition="Double default 0")
    double threshValue; //
	
		
	
	
	public void syncDevice(MeterStatus meter) {
		allEnergy = meter.allEnergy;  //组合有功总电量		
		allJianEnvery	=meter.allJianEnvery;
		allFengEnvery	=meter.allFengEnvery;
		allPingEnvery   =meter.allPingEnvery ;
		allGuEnvery     =meter.allGuEnvery  ; 
		avol=   meter.avol;
		bvol=   meter.bvol;
		cvol=   meter.cvol;
		    
		acur=   meter.acur;
		bcur=   meter.bcur;
		ccur=   meter.ccur;
		
		aActionPower=meter.aActionPower;
		bActionPower=meter.bActionPower;
		cActionPower=meter.cActionPower;
		
		afactor=meter.afactor;
		bfactor=meter.bfactor;
		cfactor=meter.cfactor;
		
		
		groupactionEnergy  =meter.groupactionEnergy	;
		actionEnergy       =meter.actionEnergy    ;        
		reactionEnergy     =meter.reactionEnergy  ;      
		vol                =meter.vol            ;  
		cur                =meter.cur              ;
		actionPower        =meter.actionPower   ;   
		freq               =meter.freq        ;     
		factor             =meter.factor     ;      
		switchStat         =meter.switchStat    ;   
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

	public double getAllEnergy() {
		return allEnergy;
	}

	public void setAllEnergy(double allEnergy) {
		this.allEnergy = allEnergy;
	}

	public double getAllJianEnvery() {
		return allJianEnvery;
	}

	public void setAllJianEnvery(double allJianEnvery) {
		this.allJianEnvery = allJianEnvery;
	}

	public double getAllFengEnvery() {
		return allFengEnvery;
	}

	public void setAllFengEnvery(double allFengEnvery) {
		this.allFengEnvery = allFengEnvery;
	}

	public double getAllPingEnvery() {
		return allPingEnvery;
	}

	public void setAllPingEnvery(double allPingEnvery) {
		this.allPingEnvery = allPingEnvery;
	}

	public double getAllGuEnvery() {
		return allGuEnvery;
	}

	public void setAllGuEnvery(double allGuEnvery) {
		this.allGuEnvery = allGuEnvery;
	}

	public double getAvol() {
		return avol;
	}

	public void setAvol(double avol) {
		this.avol = avol;
	}

	public double getBvol() {
		return bvol;
	}

	public void setBvol(double bvol) {
		this.bvol = bvol;
	}

	public double getCvol() {
		return cvol;
	}

	public void setCvol(double cvol) {
		this.cvol = cvol;
	}

	public double getAcur() {
		return acur;
	}

	public void setAcur(double acur) {
		this.acur = acur;
	}

	public double getBcur() {
		return bcur;
	}

	public void setBcur(double bcur) {
		this.bcur = bcur;
	}

	public double getCcur() {
		return ccur;
	}

	public void setCcur(double ccur) {
		this.ccur = ccur;
	}

	public double getaActionPower() {
		return aActionPower;
	}

	public void setaActionPower(double aActionPower) {
		this.aActionPower = aActionPower;
	}

	public double getbActionPower() {
		return bActionPower;
	}

	public void setbActionPower(double bActionPower) {
		this.bActionPower = bActionPower;
	}

	public double getcActionPower() {
		return cActionPower;
	}

	public void setcActionPower(double cActionPower) {
		this.cActionPower = cActionPower;
	}

	public double getAfactor() {
		return afactor;
	}

	public void setAfactor(double afactor) {
		this.afactor = afactor;
	}

	public double getBfactor() {
		return bfactor;
	}

	public void setBfactor(double bfactor) {
		this.bfactor = bfactor;
	}

	public double getCfactor() {
		return cfactor;
	}

	public void setCfactor(double cfactor) {
		this.cfactor = cfactor;
	}

	public Long getLastHeartBeatTime() {
		return lastHeartBeatTime;
	}

	public void setLastHeartBeatTime(Long lastHeartBeatTime) {
		this.lastHeartBeatTime = lastHeartBeatTime;
	}

	public String getCmdId() {
		return cmdId;
	}

	public void setCmdId(String cmdId) {
		this.cmdId = cmdId;
	}

	public Long getCmdTime() {
		return cmdTime;
	}

	public void setCmdTime(Long cmdTime) {
		this.cmdTime = cmdTime;
	}	
	
	
}
