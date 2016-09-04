import java.io.IOException;





import com.yc.javax.servlet.ServletRequest;
import com.yc.javax.servlet.ServletResponse;
import com.yc.javax.servlet.http.HttpServlet;
import com.yc.javax.servlet.http.HttpServletRequest;
import com.yc.javax.servlet.http.HttpServletResponse;


public class HelloServlet extends HttpServlet {

	  public HelloServlet() {
	        super();
	        System.out.println("HelloServlet的构造方法");
	    }
		
		public void init(){
			super.init();
			System.out.println("init方法");
		}
		
		
		public void service(HttpServletRequest arg0, HttpServletResponse arg1){
			System.out.println("service被调用了...");
			super.service(arg0, arg1);
		}
		
		
		
		
		protected void doGet(HttpServletRequest request, HttpServletResponse response)  {
			System.out.println("doGet()");
			doPost(request, response);
		}
		protected void doPost(HttpServletRequest request, HttpServletResponse response)  {
			System.out.println("doPost被调用了");
			int r=5/0;
			
			
		}

	
	
}
