package com.shicha.yzmgt.aircb;

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
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.DeviceSettingDomain;
import com.shicha.yzmgt.domain.SearchDevice;
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
	
	@RequestMapping(value="/searchDevice", method=RequestMethod.POST)
	public APIResult searchDevices(
			@RequestBody SearchDevice search,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		Page<Device>devices = deviceService.searchDevice(search);
		
		return new APIResult(0, "", devices);
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
			@RequestBody String[]ids,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		User user= deviceService.getCurrentUser();
		String userName = user == null?null:user.getName();
		String groupName = user == null?null:user.getGroupName();
		for(String addr : ids)
			airService.switchOff(addr,userName,groupName);
		
		return new APIResult(0,"命令已经发送");
	}
	
	@RequestMapping(value="/on", method=RequestMethod.POST)
	public APIResult onDevice(
			@RequestBody String[] ids,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		User user= deviceService.getCurrentUser();
		String userName = user == null?null:user.getName();
		String groupName = user == null?null:user.getGroupName();
		for(String addr : ids)
			airService.switchOn(addr,userName,groupName);
		
		return new APIResult(0,"命令已经发送");
	}
	
	@RequestMapping(value="/del", method=RequestMethod.POST)
	public APIResult delDevice(
			@RequestBody String[]ids,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		
		deviceService.delDevice(ids);
		
		return new APIResult(0);
	}
	
	@RequestMapping(value="/setting/add", method=RequestMethod.POST)
	public APIResult addSetting(
			@RequestBody DeviceSettingDomain settingDomain,
			HttpServletRequest req, HttpServletResponse response) throws IOException{		
		
		deviceService.addDeviceSetting(settingDomain);
		
		return new APIResult(0,"命令已经发送");
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
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public APIResult uploadBlacklist(
			@RequestParam("uploadFile") MultipartFile file,	
			HttpServletRequest req, HttpServletResponse response) throws IOException{		
		
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			
			boolean result = deviceService.importDeviceFromFile(file, userName);
			
			if(result)
				return new APIResult(0);
			else
				return new APIResult(1, "导入文件失败");
		
	}
}
