package com.shicha.yzmgt.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.AutoOnOff;

@FeignClient(url = "${dianbiao.server.url}",name="aircb2")
public interface IDaemon {
	
	@RequestMapping(method = RequestMethod.POST, value = "/command/read")
	APIResult read(String addr);
	
	@RequestMapping(method = RequestMethod.POST, value = "/command/get")
	APIResult getData(String addr);
	
	@RequestMapping(method = RequestMethod.POST, value = "/command/on")
	APIResult swtichon(AutoOnOff on);
	
	@RequestMapping(method = RequestMethod.POST, value = "/command/off")
	APIResult switchoff(AutoOnOff off);
	
	@RequestMapping(method = RequestMethod.POST, value = "/command/autoonoff")
	APIResult swtichonoff(AutoOnOff setting);	
}
