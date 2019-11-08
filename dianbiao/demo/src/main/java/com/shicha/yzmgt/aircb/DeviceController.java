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
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.service.AirCbService;
import com.shicha.yzmgt.service.DeviceService;

@RestController
@RequestMapping("/device")
public class DeviceController {

	private static final Logger log = LoggerFactory.getLogger(DeviceController.class);
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	AirCbService airService;
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	public List<Device> getDevices(
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		return deviceService.getAll();
	}
	
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public APIResult addDevice(
			@RequestBody Device device,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		if(deviceService.deviceNameExisted(device)) {
			return new APIResult(1, "name existed");
		}
		deviceService.addDevice(device);
		
		return new APIResult(0);
	}
	
	@RequestMapping(value="/off", method=RequestMethod.POST)
	public APIResult offDevice(
			@RequestBody Device device,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		AirResult result= airService.switchOff(device.getDeviceNo());
		
		return new APIResult(result.isCmdStat()?0:1,result.getMsg());
	}
	
	@RequestMapping(value="/on", method=RequestMethod.POST)
	public APIResult onDevice(
			@RequestBody Device device,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		AirResult result=airService.switchOn(device.getDeviceNo());
		
		return new APIResult(result.isCmdStat()?0:1,result.getMsg());
	}
	
	@RequestMapping(value="/del", method=RequestMethod.POST)
	public APIResult delDevice(
			@RequestBody Device device,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		
		deviceService.delDevice(device);
		
		return new APIResult(0);
	}
	
	@RequestMapping(value="/setting/add", method=RequestMethod.POST)
	public APIResult addSetting(
			@RequestBody DeviceSetting[] setting,
			HttpServletRequest req, HttpServletResponse response) throws IOException{		
		
		AirResult result = deviceService.addDeviceSetting(setting);
		log.info(result.isCmdStat()+""+result.getMsg());
		log.info(result.getMsg());
		
		return new APIResult(result.isCmdStat()?0:1, result.getMsg());
	}
	
	@RequestMapping(value="/setting/del", method=RequestMethod.POST)
	public APIResult delSetting(
			@RequestBody DeviceSetting setting,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		deviceService.removeDeviceSetting(setting);
		
		return new APIResult(0);
	}
	
	@RequestMapping(value="/setting/get", method=RequestMethod.POST)
	public List<DeviceSetting> getSetting(
			@RequestBody DeviceSetting setting,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		return deviceService.getByDeviceNo(setting.getDeviceNo());
	}
}
