package com.shicha.yzmgt.aircb;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("aircb")
public interface IAirCB {
	
	@RequestMapping(method = RequestMethod.POST, value = "/getdata", consumes = "application/json")
	AirResult getData(CbCommand cmd);
	
	@RequestMapping(method = RequestMethod.POST, value = "/getdata", consumes = "application/json")
	AirResult getData(CbCommand2 cmd);
	
}
