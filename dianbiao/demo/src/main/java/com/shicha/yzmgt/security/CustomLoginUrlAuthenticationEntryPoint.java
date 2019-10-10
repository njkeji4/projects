package com.shicha.yzmgt.security;

import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
/**
 * 
 * as default, then entry point will forward
 * 443 ===> 80, 8443 ==>8080
 *
 *setUseForward  will change the default behavior
 */
public class CustomLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint{

	public CustomLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);		
		this.setUseForward(true);
	}

	
}
