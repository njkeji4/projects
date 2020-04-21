package com.shicha.yzmgt.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shicha.yzmgt.bean.basestation.BaseConf;
import com.shicha.yzmgt.bean.basestation.BaseStation;
import com.shicha.yzmgt.dao.IBaseConfDao;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.SearchStation;
import com.shicha.yzmgt.service.BaseStationService;

@RestController
@RequestMapping("/baseconf")
public class BaseConfController {

	private static final Logger log = LoggerFactory.getLogger(BaseConfController.class);
	
	@Autowired
	IBaseConfDao	baseConfDao;	
		
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	public APIResult getall(			
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		BaseConf conf = new BaseConf();
		
		List<BaseConf> confList =  baseConfDao.findAll();
		if(confList == null || confList.size() == 0) {
			baseConfDao.save(conf);
		}else {
			conf = confList.get(0);
		}
		
		return new APIResult(0, "", conf);
	}
	
	@RequestMapping(value="/set", method=RequestMethod.POST)
	public APIResult searchDevices(
			@RequestBody BaseConf conf,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		baseConfDao.save(conf);
		
		return new APIResult(0);
	}
}
