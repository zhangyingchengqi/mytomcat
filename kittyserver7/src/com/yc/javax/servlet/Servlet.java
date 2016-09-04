package com.yc.javax.servlet;

/**
 * Servlet接口用来定义生命周期的回调方法
 * @author lenovo
 *
 */
public interface Servlet {

	
	public void init();
	
	public void destroy();
	
	public void service( ServletRequest request, ServletResponse response  );
	
}
