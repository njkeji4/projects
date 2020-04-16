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
import com.shicha.yzmgt.bean.DeviceStat;
import com.shicha.yzmgt.bean.UserCmd;
import com.shicha.yzmgt.dao.IDeviceDao;
import com.shicha.yzmgt.dao.IDeviceStatDao;
import com.shicha.yzmgt.dao.IUserCmdDao;
import com.shicha.yzmgt.util.Util;

@Component
public class DeviceScanTask {
	
	private static final Logger log = LoggerFactory.getLogger(DeviceScanTask.class);
	
	@Autowired
	IDeviceDao deviceDao;
	
	@Autowired
	IDeviceStatDao deviceStatDao;
	
	
	@Value("${device.heartbeat:10}")
	int heartBeat;
	
	//check device online/offline
	@Scheduled(fixedRate = 1000 * 60 * 10)
	public void scanDevice() {		
		try {
			//List<Device> list = deviceDao.findAll();
			List<Device> list = deviceDao.findByStatus(Device.device_status_online);
			if(list == null || list.size() == 0) {
				return;
			}
			
			for(Device d : list) {			
				if(System.currentTimeMillis()- d.getLastHeartBeatTime() > heartBeat * 60 * 1000) {
					d.setStatus(Device.device_status_offline);
					d.setaState(Device.device_switch_open);
					d.setbState(Device.device_switch_open);
					d.setcState(Device.device_switch_open);
					d.setdState(Device.device_switch_open);
					deviceDao.save(d);
				}	
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	//统计任务
	@Scheduled(cron = "0 0 0 * * ?")
	public void doStatis() {		
		try {
			
			long yesterday = Util.getPreviousDay(System.currentTimeMillis());	
			
			log.info("midnight task start to calc :" + Util.formatDate(yesterday));
			
			List<Device> list = deviceDao.findAll();
			if(list == null || list.size() == 0) {
				return;
			}
			for(Device d : list) {				
				
				long todayOnTime = d.getTodayOnTime() + (d.getaState() == 0? (System.currentTimeMillis() - d.getOnTime())/60000 : 0);
				todayOnTime =  todayOnTime / 60;
				DeviceStat stat = new DeviceStat(d.getDeviceNo(), yesterday, 
						d.getTodayEnergy(),todayOnTime, d.getDeviceName(), d.getGroupName());					
				
				d.setTotalOnTime(d.getTotalOnTime() + todayOnTime);
				d.setLastEnergy(d.getAllEnergy());
				d.setTodayOnTime(0);
				d.setOnTime(System.currentTimeMillis());				
				
				deviceStatDao.save(stat);
				deviceDao.save(d);
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		log.info("midnight task finished");
	}	
	
}
