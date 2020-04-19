package com.shicha.yzmgt.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.DeviceSetting;
import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.bean.basestation.BaseStation;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.AutoOnOff;
import com.shicha.yzmgt.domain.DeviceSettingDomain;
import com.shicha.yzmgt.domain.SearchDevice;
import com.shicha.yzmgt.domain.SearchStation;
import com.shicha.yzmgt.service.AirCbService2;
import com.shicha.yzmgt.service.BaseStationService;
import com.shicha.yzmgt.service.DeviceService;

@RestController
@RequestMapping("/basestation")
public class BaseStationController {

	private static final Logger log = LoggerFactory.getLogger(BaseStationController.class);
	
	@Autowired
	BaseStationService baseService;	
		
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	public APIResult getall(			
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		List<BaseStation>stations = baseService.getAll();
		
		return new APIResult(0, "", stations);
	}
	
	@RequestMapping(value="/searchDevice", method=RequestMethod.POST)
	public APIResult searchDevices(
			@RequestBody SearchStation search,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		Page<BaseStation>devices = baseService.searchDevice(search);
		
		return new APIResult(0, "", devices);
	}
	
	
	@RequestMapping(value="/del", method=RequestMethod.POST)
	public APIResult delDevice(
			@RequestBody String[]ids,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		baseService.delDevice(ids);
		
		return new APIResult(0);
	}
	
	@RequestMapping(value="/setting/edit", method=RequestMethod.POST)
	public APIResult addSetting(
			@RequestBody DeviceSettingDomain settingDomain,
			HttpServletRequest req, HttpServletResponse response) throws IOException{		
		
		return baseService.addDeviceSetting(settingDomain);
		
		//return new APIResult(0,"命令已经发送");
	}	
	
	@RequestMapping(value="/setting/get", method=RequestMethod.POST)
	public List<DeviceSetting> getSetting(
			@RequestBody DeviceSetting setting,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		return baseService.getByDeviceNo(setting.getDeviceNo());
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public APIResult uploadBlacklist(
			@RequestParam("uploadFile") MultipartFile file,	
			HttpServletRequest req, HttpServletResponse response) throws IOException{		
		
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			
			boolean result = baseService.imporBaseDataFromFile(file, userName);
			
			if(result)
				return new APIResult(0);
			else
				return new APIResult(1, "导入文件失败");
		
	}
}
