package com.shicha.yzmgt.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.DeviceStat;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.OverviewData;
import com.shicha.yzmgt.domain.SearchDevice;
import com.shicha.yzmgt.domain.SearchStat;
import com.shicha.yzmgt.service.DeviceStatService;

@RestController
@RequestMapping("/devicestat")
public class DeviceStatController {

	private static final Logger log = LoggerFactory.getLogger(DeviceStatController.class);
	
	@Autowired
	DeviceStatService statService;
	
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public APIResult searchDevices(
			@RequestBody SearchStat search,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		Page<DeviceStat>stat = statService.searchDevice(search);
		
		return new APIResult(0, "", stat);
	}
	
	@RequestMapping(value="/overview", method=RequestMethod.POST)
	public APIResult overview(			
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		OverviewData overview = statService.getOverview();
		
		return new APIResult(0, "", overview);
	}
	
	@RequestMapping(value="/overview/monthdays/{yearmonth}", method=RequestMethod.GET)
	public APIResult overviewMonthDays(
			@PathVariable(name="yearmonth") long yearmonth,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		OverviewData overview = statService.getOverviewMonthDays(yearmonth);
		
		return new APIResult(0, "", overview);
	}
	
}
