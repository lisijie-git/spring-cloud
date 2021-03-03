package com.lisijietech.producer.module.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 使用Ribbon的Controller层。
 * 有eureka客户端，默认是配合eureka使用，不需要自己维护服务列表。
 * @author LiSiJie
 * @date 2021年3月1日 上午7:50:12
 */
@RestController
public class RibbonRestTemplateController {
	@Autowired
	private RestTemplate restTemplate;
	
//	private LoadBalancerClient loadBalancerClient
	
	@Value("${spring.application.name}")
	private String appName;
	
	@Value("${server.port}")
	private String port;
	
	@GetMapping(value = "/result")
	public String getResult() {
		return new StringBuilder(appName).append(",").append(port).toString();
	}
	
	/**
	 * Ribbon负载均衡，使用RestTempate远程连接
	 * @param app
	 * @return
	 */
	@GetMapping("/rest-template/{app}")
	public String doRibbonRestTemplate(@PathVariable(value = "app",required = true) String app) {
		//Ribbon会把服务名字app转换成实际要调用的服务的host和port。
		return restTemplate.getForObject("http://" + app + "/result", String.class);
	}

}
