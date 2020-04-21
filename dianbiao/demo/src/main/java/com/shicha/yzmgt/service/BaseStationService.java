package com.shicha.yzmgt.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.DeviceGroup;
import com.shicha.yzmgt.bean.DeviceSetting;
import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.bean.basestation.BaseAnalyzeResult;
import com.shicha.yzmgt.bean.basestation.BaseConf;
import com.shicha.yzmgt.bean.basestation.BaseFile;
import com.shicha.yzmgt.bean.basestation.BaseStation;
import com.shicha.yzmgt.dao.IBaseAnalyzeResultDao;
import com.shicha.yzmgt.dao.IBaseConfDao;
import com.shicha.yzmgt.dao.IBaseStationDao;
import com.shicha.yzmgt.dao.IDevcieSettingDao;
import com.shicha.yzmgt.dao.IDeviceDao;
import com.shicha.yzmgt.dao.IUserDao;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.AutoOnOff;
import com.shicha.yzmgt.domain.DeviceSettingDomain;
import com.shicha.yzmgt.domain.SearchDevice;
import com.shicha.yzmgt.domain.SearchStation;

@Service
public class BaseStationService {

	private static final Logger log = LoggerFactory.getLogger(BaseStationService.class);
	
	@Autowired
	IBaseStationDao baseStationDao;
	
	@Autowired
	IBaseConfDao baseConfDao;
	
	@Autowired
	IBaseAnalyzeResultDao analyzeResultDao;
	
	@Autowired
	IDeviceDao deviceDao;
	
	@Autowired
	IUserDao	userDao;
	
	@Autowired
	UserCmdService cmdService;
	
	@Autowired
	AirCbService2 airService;
	
	@Autowired
	IDevcieSettingDao deviceSettingDao;
	
	public List<BaseStation> getAll(){		
		try{
			return baseStationDao.findAll();			
		}catch(Exception ex) {
			return null;
		}
	}
	
	public Page<BaseStation> searchDevice(final SearchStation device) {			
		
		Direction orderBy  = (device.getOrder() == null || device.getOrder().equals("asc")) ? Direction.ASC : Direction.DESC;		
		String sort = device.getSort();		
		Pageable pageable = PageRequest.of(device.getPage(), device.getSize(), Sort.by(orderBy, sort));	
		
		return baseStationDao.findAll(new Specification<BaseStation>() {

			@Override
			public Predicate toPredicate(Root<BaseStation> root, CriteriaQuery<?> criteria, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<>();
				
				if(device.getName() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("name"), "%" + device.getName() + "%")
							));
				}				
				
				return builder.and(
	                    predicatesList.toArray(new Predicate[predicatesList.size()]));
			}}, pageable);
	}
	
	public User getCurrentUser() {
		String userName = null;
		try{
			userName = SecurityContextHolder.getContext().getAuthentication().getName();			
			if(userName == null)
				return null;
			
			User user = userDao.findByName(userName);
			return user;
		}catch(Exception ex) {
			return null;
		}
	}
	
	public boolean deviceNameExisted(Device d) {
		
		return (deviceDao.findByDeviceName(d.getDeviceName()) != null);
	}
	
	public void addDevice(Device d) {
		User user= getCurrentUser();		
		if(user!=null && !user.getRole().equals(User.ROLE_ADMIN)) {			
			d.setGroupName(user.getGroupName());
		}
		d.setLastHeartBeatTime(0l);
		if(deviceDao.findByDeviceName(d.getDeviceName()) == null) {
			deviceDao.save(d);
		}
	}
	
	public void delDevice(String[] ids) {
		for(String id : ids)
			deviceDao.deleteById(id);
	}
	
	
	//device setting
	public List<DeviceSetting> getByDeviceNo(String deviceNo){
		
		return deviceSettingDao.findByDeviceNo(deviceNo);
	}
	
	private int getbcdTime(long t) {
		if(t == 0) {
			return 0x999999;
		}
		Calendar d= Calendar.getInstance();
		d.setTimeInMillis(t);
		int h = d.get(Calendar.HOUR_OF_DAY);
		int m = d.get(Calendar.MINUTE);
		
		return ((h << 8) | m) << 8 ;
		
	}
	public APIResult addDeviceSetting(DeviceSettingDomain settingDomain) {
		String[]ids = settingDomain.getIds();
		DeviceSetting[]settings = settingDomain.getSettings();
		
		for(String id : ids)
			deviceSettingDao.deleteByDeviceNo(id);	
		
		int[] value = new int[settings.length * 2];
		int idx = 0;
		for(DeviceSetting setting : settings) {		
			
			value[idx++] = getbcdTime(setting.getOffTime());
			value[idx++] = getbcdTime(setting.getOnTime());
			
			for(String id : ids) {
				Device d = deviceDao.findByDeviceNo(id);
				if(d == null)continue;
				DeviceSetting ds=new DeviceSetting();
				ds.setDeviceName(d.getDeviceName());
				ds.setDeviceNo(d.getDeviceNo());
				ds.setOffTime(setting.getOffTime());
				ds.setOnTime(setting.getOnTime());
				deviceSettingDao.save(ds);
			}
		}
		
		User user= getCurrentUser();
		String userName = user == null?null:user.getName();
		String groupName = user == null?null:user.getGroupName();
		
		
		for(String id : ids) {
			airService.setAutoOffOn(new AutoOnOff(id,  value), userName, groupName);
		}		
		return new APIResult(0);
	}
	
	public void removeDeviceSetting(DeviceSetting setting) {
		
		deviceSettingDao.deleteById(setting.getId());
	}
	
	
	
	public boolean imporBaseDataFromFile(MultipartFile file) {
		
		String fname = file.getOriginalFilename();
		if(fname.length() <= 3) {
			return false;
		}	
		
		Workbook wb = null;		
		String last3str = fname.substring(fname.length() - 3);
		try {
			
			boolean is2003 = true;
			if(last3str.equals("xls")) {	//<=excel2003
				wb = new HSSFWorkbook(file.getInputStream());
			}else {	//excel 2007
				wb = new XSSFWorkbook(file.getInputStream());
				is2003 = false;
			}
			
			long start = System.currentTimeMillis();
			
			List<BaseStation>stations= new ArrayList<BaseStation>();
			for(int sc = 0; sc < 4; sc++) {
				
				Sheet sheet = wb.getSheetAt(sc);				
				int lastRowNum = sheet.getLastRowNum();
				
				for(int i = 1; i <= lastRowNum; i++) {
					
					try {
						Row row = sheet.getRow(i);	
						Date date = row.getCell(0).getDateCellValue();
						String name = row.getCell(1).getStringCellValue();
						
						BaseStation station = null;
						if(sc == 0) {							
							station = new BaseStation(name, date.getTime());
							stations.add(station);
						}else {
							station = stations.get(i - 1);
						}
						
						double[] value = new double[24];
						for(int j = 0; j < 24; j++) {
							value[j] = row.getCell(2+j).getNumericCellValue();
						}
						station.addData(value, sc);
						
//						if(sc == 3) {							
//							station.analyze(); //分析每个基站自己有多少时间段满足条件
//						}
					}catch(Exception ex) {
						//ex.printStackTrace();
						break;
					}
				}
				
			}
			
			wb.close();				
			
			//new BaseFile().parse(stations);		
			
			baseStationDao.saveAll(stations);
			
			return true;
			
		}catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}		
	}
	
	public APIResult analyzeBaseStation(SearchStation base) {		
		
		List<BaseStation>stations= baseStationDao.findAll();
		if(stations == null || stations.size() == 0) {
			return new APIResult(0);
		}
		
		List<BaseConf> list = baseConfDao.findAll();		
		BaseConf conf = new BaseConf();
		if(list != null && list.size() > 0 )
			conf = list.get(0);
		
		for(BaseStation bs : stations) {
			bs.analyze(conf);
		}
			
		List<BaseAnalyzeResult> results= new BaseFile().parse(stations,conf);
		
		results = removeRepeatedResult(results);
		
		if(results != null && results.size() > 0) {
			
			analyzeResultDao.deleteAll();
			
			analyzeResultDao.saveAll(results);
		}		
		
		return new APIResult(0);
	}
	
	public List<BaseAnalyzeResult> getAllAnalyzeResult(){
		return analyzeResultDao.findAll(Sort.by(Direction.ASC, "name"));
	}
	
	public List<BaseAnalyzeResult> removeRepeatedResult(List<BaseAnalyzeResult> results) {
		if(results == null || results.size() == 0) {
			return null;
		}
		List<BaseAnalyzeResult> newresult = new ArrayList<BaseAnalyzeResult>();
		
		for(BaseAnalyzeResult tmp : results) {
			boolean added = true;
			
			for(BaseAnalyzeResult t : newresult) {
				
				//parent
				if(t.getName().equals(tmp.getName()) && (tmp.getStart() <= t.getStart() && tmp.getEnd() >= t.getEnd())){
					t.setStart(tmp.getStart());
					t.setEnd(tmp.getEnd());
					
					added = false;
					
					break;
				}
				
				//child
				if(t.getName().equals(tmp.getName()) && (tmp.getStart() >= t.getStart() && tmp.getEnd() <= t.getEnd())){
					t.setStart(tmp.getStart());
					t.setEnd(tmp.getEnd());
					
					added = false;
					
					break;
				}
				
			}
			if(added) {
				newresult.add(tmp);
			}
		}
		
		return newresult;		
	}
}
