package com.shicha.yzmgt.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.shicha.yzmgt.aircb.TestClient;
import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.UserCmd;
import com.shicha.yzmgt.dao.IDeviceDao;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shicha.yzmgt.aircb.AirResult;
import com.shicha.yzmgt.aircb.CbCommand;
import com.shicha.yzmgt.aircb.IAirCB;
import com.shicha.yzmgt.aircb.MeterPeriod;
import com.shicha.yzmgt.aircb.MeterStatus;

@Service
public class AirCbService {

	private static final Logger log = LoggerFactory.getLogger(AirCbService.class);
	
	@Autowired
	IAirCB aircb;
	
	@Autowired
	IDeviceDao deviceDao;
	
	@Autowired
	UserCmdService cmdService;
	
	@Autowired
	private SimpMessagingTemplate webSocket;
	
	@Async
	public AirResult getDeviceStatus(String addr) {
		log.info("addr="+addr);
		Device device = deviceDao.findByDeviceNo(addr);
		if(device == null) {
			log.info("device is not registered :"+addr);
		}
		
		CbCommand command = new CbCommand(1, addr);
		AirResult result = aircb.getData(command);		
		
		if(result.getData() == null || !result.isCmdStat()) {
			log.info("get device data failed, device is offline");
			
			if(device != null)
			{
				device.setStatus(Device.device_status_offline);
				deviceDao.save(device);
			}
			
			return result;
		}
		
		Object o = result.getData();
		
		ObjectMapper objectMapper = new ObjectMapper();		
		MeterStatus status = objectMapper.convertValue(o, MeterStatus.class);
		
		if(device != null) {		
			device.syncDevice(status);
			device.setStatus(Device.device_status_online);
			deviceDao.save(device);
		}		
		
		return result;
	}
	
	@Async
	public AirResult switchOff(String addr, String userName, String groupName) {
		log.info("addr="+addr);
		
		Device device = deviceDao.findByDeviceNo(addr);
		if(device == null) {
			log.info("device isnot existed:"+addr);
			return null;
		}
		UserCmd userCmd = new UserCmd(2,null,addr,device.getDeviceName(), userName, groupName, UserCmd.cmd_status_processing);
		String cmdId = cmdService.newCmd(userCmd);
		
		CbCommand command = new CbCommand(2, addr);
		AirResult result = aircb.getData(command);
		
		cmdService.updateCmdStatus(cmdId, result.isCmdStat()?UserCmd.cmd_status_ok:UserCmd.cmd_status_fail,result.getMsg(),null);
		
		if(result.isCmdStat()) {
			device.setSwitchStat(Device.device_switch_open);
			deviceDao.save(device);
		}
		
		String target = "/topic/greetings/" + userName;
		if(result.isCmdStat())
			webSocket.convertAndSend(target, addr+"拉闸成功");
		else
			webSocket.convertAndSend(target, addr+"拉闸失败");
		
		return null;
	}
	
	@Async
	public AirResult switchOn(String addr, String userName, String groupName) {
		log.info("addr="+addr);
		
		Device device = deviceDao.findByDeviceNo(addr);
		UserCmd userCmd = new UserCmd(3,null,addr,device.getDeviceName(), userName, groupName, UserCmd.cmd_status_processing);
		String cmdId = cmdService.newCmd(userCmd);
		
		CbCommand command = new CbCommand(3, addr);
		AirResult result = aircb.getData(command);
		
		cmdService.updateCmdStatus(cmdId, result.isCmdStat()?UserCmd.cmd_status_ok:UserCmd.cmd_status_fail,result.getMsg(),null);
		
		if(result.isCmdStat()) {
			device.setSwitchStat(Device.device_switch_close);
			deviceDao.save(device);
		}		
		
		String target = "/topic/greetings/" + userName;
		
		if(result.isCmdStat())
			webSocket.convertAndSend(target, addr+"合闸成功");
		else
			webSocket.convertAndSend(target, addr+"合闸失败");
		
		return null;
	}
	
	@Async
	public AirResult getPullTime(String addr, String userName, String groupName) {
		log.info("addr="+addr);
		
		Device device = deviceDao.findByDeviceNo(addr);
		UserCmd userCmd = new UserCmd(4,null,addr,device.getDeviceName(), userName, groupName, UserCmd.cmd_status_processing);
		String cmdId = cmdService.newCmd(userCmd);
		
		
		CbCommand command = new CbCommand(4, addr);
		AirResult result = aircb.getData(command);
		
		if(result.getData() == null) {
			log.info("device is offline");
			
			cmdService.updateCmdStatus(cmdId, result.isCmdStat()?UserCmd.cmd_status_ok:UserCmd.cmd_status_fail,result.getMsg(),null);
			
			return result;
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		String content="";
		if(result.getData() instanceof  List) {
			List list = (List)result.getData();
			for(int i = 0; i < list.size(); i++) {
				Object o = list.get(i);
				MeterPeriod p = objectMapper.convertValue(o, MeterPeriod.class);
				content+=p.getHH()+"-"+p.getMM()+"-"+p.getSS()+"|";
				log.info(p.getHH() + "");
			}
			
		}
		
		cmdService.updateCmdStatus(cmdId, result.isCmdStat()?UserCmd.cmd_status_ok:UserCmd.cmd_status_fail,result.getMsg(),content);
		
		return result;
	}
	
	@Async
	public AirResult readThresh(String addr, String userName, String groupName) {
		
		log.info("addr="+addr);
		Device device = deviceDao.findByDeviceNo(addr);
		UserCmd userCmd = new UserCmd(5,null,addr,device.getDeviceName(), userName, groupName, UserCmd.cmd_status_processing);
		String cmdId = cmdService.newCmd(userCmd);
		
		CbCommand command = new CbCommand(5, addr);
		AirResult result = aircb.getData(command);
		
		cmdService.updateCmdStatus(cmdId, result.isCmdStat()?UserCmd.cmd_status_ok:UserCmd.cmd_status_fail,result.getMsg(),null);
				
		return result;
	}
	
	@Async
	public AirResult readPeriod(String addr, String userName, String groupName) {
		log.info("addr="+addr);
		
		Device device = deviceDao.findByDeviceNo(addr);
		UserCmd userCmd = new UserCmd(6,null,addr,device.getDeviceName(), userName, groupName, UserCmd.cmd_status_processing);
		String cmdId = cmdService.newCmd(userCmd);
		
		CbCommand command = new CbCommand(6, addr);
		AirResult result = aircb.getData(command);
		
		cmdService.updateCmdStatus(cmdId, result.isCmdStat()?UserCmd.cmd_status_ok:UserCmd.cmd_status_fail,result.getMsg(),null);
		
		return result;
	}
	
	@Async
	public AirResult setThresh(String addr, double value, String userName, String groupName) {
		log.info("addr="+addr);
		
		Device device = deviceDao.findByDeviceNo(addr);
		UserCmd userCmd = new UserCmd(7,null,addr,device.getDeviceName(), userName, groupName, UserCmd.cmd_status_processing);
		String cmdId = cmdService.newCmd(userCmd);
		
		CbCommand command = new CbCommand(7, addr, value);
		AirResult result = aircb.getData(command);
		
		cmdService.updateCmdStatus(cmdId, result.isCmdStat()?UserCmd.cmd_status_ok:UserCmd.cmd_status_fail,result.getMsg(),null);
		
		return result;
	}
	
	@Async
	public AirResult setPeriod(String addr, int value, String userName, String groupName) {
		log.info("addr="+addr);
		
		Device device = deviceDao.findByDeviceNo(addr);
		UserCmd userCmd = new UserCmd(8,null,addr,device.getDeviceName(), userName, groupName, UserCmd.cmd_status_processing);
		String cmdId = cmdService.newCmd(userCmd);
		
		CbCommand command = new CbCommand(8, addr, value);
		AirResult result = aircb.getData(command);
		
		cmdService.updateCmdStatus(cmdId, result.isCmdStat()?UserCmd.cmd_status_ok:UserCmd.cmd_status_fail,result.getMsg(),null);
		
		return result;
	}
	
	private String getHHMMSS(long v) {
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(v);
		
		SimpleDateFormat sdf=new SimpleDateFormat("HHmmss");
		String result = sdf.format(c.getTime());
		
		//String result = c.get(Calendar.HOUR_OF_DAY)+""+c.get(Calendar.MINUTE)+""+c.get(Calendar.SECOND);
		
		//System.out.println(result);
		return result;//c.get(Calendar.HOUR_OF_DAY)+""+c.get(Calendar.MINUTE)+""+c.get(Calendar.SECOND);
	}
	
	@Async
	public AirResult setPullUpDownPeriod(String addr, long[] value, String userName, String groupName) {
		
		log.info("addr="+addr);
		
		Device device = deviceDao.findByDeviceNo(addr);
		UserCmd userCmd = new UserCmd(9,null,addr,device.getDeviceName(), userName, groupName, UserCmd.cmd_status_processing);
		String cmdId = cmdService.newCmd(userCmd);
		
		
		log.info("value.length="+value.length);
		if(value.length > 8) {
			AirResult result= new AirResult(9);
			result.setCmdStat(false);
			result.setMsg("value length >8" + value.length);
			
			cmdService.updateCmdStatus(cmdId, result.isCmdStat()?UserCmd.cmd_status_ok:UserCmd.cmd_status_fail,result.getMsg(),null);
			
			return result;
		}
		//String date= "010100020200030300040400050500060600070700080800";
		String date="";
		for(int i = 0; i < value.length; i++) {
			date+=this.getHHMMSS(value[i]);
		}
		for(int i = value.length; i < 8; i++)
			date += "000000";
		
		System.out.println("date="+date);
			
		
		CbCommand command = new CbCommand(9, addr, date);
		AirResult result = aircb.getData(command);
		
		cmdService.updateCmdStatus(cmdId, result.isCmdStat()?UserCmd.cmd_status_ok:UserCmd.cmd_status_fail,result.getMsg(),date);
		
		return result;
	}
}
