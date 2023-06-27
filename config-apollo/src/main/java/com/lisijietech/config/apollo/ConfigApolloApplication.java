package com.lisijietech.config.apollo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;

@SpringBootApplication
@EnableApolloConfig
public class ConfigApolloApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigApolloApplication.class, args);
	}

}
