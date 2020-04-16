package com.shicha.yzmgt.service;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.shicha.yzmgt.dao.IDevcieSettingDao;
import com.shicha.yzmgt.dao.IDeviceDao;
import com.shicha.yzmgt.dao.IUserDao;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.AutoOnOff;
import com.shicha.yzmgt.domain.DeviceSettingDomain;
import com.shicha.yzmgt.domain.SearchDevice;

@Service
public class DeviceService {

	private static final Logger log = LoggerFactory.getLogger(DeviceService.class);
	
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
	
	public List<Device> getAll(){
		
		String userName = null;
		try{
			userName = SecurityContextHolder.getContext().getAuthentication().getName();			
			if(userName == null)
				return null;
			
			User user = userDao.findByName(userName);
			if(user.getRole().equals(User.ROLE_ADMIN)) {
				return deviceDao.findAll();
			}
			
			return deviceDao.findByGroupName(user.getGroupName());//.findAll();
			
		}catch(Exception ex) {
			return null;
		}
	}
	
	public Page<Device> searchDevice(final SearchDevice device) {
		
		String userName = null;
		try{
			userName = SecurityContextHolder.getContext().getAuthentication().getName();			
			if(userName == null)
				return null;
		}catch(Exception ex) {
			return null;
		}
			
		User user = userDao.findByName(userName);
		Direction orderBy  = (device.getOrder() == null || device.getOrder().equals("asc")) ? Direction.ASC : Direction.DESC;		
		String sort = device.getSort();		
		Pageable pageable = PageRequest.of(device.getPage(), device.getSize(), Sort.by(orderBy, sort));	
		
		return deviceDao.findAll(new Specification<Device>() {

			@Override
			public Predicate toPredicate(Root<Device> root, CriteriaQuery<?> criteria, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<>();
				
				if(user != null && !user.getRole().equals(User.ROLE_ADMIN)) {						
					predicatesList.add(builder.and(
							builder.equal(root.get("groupName"), user.getGroupName())
							));
				}
				
				if(device.getDeviceName() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("deviceName"), "%" + device.getDeviceName() + "%")
							));
				}
				if(device.getDeviceNo() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("deviceNo"), "%" + device.getDeviceNo() + "%")
							));
				}
				
				if(device.getStatus() != null) {
					predicatesList.add(builder.and(
							builder.equal(root.get("status"), device.getStatus())
							));
				}				
				if(device.getSwitchStat() != null) {
					predicatesList.add(builder.and(
							builder.equal(root.get("switchStat"), device.getSwitchStat())
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
		
		APIResult result = null;
		for(String id : ids) {
			result = airService.setAutoOffOn(new AutoOnOff(id,  value), userName, groupName);
		}		
		return result;
	}
	
	public void removeDeviceSetting(DeviceSetting setting) {
		
		deviceSettingDao.deleteById(setting.getId());
	}
	
	public boolean importDeviceFromFile(MultipartFile file, String userName) {
		
		String fname = file.getOriginalFilename();
		if(fname.length() <= 3) {
			return false;
		}
		
		User user = this.getCurrentUser();
		if(user == null) {
			log.info("user is null");
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
			Sheet sheet = wb.getSheetAt(0);
			int lastRowNum = sheet.getLastRowNum();
//			if(is2003) {
//				lastRowNum++;
//			}
			
			log.info("lastrownumber:"+lastRowNum);
			
			List<Device> bl = new ArrayList<Device>();
			
			for(int i = 1; i <= lastRowNum; i++) {
				Row row = sheet.getRow(i);
				
				String name = row.getCell(0).getStringCellValue();
				if(name == null || name.length() == 0) {
					break;
				}
				String cardNo = row.getCell(1).getStringCellValue();
					
				Device d = new Device();
				d.setDeviceName(name);
				d.setDeviceNo(cardNo);
				d.setGroupName(user.getGroupName());
				
				bl.add(d);
			}
			
			wb.close();
			
			deviceDao.saveAll(bl);
			
			return true;
			
		}catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}		
	}
}
