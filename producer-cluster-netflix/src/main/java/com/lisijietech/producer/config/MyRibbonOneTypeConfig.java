package com.lisijietech.producer.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;

/**
 * Ribbon针对某种服务的配置。
 * @author LiSiJie
 * @date 2021年3月1日 上午7:22:34
 */
public class MyRibbonOneTypeConfig {
	/*
	 * 轮询规则
	 */
	public IRule getRoundRobinRule() {
		return new RoundRobinRule();
	}
}
