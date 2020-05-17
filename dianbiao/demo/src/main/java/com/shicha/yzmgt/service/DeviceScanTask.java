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
				
				long aon = d.getAtodayOnTime() + (d.getaState() == 0? (System.currentTimeMillis() - d.getAonTime())/60000 : 0);
				aon =  aon / 60;
				
				long bon = d.getBtodayOnTime() + (d.getbState() == 0? (System.currentTimeMillis() - d.getBonTime())/60000 : 0);
				bon =  bon / 60;
				
				long con = d.getCtodayOnTime() + (d.getcState() == 0? (System.currentTimeMillis() - d.getConTime())/60000 : 0);
				con =  con / 60;
				
				long don = d.getDtodayOnTime() + (d.getdState() == 0? (System.currentTimeMillis() - d.getDonTime())/60000 : 0);
				don =  don / 60;
				
				DeviceStat stat = new DeviceStat(
						d.getDeviceNo(), d.getDeviceName(),
						d.getGroupName(),d.getRoomName(), d.getRoomId(),
						yesterday, 
						d.getAllEnergy() - d.getLastEnergy(), 
						d.getaEnergy() - d.getAlastEnergy(),
						d.getbEnergy()- d.getBlastEnergy(),
						d.getcEnergy()- d.getClastEnergy(),
						d.getdEnergy() - d.getDlastEnergy(),
						aon, bon, con, don);	
				
				d.setLastEnergy(d.getAllEnergy());
				d.setAlastEnergy(d.getaEnergy());
				d.setBlastEnergy(d.getbEnergy());
				d.setClastEnergy(d.getcEnergy());
				d.setDlastEnergy(d.getdEnergy());				
				
				d.setAtodayOnTime(0);d.setBtodayOnTime(0);
				d.setCtodayOnTime(0);d.setDtodayOnTime(0);
				d.setAonTime(System.currentTimeMillis());
				d.setBonTime(System.currentTimeMillis());		
				d.setConTime(System.currentTimeMillis());		
				d.setDonTime(System.currentTimeMillis());		
				
				deviceStatDao.save(stat);
				deviceDao.save(d);
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		log.info("midnight task finished");
	}	
	
}
