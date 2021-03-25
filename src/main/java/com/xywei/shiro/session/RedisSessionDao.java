package com.xywei.shiro.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SerializationUtils;

import com.xywei.shiro.utils.JedisUtil;

public class RedisSessionDao extends AbstractSessionDAO {

	@Autowired
	private JedisUtil jedisUtil;

	private String SHIRO_SESSION_PREFIX = "shiro_session_";

	/**
	 * 统一前缀，方便查询所有的session
	 * 
	 * @param sessionId
	 * @return
	 */
	private byte[] getKey(String sessionId) {

		if (null == sessionId) {
			return null;
		}

		return (SHIRO_SESSION_PREFIX + sessionId).getBytes();
	}

	private void save(Session session) {

		if (session != null && null != session.getId()) {
			// 为什么要是字节，不是字符串
			byte[] key = getKey(session.getId().toString());
			byte[] value = SerializationUtils.serialize(session);
			jedisUtil.save(key, value);
			jedisUtil.expire(key, 120);
		}

	}

	// TODO session过期抛出异常问题
	@Override
	public void update(Session session) throws UnknownSessionException {

		System.out.println("===update session===");
		if (null != session && null != session.getId()) {
			save(session);
		}
	}

	@Override
	public void delete(Session session) {

		System.out.println("===delete session===");
		if (session != null && null != session.getId()) {

			byte[] key = getKey(session.getId().toString());
			jedisUtil.delete(key);
		}
	}

	@Override
	public Collection<Session> getActiveSessions() {

		System.out.println("===get all session===");
		Set<Session> sessions = new HashSet<>();

		Set<byte[]> keys = jedisUtil.getAllKeys(SHIRO_SESSION_PREFIX);

		if (CollectionUtils.isEmpty(keys)) {
			return null;

		} else {
			for (byte[] key : keys) {
				byte[] value = jedisUtil.get(key);
				Session session = (Session) SerializationUtils.deserialize(value);
				sessions.add(session);
			}
		}
		return sessions;
	}

	/**
	 * 使用当前session创建一个session
	 */
	@Override
	protected Serializable doCreate(Session session) {

		// sessionId和key--session.getId()有没有关系？应该是一样的只是不同形式表现
		// sessionId是为了方便保持
		Serializable sessionId = generateSessionId(session);
		// why?
		assignSessionId(session, sessionId);
		System.out.println("doCreate sessionId==" + sessionId);
		save(session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {

		System.out.println("doReadSession sessionId Serializable==" + sessionId);
		if (null == sessionId) {
			return null;
		}
		byte[] key = getKey(sessionId.toString());
		byte[] value = jedisUtil.get(key);

		Session session = (Session) SerializationUtils.deserialize(value);

		System.out.println("doReadSession sesion==" + session);
		return session;
	}

}
