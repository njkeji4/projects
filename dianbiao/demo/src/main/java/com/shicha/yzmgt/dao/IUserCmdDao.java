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
import com.shicha.yzmgt.bean.UserCmd;


@Repository
public interface IUserCmdDao extends JpaRepository<UserCmd, String>,JpaSpecificationExecutor<UserCmd>{
	
	List<UserCmd> findByGroupName(String groupName);
}



