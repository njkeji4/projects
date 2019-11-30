package com.shicha.yzmgt.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shicha.yzmgt.bean.DeviceSetting;

public interface IDevcieSettingDao extends JpaRepository<DeviceSetting, String>,JpaSpecificationExecutor<DeviceSetting>{
	
	List<DeviceSetting> findByDeviceNo(String deviceNo);
	
	
	@Transactional
	@Modifying
	@Query(value="delete from device_setting where device_no=:deviceNo",nativeQuery=true)
	void deleteByDeviceNo(@Param("deviceNo")String deviceNo);
	
}
