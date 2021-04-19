package com.lisijietech.rpc.api.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.lisijietech.rpc.api.common.CommonFeignInterface;

/**
 * 消费者的Feign接口定义。
 * 多个FeignClient接口使用相同服务解决方法，参考：
 * https://www.jianshu.com/p/23983f299e03，
 * https://www.jianshu.com/p/fd18df149335
 * 因为多个FeignClient都是有用的，不能用spring.main.allow-bean-definition-overriding配置覆盖bean，
 * spring.main.allow-bean-definition-overriding配置的作用：
 * https://blog.csdn.net/liubenlong007/article/details/87885567
 * @author LiSiJie
 * @date 2021年3月13日 上午8:31:08
 */
@FeignClient(name = "consumer")
public interface ConsumerFeign extends CommonFeignInterface {
	@GetMapping("/ribbon/rest-template/{app}")
	public String doRibbonRestTemplate(@PathVariable(value = "app",required = true) String app);
}
