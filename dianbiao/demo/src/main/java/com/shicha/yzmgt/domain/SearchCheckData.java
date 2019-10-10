package com.shicha.yzmgt.domain;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;

public class SearchCheckData {

	String deviceName;	
	String deviceNo;
	
	String name;
	String cardNo;
	
	
	Integer result;			//1 ok,2 fail, 3 undetected face 4 undetect fingerprint
	String folk;
	Integer sex;
	String birthday;
	String address;	
	String authority;
	Long compareDate;
	
	Long betrween1;
	Long betrween2;
	
	Integer page = 0;
	Integer size = 10;
	String sort="compareDate";
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
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public String getFolk() {
		return folk;
	}
	public void setFolk(String folk) {
		this.folk = folk;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public Long getCompareDate() {
		return compareDate;
	}
	public void setCompareDate(Long compareDate) {
		this.compareDate = compareDate;
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
