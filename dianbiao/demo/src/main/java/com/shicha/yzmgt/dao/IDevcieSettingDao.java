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
	
	//List<DeviceSetting> findByDeviceNo(String deviceNo);
	
	List<DeviceSetting> findByDeviceNoAndBranch(String deviceNo, int branch);	
	

	@Transactional
	@Modifying
	@Query(value="delete from device_setting where device_no=:deviceNo and branch=:branch",nativeQuery=true)
	void deleteByDeviceNoAndBranch(@Param("deviceNo")String deviceNo, @Param("branch")int branch);
	
}
