package com.shicha.yzmgt.domain;

import com.shicha.yzmgt.bean.DeviceSetting;

public class DeviceSettingDomain {

	String id;
	int branch;
	DeviceSetting[] settings;
	
	
	public DeviceSetting[] getSettings() {
		return settings;
	}
	public void setSettings(DeviceSetting[] settings) {
		this.settings = settings;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getBranch() {
		return branch;
	}

	public void setBranch(int branch) {
		this.branch = branch;
	}
	
	
	
	
}
