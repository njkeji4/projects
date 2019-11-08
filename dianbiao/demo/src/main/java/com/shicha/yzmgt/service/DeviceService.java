package com.shicha.yzmgt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shicha.yzmgt.aircb.AirResult;
import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.DeviceSetting;
import com.shicha.yzmgt.dao.IDevcieSettingDao;
import com.shicha.yzmgt.dao.IDeviceDao;

@Service
public class DeviceService {

	private static final Logger log = LoggerFactory.getLogger(DeviceService.class);
	
	@Autowired
	IDeviceDao deviceDao;
	
	@Autowired
	AirCbService airService;
	
	@Autowired
	IDevcieSettingDao deviceSettingDao;
	
	public List<Device> getAll(){
		
		return deviceDao.findAll();
	}
	
	public boolean deviceNameExisted(Device d) {
		
		return (deviceDao.findByDeviceName(d.getDeviceName()) != null);
	}
	
	public void addDevice(Device d) {
		
		if(deviceDao.findByDeviceName(d.getDeviceName()) == null)
			deviceDao.save(d);
	}
	
	public void delDevice(Device d) {
		deviceDao.deleteById(d.getId());
	}
	
	
	//device setting
	public List<DeviceSetting> getByDeviceNo(String deviceNo){
		
		return deviceSettingDao.findByDeviceNo(deviceNo);
	}
	
	public AirResult addDeviceSetting(DeviceSetting[] settings) {
		deviceSettingDao.deleteAll();
		long[] value = new long[settings.length * 2];
		int idx = 0;
		for(DeviceSetting setting : settings) {
			deviceSettingDao.save(setting);
			value[idx++] = setting.getOffTime();
			value[idx++] = setting.getOnTime();
		}
		
		AirResult result = airService.setPullUpDownPeriod(settings[0].getDeviceNo(), value);
		
		return result;
	}
	
	public void removeDeviceSetting(DeviceSetting setting) {
		
		deviceSettingDao.deleteById(setting.getId());
	}
}
