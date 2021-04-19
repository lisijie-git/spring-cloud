package com.lisijietech.service.config;

import org.springframework.context.annotation.Bean;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;

/**
 * Ribbon针对某种服务的java配置。
 * 网上参考需要在此类加@Configuration和@Bean注解，
 * 但测试可以不加注解@Configuration，也能在MyRibbonAnnotationConfig中注解指定类获取配置规则。
 * 而且加了@Configuration注解反而需要通过配置@ComponentScan注解排除此类的扫描。
 * 但是必须在方法上加@Bean注解。
 * 网上参考https://blog.csdn.net/u013087513/article/details/79775306。
 * 猜想：
 * 可能在类上加@Configuration注解是全局配置，和Spring框架有关。
 * 但在后期觉得既然在@RibbonClient注解上指明了针对服务细粒度的配置的类，完全可以用反射等原理获取配置，
 * 但必须要在方法上有@Bean注解，可能是需要指明哪些是有效的配置方法，或者是用到了Spring的处理@bean的功能。
 * 还有针对某些服务的细粒度的java方式的配置，如果和java方式的全局配置有配置有重复，会报错springbean不唯一。
 * 那就可以说明加@Bean是因为用到Spring框架的功能去处理@Bean，并没有单独管理针对服务的细粒度的配置Spring bean。
 * 具体是什么原因需要看源码，目前只是大胆猜测下。怎么学源码，以后有机会研究。
 * @author LiSiJie
 * @date 2021年3月1日 上午7:22:34
 */
//@Configuration
public class MyRibbonOneTypeConfig {
	/**
	 * 随机规则
	 * @return
	 */
	@Bean
	public IRule getRoundRobinRule() {
		return new RandomRule();
	}
}
