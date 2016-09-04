版 本一:   http服务器，提供静态资源访问. 


用到的技术:
1. socket
2. 线程
3. log4j
4. dom解析


注意的问题:
  1. HttpServletRequest类中的  private String readFromInputStream()方法，要一次读取所有的请求头数据. 