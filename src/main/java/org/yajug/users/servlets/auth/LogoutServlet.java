package org.yajug.users.servlets.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LogoutServlet extends HttpServlet{

	private static final long serialVersionUID = 7267921282117546473L;
	
	@Inject private LogoutHelper logoutHelper;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		logoutHelper.logout(req);
		resp.sendRedirect("logint.html");
	}
}
