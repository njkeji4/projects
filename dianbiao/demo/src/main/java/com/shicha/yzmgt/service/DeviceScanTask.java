package com.shicha.yzmgt.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.UserCmd;
import com.shicha.yzmgt.dao.IDeviceDao;
import com.shicha.yzmgt.dao.IUserCmdDao;
import com.shicha.yzmgt.util.Util;

@Component
public class DeviceScanTask {
	
	private static final Logger log = LoggerFactory.getLogger(DeviceScanTask.class);
	
	@Autowired
	IDeviceDao deviceDao;
	
	@Autowired
	IUserCmdDao userCmdDao;
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	AirCbService airService;
	
	@Autowired
	AirCbService2 airService2;
	
	@Value("${device.heartbeat:10}")
	int heartBeat;
	
	//check device online/offline
	@Scheduled(fixedRate = 1000 * 60 * 10)
	public void scanDevice() {		
		try {
			List<Device> list = deviceDao.findAll();
			if(list == null || list.size() == 0) {
				return;
			}
			
			for(Device d : list) {			
				if(System.currentTimeMillis()- d.getLastHeartBeatTime() > heartBeat * 60 * 1000) {
					d.setStatus(Device.device_status_offline);
					deviceDao.save(d);
					continue;
				}
				
				airService2.getDeviceStatus(d.getDeviceNo());				
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	//clear command,timeout
	@Scheduled(fixedRate = 1000 * 60)
	public void scanDevice2() {		
		try {
			List<Device> list = deviceDao.findByCmdIdIsNotNull();
			if(list == null || list.size() == 0) {
				return;
			}
			
			for(Device d : list) {			
				if(System.currentTimeMillis()- d.getCmdTime() > 60 * 1000) {
					
					Optional<UserCmd> opt = userCmdDao.findById(d.getCmdId());					
					if(opt.isPresent() && opt.get().getStatus() == UserCmd.cmd_status_processing) {
						UserCmd userCmd = opt.get();
						userCmd.setStatus(UserCmd.cmd_status_fail);
						userCmd.setRetMessage("超时没有返回");
						userCmd.setRetTime(System.currentTimeMillis());
						
						userCmdDao.save(userCmd);
					}
					
					d.setCmdId(null);
					d.setCmdTime(null);
					deviceDao.save(d);
					continue;
				}	
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}	
}
