package com.shicha.yzmgt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.shicha.yzmgt.bean.basestation.BaseAnalyzeResult;

public interface IBaseAnalyzeResultDao  extends JpaRepository<BaseAnalyzeResult, String>,JpaSpecificationExecutor<BaseAnalyzeResult>{

}
