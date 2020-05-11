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
import com.shicha.yzmgt.bean.jifang.Room;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.AutoOnOff;
import com.shicha.yzmgt.domain.DeviceSettingDomain;
import com.shicha.yzmgt.domain.SearchDevice;
import com.shicha.yzmgt.service.AirCbService2;
import com.shicha.yzmgt.service.DeviceService;
import com.shicha.yzmgt.service.RoomService;

@RestController
@RequestMapping("/room")
public class RoomController {

	private static final Logger log = LoggerFactory.getLogger(RoomController.class);
	
	@Autowired
	RoomService roomService;		
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public APIResult search(
			@RequestBody Room room,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		Page<Room>rooms = roomService.searchDevice(room);
		
		return new APIResult(0, "", rooms);
	}
	
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public APIResult addDevice(
			@RequestBody Room room,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		if(roomService.nameExisted(room)) {
			return new APIResult(1, "name existed");
		}
		roomService.add(room);
		
		return new APIResult(0);
	}	
	
	@RequestMapping(value="/del", method=RequestMethod.POST)
	public APIResult delDevice(
			@RequestBody String[]ids,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		roomService.del(ids);
		
		return new APIResult(0);
	}
	
}
