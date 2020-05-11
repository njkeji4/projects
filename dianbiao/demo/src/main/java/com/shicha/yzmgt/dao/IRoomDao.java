package com.shicha.yzmgt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.jifang.Room;

@Repository
public interface IRoomDao extends JpaRepository<Room, String>,JpaSpecificationExecutor<Room>{

	Room findByName(String name);
}
