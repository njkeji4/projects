package com.shicha.yzmgt.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.DeviceGroup;
import com.shicha.yzmgt.dao.IDeviceDao;
import com.shicha.yzmgt.dao.IDeviceGroupDao;
import com.shicha.yzmgt.domain.SearchDeviceGroup;

@Service
public class DeviceGroupService {

	private static final Logger log = LoggerFactory.getLogger(DeviceGroupService.class);
	
	
	@Autowired
	IDeviceGroupDao groupDao;
	
	@Autowired
	IDeviceDao deviceDao;
	
	public void addGroup(DeviceGroup group) {
		
		group.setCreateTime(System.currentTimeMillis());
		groupDao.save(group);
	}
	
	public boolean updateGroup(DeviceGroup group) {
		
		Optional<DeviceGroup> g = groupDao.findById(group.getGroupId());
		if(!g.isPresent()) {
			return false;
		}
		g.get().setGroupName(group.getGroupName());
		
		groupDao.save(g.get());
		
		return true;
	}
	
	public DeviceGroup getByName(String name) {
		return groupDao.findByGroupName(name);
	}
	
	public boolean removeGroups(String[] ids) {
		try {
			for(String id : ids) {
				groupDao.deleteById(id);
			}
			return true;
		}catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public List<DeviceGroup> searchDeviceGroup(){
		List<DeviceGroup>result = groupDao.findAll();
		return result;
	}
	
	public Page<DeviceGroup> searchDeviceGroup(SearchDeviceGroup search){
		
		Direction orderBy  = (search.getOrder() == null || search.getOrder().equals("asc")) ? Direction.ASC : Direction.DESC;		
		Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), Sort.by(orderBy, search.getSort()));	
		
		DeviceGroup deviceGroup = new DeviceGroup();
		deviceGroup.setGroupName(search.getGroupName());
		
		
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues()
									.withStringMatcher(StringMatcher.CONTAINING);								
		
		Example<DeviceGroup> example = Example.of(deviceGroup, matcher);
		
		return groupDao.findAll(example, pageable);
	}
}
