package com.shicha.yzmgt.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class LoginAuthenticationDetails extends WebAuthenticationDetails{

	private static final long serialVersionUID = 1L;
	
	String others;

	public LoginAuthenticationDetails(HttpServletRequest request) {
		super(request);
		// TODO Auto-generated constructor stub
		others = request.getParameter("others");
	}
	
	public String getOthers() {
		return others;
	}

}
