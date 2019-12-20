package com.shicha.yzmgt.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity(name="user_cmd")
@Table(indexes={
		@Index(name="cmduser_Index",columnList="userName"),
		@Index(name="cmddevice_Index",columnList="deviceNo"),
		@Index(name="cmddn_Index",columnList="deviceName"),
		@Index(name="cmddate_Index",columnList="cmdTime"),
	})
public class UserCmd {

	public static int cmd_status_processing = 2;
	public static int cmd_status_ok = 0;
	public static int cmd_status_fail = 1;
	
	public static String[] cmd_name = {"获取电表信息","拉闸", "合闸", "获取拉合闸时间","读取阈值","读取上报周期","设置阈值","设置上报周期","设置拉合闸时间"};
	
	
	@Id
	@Column(name="id", nullable=false, length=36)
	@GenericGenerator(name="system-uuid", strategy="uuid2")
	@GeneratedValue(generator="system-uuid")
	String id;
	
	
	String cmdName;
	int cmdCode;
	String cmdContent;
	String deviceNo;
	String deviceName;
	Long cmdTime;
	Long retTime;
	
	String userName;
	String groupName;
	
	String retMessage;
	String retValue;
	
	int status;
	
	public UserCmd() {}
	
	public UserCmd(int cmdCode, String cmdContent, String deviceNo, String deviceName, String userName, String groupName, int status) {
		this.cmdCode = cmdCode;
		this.cmdContent = cmdContent;
		this.deviceNo =  deviceNo;
		this.deviceName = deviceName;
		this.userName = userName;
		this.groupName= groupName;
		
		this.cmdName = cmd_name[cmdCode];
		this.cmdTime = System.currentTimeMillis();
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCmdName() {
		return cmdName;
	}

	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}

	public String getCmdContent() {
		return cmdContent;
	}

	public void setCmdContent(String cmdContent) {
		this.cmdContent = cmdContent;
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

	public Long getCmdTime() {
		return cmdTime;
	}

	public void setCmdTime(Long cmdTime) {
		this.cmdTime = cmdTime;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCmdCode() {
		return cmdCode;
	}

	public void setCmdCode(int cmdCode) {
		this.cmdCode = cmdCode;
	}

	public String getRetMessage() {
		return retMessage;
	}

	public void setRetMessage(String retMessage) {
		this.retMessage = retMessage;
	}

	public String getRetValue() {
		return retValue;
	}

	public void setRetValue(String retValue) {
		this.retValue = retValue;
	}

	public Long getRetTime() {
		return retTime;
	}

	public void setRetTime(Long retTime) {
		this.retTime = retTime;
	}
	
}
