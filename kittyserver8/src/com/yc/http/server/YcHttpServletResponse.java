package com.yc.http.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.yc.javax.servlet.http.Cookie;
import com.yc.javax.servlet.http.HttpServletResponse;
import com.yc.javax.servlet.http.JspWriter;

public class YcHttpServletResponse implements HttpServletResponse{
	private OutputStream oos;
	private YcHttpServletRequest request;
	private String contentType;
	
	private Cookie[] cookies=new Cookie[50];
	private int cookieIndex=0;

	public YcHttpServletResponse(YcHttpServletRequest request, OutputStream oos) {
		this.oos = oos;
		this.request = request;
		
		Cookie c=new Cookie(  YcConstants.SESSIONID, request.getSessionid()  );
		cookies[cookieIndex]=c;
		cookieIndex++;
	}
	
	
	@Override
	public void addCookie(Cookie cookie) {
		if(   cookieIndex>=cookies.length  ){
			return;
		}
		cookies[cookieIndex]=cookie;
		cookieIndex++;
	}

	@Override
	public JspWriter getJspWriter() {
		JspWriter jspWriter=new JspWriter(  this.oos, this  );
		return jspWriter;
	}

	@Override
	public Cookie[] getCookies() {
		Cookie[] cs=new Cookie[cookieIndex];
		for( int i=0;i<cookieIndex;i++){
			cs[i]=cookies[i];
		}
		return cs;
	}

	/*
	 * 从request中取出uri 2. 判断是否在本地存在这个uri指代的文件 没有, 404 有 3. 以输入流读取这个文件 4.
	 * 以输出流将文件写到客户端,要加入响应的协议.
	 */
	public void sendRedirect() {
		String responseprotocal = null; // 响应协议头
		byte[] fileContent = null; // 响应的内容
		String uri = request.getRequestURI();  //请求资源的地址
		File f = new File(request.getRealPath(), uri);   //请求的文件
		if (!f.exists()) {
			fileContent = readFile(new File(request.getRealPath(), request.getContextPath()+"/404.html"));
			responseprotocal = gen404(fileContent.length);
		} else {
			fileContent = readFile(f);
			responseprotocal = gen200(fileContent.length);
		}
		try {
			oos.write(responseprotocal.getBytes()); // 写协议
			oos.flush();
			oos.write(fileContent); // 写出文件
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private byte[] readFile(File f) {
		FileInputStream fis = null;
		//字节数组输出流:   读取到字节数组后，存到内存. 
		ByteArrayOutputStream boas = new ByteArrayOutputStream(); // 字节数组输出流 (
																	// 将字节数组数据保存到内存
																	// )
		try {
			// 读取这个文件
			fis = new FileInputStream(f);
			byte[] bs = new byte[1024];
			int length = -1;
			while ((length = fis.read(bs, 0, bs.length)) != -1) {
				boas.write(bs, 0, length); // 写到内存缓存
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return boas.toByteArray(); // 一次性地从内存中读取所有的字节数组返回
	}

	/**
	 * 要考虑静态文件的类型
	 * @param bodylength
	 *            , 内容的长度
	 * @return
	 */
	private String gen200(long bodylength) {
		String uri = this.request.getRequestURI(); // 取出要访问的文件的地址
		int index = uri.lastIndexOf(".");
		if (index >= 0) {
			index = index + 1;
		}
		String fileExtension = uri.substring(index); // 文件的后缀名
		String protocal200 = "";
		if ("JPG".equalsIgnoreCase(fileExtension)
				|| "JPEG".equalsIgnoreCase(fileExtension)) {
			contentType="image/JPEG";
			protocal200 = "HTTP/1.1 200 OK\r\nContent-Type: "+contentType+"\r\nContent-Length: "
					+ bodylength + "\r\n\r\n";
		} else if ("PNG".equalsIgnoreCase(fileExtension)) {
			contentType="image/PNG";
			protocal200 = "HTTP/1.1 200 OK\r\nContent-Type: "+ contentType+"\r\nContent-Length: "
					+ bodylength + "\r\n\r\n";
		} else if ("json".equalsIgnoreCase(fileExtension)) {
			contentType="application/json";
			protocal200 = "HTTP/1.1 200 OK\r\nContent-Type: "+contentType+"\r\nContent-Length: "
					+ bodylength + "\r\n\r\n";
		} else {
			contentType="text/html";
			protocal200 = "HTTP/1.0 200 OK\r\nContent-Type: "+contentType+"\r\nContent-Length: "
					+ bodylength + "\r\n\r\n";
		}
		return protocal200;
	}

	/**
	 * 产生404响应
	 * 
	 * @return
	 */
	private String gen404(long bodylength) {
		String protocal404 = "HTTP/1.1 404 File Not Found\r\nContent-Type: text/html\r\nContent-Length: "
				+ bodylength + "\r\n\r\n";
		return protocal404;
	}

	@Override
	public PrintWriter getWriter() {
		PrintWriter pw=new PrintWriter(   this.oos );
		return pw;
	}

	@Override
	public String getContentType() {
		return this.contentType;
	}



}
