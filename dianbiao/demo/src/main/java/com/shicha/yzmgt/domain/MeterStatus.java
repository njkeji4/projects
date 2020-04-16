package com.shicha.yzmgt.domain;

import javax.persistence.Column;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class MeterStatus {
	
	
	
	public double allEnergy;  //组合有功总电量	
	public double aEnergy;
	public double bEnergy;
	public double cEnergy;	
	public double dEnergy;
	
	public double avol;
	public double bvol;
	public double cvol;
	public double dvol;
	
	public double acur;
	public double bcur;
	public double ccur;
	public double dcur;	
	
	public int switchStat;//":1, 
	
	String deviceNo;

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
}
