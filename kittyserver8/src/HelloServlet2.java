import java.io.PrintWriter;






import com.yc.javax.servlet.ServletContext;
import com.yc.javax.servlet.http.Cookie;
import com.yc.javax.servlet.http.HttpServlet;
import com.yc.javax.servlet.http.HttpServletRequest;
import com.yc.javax.servlet.http.HttpServletResponse;
import com.yc.javax.servlet.http.HttpSession;
import com.yc.javax.servlet.http.JspWriter;


public class HelloServlet2 extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)  {
		
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)  {
		
		Cookie c=new Cookie("name","zy");
		Cookie c2=new Cookie( "pwd","a");
		Cookie c3=new Cookie("age","20");
		response.addCookie(  c  );
		response.addCookie(  c2  );
		response.addCookie(  c3  );   ///   有一个集合存Cookie
		
		HttpSession session=request.getSession();
		
		Integer count=0;
		
		if(  session.getAttribute("count")!=null){
			count=(Integer) session.getAttribute("count");
		}
		count++;
		session.setAttribute("count", count);
		
	
		
		
		
		JspWriter pw=response.getJspWriter();  //   方法:  println();
											  //   PrintWriter一定要有一个response  构造方法( response
		pw.println("<html><body>hello,你访问了本站点:"+ count+"次</body></html>");
		
	}
}
