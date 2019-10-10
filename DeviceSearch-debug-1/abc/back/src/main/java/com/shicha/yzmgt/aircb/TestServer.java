package com.shicha.yzmgt.aircb;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/getdata")
public class TestServer {

	private static final Logger log = LoggerFactory.getLogger(TestServer.class);
	
	@RequestMapping(method=RequestMethod.POST)
	public AirResult exeCmd(
			@RequestBody CbCommand command,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		int cmd = command.getCmd();
		
		log.info("cmd="+cmd );
		
		String addr = command.getParam().toString();
		
		log.info("cmd="+cmd + ",param="+addr);
		
		
		return makeResult(cmd);
	}
	
	
	private AirResult makeResult(int cmd) {
		AirResult r = new AirResult(cmd);
		if(cmd == 1) {
			r.setData(new MeterStatus());
		}
		
		if(cmd == 4) {
			ArrayList<MeterPeriod> list = new ArrayList<MeterPeriod>();
			for(int i = 0; i < 5; i++) {
				list.add(new MeterPeriod());
			}
			r.setData(list);
		}
		
		return r;
	}
}
