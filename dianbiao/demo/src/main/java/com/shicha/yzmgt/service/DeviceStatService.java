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
import com.shicha.yzmgt.bean.DeviceStat;
import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.dao.IDevcieSettingDao;
import com.shicha.yzmgt.dao.IDeviceDao;
import com.shicha.yzmgt.dao.IDeviceStatDao;
import com.shicha.yzmgt.dao.IUserDao;
import com.shicha.yzmgt.domain.AutoOnOff;
import com.shicha.yzmgt.domain.DeviceSettingDomain;
import com.shicha.yzmgt.domain.SearchDevice;
import com.shicha.yzmgt.domain.SearchStat;

@Service
public class DeviceStatService {

	private static final Logger log = LoggerFactory.getLogger(DeviceStatService.class);
	
	@Autowired
	IDeviceStatDao stateDao;
	
	@Autowired
	IUserDao	userDao;
	
	
	public Page<DeviceStat> searchDevice(final SearchStat search) {
		
		String userName = null;
		try{
			userName = SecurityContextHolder.getContext().getAuthentication().getName();			
			if(userName == null)
				return null;
		}catch(Exception ex) {
			return null;
		}
			
		User user = userDao.findByName(userName);
		Direction orderBy  = (search.getOrder() == null || search.getOrder().equals("asc")) ? Direction.ASC : Direction.DESC;		
		String sort = search.getSort();		
		Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), Sort.by(orderBy, sort));	
		
		return stateDao.findAll(new Specification<DeviceStat>() {

			@Override
			public Predicate toPredicate(Root<DeviceStat> root, CriteriaQuery<?> criteria, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<>();
				
				if(user != null && !user.getRole().equals(User.ROLE_ADMIN)) {						
					predicatesList.add(builder.and(
							builder.equal(root.get("groupName"), user.getGroupName())
							));
				}
				
				if(search.getDeviceName() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("deviceName"), "%" + search.getDeviceName() + "%")
							));
				}
				if(search.getDeviceNo() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("deviceNo"), "%" + search.getDeviceNo() + "%")
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
	
}
