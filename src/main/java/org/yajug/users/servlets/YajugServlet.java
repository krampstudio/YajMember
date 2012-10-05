package org.yajug.users.servlets;

import java.io.IOException;
import javax.servlet.http.*;
@SuppressWarnings("serial")
public class YajugServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
//		UserService userService = UserServiceFactory.getUserService();
//		User user = userService.getCurrentUser();
//		if(user != null){
			resp.sendRedirect("index.html");
//		} else {
//			resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
//		}
	}
}
