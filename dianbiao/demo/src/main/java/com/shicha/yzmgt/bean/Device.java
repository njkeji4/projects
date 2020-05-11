package com.shicha.yzmgt.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.shicha.yzmgt.domain.MeterStatus;

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
	
	@Column(nullable=true, columnDefinition="BIGINT default 0")
	Long lastHeartBeatTime = 0l;
	
	@Column(nullable=true, columnDefinition="BIGINT default 0")
	Long lastDataTime = 0l;
	
	String userName;
	String groupName;	
	
	String cmdId;		//一个设备一次只能执行一条命令，必须等待返回或者超时
	Long cmdTime;		//发送命令时间	
	
	double lastEnergy = 0;
	double todayEnergy = 0;
	long onTime = 0;
	long todayOnTime = 0;	
	long totalOnTime = 0;
	
	double blastEnergy = 0;
	double btodayEnergy = 0;
	long bonTime = 0;
	long btodayOnTime = 0;	
	long btotalOnTime = 0;
	
	double clastEnergy = 0;
	double ctodayEnergy = 0;
	long conTime = 0;
	long ctodayOnTime = 0;	
	long ctotalOnTime = 0;
	
	double dlastEnergy = 0;
	double dtodayEnergy = 0;
	long donTime = 0;
	long dtodayOnTime = 0;	
	long dtotalOnTime = 0;
	
	
	String roomId;
	String roomName;
	
	//////data field
	@Column(nullable=false, columnDefinition="Double default 0")
	double allEnergy;  //组合有功总电量		
	
	@Column(nullable=false, columnDefinition="Double default 0")
	double aEnergy;  //组合有功总电量		
	@Column(nullable=false, columnDefinition="Double default 0")
	double bEnergy;  //组合有功总电量		
	@Column(nullable=false, columnDefinition="Double default 0")
	double cEnergy;  //组合有功总电量		
	@Column(nullable=false, columnDefinition="Double default 0")
	double dEnergy;  //组合有功总电量		
	
	
	@Column(nullable=false, columnDefinition="Double default 0")
	double avol;
	@Column(nullable=false, columnDefinition="Double default 0")
	double bvol;
	@Column(nullable=false, columnDefinition="Double default 0")
	double cvol;
	@Column(nullable=false, columnDefinition="Double default 0")
	double dvol;
	
	
	
	@Column(nullable=false, columnDefinition="Double default 0")
	double acur;
	@Column(nullable=false, columnDefinition="Double default 0")
	double bcur;
	@Column(nullable=false, columnDefinition="Double default 0")
	double ccur;
	@Column(nullable=false, columnDefinition="Double default 0")
	double dcur;
	
	
	@Column(nullable=false, columnDefinition="INT default 1")
    int aState = 1;//":1,     #开关状态： 0-合闸， 其他--拉闸状态  
	@Column(nullable=false, columnDefinition="INT default 1")
    int bState = 1;//":1,     #开关状态： 0-合闸， 其他--拉闸状态  
	@Column(nullable=false, columnDefinition="INT default 1")
    int cState = 1;//":1,     #开关状态： 0-合闸， 其他--拉闸状态  
	@Column(nullable=false, columnDefinition="INT default 1")
    int dState = 1;//":1,     #开关状态： 0-合闸， 其他--拉闸状态  
    
	
	public void syncDevice(MeterStatus meter) {
		allEnergy = meter.allEnergy;  //组合有功总电量		
	
		aEnergy = meter.aEnergy;
		bEnergy = meter.bEnergy;
		cEnergy = meter.cEnergy;
		dEnergy = meter.dEnergy;
		
		avol=   meter.avol;
		bvol=   meter.bvol;
		cvol=   meter.cvol;
		dvol=   meter.dvol;
		
		    
		acur=   meter.acur;
		bcur=   meter.bcur;
		ccur=   meter.ccur;
		dcur=   meter.dcur;		
		
		aState = (meter.switchStat & 0x02) >> 1;
		bState = (meter.switchStat & 0x08) >> 3;
		cState = (meter.switchStat & 0x20 ) >> 5;
		dState = (meter.switchStat & 0x80) >> 7;
		  
	}


	public static int getDevice_status_online() {
		return device_status_online;
	}


	public static void setDevice_status_online(int device_status_online) {
		Device.device_status_online = device_status_online;
	}


	public static int getDevice_status_offline() {
		return device_status_offline;
	}


	public static void setDevice_status_offline(int device_status_offline) {
		Device.device_status_offline = device_status_offline;
	}


	public static int getDevice_switch_close() {
		return device_switch_close;
	}


	public static void setDevice_switch_close(int device_switch_close) {
		Device.device_switch_close = device_switch_close;
	}


	public static int getDevice_switch_open() {
		return device_switch_open;
	}


	public static void setDevice_switch_open(int device_switch_open) {
		Device.device_switch_open = device_switch_open;
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


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public Long getLastHeartBeatTime() {
		return lastHeartBeatTime;
	}


	public void setLastHeartBeatTime(Long lastHeartBeatTime) {
		this.lastHeartBeatTime = lastHeartBeatTime;
	}


	public Long getLastDataTime() {
		return lastDataTime;
	}


	public void setLastDataTime(Long lastDataTime) {
		this.lastDataTime = lastDataTime;
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


	public double getLastEnergy() {
		return lastEnergy;
	}


	public void setLastEnergy(double lastEnergy) {
		this.lastEnergy = lastEnergy;
	}


	public double getTodayEnergy() {
		return todayEnergy;
	}


	public void setTodayEnergy(double todayEnergy) {
		this.todayEnergy = todayEnergy;
	}


	public long getOnTime() {
		return onTime;
	}


	public void setOnTime(long onTime) {
		this.onTime = onTime;
	}


	public long getTodayOnTime() {
		return todayOnTime;
	}


	public void setTodayOnTime(long todayOnTime) {
		this.todayOnTime = todayOnTime;
	}


	public long getTotalOnTime() {
		return totalOnTime;
	}


	public void setTotalOnTime(long totalOnTime) {
		this.totalOnTime = totalOnTime;
	}


	public double getAllEnergy() {
		return allEnergy;
	}


	public void setAllEnergy(double allEnergy) {
		this.allEnergy = allEnergy;
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

	public double getBlastEnergy() {
		return blastEnergy;
	}


	public void setBlastEnergy(double blastEnergy) {
		this.blastEnergy = blastEnergy;
	}


	public double getBtodayEnergy() {
		return btodayEnergy;
	}


	public void setBtodayEnergy(double btodayEnergy) {
		this.btodayEnergy = btodayEnergy;
	}


	public long getBonTime() {
		return bonTime;
	}


	public void setBonTime(long bonTime) {
		this.bonTime = bonTime;
	}


	public long getBtodayOnTime() {
		return btodayOnTime;
	}


	public void setBtodayOnTime(long btodayOnTime) {
		this.btodayOnTime = btodayOnTime;
	}


	public long getBtotalOnTime() {
		return btotalOnTime;
	}


	public void setBtotalOnTime(long btotalOnTime) {
		this.btotalOnTime = btotalOnTime;
	}


	public double getClastEnergy() {
		return clastEnergy;
	}


	public void setClastEnergy(double clastEnergy) {
		this.clastEnergy = clastEnergy;
	}


	public double getCtodayEnergy() {
		return ctodayEnergy;
	}


	public void setCtodayEnergy(double ctodayEnergy) {
		this.ctodayEnergy = ctodayEnergy;
	}


	public long getConTime() {
		return conTime;
	}


	public void setConTime(long conTime) {
		this.conTime = conTime;
	}


	public long getCtodayOnTime() {
		return ctodayOnTime;
	}


	public void setCtodayOnTime(long ctodayOnTime) {
		this.ctodayOnTime = ctodayOnTime;
	}


	public long getCtotalOnTime() {
		return ctotalOnTime;
	}


	public void setCtotalOnTime(long ctotalOnTime) {
		this.ctotalOnTime = ctotalOnTime;
	}


	public double getDlastEnergy() {
		return dlastEnergy;
	}


	public void setDlastEnergy(double dlastEnergy) {
		this.dlastEnergy = dlastEnergy;
	}


	public double getDtodayEnergy() {
		return dtodayEnergy;
	}


	public void setDtodayEnergy(double dtodayEnergy) {
		this.dtodayEnergy = dtodayEnergy;
	}


	public long getDonTime() {
		return donTime;
	}


	public void setDonTime(long donTime) {
		this.donTime = donTime;
	}


	public long getDtodayOnTime() {
		return dtodayOnTime;
	}


	public void setDtodayOnTime(long dtodayOnTime) {
		this.dtodayOnTime = dtodayOnTime;
	}


	public long getDtotalOnTime() {
		return dtotalOnTime;
	}


	public void setDtotalOnTime(long dtotalOnTime) {
		this.dtotalOnTime = dtotalOnTime;
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


	public double getDvol() {
		return dvol;
	}


	public void setDvol(double dvol) {
		this.dvol = dvol;
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


	public double getDcur() {
		return dcur;
	}


	public void setDcur(double dcur) {
		this.dcur = dcur;
	}


	public int getaState() {
		return aState;
	}


	public void setaState(int aState) {
		this.aState = aState;
	}


	public int getbState() {
		return bState;
	}


	public void setbState(int bState) {
		this.bState = bState;
	}


	public int getcState() {
		return cState;
	}


	public void setcState(int cState) {
		this.cState = cState;
	}


	public int getdState() {
		return dState;
	}


	public void setdState(int dState) {
		this.dState = dState;
	}
	
	public void setSwitchStat(int branch, int stat) {
		if(branch == 0 || branch == 1) {
			this.aState = stat;
		}
		if(branch == 0 || branch == 2) {
			this.bState = stat;
		}
		if(branch == 0 || branch == 3) {
			this.cState = stat;
		}
		if(branch == 0 || branch == 4) {
			this.dState = stat;
		}
	}


	public String getRoomId() {
		return roomId;
	}


	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}


	public String getRoomName() {
		return roomName;
	}


	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
}
