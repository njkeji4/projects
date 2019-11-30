package com.shicha.yzmgt.domain;

import com.shicha.yzmgt.bean.DeviceSetting;

public class DeviceSettingDomain {

	String[] ids;
	DeviceSetting[] settings;
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	public DeviceSetting[] getSettings() {
		return settings;
	}
	public void setSettings(DeviceSetting[] settings) {
		this.settings = settings;
	}
	
	
}
