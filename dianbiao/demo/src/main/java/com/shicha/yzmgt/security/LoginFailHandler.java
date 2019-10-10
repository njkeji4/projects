package com.shicha.yzmgt.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shicha.yzmgt.bean.User;

public class LoginFailHandler implements AuthenticationFailureHandler{

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json);charset=utf-8");
		
		PrintWriter out = response.getWriter();
		
		String msg = exception.getMessage();
		
		int status = User.USER_STATUS_UNKNOWN;
		if(exception instanceof AccoutExceptionWithStatus)
			status = ((AccoutExceptionWithStatus)exception).getStatus();
		
		
		out.write("{\"status\":"+status+",\"msg\":\""+ msg + "\"}");
		out.flush();
		out.close();
	}

}
