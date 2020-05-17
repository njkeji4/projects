package com.shicha.yzmgt.bean.jifang;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.DeviceGroup;

@Entity(name="room")
public class Room {

	@Id
	@Column(name="id", nullable=false, length=36)
	@GenericGenerator(name="system-uuid", strategy="uuid2")
	@GeneratedValue(generator="system-uuid")
	String id;
	
	String address;
	String name;
	
	String latlong;
	
	String groupId;
	String groupName;
	
	long createTime;
	
	
	@Transient
	Integer page = 0;
	@Transient
	Integer size = 10;
	@Transient
	String sort="name";
	@Transient
	String order = "asc";//"dsc"
	
	@Transient
	Integer deviceCount = 0;
	@Transient
	Integer offDeviceCount = 0;
	@Transient
	Integer onDeviceCount = 0;
	
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "roomId")
	List<Device> devices;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
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
	public List<Device> getDevices() {
		return devices;
	}
	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}
	public Integer getDeviceCount() {
		return deviceCount;
	}
	public void setDeviceCount(Integer deviceCount) {
		this.deviceCount = deviceCount;
	}
	public Integer getOffDeviceCount() {
		return offDeviceCount;
	}
	public void setOffDeviceCount(Integer offDeviceCount) {
		this.offDeviceCount = offDeviceCount;
	}
	public Integer getOnDeviceCount() {
		return onDeviceCount;
	}
	public void setOnDeviceCount(Integer onDeviceCount) {
		this.onDeviceCount = onDeviceCount;
	}
	public String getLatlong() {
		return latlong;
	}
	public void setLatlong(String latlong) {
		this.latlong = latlong;
	}
	
	
	
}
