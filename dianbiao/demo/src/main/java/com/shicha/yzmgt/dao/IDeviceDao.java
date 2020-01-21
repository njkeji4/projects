package com.shicha.yzmgt.dao;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.shicha.yzmgt.bean.Device;


@Repository
public interface IDeviceDao extends JpaRepository<Device, String>,JpaSpecificationExecutor<Device>{
	
	Device findByDeviceName(String name);
	
	List<Device> findByStatus(Integer status);
	
	List<Device> findByCmdIdIsNotNull ();
	
	Device findByDeviceNo(String deviceNo);
	
	List<Device> findByGroupName(String name);
	
	
	@Query(value="select count(*) from device where status=:status and group_name=:groupName",nativeQuery=true)
	int getCountByStatus(@Param("status") int status, @Param("groupName") String groupName);
	
	
	@Query(value="select count(*) from device where status = 0 and switch_stat=:stat and group_name=:groupName",nativeQuery=true)
	int getCountByStat(@Param("stat") int stat, @Param("groupName") String groupName);
	
	
	@Query(value="select count(*) from device where status=:status",nativeQuery=true)
	int getCountByStatus(@Param("status") int status);
	
	
	@Query(value="select count(*) from device where status = 0 and switch_stat=:stat",nativeQuery=true)
	int getCountByStat(@Param("stat") int stat);
	
	@Query(value="select count(*) from device where status = 0 and switch_stat != 0",nativeQuery=true)
	int getOffDeviceCount();
	
	@Query(value="select count(*) from device where status = 0 and switch_stat != 0 and group_name=:groupName",nativeQuery=true)
	int getOffDeviceCount(@Param("groupName") String groupName);
	
	
	
	@Query(value="select * from device order by action_energy desc limit 10;",nativeQuery=true)
	List<Device> top10Devices();
	
	
	@Query(value="select * from device where group_name=:groupName order by action_energy desc limit 10;",nativeQuery=true)
	List<Device> top10Devices( @Param("groupName") String groupName);	
}



