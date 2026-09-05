package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import service.IUserService;
import service.impl.UserServiceImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/login"})  
public class LoginController extends HttpServlet { 
    private static final long serialVersionUID = 1L;
    private IUserService userService = new UserServiceImpl();
    
    @Override  
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false); 
        if (session != null && session.getAttribute("username") != null) {  
            resp.sendRedirect(req.getContextPath() + "/");  
            return; 
        }
        
        Cookie[] cookies = req.getCookies();  
        if (cookies != null) { 
            for (Cookie cookie : cookies) { 
                if ("username".equals(cookie.getName())) { 
                    session = req.getSession(true);
                    String username = cookie.getValue();
                    session.setAttribute("username", username);
                    User user = userService.findByUsername(username);
                    if (user != null) {
                        session.setAttribute("user", user);
                        session.setAttribute("name", user.getFullname());
                    } else {
                        session.setAttribute("name", username);
                    }
                    resp.sendRedirect(req.getContextPath() + "/");  
                    return; 
                }
            }
        } 
        req.getRequestDispatcher("/views/login.jsp").include(req, resp);
    }

    @Override  
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { 
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username"); 
        String password = req.getParameter("password"); 
        
        // --- FORM VALIDATION ---
        Map<String, String> errors = new HashMap<>();

        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "Vui lòng nhập tên đăng nhập");
        }
        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "Vui lòng nhập mật khẩu");
        }

        String redirect = req.getParameter("redirect");
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("username", username);
            req.setAttribute("redirect", redirect);
            req.getRequestDispatcher("/views/login.jsp").include(req, resp);
            return;
        }

        User user = null;
        try {
            user = userService.login(username.trim(), password.trim());
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("username", username);
            req.setAttribute("redirect", redirect);
            req.setAttribute("errorMessage", "Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
            req.getRequestDispatcher("/views/login.jsp").include(req, resp);
            return;
        }

        if (user != null) { 
            if (user.getStatus() == 0) {
                req.getSession().setAttribute("errorMessage", "Tài khoản của bạn chưa được kích hoạt. Vui lòng nhập mã OTP để kích hoạt!");
                resp.sendRedirect(req.getContextPath() + "/verify-otp?email=" + (user.getEmail() != null ? user.getEmail() : ""));
                return;
            }

            HttpSession session = req.getSession(true); 
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername()); 
            session.setAttribute("name", user.getFullname()); 
            
            Cookie cookie = new Cookie("username", user.getUsername()); 
            cookie.setMaxAge(60 * 60 * 24); // 1 ngày
            resp.addCookie(cookie); 
            
            if (redirect != null && !redirect.trim().isEmpty() && !redirect.contains("/login")) {
                resp.sendRedirect(redirect.trim());
            } else {
                resp.sendRedirect(req.getContextPath() + "/");
            }
        } else { 
            req.setAttribute("username", username);
            req.setAttribute("redirect", redirect);
            req.setAttribute("errorMessage", "Tài khoản hoặc mật khẩu không chính xác");
            req.getRequestDispatcher("/views/login.jsp").include(req, resp);
        }
    }
}
