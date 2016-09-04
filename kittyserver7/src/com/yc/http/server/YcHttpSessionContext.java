package com.yc.http.server;

import java.util.HashMap;
import java.util.Map;

import com.yc.javax.servlet.http.HttpSession;
import com.yc.javax.servlet.http.HttpSessionContext;

public class YcHttpSessionContext implements HttpSessionContext {
	private static Map<String, HttpSession> map = new HashMap<String, HttpSession>();
	private static HttpSessionContext httpSessionContext;

	@Override
	public HttpSession getSession(String sessionId) {
		if (map.containsKey(sessionId)) {
			return map.get(sessionId);
		} else {
			return null;
		}
	}

	public void setSession(String sessionId, HttpSession session) {
		map.put(sessionId, session);
	}

	//单例
	private YcHttpSessionContext() {
	}

	public synchronized static HttpSessionContext getInstance() {
		if (httpSessionContext == null) {
			httpSessionContext = new YcHttpSessionContext();
		}
		for(   Map.Entry<String, HttpSession> entry: map.entrySet() ){
			System.out.println(   entry.getKey()+"      "+  entry.getValue() );
		}
		return httpSessionContext;
	}
}
