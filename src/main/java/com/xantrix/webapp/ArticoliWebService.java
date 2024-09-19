package com.xantrix.webapp;

import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ArticoliWebService {
	public static void main(String[] args) {
		SpringApplication.run(ArticoliWebService.class, args);
	}
}
