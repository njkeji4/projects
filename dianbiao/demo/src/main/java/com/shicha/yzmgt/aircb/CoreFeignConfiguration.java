package com.shicha.yzmgt.aircb;

import javax.naming.spi.ObjectFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.HttpMessageConverter;

import feign.codec.Encoder;
import feign.form.FormEncoder;

public class CoreFeignConfiguration {

//	 @Autowired
//	  private ObjectFactory<HttpMessageConverter> messageConverters;
//
//	  @Bean
//	  @Primary	 
//	  Encoder feignFormEncoder() {
//	      new FormEncoder(new SpringEncoder(messageConverters));
//	  }
}
