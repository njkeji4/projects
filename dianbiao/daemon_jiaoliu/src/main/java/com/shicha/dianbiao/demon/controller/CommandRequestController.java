package com.shicha.dianbiao.demon.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shicha.dianbiao.demon.domain.APIResult;
import com.shicha.dianbiao.demon.domain.AutoOnOff;
import com.shicha.dianbiao.demon.domain.RawCmd;
import com.shicha.dianbiao.demon.domain.ReadCommand;
import com.shicha.dianbiao.demon.domain.WriteCommand;
import com.shicha.dianbiao.demon.netty.CmdRes;
import com.shicha.dianbiao.demon.netty.Device;


@RestController
@RequestMapping("/command")
public class CommandRequestController {

	private static final Logger log = LoggerFactory.getLogger(CommandRequestController.class);	
	
	public static int CMD_EXE_OK = 0;
	public static int CMD_EXE_OFFLINE = 1;
	public static int CMD_EXE_BUSY = 2;
	public static int CMD_EXE_TIMEOUT = 3;
	public static int CMD_EXE_FAIL = 4;
	public static int CMD_UNSUPPORT = 5;	
	
	public static String[] MESSAGES = {"", "Device Offline", "Device busy", "Command timeout","Command failed:","unsupported command"};
		
	@Value("${cmd.timeout:30}")
	int cmdTimeout;		
	
	@RequestMapping(value="/read", method=RequestMethod.POST)
	public APIResult getData(
			@RequestBody ReadCommand cmd,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
			
			int ret = Device.readData(cmd.getAddr(), cmd.getType());			
			if(ret != 0) {
				return new APIResult(ret,MESSAGES[ret]);
			}
		
			return waitResult(cmd.getAddr());
	}
	
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public APIResult setData(
			@RequestBody WriteCommand cmd,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
			
			int ret = Device.writeData(cmd.getAddr(), cmd.getType(), cmd.getBuf());			
			if(ret != 0) {
				return new APIResult(ret,MESSAGES[ret]);
			}
		
			return waitResult(cmd.getAddr());
	}
	
//	@RequestMapping(value="/get/time", method=RequestMethod.POST)
//	public APIResult getTime(
//			@RequestBody String addr,
//			HttpServletRequest req, HttpServletResponse response) throws IOException{
//			
//			int ret = Device.readTime(addr);			
//			if(ret != 0) {
//				return new APIResult(ret,MESSAGES[ret]);
//			}
//		
//			return waitResult(addr);
//	}
//	
//	@RequestMapping(value="/get/date", method=RequestMethod.POST)
//	public APIResult getDate(
//			@RequestBody String addr,
//			HttpServletRequest req, HttpServletResponse response) throws IOException{
//			
//			int ret = Device.readDate(addr);		
//			if(ret != 0) {
//				return new APIResult(ret,MESSAGES[ret]);
//			}
//		
//			return waitResult(addr);
//	}
//	
//	@RequestMapping(value="/get/period", method=RequestMethod.POST)
//	public APIResult readPeriod(
//			@RequestBody String addr,
//			HttpServletRequest req, HttpServletResponse response) throws IOException{
//			
//			int ret = Device.readPeriod(addr);			
//			if(ret != 0) {
//				return new APIResult(ret,MESSAGES[ret]);
//			}
//		
//			return waitResult(addr);
//	}
//	
//	@RequestMapping(value="/get/autoonoff", method=RequestMethod.POST)
//	public APIResult getAutoOnOff(
//			@RequestBody String addr,
//			HttpServletRequest req, HttpServletResponse response) throws IOException{
//			
//			int ret = Device.readAutoOnOff(addr);
//			if(ret != 0) {
//				return new APIResult(ret,MESSAGES[ret]);
//			}
//			
//			return waitResult(addr);
//			
//	}
	
//	@RequestMapping(value="/set/period", method=RequestMethod.POST)
//	public APIResult setPeriod(
//			@RequestBody String addr,
//			HttpServletRequest req, HttpServletResponse response) throws IOException{
//			
//			int ret = Device.switchOnCmd(addr);			
//			if(ret != 0) {
//				return new APIResult(ret,MESSAGES[ret]);
//			}
//		
//			return waitResult(addr);
//	}
	
	
	@RequestMapping(value="/on", method=RequestMethod.POST)
	public APIResult devcieon(
			@RequestBody String addr,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
			
			int ret = Device.switchOnCmd(addr);			
			if(ret != 0) {
				return new APIResult(ret,MESSAGES[ret]);
			}
		
			return waitResult(addr);
	}
	
	@RequestMapping(value="/off", method=RequestMethod.POST)
	public APIResult deviceOff(
			@RequestBody String addr,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
			
			int ret = Device.switchOffCmd(addr);
			if(ret != 0) {
				return new APIResult(ret,MESSAGES[ret]);
			}
		
			return waitResult(addr);
	}
	
	@RequestMapping(value="/autoonoff", method=RequestMethod.POST)
	public APIResult settingAutoOnOff(
			@RequestBody AutoOnOff setting,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
			
			int ret = Device.setAutoSwitchOnOff(setting.getAddr(), setting.getTimes());
			if(ret != 0) {
				return new APIResult(ret,MESSAGES[ret]);
			}
			
			return waitResult(setting.getAddr());
			
	}	
	
	@RequestMapping(value="/raw", method=RequestMethod.POST)
	public APIResult rawCommand(
			@RequestBody RawCmd raw,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
			
			byte[] buff = raw.getBuff();
			calcCS(buff);
			
			int ret = Device.cmd(raw.getAddr(), buff);
			if(ret != 0 ) {
				return new APIResult(ret,MESSAGES[ret]);
			}
			
			return waitResult(raw.getAddr());
	}
	
	public APIResult waitResult(String addr) {
		
		log.info("waitting for command result");
		Device d = Device.getDevice(addr);
		
		try {
			synchronized(d) {
				d.wait(cmdTimeout * 1000);
			}
		}catch(Exception ex) {
			ex.printStackTrace();			
			log.error("wait for command result exception:" + ex.getMessage());
		}
		
		d.setBusy(false);
		
		CmdRes res = d.getCmdRes();
		if(res == null) {
			log.warn("command timeout");			
			return new APIResult(CMD_EXE_TIMEOUT, MESSAGES[CMD_EXE_TIMEOUT]);
		}
		
		log.info("get command result:"+res.getResponse());
		
		int ret = res.getStatus();	
		return new APIResult(ret, MESSAGES[ret] + res.getResponse(), res.getData());
	}
	
	public static void calcCS(byte[] buf) {
		int sum = 0;
		for(int i = 4; i < buf.length-2; i++) {
			sum += buf[i];
			sum = sum % 256;
		}
		buf[buf.length - 2] = (byte)sum;
	}
	
}
