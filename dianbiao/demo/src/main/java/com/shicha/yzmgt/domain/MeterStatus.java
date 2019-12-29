package com.shicha.yzmgt.domain;

import javax.persistence.Column;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class MeterStatus {
	
	String deviceNo;
	
	public double allEnergy;  //组合有功总电量		
	public double allJianEnvery;  //组合有功尖费率电量		
	public double allFengEnvery;  //组合有功峰费率电量		
	public double allPingEnvery;  //组合有功平费率电量
	public double allGuEnvery; //组合有功谷费率电量/
	public double avol;
	public double bvol;
	public double cvol;
	
	public double acur;
	public double bcur;
	public double ccur;
	
	public double aActionPower;
	public double bActionPower;
	public double cActionPower;
	
	public double afactor;//":0.953,     #功率因数
	public double bfactor;//":0.953,     #功率因数
	public double cfactor;//":0.953,     #功率因数
	
	
	public double groupactionEnergy;  //组合有功电能量		
	public double actionEnergy;       //":65.21,       #正向有功电能量		
	public double reactionEnergy;     //:219.3,     #反向有功电能量	
	public double vol;               //":220.5,    #电压	
	public double cur; //":1.503,    #电流
	public double actionPower; //":0.3254,   #有功功率	
	public double freq;//":49.92,   #电网频率		
	public double factor;//":0.953,     #功率因数	
	public int switchStat;//":1,     #开关状态： 0-合闸

    public MeterStatus() {}

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

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
}
