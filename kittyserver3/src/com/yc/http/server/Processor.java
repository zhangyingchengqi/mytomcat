package com.yc.http.server;

import com.yc.javax.servlet.ServletRequest;
import com.yc.javax.servlet.ServletResponse;

/**
 * 资源处理器: 处理静态或动态的资源 
 */
public interface Processor {
	
	/**
	 * 处理资源(动，静) 的方法，
	 * @param request:请求对象:   解析请求头,得到  uri, method( http), parse, parameter
	 * @param response:响应对象    输出.
	 */
	public void process(  ServletRequest request,   ServletResponse response     );
}
