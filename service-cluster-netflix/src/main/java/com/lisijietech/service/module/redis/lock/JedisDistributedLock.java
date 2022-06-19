package com.lisijietech.service.module.redis.lock;

import java.util.Collections;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

/**
 * redis分布式锁。
 * Jedis连接实现。
 * @author LiSiJie
 * @date 2022年6月3日 上午7:19:57
 */
public class JedisDistributedLock {
	private static final String SET_SUCCESS = "OK";
	private static final Long DEL_LOCK_SUCCESS = 1L;
	private static final String NX = "NX";
	private static final String PX = "PX";
	//以后优化脚本，加锁和解锁lua脚本，单独成脚本文件，从文件中读取脚本。
	private static final String SCRIPT_UNLOCK = 
			"if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
	
	/**
	 * 尝试获取分布式锁。简单实现。
	 * 
	 * @param connect 连接客户端
	 * @param key 锁key
	 * @param uuid 锁值，可以用请求标识requestId,订单号，流水号，版本号等唯一标识。
	 * @param expireTime 过期时间
	 * @return
	 */
	public static boolean tryGetDistributedLock(Jedis connect, String key, String uuid, int expireTime) {
		String result = connect.set(key, uuid, SetParams.setParams().nx().px(expireTime));
		if(SET_SUCCESS.equals(result)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 释放分布式锁。
	 * 用lua脚本保证redis操作原子性。redis本身执行操作是单一主线程，不会出现竞态条件。类似保证事务一样。
	 * 
	 * @param connect 连接客户端
	 * @param key 锁key
	 * @param uuid 锁值，可以用请求标识requestId,订单号，流水号，版本号等唯一标识。
	 * @return
	 */
	public static boolean releaseDistributedLock(Jedis connect, String key, String uuid) {
		Object result = connect.eval(SCRIPT_UNLOCK,Collections.singletonList(key),Collections.singletonList(uuid));
		if(DEL_LOCK_SUCCESS.equals(result)) {
			return true;
		}
		return false;
	}

}
