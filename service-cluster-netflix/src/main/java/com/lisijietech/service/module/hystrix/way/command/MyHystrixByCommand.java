package com.lisijietech.service.module.hystrix.way.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * 使用原生Hystrix。
 * 通过实现不同Command抽象类，可以有不同功能，如，同步执行，异步执行，响应式，等等。
 * 使用参考：
 * https://blog.csdn.net/qq_26440803/article/details/82664870
 * 
 * https://blog.csdn.net/chenxyz707/article/details/80913725
 * https://blog.csdn.net/qq_26440803/article/details/82664870
 * @author LiSiJie
 * @date 2021年3月29日 下午6:48:45
 */
public class MyHystrixByCommand extends HystrixCommand<String> {
	private int millisecond;

	public MyHystrixByCommand(int millisecond) {
		//只进行了简单配置。使用原生Hystrix时用java简单的配置，文件配置是比较复古的xml配置，比较繁琐。
		super(HystrixCommandGroupKey.Factory.asKey("myHystrixByCommand"));
		this.millisecond = millisecond;
	}

	@Override
	protected String run() throws Exception {
		if(millisecond > 0) {
			Thread.sleep(millisecond);
		}
		String s = "Hystrix的Command对象执行代码，成功。线程睡眠时间：" + millisecond;
		return s;
	}
	
	@Override
	protected String getFallback() {
		String s = "Hystrix的Command对象执行代码，失败，进行降级处理调用getFallback方法。线程睡眠时间："+ millisecond;
		return s;
	}
}
