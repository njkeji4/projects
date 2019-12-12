package com.shicha.yzmgt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

import com.shicha.yzmgt.aircb.AirResult;
import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.DeviceSetting;
import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.bean.UserCmd;
import com.shicha.yzmgt.dao.IDevcieSettingDao;
import com.shicha.yzmgt.dao.IDeviceDao;
import com.shicha.yzmgt.dao.IUserCmdDao;
import com.shicha.yzmgt.dao.IUserDao;
import com.shicha.yzmgt.domain.SeachUserCmd;
import com.shicha.yzmgt.domain.SearchDevice;

@Service
public class UserCmdService {

	private static final Logger log = LoggerFactory.getLogger(UserCmdService.class);
	
	@Autowired
	IDeviceDao deviceDao;
	
	@Autowired
	IUserCmdDao userCmdDao;
	
	@Autowired
	IUserDao	userDao;
	
	@Autowired
	IDevcieSettingDao deviceSettingDao;
	
	
	public String newCmd(UserCmd cmd) {
		
		UserCmd result = userCmdDao.save(cmd);
		
		return result.getId();
	}
	
	public void updateCmdStatus(String id, int status, String retMessage, String retContent) {
		Optional<UserCmd> op = userCmdDao.findById(id);
		if(op.isPresent()) {
			UserCmd one = op.get();
			
			one.setStatus(status);
			one.setRetMessage(retMessage);
			one.setRetValue(retContent);
			one.setRetTime(System.currentTimeMillis());
		
			
			userCmdDao.save(one);
		}
		
	}
	
	public List<UserCmd> getAll(){
		User user=getCurrentUser();
		if(user == null)
			return null;
		
		if(user.getRole().equals(User.ROLE_ADMIN))
			return userCmdDao.findAll();
			
		return userCmdDao.findByGroupName(user.getGroupName());
	}
	
	public Page<UserCmd> searchCmd(final SeachUserCmd search) {
		
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
		
		return userCmdDao.findAll(new Specification<UserCmd>() {

			@Override
			public Predicate toPredicate(Root<UserCmd> root, CriteriaQuery<?> criteria, CriteriaBuilder builder) {
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
				
				if(search.getCmdName() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("deviceNo"), "%" + search.getCmdName() + "%")
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
