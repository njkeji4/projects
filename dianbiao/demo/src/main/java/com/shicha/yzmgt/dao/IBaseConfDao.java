package com.shicha.yzmgt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.shicha.yzmgt.bean.basestation.BaseConf;


public interface IBaseConfDao  extends JpaRepository<BaseConf, String>,JpaSpecificationExecutor<BaseConf>{

}
