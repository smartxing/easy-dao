package com.servlet;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TestServlet
 */
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public TestServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		System.out.println(request.getParameter("test"));
		System.out.println(URLDecoder.decode(request.getParameter("test"),"UTF-8"));
		System.out.println(new String(URLDecoder.decode(request.getParameter("test"),"ISO-8859-1").getBytes("UTF-8")));
		System.out.println(URLDecoder.decode((URLDecoder.decode(request.getParameter("test"),"UTF-8")),"UTF-8"));
		System.out.println(new String(request.getParameter("test").getBytes(),"UTF-8"));
		response.sendRedirect("index.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
