package com.shicha.yzmgt.domain;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;

public class SearchAlarmData {

	String deviceName;	
	String deviceNo;
	
	String name;
	String cardNo;
	
	
	Long alarmTime;
	Integer alarmType;
	
	Long betrween1;
	Long betrween2;
	
	Integer page = 0;
	Integer size = 10;
	String sort="alarmTime";
	String order = "desc";//"asc"
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public Long getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(Long alarmTime) {
		this.alarmTime = alarmTime;
	}
	public Integer getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(Integer alarmType) {
		this.alarmType = alarmType;
	}
	public Long getBetrween1() {
		return betrween1;
	}
	public void setBetrween1(Long betrween1) {
		this.betrween1 = betrween1;
	}
	public Long getBetrween2() {
		return betrween2;
	}
	public void setBetrween2(Long betrween2) {
		this.betrween2 = betrween2;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}	
}
