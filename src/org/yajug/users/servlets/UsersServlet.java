package org.yajug.users.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("serial")
public class UsersServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		Gson gson = new GsonBuilder().serializeNulls().create();
		resp.setContentType("javascript/json");
        resp.getWriter().print(gson.toJson("ok"));
	}
	
}
