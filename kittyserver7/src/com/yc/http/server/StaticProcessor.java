package com.yc.http.server;

import com.yc.javax.servlet.ServletRequest;
import com.yc.javax.servlet.ServletResponse;

public class StaticProcessor implements Processor {

	@Override
	public void process(ServletRequest request, ServletResponse response) {
		((YcHttpServletResponse)response).sendRedirect();
		
	}
	
}
