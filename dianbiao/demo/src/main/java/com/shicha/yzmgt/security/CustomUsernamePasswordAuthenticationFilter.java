package com.shicha.yzmgt.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shicha.yzmgt.domain.UserDomain;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		
		if(request.getContentType().indexOf(MediaType.APPLICATION_JSON_VALUE) >= 0) {
			
			ObjectMapper mapper = new ObjectMapper();
			UsernamePasswordAuthenticationToken authRequest = null;
			java.io.InputStream is = null;
			try {
				is = request.getInputStream();
				UserDomain userDomain = mapper.readValue(is, UserDomain.class);
				authRequest = new UsernamePasswordAuthenticationToken(userDomain.getName(),userDomain.getPassword());
			}catch(Exception ex) {
				authRequest = new UsernamePasswordAuthenticationToken("","");
			}finally {
				try{is.close();}catch(Exception e) {}
			}
			setDetails(request, authRequest);
			return this.getAuthenticationManager().authenticate(authRequest);
			
		}else {
			return super.attemptAuthentication(request, response);
		}
		
	}
}
