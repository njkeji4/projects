package com.shicha.yzmgt.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shicha.yzmgt.aircb.TestClient;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shicha.yzmgt.aircb.AirResult;
import com.shicha.yzmgt.aircb.CbCommand;
import com.shicha.yzmgt.aircb.IAirCB;
import com.shicha.yzmgt.aircb.MeterPeriod;
import com.shicha.yzmgt.aircb.MeterStatus;

@Service
public class AirCbService {

	private static final Logger log = LoggerFactory.getLogger(TestClient.class);
	
	@Autowired
	IAirCB aircb;
	
	public AirResult getDeviceStatus(String addr) {
		CbCommand command = new CbCommand(1, addr);
		AirResult result = aircb.getData(command);
		
		if(result.getData() == null) {
			log.info("device is offline");
			return result;
		}
		
		Object o = result.getData();
		
		ObjectMapper objectMapper = new ObjectMapper();		
		MeterStatus status = objectMapper.convertValue(o, MeterStatus.class);
		log.info(status.getActionEnergy()+"");
		
		
		return result;
	}
	
	public AirResult switchOff(String addr) {
		CbCommand command = new CbCommand(2, addr);
		AirResult result = aircb.getData(command);
		
		return result;
	}
	
	public AirResult switchOn(String addr) {
		CbCommand command = new CbCommand(3, addr);
		AirResult result = aircb.getData(command);
		
		return result;
	}
	
	public AirResult getPullTime(String addr) {
		CbCommand command = new CbCommand(4, addr);
		AirResult result = aircb.getData(command);
		
		if(result.getData() == null) {
			log.info("device is offline");
			return result;
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		
		if(result.getData() instanceof  List) {
			List list = (List)result.getData();
			if(list.size() > 0) {
				Object o = list.get(0);
				MeterPeriod p = objectMapper.convertValue(o, MeterPeriod.class);
				log.info(p.getHH() + "");
			}
		}
		
		return result;
	}
	
	public AirResult readThresh(String addr) {
		CbCommand command = new CbCommand(5, addr);
		AirResult result = aircb.getData(command);
		
		return result;
	}
	
	public AirResult readPeriod(String addr) {
		CbCommand command = new CbCommand(6, addr);
		AirResult result = aircb.getData(command);
		
		return result;
	}
	
	public AirResult setThresh(String addr, double value) {
		CbCommand command = new CbCommand(7, addr, value);
		AirResult result = aircb.getData(command);
		
		return result;
	}
	
	public AirResult setPeriod(String addr, int value) {
		CbCommand command = new CbCommand(8, addr, value);
		AirResult result = aircb.getData(command);
		
		return result;
	}
	
	private String getHHMMSS(long v) {
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(v);
		
		SimpleDateFormat sdf=new SimpleDateFormat("HHmmss");
		String result = sdf.format(c.getTime());
		
		//String result = c.get(Calendar.HOUR_OF_DAY)+""+c.get(Calendar.MINUTE)+""+c.get(Calendar.SECOND);
		
		System.out.println(result);
		return result;//c.get(Calendar.HOUR_OF_DAY)+""+c.get(Calendar.MINUTE)+""+c.get(Calendar.SECOND);
	}
	
	
	public AirResult setPullUpDownPeriod(String addr, long[] value) {
		
		log.info("value.length="+value.length);
		if(value.length > 8) {
			AirResult result= new AirResult(9);
			result.setCmdStat(false);
			result.setMsg("value length >8" + value.length);
			return result;
		}
		//String date= "010100020200030300040400050500060600070700080800";
		String date="";
		for(int i = 0; i < value.length; i++) {
			date+=this.getHHMMSS(value[i]);
		}
		for(int i = value.length; i < 8; i++)
			date += "000000";
		
		System.out.println("date="+date);
			
		
		CbCommand command = new CbCommand(9, addr, date);
		AirResult result = aircb.getData(command);
		
		return result;
	}
}
