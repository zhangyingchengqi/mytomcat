package com.yc.javax.servlet.http;

import java.util.Map;

import com.yc.javax.servlet.Servlet;
import com.yc.javax.servlet.ServletRequest;
import com.yc.javax.servlet.ServletResponse;

public abstract class HttpServlet implements Servlet {

	@Override
	public void init() {
		
	}

	@Override
	public void destroy() {
		
	}
	
	
	protected void doGet( HttpServletRequest request, HttpServletResponse response){}
	
	protected void doPost( HttpServletRequest request, HttpServletResponse response){}
	
	protected void doHead( HttpServletRequest request, HttpServletResponse response){}
	
	protected void doDelete( HttpServletRequest request, HttpServletResponse response){}

	public void service(HttpServletRequest request, HttpServletResponse response) {
		//取出 request中的method
		String method=((HttpServletRequest)request).getMethod();
		//判断是get/post
		if(  "get".equalsIgnoreCase(method)){
			doGet(  (HttpServletRequest)request, (HttpServletResponse)response);
		}else if( "post".equalsIgnoreCase(method)){
			doPost(  (HttpServletRequest)request, (HttpServletResponse)response);
		}else if( "head".equalsIgnoreCase(method)){
			doHead(  (HttpServletRequest)request, (HttpServletResponse)response);
		}else if( "delete".equalsIgnoreCase(method)){
			doDelete(  (HttpServletRequest)request, (HttpServletResponse)response);
		}
		
		//调用  doGet/doPost
	}
	
	public void service( ServletRequest request, ServletResponse response  ){
		this.service((HttpServletRequest)request, (HttpServletResponse)response);
	}



}
