package com.yc.http.server;

import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.yc.javax.servlet.Servlet;
import com.yc.javax.servlet.ServletRequest;
import com.yc.javax.servlet.ServletResponse;
import com.yc.javax.servlet.http.HttpServlet;
import com.yc.javax.servlet.http.HttpServletRequest;
import com.yc.javax.servlet.http.HttpServletResponse;

public class DynamicProcessor implements Processor {
	
	

	@Override   // request的  requestURI=> /res/HelloServlet.do
	public void process(ServletRequest request, ServletResponse response) {
		//1 取出uri, 从uri中取出请求的servlet名字
		String uri=((HttpServletRequest)request).getRequestURI();
		String servletName=uri.substring(    uri.lastIndexOf("/")+1  , uri.lastIndexOf("."));
		//2. 动态字节码加载     到  res/找servlet.class文件   
		//    URLClassLoader
		URL[] urls=new URL[ 1  ];
		try {
			urls[0]=new URL("file", null,  YcConstants.KITTYSERVER_BASEPATH    );  // ??
			URLClassLoader ucl=  new URLClassLoader(  urls)     ;
			// 3. URL地址  =>   file:\\\
			//4.  Class urlclassloader.loadClass(  类的名字  );
			Class c=ucl.loadClass(  servletName );
			//5.   以反射的形式  newInstance()创建  servlet实例. 
			Servlet servlet=(Servlet) c.newInstance();   //  -> 调用 构造方法
			// 6. 再以生命周期的方式 来调用servlet中的各方法
			if(   servlet!=null&&   servlet instanceof Servlet){
				//生命周期方法的调用 
				servlet.init();
				//父类引用只能调用子类重写了父类的方法而不能调用子类所特有的方法
				((HttpServlet)servlet).service( (HttpServletRequest)request, (HttpServletResponse)response);   
			}
		} catch (Exception e) {
			String bodyentity=e.toString();
			String protocal= gen500(   bodyentity.getBytes().length );
			PrintWriter pw=response.getWriter();
			pw.println(  protocal    );
			pw.println(  bodyentity  );
			pw.flush();
		} 
		
	}
	
	private String gen500(   long bodylength ){
		String protocal500="HTTP/1.1 500 Internal Server Error\r\nContent-Type: text/html;charset=utf-8\r\nContent-Length: "
					+ bodylength + "\r\n\r\n";
		return protocal500;
	}

}
