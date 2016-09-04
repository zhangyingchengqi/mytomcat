package com.yc.javax.servlet.http;

import com.yc.javax.servlet.ServletResponse;

public interface HttpServletResponse extends ServletResponse{
	/**
	 * 输出重定向方法
	 */
	public void sendRedirect();
	
	public void addCookie(  Cookie cookie );
	
	public JspWriter getJspWriter();
	
	public Cookie[] getCookies();
}
