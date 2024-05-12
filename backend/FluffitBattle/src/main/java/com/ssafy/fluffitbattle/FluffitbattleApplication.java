package com.ssafy.fluffitbattle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.ssafy.fluffitbattle.repository")
@EnableRedisRepositories(basePackages = "com.ssafy.fluffitbattle.redis")
@EnableRedisHttpSession
public class FluffitbattleApplication {

	public static void main(String[] args) {
		SpringApplication.run(FluffitbattleApplication.class, args);
	}

}
