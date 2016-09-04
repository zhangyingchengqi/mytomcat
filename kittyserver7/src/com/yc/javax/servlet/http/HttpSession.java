package com.yc.javax.servlet.http;


/**
 * 会话机制: 会话容器,存一个客户端与服务器之间的多次交流的数据. 
 * 解决http协议无状态问题.
 * 原理: 利用cookie向客户端发送一次唯一的一个sessionid, 下次客户端又会将这个sessionid存在cookie发过来.  
 *
 */
public interface HttpSession {

	
	public Object getAttribute( String name);
	
	public void setAttribute( String name, Object obj);
	
	public void removeAttribute( String name);
	
	public String getId();
	
	/**
	 * 创建时间
	 * @return
	 */
	public long getCreationTime();
	
	/**
	 * 最后一次访问时间
	 * @return
	 */
	public long getLastAccessedTime();
	
	
}
