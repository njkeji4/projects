package com.shicha.yzmgt.controller;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shicha.yzmgt.aircb.MeterStatus;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.CmdRes;
import com.shicha.yzmgt.service.AirCbService2;


@RestController
@RequestMapping("/terminal")
public class TerminalMessageController {

	private static final Logger log = LoggerFactory.getLogger(TerminalMessageController.class);
	
	@Autowired
	AirCbService2 service;
	
	@RequestMapping(value="/period", method=RequestMethod.POST)
	public APIResult cmdResponse(
			@RequestBody MeterStatus meter,
			HttpServletRequest req, HttpServletResponse response) throws IOException{			
		
			log.info("receive period report message:" + meter.getDeviceNo());			
			
			service.getPeriodReport(meter);
			
			return new APIResult(0);
	}
	
	@RequestMapping(value="/heartbeat", method=RequestMethod.POST)
	public APIResult heartBeat(
			@RequestBody String addr,
			HttpServletRequest req, HttpServletResponse response) throws IOException{			
		
			log.info("receive heart beat message:" + addr);
		
			service.getHeartBeat(addr);
			
			return new APIResult(0);
	}
}
