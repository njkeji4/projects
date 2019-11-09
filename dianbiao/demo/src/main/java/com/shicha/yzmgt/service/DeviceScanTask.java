package com.shicha.yzmgt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shicha.yzmgt.bean.Device;

import com.shicha.yzmgt.dao.IDeviceDao;

import com.shicha.yzmgt.util.Util;

@Component
public class DeviceScanTask {
	
	private static final Logger log = LoggerFactory.getLogger(DeviceScanTask.class);
	
	@Autowired
	IDeviceDao deviceDao;
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	AirCbService airService;
	
	@Scheduled(fixedRate = 1000 * 60 * 1)
	public void scanDevice() {		
		try {
			List<Device> list = deviceDao.findAll();
			if(list == null || list.size() == 0) {
				return;
			}
			
			for(Device d : list) {
				
				airService.getDeviceStatus(d.getDeviceNo());
				
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
}
