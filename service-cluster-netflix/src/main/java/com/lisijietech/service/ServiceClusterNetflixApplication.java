package com.lisijietech.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author LiSiJie
 * @date 2021年3月14日 下午12:35:06
 */
@SpringBootApplication
//注解@EnableEurekaClient与@EnableDiscoveryClient有一定区别，前者适合eureka注册中心，后者可与其他实现（zookeeper等）
//有些版本可以不加客户端注解，就能开启eureka注册中心客户端功能。但是需要有starter的客户端jar包依赖，会自动开启。
//自动开启和SpringCloud框架的启动过程，jar包结构约定规则有关。
@EnableDiscoveryClient
//feign接口不在starter同目录及其子目录下要指定目录。
//即使SpringBoot的扫描@ComponentScan包含了feign接口路径，但不识别feign注解标识，扫描feign的功能应该不是同一个。
@EnableFeignClients(basePackages = {"com.lisijietech.rpc.api","com.lisijietech.service.module.hystrix.way.feign"})
//启动断路器
@EnableCircuitBreaker
public class ServiceClusterNetflixApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceClusterNetflixApplication.class, args);
	}

}
