package com.shicha.yzmgt.aircb;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.DeviceSetting;
import com.shicha.yzmgt.bean.UserCmd;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.service.AirCbService;
import com.shicha.yzmgt.service.DeviceService;
import com.shicha.yzmgt.service.UserCmdService;

@RestController
@RequestMapping("/devicecmd")
public class UserCmdController {

	private static final Logger log = LoggerFactory.getLogger(UserCmdController.class);
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	UserCmdService cmdService;
	
	@Autowired
	AirCbService airService;
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	public List<UserCmd> getDevices(
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		return cmdService.getAll();
	}	
}
