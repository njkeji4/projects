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
	
//	public boolean beforeCommand(int cmd, Device device, String addr, String userName, String groupName) {
//		log.info("addr="+addr);
//		
//		
//		if(device == null) {
//			log.info("device isnot existed:"+addr);
//			return false;
//		}
//		
//		if(device.getCmdId() != null) {
//			UserCmd userCmd = new UserCmd(cmd,null,addr,device.getDeviceName(), userName, groupName, UserCmd.cmd_status_processing);
//			
//			userCmd.setStatus(UserCmd.cmd_status_fail);
//			userCmd.setRetMessage("还有命令在执行中，这条命令没有执行");
//			userCmd.setRetTime(System.currentTimeMillis());
//			cmdService.newCmd(userCmd);
//			
//			log.info("还有命令在执行中，这条命令没有执行:"+addr);
//			
//			return false;//new APIResult(1, "还有命令在执行中，这条命令没有执行");
//		}
//		return true;
//	}
	
	
	
	public void checkReturn(String cmdId, APIResult ret, String addr, String userName) {
		
		Optional<UserCmd>option = userCmdDao.findById(cmdId);
		if(!option.isPresent()) {
			return;
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
	}	
	
	//@Async
	public APIResult switchOff(String addr,int branch, String userName, String groupName) {
		Device device = deviceDao.findByDeviceNo(addr);
		int cmd = 1;
		
		UserCmd userCmd = new UserCmd(cmd,null,addr,device.getDeviceName(), userName, groupName, UserCmd.cmd_status_processing);
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
		
		return ret;
	}
	
	//@Async
	public APIResult switchOn(String addr, int branch, String userName, String groupName) {
		Device device = deviceDao.findByDeviceNo(addr);
		int cmd = 2;
		
		UserCmd userCmd = new UserCmd(cmd,null,addr,device.getDeviceName(), userName, groupName, UserCmd.cmd_status_processing);
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
		
		return ret;
		
	}	
	
	//@Async
	public APIResult setAutoOffOn(AutoOnOff auto, String userName, String groupName) {
		Device device = deviceDao.findByDeviceNo(auto.getAddr());
		int cmd = 8;
		
		UserCmd userCmd = new UserCmd(cmd,null,auto.getAddr(),device.getDeviceName(), userName, groupName, UserCmd.cmd_status_processing);
		String cmdId = cmdService.newCmd(userCmd);
		
		APIResult ret = new APIResult(1);
		try{
			 ret = client.swtichonoff(auto);
			
		}catch(Exception ex) {
			
			ret.setMessage(ex.getMessage());
		}
		
		checkReturn(cmdId, ret, auto.getAddr(), userName);
		
		return ret;
	}	
	
	
	public void getPeriodReport(MeterStatus meter) {
		
		Device d = deviceDao.findByDeviceNo(meter.getDeviceNo());
		if(d == null) {
			log.info("device:" + meter.getDeviceNo() + " is not registered in system");
			return;
		}
		
		int pre = d.getaState();				
		d.syncDevice(meter);			
		d.setLastDataTime(System.currentTimeMillis());
		
		//合闸-》拉闸,统计这次合闸的时间
		if(pre == 0 && d.getaState() != 0) {
			d.setTodayOnTime(d.getTodayOnTime() + (System.currentTimeMillis() - d.getOnTime()) / 3600000);
		}
		//拉闸--》合闸，更新合闸时间
		if(pre != 0 && d.getaState() == 0) {
			d.setOnTime(System.currentTimeMillis());
		}
		
		d.setTodayEnergy(d.getAllEnergy() - d.getLastEnergy());
		
		d.setStatus(Device.device_status_online);
		d.setLastHeartBeatTime(System.currentTimeMillis());
		
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
