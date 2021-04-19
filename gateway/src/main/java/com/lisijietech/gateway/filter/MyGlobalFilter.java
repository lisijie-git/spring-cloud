package com.lisijietech.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * 网关全局过滤器。
 * 参考：
 * https://www.ngui.cc/51cto/show-34038.html
 * @author LiSiJie
 * @date 2021年3月28日 上午3:47:23
 */
@Component
public class MyGlobalFilter implements GlobalFilter,Ordered{

	/*
	 * 优先级。
	 * 数字越大优先级越低
	 */
	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String filterStr = exchange.getRequest().getQueryParams().getFirst("param");
		if("fuck".equals(filterStr)) {
			System.out.println("口吐芬芳");
			exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
			return exchange.getResponse().setComplete();
		}
		return chain.filter(exchange);
	}

}
