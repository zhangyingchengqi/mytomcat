package com.yc.http.server;

import org.apache.log4j.Logger;

public class YcConstants {
	/* 配置server.xml文件名字 */
	public final static String SERVERCONFIG="conf/server.xml";
	
	
	/** 服务器路径  */
	public final static String KITTYSERVER_BASEPATH=System.getProperty("user.dir");
	
	/**
	 * 日志對象
	 */
	public final static Logger logger=Logger.getLogger(YcConstants.class);
}
