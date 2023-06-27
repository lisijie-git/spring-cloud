package com.lisijietech.config.apollo.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 通过Spring的方式从Apollo获取的配置数据。
 * 新版本的apollo客户端，通过@Value注解直接注入使用是实时刷新从apollo服务获取的配置数据的。
 * 但通过@ConfigurationProperties,需要通过配置达到实时刷新。
 * 当然，@Component的bean声明可以改为java config的配置方式，用@Configuration注解，在配置类统一声明创建bean。
 * @author LiSiJie
 * @date 2021年8月9日 下午4:38:42
 */
@Component
public class ApolloValueBean {
	@Value("${name}")
	private String name;
	@Value("${age}")
	private String age;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	
}
