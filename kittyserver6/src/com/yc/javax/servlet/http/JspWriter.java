package com.yc.javax.servlet.http;

import java.io.OutputStream;
import java.io.PrintWriter;

import com.yc.javax.servlet.ServletResponse;

/**
 * 功能: 1. 从response对象中取出  cookie数组
 *       2. 拼接响应头
 *       3. 输出响应头及内容
 *
 */
public class JspWriter extends PrintWriter{   // java.io.PrintWriter
	private ServletResponse response;

	public JspWriter(OutputStream out) {
		super(out);
	}
	
	public JspWriter(   OutputStream out, ServletResponse response){
		super( out);
		this.response=response;
	}
	
	public void println( String content){
		//协议的拼装...
		long length=content.getBytes().length;
		StringBuffer sb=new StringBuffer();
		
		String contentType="text/html";
		String protocal200 = "HTTP/1.0 200 OK\r\nContent-Type: "+contentType+"\r\nContent-Length: "
				+ length +"\r\n";
		sb.append(   protocal200 );
		
		//拼接cookie
		Cookie[] cs=((HttpServletResponse)this.response).getCookies();
		if( cs!=null&&cs.length>0){
			sb.append("Set-Cookie: ");
			for(  Cookie c:cs){
				sb.append(   c.toString() );
			}
		}
		sb.append("\r\n\r\n");
		sb.append(   content);
		
		super.println(  sb.toString()  );
		super.flush();
		
	}

}
