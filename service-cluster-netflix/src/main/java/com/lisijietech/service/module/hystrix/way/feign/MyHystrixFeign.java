package com.lisijietech.service.module.hystrix.way.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Hystrix与SpringCloud的OpenFeign集成。
 * contextId参数属性是因为依赖的rpc-api包中已经定义了name = producer的feign客户端bean，bean重复会报错，
 * 用contextId属性来解决。
 * 老版本的SpringCloud中，feign默认集成了Hystrix，开启后，可通过fallback配置实现类使用，
 * 或者fallbackFactory来实现,fallbackFactory可以得到异常对象，这样能得到异常自己处理，记录日志。
 * Netflix的Hystrix不太维护了，新版SpringCloud中有其他自我保护断路器框架替代。
 * 参考：
 * https://www.jb51.net/article/202137.htm#_label7
 * https://chenwei.blog.csdn.net/article/details/85041077
 * https://blog.csdn.net/chenxyz707/article/details/80913725
 * @author LiSiJie
 * @date 2021年4月9日 上午4:25:51
 */
@FeignClient(name = "producer",contextId = "myHystrix",fallback = MyHystrixFeignFallBack.class)
public interface MyHystrixFeign {
	@GetMapping("/hystrix/timeout/{millisecond}")
	public String doTimeout(@PathVariable(value = "millisecond",required = true) int millisecond);
}
