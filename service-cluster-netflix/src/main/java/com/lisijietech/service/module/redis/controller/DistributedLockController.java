package com.lisijietech.service.module.redis.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lisijietech.service.module.redis.lock.JedisDistributedLock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis分布式锁。
 * @author LiSiJie
 * @date 2022年6月3日 上午9:37:08
 */
@RestController
public class DistributedLockController {
	public static int NUM = 0;
	
	@Autowired
	private JedisPool jedisPool;
	
	/**
	 * 多线程并行操作测试。
	 * 多次结果，有几率会得出不满10000，会在9998左右，看计算机性能。
	 * @return
	 */
	@GetMapping(value = "/redis/test/parallel")
	public String parallel() {
		NUM = 0;
		for(int i = 0;i < 10000;i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					++NUM;
					System.out.println(NUM);
				}
			}).start();
		}
		return "ok";
	}
	
	/**
	 * 分布式锁测试
	 * @return
	 */
	@GetMapping(value = "/redis/test/distributed")
	public String DistributedLock() {
		NUM = 0;
		for(int i = 0;i < 10000;i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Jedis jedis = jedisPool.getResource();
					String key = "lock";
					String uuid = System.currentTimeMillis() + "";
					boolean lock = JedisDistributedLock.tryGetDistributedLock(jedis, key, uuid, 2000);
					while(!lock) {
						lock = JedisDistributedLock.tryGetDistributedLock(jedis, key, uuid, 2000);
					}
					++NUM;
					System.out.println(NUM);
					JedisDistributedLock.releaseDistributedLock(jedis, key, uuid);
					jedis.close();
				}
			}).start();
		}
		return "ok";
	}

}
