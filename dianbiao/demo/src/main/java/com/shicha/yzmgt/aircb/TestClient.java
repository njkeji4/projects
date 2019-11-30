package com.shicha.yzmgt.aircb;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shicha.yzmgt.service.AirCbService;

@RestController
@RequestMapping("/test")
public class TestClient {

	private static final Logger log = LoggerFactory.getLogger(TestClient.class);
	
	@Autowired
	AirCbService airService;
	
	
	@RequestMapping(value="/{cmd}", method=RequestMethod.GET)
	public AirResult exeCmd(
			@PathVariable("cmd") Integer cmd,
			@RequestParam(defaultValue = "111111111111") String addr,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		//String addr="111111111111";
		
		if(cmd == 1)
			return airService.getDeviceStatus(addr);
		
		if(cmd == 2)
			return airService.switchOff(new String[] {addr},null,null);
	
		if(cmd == 3)
			return airService.switchOn(new String[] {addr},null,null);
		
		if(cmd == 4)
			return airService.getPullTime(addr,null,null);
		
		if(cmd == 5)
			return airService.readThresh(addr,null,null);
		
		if(cmd == 6)
			return airService.readPeriod(addr,null,null);
		
		if(cmd == 7)
			return airService.setThresh(addr,10,null,null);
		
		if(cmd == 8)
			return airService.setPeriod(addr,1,null,null);
		
		if(cmd == 9)
			return airService.setPullUpDownPeriod(addr, new long[] {1573125378000l,1573125302000l},null,null);
		
		return null;
	}
}
