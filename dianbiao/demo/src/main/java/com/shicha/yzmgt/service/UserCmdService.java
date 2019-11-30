package com.shicha.yzmgt.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class UserCmdService {

	private static final Logger log = LoggerFactory.getLogger(UserCmdService.class);
	
	@Autowired
	IDeviceDao deviceDao;
	
	@Autowired
	IUserCmdDao userCmdDao;
	
	@Autowired
	AirCbService airService;
	
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
