package com.example.spring.demo40;

import javax.servlet.http.HttpServlet;

public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws javax.servlet.ServletException, java.io.IOException {
       resp.setContentType("text/html");
        resp.getWriter().write("Hello, World!");
    }
}
