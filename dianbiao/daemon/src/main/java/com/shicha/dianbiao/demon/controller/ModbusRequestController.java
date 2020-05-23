package com.shicha.dianbiao.demon.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.ip.tcp.TcpMaster;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersResponse;
import com.shicha.dianbiao.demon.domain.APIResult;
import com.shicha.dianbiao.demon.domain.ReadCommand;
import com.shicha.dianbiao.demon.netty.Device;
import com.shicha.dianbiao.demon.netty.Utils;

@RestController
@RequestMapping("/modbus")
public class ModbusRequestController {

	private static final Logger log = LoggerFactory.getLogger(ModbusRequestController.class);	
	
	@RequestMapping(value="/read", method=RequestMethod.GET)
	public APIResult getData(
			
			HttpServletRequest req, HttpServletResponse response) throws IOException{
			
		log.info("modbus read");	
		
		IpParameters ip = new  IpParameters();
		ip.setPort(503);
		ip.setHost("218.19.146.64");
		TcpMaster master = new TcpMaster(ip,false);
		try {
			ReadInputRegistersRequest modReq = new ReadInputRegistersRequest(1, 0, 1);
			ReadInputRegistersResponse modRes = (ReadInputRegistersResponse)master.sendImpl(modReq);
			
			byte[] data = modRes.getData();		
			System.out.println("received data length:"+data.length);
			
			System.out.println(Utils.byte2str(data));
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return new APIResult(0);
			
	}
}
