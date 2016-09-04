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
			HttpServletRequest request=new HttpServletRequest( this.iis );  // uri 
			//包装一个HttpServletResponse对象
			HttpServletResponse response=new HttpServletResponse(   request,    this.oos);
			response.sendRedirect();
		}
		try {
			this.socket.close();
		} catch (IOException e) {
			YcConstants.logger.error(" failed to close connection to client  ",e);
		}
		return null;
	}

}
