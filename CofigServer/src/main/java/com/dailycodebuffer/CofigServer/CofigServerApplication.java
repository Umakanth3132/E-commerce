package com.dailycodebuffer.CofigServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class CofigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CofigServerApplication.class, args);
	}

}
