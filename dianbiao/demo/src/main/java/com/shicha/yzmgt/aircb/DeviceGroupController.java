package com.shicha.yzmgt.aircb;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.DeviceGroup;

import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.SearchDeviceGroup;
import com.shicha.yzmgt.service.DeviceGroupService;

import com.shicha.yzmgt.util.Util;

@RestController
@RequestMapping("/devicegroup")
public class DeviceGroupController {

	private static final Logger log = LoggerFactory.getLogger(DeviceGroupController.class);
	
	
	@Autowired
	DeviceGroupService groupService;
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public APIResult addGroup(
			@RequestBody DeviceGroup group,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		if(groupService.getByName(group.getGroupName()) != null) {
			return new APIResult(1, "组名字已经存在");
		}
		group.setGroupId(Util.UUID());
		groupService.addGroup(group);

		return new APIResult(0);
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public APIResult updateGroup(
			@RequestBody DeviceGroup group,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		if(groupService.getByName(group.getGroupName()) != null) {
			return new APIResult(1, "组名字已经存在");
		}
		
		if(groupService.updateGroup(group)) {
			return new APIResult(0);
		}
		return new APIResult(1, "更新失败");
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public APIResult delGroup(
			@RequestBody String[] ids,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		if(groupService.removeGroups(ids)) {
			return new APIResult(0);
		}
		
		return new APIResult(1, "组已被用户关联");
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public APIResult getGroups(
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		List<DeviceGroup>groups = groupService.searchDeviceGroup();
		
		return new APIResult(0, "", groups);
	}
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public APIResult getGroups(		
			@RequestBody SearchDeviceGroup search,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		Page<DeviceGroup>groups = groupService.searchDeviceGroup(search);
		
		return new APIResult(0, "", groups);
	}
}
