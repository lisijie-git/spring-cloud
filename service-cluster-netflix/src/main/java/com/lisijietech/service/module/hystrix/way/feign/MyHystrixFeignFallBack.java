package com.lisijietech.service.module.hystrix.way.feign;

import org.springframework.stereotype.Component;

/**
 * Feign的fallback实现。
 * @author LiSiJie
 * @date 2021年4月11日 上午7:00:06
 */
@Component
public class MyHystrixFeignFallBack implements MyHystrixFeign {

	@Override
	public String doTimeout(int millisecond) {
		return "使用Feign集成Hystrix，远程调用超时，失败，进行降级处理。线程休眠时间：" + millisecond;
	}

}
