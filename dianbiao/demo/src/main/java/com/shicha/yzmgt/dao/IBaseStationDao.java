package com.shicha.yzmgt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.shicha.yzmgt.bean.basestation.BaseStation;

public interface IBaseStationDao extends JpaRepository<BaseStation, String>,JpaSpecificationExecutor<BaseStation>{

}
