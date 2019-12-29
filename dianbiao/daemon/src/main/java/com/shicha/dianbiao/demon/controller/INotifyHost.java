package com.shicha.dianbiao.demon.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shicha.dianbiao.demon.domain.APIResult;
import com.shicha.dianbiao.demon.domain.MeterData;
import com.shicha.dianbiao.demon.netty.CmdRes;


@FeignClient(url = "${post.host}",name="notificationHost")
public interface INotifyHost {

	//@RequestMapping(method = RequestMethod.POST, value = "/cmdresult")
	//APIResult postCmdResult(CmdRes res);
	
	@RequestMapping(method = RequestMethod.POST, value = "/period")
	APIResult postPeriod(MeterData meter);
	
	@RequestMapping(method = RequestMethod.POST, value = "/heartbeat")
	APIResult postHeartBeat(String addr);
	
}
