package com.yc.javax.servlet;

import java.util.Map;

/**
 * applicationÈÝÆ÷
 */
public interface ServletContext {
	public Map<String,Servlet> getServlets();
	
	public Servlet getServlet( String servletName);
	
	public void setServlet( String key, Servlet servlet);
	
	public void setAttribute( String key, Object obj);
	
	public Object getAttribute( String key);
	
	
}
