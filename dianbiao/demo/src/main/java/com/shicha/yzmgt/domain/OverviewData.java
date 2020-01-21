package com.shicha.yzmgt.domain;

import java.util.List;

import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.DeviceStat;

public class OverviewData {

	int onlineCount;
	int offlineCount;
	
	int onCount;
	int offCount;
	
	List<Device>top10;
	
	List<DeviceStat> months;
	List<DeviceStat> days;

	public int getOnlineCount() {
		return onlineCount;
	}

	public void setOnlineCount(int onlineCount) {
		this.onlineCount = onlineCount;
	}

	public int getOfflineCount() {
		return offlineCount;
	}

	public void setOfflineCount(int offlineCount) {
		this.offlineCount = offlineCount;
	}

	public int getOnCount() {
		return onCount;
	}

	public void setOnCount(int onCount) {
		this.onCount = onCount;
	}

	public int getOffCount() {
		return offCount;
	}

	public void setOffCount(int offCount) {
		this.offCount = offCount;
	}

	public List<Device> getTop10() {
		return top10;
	}

	public void setTop10(List<Device> top10) {
		this.top10 = top10;
	}

	public List<DeviceStat> getMonths() {
		return months;
	}

	public void setMonths(List<DeviceStat> months) {
		this.months = months;
	}

	public List<DeviceStat> getDays() {
		return days;
	}

	public void setDays(List<DeviceStat> days) {
		this.days = days;
	}
}


