package com.lisijietech.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
//@EnableEurekaClient 与 @EnableDiscoveryClient有一定区别，前者适合eureka注册中心，后者可与其他实现（zookeeper等）
//新版本可以不加客户端注解，就能开启eureka注册中心客户端功能。但是需要有starter的客户端jar包依赖，会自动开启。
//自动开启和SpringCloud框架的启动过程，jar包结构约定规则有关。
public class ProducerClusterNetflixApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducerClusterNetflixApplication.class, args);
	}

}
