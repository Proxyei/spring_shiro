package com.xywei.shiro.cache;

import java.util.Collection;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import com.xywei.shiro.utils.JedisUtil;

@Component
public class RedisCache<K, V> implements Cache<K, V> {

	private final String SHIRO_CACHE_PREFIX = "SHIRO_CACHE_";

	@Autowired
	private JedisUtil jedisUtil;

	private byte[] getKey(K k) {
		if (k instanceof String) {
			byte[] key = (SHIRO_CACHE_PREFIX + k).getBytes();
			return key;
		}
		return SerializationUtils.serialize(k);
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(K k) throws CacheException {

		System.err.println("================== 查询缓存 ====================");
		
		byte[] key = getKey(k);
		byte[] value = jedisUtil.get(key);

		if (null != value) {
			return (V) SerializationUtils.deserialize(value);
		}

		return null;
	}

	@Override
	public V put(K k, V v) throws CacheException {

		System.err.println("================== 添加缓存 ====================");
		byte[] key = getKey(k);
		byte[] value = SerializationUtils.serialize(v);
		jedisUtil.save(key, value);
		jedisUtil.expire(key, 600);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V remove(K k) throws CacheException {

		System.err.println("================== 删除缓存 ====================");
		byte[] key = getKey(k);
		byte[] value = jedisUtil.get(key);
		jedisUtil.delete(key);
		if (null != value) {
			return (V) SerializationUtils.deserialize(value);
		}
		return null;
	}

	@Override
	public void clear() throws CacheException {
		// TODO Auto-generated method stub

	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<K> keys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

}
