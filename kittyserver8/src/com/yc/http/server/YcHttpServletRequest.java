package com.yc.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.yc.javax.servlet.ServletContext;
import com.yc.javax.servlet.http.HttpServletRequest;
import com.yc.javax.servlet.http.HttpSession;

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
	
	//所有的头信息
	private Map<String,String> headers=new HashMap<String,String>();
	private String sessionid;
	
	@Override
	public String getHeader(String name) {
		String value=null;
		if(  headers.containsKey(  name)){
			value=headers.get(name);
		}
		return value;
	}

	public String getSessionid() {
		return this.sessionid;
	}

	@Override
	public HttpSession getSession() {
		HttpSession session=null;
		//从headers中取出  Cookie头
		//从Cookie头中取   kittysessionid     Cookie: kittysessionid=xxxx
		String cookievalue= headers.get("Cookie");
		if(  cookievalue!=null&&cookievalue.length()>0){
			String[] cookies=cookievalue.split("; ");
			for(  String s: cookies){
				String[] cookie=s.split("=");
				if(   cookie[0].equals(   YcConstants.SESSIONID)){
					this.sessionid=cookie[1];
					break;
				}
			}
		}
		if(    sessionid!=null &&!"".equals(sessionid)){
			//再从 YcHttpSessionContext中根据sessionid取session
			 session=YcHttpSessionContext.getInstance().getSession(   sessionid);
		}
		//如果没有，则创建一个session,同时指定它的id,创建时间 ,再存到  YcHttpSessionContext 中
		 if(   session==null){
			 session=new YcHttpSession();
			 this.sessionid=session.getId();
			 YcHttpSessionContext.getInstance().setSession(session.getId(), session);
		 }
		//有，就取出来. 
		return session;
	}
	
	private void parseRequestInfoString(String requestInfoString) {
		StringTokenizer st = new StringTokenizer(requestInfoString);
		if (st.hasMoreTokens()) {
			this.method = st.nextToken();
			this.requestURI = st.nextToken();   //    /res/aaaa/index.html
			this.protocal = st.nextToken();
			this.contextPath=   "/"+     this.requestURI.split("/")[1];   //    contextPath应用上下文路径  => /res
		}
		// 
		parseParameter(  requestInfoString);
		//解析 header
		parseHeader(  requestInfoString);
		//      解析Cookie
	}

	private void parseHeader(String requestInfoString) {
		//取出\r\n\r\n前面的
		String head=requestInfoString.substring(0,  requestInfoString.indexOf( "\r\n\r\n" ));
		String [] ss=head.split("\r\n");
		
		if(  ss!=null&&ss.length>0){
			for( int i=1;i<ss.length;i++){
				//取出一行
				String [] strs=ss[i].split(": ");
				headers.put(strs[0], strs[1]);
			}
		}
		getSession();
	}

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
