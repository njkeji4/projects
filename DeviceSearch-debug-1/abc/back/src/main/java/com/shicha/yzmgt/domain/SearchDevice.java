package com.shicha.yzmgt.domain;



public class SearchDevice {

	String name;	
	String deviceNo;
	Integer deviceType;
	
	Integer status;
	
	String versionNo;
	
	String sysVer;
	String model;
	String system;
	String firm;
	String ipAddr;
	String sysIccid;
	
	Integer page = 0;
	Integer size = 10;
	String sort="deviceNo";
	String order = "asc";//"dsc"
	
	String groupId;
	
	
	Long totalSuccCount ;
	Long totalFailCount ;
	
	
	Long todaySuccCount ;
	Long todayFailCount ;
	
	
	public Long getTotalSuccCount() {
		return totalSuccCount;
	}
	public void setTotalSuccCount(Long totalSuccCount) {
		this.totalSuccCount = totalSuccCount;
	}
	public Long getTotalFailCount() {
		return totalFailCount;
	}
	public void setTotalFailCount(Long totalFailCount) {
		this.totalFailCount = totalFailCount;
	}
	public Long getTodaySuccCount() {
		return todaySuccCount;
	}
	public void setTodaySuccCount(Long todaySuccCount) {
		this.todaySuccCount = todaySuccCount;
	}
	public Long getTodayFailCount() {
		return todayFailCount;
	}
	public void setTodayFailCount(Long todayFailCount) {
		this.todayFailCount = todayFailCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	public Integer getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	public String getSysVer() {
		return sysVer;
	}
	public void setSysVer(String sysVer) {
		this.sysVer = sysVer;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getFirm() {
		return firm;
	}
	public void setFirm(String firm) {
		this.firm = firm;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public String getSysIccid() {
		return sysIccid;
	}
	public void setSysIccid(String sysIccid) {
		this.sysIccid = sysIccid;
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
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}
