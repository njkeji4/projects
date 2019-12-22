package com.shicha.dianbiao.demon.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shicha.dianbiao.demon.domain.APIResult;
import com.shicha.dianbiao.demon.domain.AutoOnOff;
import com.shicha.dianbiao.demon.domain.RawCmd;
import com.shicha.dianbiao.demon.netty.Device;
import com.shicha.dianbiao.demon.netty.DeviceMessageDecoder;


@RestController
@RequestMapping("/command")
public class CommandRequestController {

	private static final Logger log = LoggerFactory.getLogger(CommandRequestController.class);	
	
	@RequestMapping(value="/read", method=RequestMethod.POST)
	public APIResult readData(
			@RequestBody String addr,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
					
			int ret = Device.queryCmd(addr);	
			String message= null;
			if(ret == -1) {
				message="device is offline";
			}
		
			return new APIResult(ret,message);
	}
	
	@RequestMapping(value="/on", method=RequestMethod.POST)
	public APIResult devcieon(
			@RequestBody String addr,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
			
			int ret = Device.switchOnCmd(addr);
		
			String message= null;
			if(ret == -1) {
				message="device is offline";
			}
		
			return new APIResult(ret,message);
	}
	
	@RequestMapping(value="/off", method=RequestMethod.POST)
	public APIResult deviceOff(
			@RequestBody String addr,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
			
			int ret = Device.switchOffCmd(addr);
		
			String message= null;
			if(ret == -1) {
				message="device is offline";
			}
		
			return new APIResult(ret,message);
	}
	
	@RequestMapping(value="/autoonoff", method=RequestMethod.POST)
	public APIResult settingAutoOnOff(
			@RequestBody AutoOnOff setting,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
			
			int ret = Device.setAutoSwitchOnOff(setting.getAddr(), setting.getTimes());
		
			String message= null;
			if(ret == -1) {
				message="device is offline";
			}
		
			return new APIResult(ret,message);
	}
	
	public static void calcCS(byte[] buf) {
		int sum = 0;
		for(int i = 4; i < buf.length-2; i++) {
			sum += buf[i];
			sum = sum % 256;
		}
		
		System.out.println("cs="+sum);
		
		buf[buf.length - 2] = (byte)sum;
	}
	
	@RequestMapping(value="/raw", method=RequestMethod.POST)
	public APIResult rawCommand(
			@RequestBody RawCmd raw,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
			
			byte[] buff = raw.getBuff();
			calcCS(buff);
			
			Device.cmd(raw.getAddr(), buff);//raw.getBuff());
			
			return new APIResult(0);
	}
	
}
