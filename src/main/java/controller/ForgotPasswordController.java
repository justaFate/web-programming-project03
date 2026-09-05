package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import service.IUserService;
import service.impl.UserServiceImpl;

import java.io.IOException;

@WebServlet(urlPatterns = {"/forgot-password", "/reset-password"})
public class ForgotPasswordController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.contains("/reset-password")) {
            String email = req.getParameter("email");
            req.setAttribute("email", email);
            req.getRequestDispatcher("/views/reset-password.jsp").include(req, resp);
        } else {
            req.getRequestDispatcher("/views/forgot-password.jsp").include(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String uri = req.getRequestURI();

        if (uri.contains("/forgot-password")) {
            String emailOrUsername = req.getParameter("emailOrUsername");

            if (emailOrUsername == null || emailOrUsername.trim().isEmpty()) {
                req.setAttribute("errorMessage", "Vui lòng nhập tên đăng nhập hoặc email");
                req.getRequestDispatcher("/views/forgot-password.jsp").include(req, resp);
                return;
            }

            User user = userService.findByUsernameOrEmail(emailOrUsername.trim());
            if (user == null || user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                req.setAttribute("emailOrUsername", emailOrUsername);
                req.setAttribute("errorMessage", "Tài khoản hoặc địa chỉ email không tồn tại trong hệ thống");
                req.getRequestDispatcher("/views/forgot-password.jsp").include(req, resp);
                return;
            }

            boolean sent = userService.sendForgotPasswordOtp(emailOrUsername.trim());
            if (sent) {
                resp.sendRedirect(req.getContextPath() + "/reset-password?email=" + user.getEmail() + "&msg=otp_sent");
            } else {
                req.setAttribute("errorMessage", "Không thể gửi mã OTP. Vui lòng thử lại sau!");
                req.getRequestDispatcher("/views/forgot-password.jsp").include(req, resp);
            }

        } else if (uri.contains("/reset-password")) {
            String email = req.getParameter("email");
            String otp = req.getParameter("otp");
            String newPassword = req.getParameter("newPassword");
            String confirmPassword = req.getParameter("confirmPassword");

            req.setAttribute("email", email);

            if (email == null || email.trim().isEmpty()) {
                req.setAttribute("errorMessage", "Địa chỉ email không hợp lệ!");
                req.getRequestDispatcher("/views/reset-password.jsp").include(req, resp);
                return;
            }

            if (otp == null || otp.trim().isEmpty()) {
                req.setAttribute("errorMessage", "Vui lòng nhập mã OTP xác thực");
                req.getRequestDispatcher("/views/reset-password.jsp").include(req, resp);
                return;
            }

            if (newPassword == null || newPassword.trim().isEmpty() || newPassword.trim().length() < 3) {
                req.setAttribute("errorMessage", "Mật khẩu mới phải có tối thiểu 3 ký tự");
                req.getRequestDispatcher("/views/reset-password.jsp").include(req, resp);
                return;
            }

            if (confirmPassword == null || !confirmPassword.equals(newPassword)) {
                req.setAttribute("errorMessage", "Mật khẩu xác nhận không trùng khớp");
                req.getRequestDispatcher("/views/reset-password.jsp").include(req, resp);
                return;
            }

            boolean resetOk = userService.resetPassword(email.trim(), otp.trim(), newPassword.trim());
            if (resetOk) {
                req.getSession().setAttribute("message", "Đổi mật khẩu thành công! Vui lòng đăng nhập với mật khẩu mới.");
                resp.sendRedirect(req.getContextPath() + "/login");
            } else {
                req.setAttribute("errorMessage", "Mã OTP không chính xác hoặc đã hết hiệu lực. Vui lòng thử lại!");
                req.getRequestDispatcher("/views/reset-password.jsp").include(req, resp);
            }
        }
    }
}
