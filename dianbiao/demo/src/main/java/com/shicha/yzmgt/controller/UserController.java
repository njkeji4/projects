package com.shicha.yzmgt.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.ChangePassword;
import com.shicha.yzmgt.domain.UserDomain;
import com.shicha.yzmgt.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value="/needlogin", method=RequestMethod.GET)
	public String needLogin(HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		response.setStatus(403);		
		return "login first";
	}
	
	
	@RequestMapping(value="/logout", method=RequestMethod.POST)
	public APIResult logout(HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		req.getSession().invalidate();		
		return new APIResult(0, null, null);
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public APIResult addUser(
			@RequestBody UserDomain userDomain,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		if(userService.getUser(userDomain.getName()) != null) {
			return new APIResult(1, "用户名已存在");
		}
		
		User newUser = userService.addUser(userDomain);		

		return new APIResult(0);
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public APIResult delUser(
			@RequestBody String[] ids,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		userService.delUsers(ids);

		return new APIResult(0);
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public APIResult updateUser(
			@RequestBody User user,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		userService.updateUser(user);

		return new APIResult(0);
	}
	
	@RequestMapping(value="/unlock", method=RequestMethod.POST)
	public APIResult unLock(
			@RequestBody String[] names,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		
		if(userService.unLock(names))
			return new APIResult(0);
		else
			return new APIResult(1, "管理员登陆后解锁");
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public List<User> addUser(			
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		List<User> users= userService.getAllUser();
		
		return users;
	}
	
	@RequestMapping(value="/changepassword", method=RequestMethod.POST)
	public APIResult changePassword(
			@RequestBody ChangePassword domain,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		User user = userService.getUser(domain.getUsername());
		if(!user.getPassword().equals(domain.getPassword())) {
			return new APIResult(1, "当前密码不正确"); 
		}
		
		if(userService.changePassword(domain)) {
			return new APIResult(0);
		}
		
		return new APIResult(1, "修改密码不成功");	

	}
	
}
