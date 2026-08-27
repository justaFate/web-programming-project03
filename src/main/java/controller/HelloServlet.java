package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/hello"})
public class HelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override  
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8"); 
        PrintWriter printWriter = resp.getWriter(); 
        String name = ""; 
        
        Cookie[] cookies = req.getCookies();  
        if (cookies != null) {
            for (Cookie c : cookies) {  
                if ("username".equals(c.getName())) {   
                    name = c.getValue();
                }
            }  
        }

        if (name.isEmpty()) {     
            resp.sendRedirect(req.getContextPath() + "/login");  
            return;
        } 
        printWriter.println("<h1>Xin chào " + name + "</h1>"); 
        printWriter.println("<a href='" + req.getContextPath() + "/profile'>Xem Profile</a> | ");
        printWriter.println("<a href='" + req.getContextPath() + "/logout'>Đăng xuất</a>");
    }
}