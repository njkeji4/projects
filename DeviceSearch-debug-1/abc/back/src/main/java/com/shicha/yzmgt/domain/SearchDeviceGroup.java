package com.shicha.yzmgt.domain;



public class SearchDeviceGroup {

	String groupName;	
	Long createTime;
	
	String sysIccid;
	
	Integer page = 0;
	Integer size = 10;
	String sort="groupName";
	String order = "asc";//"dsc"
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
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
}
