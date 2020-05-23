package com.shicha.yzmgt.service;


import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.UserCmd;
import com.shicha.yzmgt.controller.IDaemon;
import com.shicha.yzmgt.dao.IDeviceDao;
import com.shicha.yzmgt.dao.IUserCmdDao;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.AutoOnOff;
import com.shicha.yzmgt.domain.CmdRes;
import com.shicha.yzmgt.domain.MeterStatus;

@Service
public class AirCbService2 {

	private static final Logger log = LoggerFactory.getLogger(AirCbService2.class);
	
	@Autowired
	IDaemon client;
	
	@Autowired
	IDeviceDao deviceDao;
	
	@Autowired
	UserCmdService cmdService;
	
	@Autowired
	IUserCmdDao userCmdDao;
	
	@Autowired
	private SimpMessagingTemplate webSocket;		
	
	
	public boolean checkReturn(String cmdId, APIResult ret, String addr, String userName) {
		
		Optional<UserCmd>option = userCmdDao.findById(cmdId);
		if(!option.isPresent()) {
			return false;
		}
		UserCmd userCmd = option.get();		
		
		userCmd.setStatus(ret.getStatus() == 0 ? UserCmd.cmd_status_ok : UserCmd.cmd_status_fail);
		userCmd.setRetMessage(ret.getMessage());
		userCmd.setRetTime(System.currentTimeMillis());
		
		userCmdDao.save(userCmd);
				
		
		String message = addr;		
		String target = "/topic/greetings/" + userName;
		message +=  UserCmd.cmd_name[userCmd.getCmdCode()] ;	
		message +=  (ret.getStatus() == 0 ? "操作成功" : "操作失败");	
		webSocket.convertAndSend(target, message);
		
		return ret.getStatus() == 0;
	}	
	
	@Async
	public void switchOff(String addr,int branch, String userName, String groupName) {
		Device device = deviceDao.findByDeviceNo(addr);
		int cmd = 1;
		
		UserCmd userCmd = new UserCmd(cmd,branch+"",addr,device.getDeviceName(), userName, groupName, UserCmd.cmd_status_processing);
		String cmdId = cmdService.newCmd(userCmd);		

		
		APIResult ret = new APIResult(1);
		try{
			ret = client.switchoff(new AutoOnOff(addr,branch));
			
		}catch(Exception ex) {
			
			ret.setMessage(ex.getMessage());
		}
		
		if(ret.getStatus() == 0) {
			device.setSwitchStat(branch, Device.device_switch_open);
			deviceDao.save(device);
		}
		
		log.info("check return:"+ret);
		
		checkReturn(cmdId, ret, addr, userName);		
	}
	
	@Async
	public void switchOn(String addr, int branch, String userName, String groupName) {
		Device device = deviceDao.findByDeviceNo(addr);
		int cmd = 2;
		
		UserCmd userCmd = new UserCmd(cmd,branch+"",addr,device.getDeviceName(), userName, groupName, UserCmd.cmd_status_processing);
		String cmdId = cmdService.newCmd(userCmd);		

		
		APIResult ret = new APIResult(1);
		try{
			ret =  client.swtichon(new AutoOnOff(addr, branch));
			
		}catch(Exception ex) {
			
			ret.setMessage(ex.getMessage());
		}
		
		if(ret.getStatus() == 0) {
			device.setSwitchStat(branch, Device.device_switch_close);
			deviceDao.save(device);
		}
		
		checkReturn(cmdId, ret, addr, userName);		
	}	
	
	//@Async
	public boolean setAutoOffOn(AutoOnOff auto, String userName, String groupName) {
		Device device = deviceDao.findByDeviceNo(auto.getAddr());
		int cmd = 8;
		
		UserCmd userCmd = new UserCmd(cmd,auto.toString(),auto.getAddr(),device.getDeviceName(), userName, groupName, UserCmd.cmd_status_processing);
		String cmdId = cmdService.newCmd(userCmd);
		
		APIResult ret = new APIResult(1);
		try{
			 ret = client.swtichonoff(auto);
			
		}catch(Exception ex) {
			
			ret.setMessage(ex.getMessage());
		}
		
		return checkReturn(cmdId, ret, auto.getAddr(), userName);		
	}	
	
	
	public void getPeriodReport(MeterStatus meter) {
		int cmd = 9;
		
		Device d = deviceDao.findByDeviceNo(meter.getDeviceNo());
		if(d == null) {
			log.info("device:" + meter.getDeviceNo() + " is not registered in system");
			return;
		}
		
		log.info("meter state ="+meter.getSwitchStat());
		
		int pre = d.getaState();
		int preb = d.getbState();
		int prec = d.getcState();
		int pred = d.getdState();
		
		d.syncDevice(meter);			
		d.setLastDataTime(System.currentTimeMillis());
		
		/////a
		//合闸-》拉闸,统计这次合闸的时间
		if(pre == 0 && d.getaState() != 0) {			
			d.setAtodayOnTime(d.getAtodayOnTime() + (System.currentTimeMillis() - d.getAonTime()) / 3600000 );			
			cmdService.newCmd(new UserCmd(cmd,"1路-拉闸" ,d.getDeviceNo(), d.getDeviceName(), null, null, UserCmd.cmd_status_ok));			
			
		}
		//拉闸--》合闸，更新合闸时间
		if(pre != 0 && d.getaState() == 0) {			
			d.setAonTime(System.currentTimeMillis());
			cmdService.newCmd(new UserCmd(cmd,"1路-合闸" ,d.getDeviceNo(), d.getDeviceName(), null, null, UserCmd.cmd_status_ok));			
		}
		
		/////b
		if(preb == 0 && d.getbState() != 0) {
			d.setBtodayOnTime(d.getBtodayOnTime() + (System.currentTimeMillis() - d.getBonTime()) / 3600000 );			
			cmdService.newCmd(new UserCmd(cmd,"2路-拉闸" ,d.getDeviceNo(), d.getDeviceName(), null, null, UserCmd.cmd_status_ok));			
		}
		//拉闸--》合闸，更新合闸时间
		if(preb != 0 && d.getbState() == 0) {			
			d.setBonTime(System.currentTimeMillis());
			cmdService.newCmd(new UserCmd(cmd,"2路-合闸" ,d.getDeviceNo(), d.getDeviceName(), null, null, UserCmd.cmd_status_ok));			
		}		
		
		/////c
		if(prec == 0 && d.getcState() != 0) {
			d.setCtodayOnTime(d.getCtodayOnTime() + (System.currentTimeMillis() - d.getConTime()) / 3600000 );			
			cmdService.newCmd(new UserCmd(cmd,"3路-拉闸" ,d.getDeviceNo(), d.getDeviceName(), null, null, UserCmd.cmd_status_ok));			
		}
		//拉闸--》合闸，更新合闸时间
		if(prec != 0 && d.getcState() == 0) {			
			d.setConTime(System.currentTimeMillis());
			cmdService.newCmd(new UserCmd(cmd,"3路-合闸" ,d.getDeviceNo(), d.getDeviceName(), null, null, UserCmd.cmd_status_ok));			
		}		
		
		/////d
		if(pred == 0 && d.getdState() != 0) {			
			d.setDtodayOnTime(d.getDtodayOnTime() + (System.currentTimeMillis() - d.getDonTime()) / 3600000 );			
			cmdService.newCmd(new UserCmd(cmd,"4路-拉闸" ,d.getDeviceNo(), d.getDeviceName(), null, null, UserCmd.cmd_status_ok));			
		}
		//拉闸--》合闸，更新合闸时间
		if(pred != 0 && d.getdState() == 0) {			
			d.setDonTime(System.currentTimeMillis());
			cmdService.newCmd(new UserCmd(cmd,"4路-合闸" ,d.getDeviceNo(), d.getDeviceName(), null, null, UserCmd.cmd_status_ok));			
		}		
		
		d.setStatus(Device.device_status_online);
		d.setLastHeartBeatTime(System.currentTimeMillis());
		
		log.info("a b c d state ="+d.getaState() + "," + d.getbState() + ", " + d.getcState() + "," + d.getdState());
		deviceDao.save(d);		
	}
	
	public void getHeartBeat(String addr) {
		
		Device d = deviceDao.findByDeviceNo(addr);
		if(d == null) {
			return;
		}
		
		d.setStatus(Device.device_status_online);
		d.setLastHeartBeatTime(System.currentTimeMillis());
		
		deviceDao.save(d);		
	}
	
}
