package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/profile"})
public class Profile extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false); 
        if (session != null && session.getAttribute("name") != null) { 
            String name = (String) session.getAttribute("name"); 
            out.print("<h1>Chào bạn, " + name + " đến với trang quản lý tài khoản</h1>"); 
            out.print("<a href='" + request.getContextPath() + "/logout'>Đăng xuất</a>");
        } else { 
            response.sendRedirect(request.getContextPath() + "/login"); 
        }
    }
}