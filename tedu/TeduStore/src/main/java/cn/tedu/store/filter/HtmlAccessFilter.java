package cn.tedu.store.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * HTML访问过滤器
 */
public class HtmlAccessFilter implements Filter {
	/**
	 * 白名单，允许直接访问的页面列表
	 */
	private List<String> whiteList = new ArrayList<String>();
	
	public void init(FilterConfig arg0) throws ServletException {
		/*
		 *  确定白名单
		 */
		whiteList.add("index.html");
		whiteList.add("product.html");
		whiteList.add("register.html");
		whiteList.add("login.html");
		whiteList.add("footerTemplate.html");
		whiteList.add("leftTemplate.html");
		whiteList.add("topTemplate.html");
		/*
		 * 输出
		 */
		System.out.println("无需登录的页面列表：");
		for (String page : whiteList) {
			System.out.println(page);
		}
	}

	public void doFilter(ServletRequest arg0, 
			ServletResponse arg1, 
			FilterChain filterChain)
			throws IOException, ServletException {
		
		/*
		 * 获取当前页面
		 */
		HttpServletRequest request 
			= (HttpServletRequest) arg0;
		/**
		 * 获取请求路径
		 */
		String uri = request.getRequestURI();
		int beginIndex = uri.lastIndexOf("/") + 1;
		String fileName = uri.substring(beginIndex);
		System.out.println("当前请求页面：" + fileName);
		
		/*
		 *  判断当前访问的是哪个页面
		 *  如果是无需登录的页面，直接放行，例如：login.html
		 */
		
		if (whiteList.contains(fileName)) {
			System.out.println("\t无需登录，直接放行");
			/*
			 *  继续执行过滤器链
			 */
			filterChain.doFilter(arg0, arg1);
			return;
		}
		
		/*
		 *  如果是需要登录的页面，判断session，决定放行或重定向
		 */
		HttpSession session
			= request.getSession();
		if (session.getAttribute("uid") != null) {
			/*
			 *  Session中有uid，表示已登录，直接放行
			 */
			System.out.println("\t已经登录，直接放行");
			/*
			 *  继续执行过滤器链
			 */
			filterChain.doFilter(arg0, arg1);
			return;
		}
		
		/* 
		 * 执行到此处，表示当前页面不在白名单中，且未登录，则拦截
		 * 
		 */
		/* 
		 * 拦截的表现是：重定向到登录页
	 */
		System.out.println("\t拦截当前页面，将重定向到登录页！");
		HttpServletResponse response
			= (HttpServletResponse) arg1;
		response.sendRedirect("../web/login.html");
	}

	public void destroy() {
	}
}






