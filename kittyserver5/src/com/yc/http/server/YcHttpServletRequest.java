package com.yc.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.yc.javax.servlet.ServletContext;
import com.yc.javax.servlet.http.HttpServletRequest;

public class YcHttpServletRequest implements HttpServletRequest  {
	private String method;
	private String protocal;
	private String serverName;
	private int serverPort;
	private String requestURI; // 资源的地址
	private String requestURL;
	private String contextPath;
	private String realPath = System.getProperty("user.dir") + "\\webapps";

	private InputStream iis;
	private String queryString;

	public YcHttpServletRequest(InputStream iis) {
		this.iis = iis;
		parse();
	}

	public String getRealPath() {
		return realPath;
	}

	public void parse() {
		String requestInfoString = readFromInputStream(); // 从输入流中读取请求头
		if(   requestInfoString==null|| "".equals(requestInfoString)){
			return;
		}
		// 解析requestInfo字符串
		parseRequestInfoString(requestInfoString);

	}

	private void parseRequestInfoString(String requestInfoString) {
		StringTokenizer st = new StringTokenizer(requestInfoString);
		if (st.hasMoreTokens()) {
			this.method = st.nextToken();
			this.requestURI = st.nextToken();   //    /res/aaaa/index.html
			this.protocal = st.nextToken();
			this.contextPath=   "/"+     this.requestURI.split("/")[1];   //    contextPath应用上下文路径  => /res
		}
		// TODO: 后面暂时不管，再加
		parseParameter(  requestInfoString);
	}
	
	private void parseParameter(   String protocal){
		//1 判断是否有 ?,有则要取?前面当做  requestURI
		//以下解决了地址栏后面的参数解析问题
		if(  requestURI.indexOf("?")>=0 ){    //     res/HelloServlet.do?name=a&
			
			this.queryString=  requestURI.substring(     requestURI.indexOf("?")+1);
			this.requestURI=   requestURI.substring(0,   requestURI.indexOf("?") );
			//从queryString中解析出参数   ?name=a&pwd=a
			String[] ss=this.queryString.split("&");
			if( ss!=null&&ss.length>0){
				for( String s: ss ){
					String[] paire=s.split("=");
					if(   paire!=null&&paire.length>0){
						this.parameters.put(paire[0], paire[1]);
					}
				}
			}
		}
		if(   this.method.equals("POST")){
			//post实体中的参数
			String ps=protocal.substring(   protocal.indexOf("\r\n\r\n")+4  );
			if(  ps!=null&& ps.length()>0){
				String[] ss=ps.split("&");
				if( ss!=null&&ss.length>0){
					for( String s: ss ){
						String[] paire=s.split("=");
						this.parameters.put(paire[0], paire[1]);
					}
				}
			}
		}
	}
	
	public String getQueryString(){
		return this.queryString;
	}

	private String readFromInputStream() {
		// 1. 从input中读出所有的内容( http请求协议 =》 protocal)
		String protocal = null;
		// TODO: 从流中取protocal
		StringBuffer sb = new StringBuffer(1024 * 10);  // 10K
		int length = -1;
		byte[] bs = new byte[1024 * 10];
		try {
			length = this.iis.read(bs);
		} catch (IOException e) {
			e.printStackTrace();
			length = -1;
		}
		for (int j = 0; j < length; j++) {
			sb.append((char) bs[j]);
		}
		protocal = sb.toString();
		return protocal;
	}

	public String getMethod() {
		return method;
	}

	public String getProtocal() {
		return protocal;
	}

	public String getServerName() {
		return serverName;
	}

	public int getServerPort() {
		return serverPort;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public String getRequestURL() {
		return requestURL;
	}

	public String getContextPath() {
		return contextPath;
	}

	private Map<String,Object> attributes=new HashMap<String,Object>();
	
	@Override
	public Object getAttribute(String key) {
		
		return attributes.get(key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}
	
	private Map<String,String> parameters=new HashMap<String,String>();

	@Override
	public String getParameter(String key) {
		return parameters.get(key);
	}

	@Override
	public Map<String, String> getParameterMap() {
		return this.parameters;
	}

	@Override
	public ServletContext getServletContext() {
		return YcServletContext.getInstance();
	}

	

	

}
