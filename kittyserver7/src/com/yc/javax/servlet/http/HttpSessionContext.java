package com.yc.javax.servlet.http;

/**
 * session的容器：用来存所有的session
 * 它的子类是单例.
 *
 */
public interface HttpSessionContext {
	
	public HttpSession getSession( String sessinId);
	
	public void setSession( String sessionId, HttpSession session);
}
