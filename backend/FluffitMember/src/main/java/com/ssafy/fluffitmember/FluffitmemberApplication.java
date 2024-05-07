package com.ssafy.fluffitmember;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class  FluffitmemberApplication {
	public static void main(String[] args) {
		SpringApplication.run(FluffitmemberApplication.class, args);
	}
}
