package com.shicha.yzmgt.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import com.shicha.yzmgt.bean.DeviceSetting;

public interface IDevcieSettingDao extends JpaRepository<DeviceSetting, String>,JpaSpecificationExecutor<DeviceSetting>{
	
	List<DeviceSetting> findByDeviceNo(String deviceNo);
	
}
