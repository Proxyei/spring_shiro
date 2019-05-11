package com.xywei.shiro.session;

import java.io.Serializable;

import javax.servlet.ServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

/**
 * 自定义session管理，用于缓存session，减轻每次都从redis中读取sessiond的压力
 * 
 * @author wodoo
 *
 */
public class MyCustomSessionManager extends DefaultWebSessionManager {

	/**
	 * 解决方案就是把session放入request中
	 */
	@Override
	protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {

		// 用于存入request的key
		Serializable sessionId = getSessionId(sessionKey);
		ServletRequest request = null;
		Session session = null;

		// 首次缓存
		if (sessionKey instanceof WebSessionKey) {
			// 用错了一个方法导致系统崩溃
			// request = ((WebSessionKey) getSession(sessionKey)).getServletRequest();
			request = ((WebSessionKey) sessionKey).getServletRequest();
		}
		// 直接从request中拿出session
		if (null != request && null != sessionId) {
			session = (Session) request.getAttribute(sessionId.toString());
			if (null != session) {
				return session;
			}
		}
		// 否则，如果是首次请求
		session = super.retrieveSession(sessionKey);
		if (null != request && null != sessionId) {
			request.setAttribute(sessionId.toString(), session);
		}

		return session;
	}

	/*
	 * @Override protected Session retrieveSession(SessionKey sessionKey) throws
	 * UnknownSessionException { Serializable sessionId = getSessionId(sessionKey);
	 * ServletRequest request = null; if (sessionKey instanceof WebSessionKey) {
	 * request = ((WebSessionKey) sessionKey).getServletRequest(); } if (request !=
	 * null && sessionId != null) { Session session = (Session)
	 * request.getAttribute(sessionId.toString()); if (session != null) { return
	 * session; } } Session session = super.retrieveSession(sessionKey); if (request
	 * != null && sessionId != null) { request.setAttribute(sessionId.toString(),
	 * session); } return session; }
	 */

}
