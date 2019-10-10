package com.shicha.yzmgt.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.domain.APIResult;

public class LoginSuccessHandler implements AuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication auth) throws IOException, ServletException {
		
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		User user = new User();
		user.setName((String)auth.getPrincipal());
		
		request.getSession().setAttribute("userName", user.getName());

		Iterator<? extends GrantedAuthority> it  = auth.getAuthorities().iterator();
		while(it.hasNext()) {
			String role = it.next().getAuthority();
			user.setRole(role);
			break;
		}				
		
		APIResult result = new APIResult(0, null, user);
		
		ObjectMapper mapper = new ObjectMapper();
		
		String json = mapper.writeValueAsString(result);
		out.write(json);
		out.flush();
		out.close();
	}

}
