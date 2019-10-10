package com.shicha.yzmgt.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.domain.UserWithoutPwd;

public interface IUserDao extends JpaRepository<User, String>{

	public User findByNameAndPassword(String name, String password);
	
	public User findByName(String name);
	
	public List<User> findByRole(String role);
	
}
