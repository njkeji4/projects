package com.shicha.yzmgt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfi extends WebSecurityConfigurerAdapter implements WebMvcConfigurer{

	@Autowired
	LoginAuthenticationDetailsSource detailsSource;
	
	@Autowired
	LoginAuthenticationProvider authProvider;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.authenticationProvider(authProvider);		
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers(
					"/upload/**","/download/**",
					"/","/user/needlogin", "/user/logout",
					
					//for test,remove it when release
					//"/devicegroup/**","/user/list","/user/update","/device/**","/blacklist/**","/checkdata/**",
					//"/alarm/**",
					//"/advertisement/**",
					//"/version/**",
					//"/user/add",
					"/basestation/**",
					"/test/**","/getdata",
					 "/terminal/**",
					"/index.html", "/favico.ico", "/css/**", "/fonts/**", "/img/**", "/js/**"
					);			
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()
			.and()
			.logout()
				.logoutUrl("/user/logout")
				.permitAll()
			.and()
			.exceptionHandling()
				.authenticationEntryPoint(new CustomLoginUrlAuthenticationEntryPoint("/user/needlogin"))
			.and()
			.formLogin()
				.loginPage("/user/needlogin")     //if API need login, forward to this URL
				.loginProcessingUrl("/user/login")  //donothing, sping security framework call auth provider
				.usernameParameter("name")
				.passwordParameter("password")
				.authenticationDetailsSource(detailsSource)
				.successHandler(new LoginSuccessHandler())
				.failureHandler(new LoginFailHandler())
			.and()
			.csrf()
				.disable();
		
		//for application/json request
		CustomUsernamePasswordAuthenticationFilter filter = new CustomUsernamePasswordAuthenticationFilter();
		filter.setFilterProcessesUrl("/user/login");
		filter.setAuthenticationDetailsSource(detailsSource);
		filter.setAuthenticationManager(authenticationManagerBean());
		filter.setAuthenticationFailureHandler(new LoginFailHandler());
		filter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
		
		http.addFilterAt(filter, UsernamePasswordAuthenticationFilter.class);	
	}
	
}
