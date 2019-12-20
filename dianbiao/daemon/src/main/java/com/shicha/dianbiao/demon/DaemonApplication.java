package com.shicha.dianbiao.demon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class DaemonApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaemonApplication.class, args);
	}

}
