package com.yc.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import threadpool.Taskable;

public class TaskService implements Taskable {
	private Socket socket;
	private InputStream iis;
	private OutputStream oos;
	private boolean flag;

	public TaskService(Socket socket) {
		this.socket = socket;
		try {
			this.iis = this.socket.getInputStream();
			this.oos = this.socket.getOutputStream();
			flag = true;
		} catch (IOException e) {
			YcConstants.logger.error(" failed to get stream  ",e);
			flag=false;
			throw new RuntimeException ( e);
		}
	}

	@Override   // HTTP协议是一次请求和响应.  http是无状态(一次请求和响应就会断开联接..
	public Object doTask() {
		if( flag){
			//包装一个HttpServletRequest对象
			YcHttpServletRequest request=new YcHttpServletRequest( this.iis );  // uri 
			//包装一个HttpServletResponse对象
			YcHttpServletResponse response=new YcHttpServletResponse(   request,    this.oos);
			//response.sendRedirect();
			//判断是静态资源还是动态资源
			Processor processor=null;
			if(  request.getRequestURI().endsWith(".do")){
				processor=new DynamicProcessor();
			}else{
				processor=new StaticProcessor();
			}
			processor.process(request, response);
		}
		try {
			this.socket.close();
		} catch (IOException e) {
			YcConstants.logger.error(" failed to close connection to client  ",e);
		}
		return null;
	}

}
