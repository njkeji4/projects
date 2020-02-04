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
import com.shicha.yzmgt.bean.DeviceStat;


@Repository
public interface IDeviceStatDao extends JpaRepository<DeviceStat, String>,JpaSpecificationExecutor<DeviceStat>{
	
	
	
	@Transactional
	@Modifying
	@Query(value="select new com.shicha.yzmgt.bean.DeviceStat(sum(energy),sum(ontime),month) from device_stat where groupName=:groupName and month >= :fromdate group by month")
	List<DeviceStat> last12Month( @Param("groupName") String groupName, @Param("fromdate") long fromdate);
	
	
	@Transactional
	@Modifying
	@Query(value="select new com.shicha.yzmgt.bean.DeviceStat(sum(energy),sum(ontime),month) from device_stat where month >= :fromdate group by month")
	List<DeviceStat> last12Month(@Param("fromdate") long fromdate);
	
	@Transactional
	@Modifying
	@Query(value="select new com.shicha.yzmgt.bean.DeviceStat(sum(energy),sum(ontime),statDate) from device_stat where month =:month group by statDate  order by stat_date")
	List<DeviceStat> getStatByMonth(@Param("month") long month);
	
	@Transactional
	@Modifying
	@Query(value="select new com.shicha.yzmgt.bean.DeviceStat(sum(energy),sum(ontime),statDate) from device_stat where groupName=:groupName and month =:month group by statDate  order by stat_date")
	List<DeviceStat> getStatByMonth(@Param("groupName") String groupName, @Param("month") long month);
}



