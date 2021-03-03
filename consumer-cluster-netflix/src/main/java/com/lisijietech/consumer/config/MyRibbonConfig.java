package com.lisijietech.consumer.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;

/**
 * Ribbon全局配置。
 * 在SpringCloud的2020版本移除了大量Netflix组件，但也可以导入用于学习使用。
 * Eureka、Feign和Zuul中已集成Ribbon，也可以单独使用。
 * 配置方式有java配置，文件配置。
 * java配置方式，可以全局配置，也可以针对某种服务进行配置。参考：
 * https://www.cnblogs.com/idoljames/p/11698923.html，
 * https://blog.csdn.net/weixin_34008805/article/details/91420636
 * 单独使用Ribbon时，设置禁用eureka，并自己维护配置服务清单。参考：
 * https://www.jianshu.com/p/f86af82fa782，
 * https://www.jianshu.com/p/be57acb72d27
 * @author LiSiJie
 * @date 2021年3月1日 上午2:57:13
 */
@Configuration
//RibbonClient针对某一种服务的java注解形式的客户端配置.@RibbonClients则是针对多种服务
//@RibbonClient(name = "service-a",configuration = MyRibbonOneTypeConfig.class)
//@RibbonClients(value = {
//		@RibbonClient(name = "producer",configuration = MyRibbonOneTypeConfig.class),
//		@RibbonClient(name = "consumer",configuration = MyRibbonOneTypeConfig.class)
//		}
//)
public class MyRibbonConfig {
	/*
	 * 使用RestTemplate远程调用，并让其有负载均衡功能。
	 */
	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	/*
	 * 设置全局负载均衡规则。随机规则
	 */
	@Bean
	public IRule getRandomRule() {
		return new RandomRule();
	}

}
