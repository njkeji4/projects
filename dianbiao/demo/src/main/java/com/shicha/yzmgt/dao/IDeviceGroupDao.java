package com.shicha.yzmgt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.DeviceGroup;

@Repository
public interface IDeviceGroupDao extends JpaRepository<DeviceGroup, String>{
	
	DeviceGroup findByGroupName(String name);
	
	DeviceGroup findByGroupId(String id);

}
