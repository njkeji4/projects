package com.shicha.yzmgt.domain;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.shicha.yzmgt.util.Util;

public class DeviceRespond {

	/**
	 * the respond field name is asked by device;
	 */
	int respCode;
	String respDesc;
	String data;
	
	public DeviceRespond() {}	
	
	public DeviceRespond(int code, String msg) {
		this.respCode = code;
		this.respDesc = msg;
		HashMap<String, String> data = new HashMap<String, String>();		
		data.put("sysTime", Util.formatDate());
		this.data = JSON.toJSONString(data);
	}
	
	public DeviceRespond(int code, String msg, Object data) {
		this.respCode = code;
		this.respDesc = msg;
		this.data = JSON.toJSONString(data);
	}
	
	public int getRespCode() {
		return respCode;
	}
	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}
	public String getRespDesc() {
		return respDesc;
	}
	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
