package com.lisijietech.service.module.hystrix.way.annotation;

import org.springframework.stereotype.Component;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * Hystrix与SpringBoot集成，用注解方式使用，原理是Hystrix通过AOP和注解帮我们处理好了。
 * 配置方式有多种，注解配置，文件配置，java配置。和其他与SpringBoot的集成框架配置无差别。
 * 注解@HystrixCommand(defaultFallback = "getDefaultFallBack")完全可以在Controller层的@RequestMapping方法上使用，
 * 因为相当于对方法进行AOP处理。
 * 使用参考：
 * https://blog.csdn.net/chenxyz707/article/details/80913725
 * https://www.jb51.net/article/202137.htm#_label7
 * https://blog.csdn.net/itjavaee/article/details/109067798
 * @author LiSiJie
 * @date 2021年3月29日 下午6:48:45
 */
@Component
public class MyHystrixByAopAnnotation {
	@HystrixCommand(defaultFallback = "getDefaultFallBack")
	public String doHystrixAopAnnotation(int millisecond) {
		if(millisecond > 0) {
			try {
				Thread.sleep(millisecond);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String s = "Hystrix注解@HystrixCommand方式，成功。";
		return s;
	}
	
	public String getDefaultFallBack() {
		return "使用Hystrix的注解@HystrixCommand，失败，进行降级处理。";
	}
}
