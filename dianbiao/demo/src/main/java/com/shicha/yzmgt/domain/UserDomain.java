package com.shicha.yzmgt.domain;

import com.shicha.yzmgt.bean.User;

public class UserDomain {

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	

	String name;
	String password;
	String role;
	String[]groupIds;
	
	public UserDomain() {
		super();
	}
	
	public void setUser(User user) {
		user.setName(name);
		user.setPassword(password);
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String[] getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String[] groupIds) {
		this.groupIds = groupIds;
	}
}
