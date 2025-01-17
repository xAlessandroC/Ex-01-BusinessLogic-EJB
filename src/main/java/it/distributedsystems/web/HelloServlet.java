package it.distributedsystems.web;

import it.distributedsystems.model.ejb.HelloBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "HelloServlet", urlPatterns = {"hello"}, loadOnStartup = 1)
public class HelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().print("Hello, World! ");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HelloBean ejb = new HelloBean();
        String name = request.getParameter("name");
        if (name == null) name = "World";
        PrintWriter out = response.getWriter();
        out.println("Hello "+name+"!!!");
        out.println(ejb.toString());
    }
}