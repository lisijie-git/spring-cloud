package com.lisijietech.service.module.hystrix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.lisijietech.service.module.hystrix.way.annotation.MyHystrixByAopAnnotation;
import com.lisijietech.service.module.hystrix.way.command.MyHystrixByCommand;
import com.lisijietech.service.module.hystrix.way.feign.MyHystrixFeign;

/**
 * 使用Hystrix的controller层。
 * Hystrix的监控仪表盘模块和仪表盘聚合就了解，不进行使用了。
 * Hystrix会有新的框架代替，了解其原理核心才有意义。
 * 测试Hystrix的配置为默认配置，测试用不配置，都是默认配置，
 * 如断路器超时时间默认为1秒，低于ribbon的超时时间但不影响测试学习，实际开发中要注意这些。
 * 使用参考：
 * https://blog.csdn.net/doctor_who2004/article/details/105475843
 * https://blog.csdn.net/chenxyz707/article/details/80913725
 * 
 * 异常处理，主要是针对feign，通过Hystrix获取异常然后传递给降级方法的方式自定义处理，或者配置feign打印日志。
 * 不然无论是开发还是生产，都会被Hystrix隐藏了异常，直接进行降级断路等处理。
 * https://blog.csdn.net/lixiaxin200319/article/details/81089898
 * https://blog.csdn.net/justlpf/article/details/84343845
 * https://blog.csdn.net/qq_37143673/article/details/91952817
 * https://chenwei.blog.csdn.net/article/details/85041077
 * @author LiSiJie
 * @date 2021年3月29日 下午6:06:25
 */
@RestController
public class HystrixController {
	@Value("${spring.application.name}")
	private String appName;
	
	@Value("${server.port}")
	private String port;
	
	@Autowired
	private MyHystrixByAopAnnotation myHystrixByAopAnnotation;
	
	@Autowired
	private MyHystrixFeign myHystrixFeign;
	
	public String getServiceInfo() {
		return new StringBuilder(appName).append(",").append(port).append("    ").toString();
	}
	
	/**
	 * 原生Hystrix使用。
	 * @param millisecond
	 * @return
	 */
	@GetMapping(value = "/hystrix/test/command/{millisecond}")
	public String doHystrixCommand(@PathVariable(name = "millisecond") int millisecond) {
		String s = new MyHystrixByCommand(millisecond).execute();
		return getServiceInfo() + s;
	}
	
	/**
	 * Hystrix与SpringBoot集成，用注解方式使用，原理是Hystrix通过AOP和注解帮我们处理好了。
	 * 配置方式有多种，注解配置，文件配置，java配置。和其他与SpringBoot的集成框架配置无差别。
	 * 注解@HystrixCommand(defaultFallback = "getDefaultFallBack")完全可以在Controller层的@RequestMapping方法上使用，
	 * 因为相当于对方法进行AOP处理。
	 * @param millisecond
	 * @return
	 */
	@GetMapping(value = "/hystrix/test/annotation/{millisecond}")
	public String doHystrixAopAnnotation(@PathVariable(name = "millisecond") int millisecond) {
		String s = myHystrixByAopAnnotation.doHystrixAopAnnotation(millisecond);
		return s + "线程睡眠时间：" + millisecond;
	}
	
	/**
	 * Hystrix与SpringCloud的OpenFeign集成。
	 * 
	 * @param millisecond
	 * @return
	 */
	@GetMapping(value = "/hystrix/test/feign/{millisecond}")
	public String doHystrixFeign(@PathVariable(name = "millisecond") int millisecond) {
		//这里失败降级如果要获取feign远程调用的host地址和端口，需要通过fallbackFactory方法实现来获取异常信息。
		return myHystrixFeign.doTimeout(millisecond);
	}
	
	//模拟远程调用响应超时的接口。
	@GetMapping(value = "/hystrix/timeout/{millisecond}")
	public String doTimeout(@PathVariable(name = "millisecond") int millisecond) {
		if(millisecond > 0) {
			try {
				Thread.sleep(millisecond);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String s = getServiceInfo() + "模拟响应超时，并未超时，成功响应。线程休眠时间：" + millisecond;
		return s;
	}

}
