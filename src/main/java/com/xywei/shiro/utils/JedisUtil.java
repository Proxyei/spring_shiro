package com.xywei.shiro.utils;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class JedisUtil {

	@Autowired
	private JedisPool jedisPool;

	private Jedis getResource() {
		return jedisPool.getResource();
	}

	private void closeJedis(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	/**
	 * 保持session
	 * 
	 * @param key
	 * @param value
	 */
	public void save(byte[] key, byte[] value) {

		Jedis jedis = getResource();
		jedis.set(key, value);
		closeJedis(jedis);
	}

	/**
	 * 设置session过期时间 TODO 过期时间是指一段时间time后不对系统进行操作，那么redis中
	 * session会被删除，假如一直操作页面，那么session也不会失效。
	 * 
	 * @param key
	 * @param time
	 */
	public void expire(byte[] key, int time) {

		Jedis jedis = getResource();
		jedis.expire(key, time);
		closeJedis(jedis);
	}

	/**
	 * 通过key返回session
	 * 
	 * @param key
	 * @return
	 */
	public byte[] get(byte[] key) {
		Jedis jedis = getResource();
		byte[] value = jedis.get(key);
		closeJedis(jedis);
		return value;
	}

	/**
	 * 删除session
	 * 
	 * @param key
	 */
	public void delete(byte[] key) {
		Jedis jedis = getResource();
		jedis.del(key);
		closeJedis(jedis);
	}

	public Set<byte[]> getAllKeys(String prefix) {

		Jedis jedis = getResource();
		String pattern = prefix + "*";
		Set<byte[]> keys = jedis.keys(pattern.getBytes());

		return keys;
	}

}
