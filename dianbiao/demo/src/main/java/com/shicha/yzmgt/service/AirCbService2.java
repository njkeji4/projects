package com.shicha.yzmgt.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shicha.yzmgt.aircb.AirResult;
import com.shicha.yzmgt.aircb.CbCommand;
import com.shicha.yzmgt.aircb.IAirCB;
import com.shicha.yzmgt.aircb.MeterPeriod;
import com.shicha.yzmgt.aircb.MeterStatus;
import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.UserCmd;
import com.shicha.yzmgt.controller.IDaemon;
import com.shicha.yzmgt.dao.IDeviceDao;
import com.shicha.yzmgt.dao.IUserCmdDao;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.AutoOnOff;
import com.shicha.yzmgt.domain.CmdRes;

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
	
	public boolean beforeCommand(int cmd, Device device, String addr, String userName, String groupName) {
		log.info("addr="+addr);
		
		
		if(device == null) {
			log.info("device isnot existed:"+addr);
			return false;
		}
		
		if(device.getCmdId() != null) {
			UserCmd userCmd = new UserCmd(cmd,null,addr,device.getDeviceName(), userName, groupName, UserCmd.cmd_status_processing);
			
			userCmd.setStatus(UserCmd.cmd_status_fail);
			userCmd.setRetMessage("还有命令在执行中，这条命令没有执行");
			userCmd.setRetTime(System.currentTimeMillis());
			cmdService.newCmd(userCmd);
			
			log.info("还有命令在执行中，这条命令没有执行:"+addr);
			
			return false;//new APIResult(1, "还有命令在执行中，这条命令没有执行");
		}
		return true;
	}
	
	public void checkReturn(int cmd, APIResult ret, Device device, String addr, String userName, String groupName) {
		
		UserCmd userCmd = new UserCmd(cmd,null,addr,device.getDeviceName(), userName, groupName, UserCmd.cmd_status_processing);
		
		if(ret.getStatus() != 0) {
			userCmd.setStatus(UserCmd.cmd_status_fail);
			userCmd.setRetMessage(ret.getMessage());
			userCmd.setRetTime(System.currentTimeMillis());
			cmdService.newCmd(userCmd);
			
			if(userCmd != null && userCmd.getCmdCode() != 0) {//websocket send response
				
				String message = addr;		
				String target = "/topic/greetings/" + userName;
				message +=  UserCmd.cmd_name[userCmd.getCmdCode()] +  "失败: " + ret.getMessage();			
				webSocket.convertAndSend(target, message);
			}
			
			return ;
		}
		
		String cmdId = cmdService.newCmd(userCmd);
		
		device.setCmdId(cmdId);
		device.setCmdTime(System.currentTimeMillis());
		deviceDao.save(device);
	}
	
	public APIResult getDeviceStatus(String addr) {
		log.info("addr="+addr);
		Device device = deviceDao.findByDeviceNo(addr);
		int cmd = 0;
		
		if(!beforeCommand(cmd, device, addr, "admin", null)) {
			return new APIResult(1);
		}		
		
		APIResult ret = new APIResult(1);
		
		try{
			ret = client.read(addr);
		}catch(Exception ex) {
			ret.setMessage(ex.getMessage());
		}
		
		checkReturn(cmd, ret, device, addr, "admin", null);
		
		return ret;
	}
	
	public APIResult switchOff(String addr, String userName, String groupName) {
		Device device = deviceDao.findByDeviceNo(addr);
		int cmd = 1;
		
		log.info("befor cmmand");
		if(!beforeCommand(cmd, device, addr, userName, groupName)) {
			return new APIResult(1);
		}		
		
		APIResult ret = new APIResult(1);
		try{
			ret = client.switchoff(addr);
			
		}catch(Exception ex) {
			
			ret.setMessage(ex.getMessage());
		}
		
		log.info("check return");
		
		checkReturn(cmd, ret, device, addr, userName, groupName);
		
		return ret;
	}
	
	
	public APIResult switchOn(String addr, String userName, String groupName) {
		Device device = deviceDao.findByDeviceNo(addr);
		int cmd = 2;
		
		if(!beforeCommand(cmd, device, addr, userName, groupName)) {
			return new APIResult(1);
		}		
		
		APIResult ret = new APIResult(1);
		try{
			ret =  client.swtichon(addr);
			
		}catch(Exception ex) {
			
			ret.setMessage(ex.getMessage());
		}
		
		checkReturn(cmd, ret, device, addr, userName, groupName);
		
		return ret;
		
	}	
	
	public APIResult setAutoOffOn(AutoOnOff auto, String userName, String groupName) {
		Device device = deviceDao.findByDeviceNo(auto.getAddr());
		int cmd = 8;
		
		if(!beforeCommand(cmd, device, auto.getAddr(), userName, groupName)) {
			return new APIResult(1);
		}		
		
		APIResult ret = new APIResult(1);
		try{
			 ret = client.swtichonoff(auto);
			
		}catch(Exception ex) {
			
			ret.setMessage(ex.getMessage());
		}
		
		checkReturn(cmd, ret, device, auto.getAddr(), userName, groupName);
		
		return ret;
	}	
	
	
	public void getCmdResponse(CmdRes res) {
		
		log.info("report command response from device:" + res.getAddr() + ":" + res.getResponse());
		
		Device d = deviceDao.findByDeviceNo(res.getAddr());
		if(d == null) {
			log.info("device:" + res.getAddr() + " is not registered in system");
			return;
		}
		
		//device report meter data
		if(res.getCmdCode() == 0 && res.getStatus() == 0) {//meter data report
			ObjectMapper objectMapper = new ObjectMapper();		
			MeterStatus meter = objectMapper.convertValue(res.getData(), MeterStatus.class);			
			d.syncDevice(meter);
			d.setLastDataTime(System.currentTimeMillis());
			deviceDao.save(d);
			return;
		}
		
		
		if(d.getCmdId() == null) {
			log.info("no command is executing on this device:" + res.getResponse());
			return;
		}
		
		//update command status
		Optional<UserCmd> opt = userCmdDao.findById(d.getCmdId());
		UserCmd userCmd = null;
		if(opt.isPresent() && opt.get().getStatus() == UserCmd.cmd_status_processing) {
			userCmd = opt.get();
			userCmd.setStatus(res.getStatus());
			userCmd.setRetMessage(res.getResponse());
			userCmd.setRetTime(System.currentTimeMillis());
			userCmd.setRetValue(res.getResponse());
			
			userCmdDao.save(userCmd);
		}
		
		//update device
		d.setCmdId(null);
		d.setCmdTime(null);
		
		//update device if response is ok
		if(res.getStatus() == 0 && userCmd != null) {
			if(userCmd.getCmdCode() == 1 ) {	//拉闸
				
				d.setSwitchStat(Device.device_switch_open);
				
			}else if(userCmd.getCmdCode() == 2) {  //合闸
				
				d.setSwitchStat(Device.device_switch_close);
				
			}
		}
		
		deviceDao.save(d);
		
		//socket
		if(userCmd != null && userCmd.getCmdCode() != 0) {//websocket send response
			
			String message = res.getAddr();		
			String target = "/topic/greetings/" + userCmd.getUserName();
			message +=  res.getStatus() == 0 ? "操作成功" : "操作失败";			
			webSocket.convertAndSend(target, message);
		}
		
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
