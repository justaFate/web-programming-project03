package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/login"})  
public class LoginController extends HttpServlet { 
    private static final long serialVersionUID = 1L;
    
    @Override  
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false); 
        if (session != null && session.getAttribute("username") != null) {  
            resp.sendRedirect(req.getContextPath() + "/hello");  
            return; 
        }
        
        Cookie[] cookies = req.getCookies();  
        if (cookies != null) { 
            for (Cookie cookie : cookies) { 
                if ("username".equals(cookie.getName())) { 
                    session = req.getSession(true);
                    session.setAttribute("username", cookie.getValue()); 
                    resp.sendRedirect(req.getContextPath() + "/hello");  
                    return; 
                }
            }
        } 
        req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
    }

    @Override  
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { 
        String username = req.getParameter("username"); 
        String password = req.getParameter("password"); 
        
        if ("trung".equals(username) && "123".equals(password)) { 
            HttpSession session = req.getSession(); 
            session.setAttribute("username", username); 
            session.setAttribute("name", username); 
            
            Cookie cookie = new Cookie("username", username); 
            cookie.setMaxAge(30); 
            resp.addCookie(cookie); 
            
            resp.sendRedirect(req.getContextPath() + "/hello");
        } else { 
            req.setAttribute("errorMessage", "Tài khoản hoặc mật khẩu không chính xác");
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
        }
    }
}