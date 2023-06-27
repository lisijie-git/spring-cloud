package com.lisijietech.service.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
	//springboot2.0约定的配置整合redis，很方便快捷，不用这么麻烦
	//如：约定的配置属性名spring.redis.host和封装的redis配置类RedisProperties。
	//但是如果需要连接多个redis数据源，要自定义配置，方便学习。
	@Value("${my.redis.host:127.0.0.1}")
	private String host;
	
	@Value("${my.redis.port:6379}")
	private int port;
	
	@Value("${my.redis.password:}")
	private String password;
	
	@Value("${my.redis.timeout:3000}")
	private int timeout;
	
	@Value("${my.redis.jedis.pool.max-active:8}")
	private int maxActive;
	
	@Value("${my.redis.jedis.pool.max-idle:5}")
	private int maxIdle;
	
	@Value("${my.redis.jedis.pool.min-idle:0}")
	private int minIdle;
	
	/**
	 * javaconfig形式配置redis连接池
	 * @return
	 */
	@Bean
	@Scope("singleton")
	public JedisPool jedisPool() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setTestOnBorrow(true);//调用borrow Object方法时，是否进行有效性检查。待研究
		jedisPoolConfig.setTestOnReturn(true);//调用return Object方法时，是否进行有效性检查。待研究
		jedisPoolConfig.setMaxTotal(maxActive);
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMinIdle(minIdle);
		
		if(StringUtils.isBlank(password)) {
			password = null;
		}
		JedisPool jedisPool = new JedisPool(jedisPoolConfig,host,port,timeout,password);
		return jedisPool;
	}
}
