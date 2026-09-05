package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
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

@WebServlet(urlPatterns = {"/register"})
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.getRequestDispatcher("/views/register.jsp").include(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        Map<String, String> errors = new HashMap<>();

        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "Tên đăng nhập không được để trống");
        } else if (username.trim().length() < 3 || username.trim().length() > 50) {
            errors.put("username", "Tên đăng nhập phải từ 3 đến 50 ký tự");
        } else if (userService.checkExistUsername(username.trim())) {
            errors.put("username", "Tên đăng nhập này đã có người sử dụng");
        }

        if (fullname == null || fullname.trim().isEmpty()) {
            errors.put("fullname", "Họ và tên không được để trống");
        } else if (fullname.trim().length() < 2 || fullname.trim().length() > 100) {
            errors.put("fullname", "Họ và tên phải từ 2 đến 100 ký tự");
        }

        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email không được để trống");
        } else if (!email.trim().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.put("email", "Địa chỉ email không đúng định dạng");
        } else if (userService.checkExistEmail(email.trim())) {
            errors.put("email", "Địa chỉ email này đã được đăng ký tài khoản khác");
        }

        if (phone != null && !phone.trim().isEmpty()) {
            if (!phone.trim().matches("^0[3|5|7|8|9][0-9]{8}$")) {
                errors.put("phone", "Số điện thoại không hợp lệ (gồm 10 số, đầu 03, 05, 07, 08, 09)");
            }
        }

        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "Mật khẩu không được để trống");
        } else if (password.trim().length() < 3) {
            errors.put("password", "Mật khẩu phải có tối thiểu 3 ký tự");
        }

        if (confirmPassword == null || !confirmPassword.equals(password)) {
            errors.put("confirmPassword", "Mật khẩu xác nhận không khớp");
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("username", username);
            req.setAttribute("fullname", fullname);
            req.setAttribute("email", email);
            req.setAttribute("phone", phone);
            req.getRequestDispatcher("/views/register.jsp").include(req, resp);
            return;
        }

        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password.trim());
        user.setFullname(fullname.trim());
        user.setEmail(email.trim());
        user.setPhone(phone != null ? phone.trim() : "");
        user.setRole(0); // Thành viên thông thường
        user.setStatus(0); // Chưa kích hoạt qua OTP

        boolean registered = userService.register(user);
        if (registered) {
            req.getSession().setAttribute("registeredEmail", user.getEmail());
            resp.sendRedirect(req.getContextPath() + "/verify-otp?email=" + user.getEmail() + "&msg=sent");
        } else {
            req.setAttribute("errorMessage", "Có lỗi xảy ra khi tạo tài khoản. Vui lòng thử lại sau!");
            req.getRequestDispatcher("/views/register.jsp").include(req, resp);
        }
    }
}
