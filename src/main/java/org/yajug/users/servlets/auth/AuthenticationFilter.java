/**
 * 
 */
package org.yajug.users.servlets.auth;

import java.io.IOException;
import java.util.Arrays;
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

import org.yajug.users.domain.User;

import com.google.inject.Singleton;

/**
 * @author bertrand
 *
 */
@Singleton
public class AuthenticationFilter implements Filter {

	private final static List<String> allowed;
	
	static{
		allowed = Arrays.asList("/login.html", "/auth", "/authCallback");
	}
	
	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) 
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		//some uris are not filtered
		if(allowed.contains(request.getServletPath())){
			chain.doFilter(req, resp);
			return;
		}
		
		//we check if the session contains a user under the activeUser key
		HttpSession session = request.getSession(false);
		if(session != null){
			User user = (User)session.getAttribute("activeUser");
			if(user != null && user.isVerifiedEmail()){
				chain.doFilter(req, resp);
				return;
			} 
			//if a session exists without a user we invalidate it
			session.invalidate();
		}
		
		if(request.getServletPath().contains("api")){
			//for the rest api we send an http code
			response.sendError(403);
		} else {
			//or we redirect to the login page
			response.sendRedirect("login.html");
		}
	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}
}
