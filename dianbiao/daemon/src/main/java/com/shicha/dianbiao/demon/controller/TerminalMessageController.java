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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shicha.dianbiao.demon.domain.APIResult;
import com.shicha.dianbiao.demon.netty.CmdRes;
import com.shicha.dianbiao.demon.netty.Device;

@RestController
@RequestMapping("/terminal")
public class TerminalMessageController {

	private static final Logger log = LoggerFactory.getLogger(TerminalMessageController.class);
	
	@RequestMapping(value="/cmdresult", method=RequestMethod.POST)
	public APIResult cmdResponse(
			@RequestBody CmdRes cmdRes,
			HttpServletRequest req, HttpServletResponse response) throws IOException{			
		
			log.info("receive post message from daemon:" + cmdRes.getResponse());
		
			//JSONObject obj = new JSONObject()
			ObjectMapper mapper = new ObjectMapper();
			log.info(mapper.writeValueAsString(cmdRes));
			
			return new APIResult(0);
	}
	
	@RequestMapping(value="/heartbeat", method=RequestMethod.POST)
	public APIResult heartBeat(
			@RequestBody String addr,
			HttpServletRequest req, HttpServletResponse response) throws IOException{			
		
			log.info("receive heart beat message:" + addr);
		
			return new APIResult(0);
	}
}
