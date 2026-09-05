package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import service.IUserService;
import service.impl.UserServiceImpl;
import util.EmailUtil;

import java.io.IOException;

@WebServlet(urlPatterns = {"/verify-otp"})
public class VerifyOtpController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String action = req.getParameter("action");

        if ("resend".equalsIgnoreCase(action) && email != null && !email.trim().isEmpty()) {
            User user = userService.findByEmail(email.trim());
            if (user != null) {
                String newOtp = EmailUtil.generateOtp(6);
                user.setCode(newOtp);
                userService.update(user);

                String subject = "[Bài Tập 03] Mã OTP mới kích hoạt tài khoản";
                String body = "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;'>"
                        + "<h2 style='color: #0d6efd;'>Gửi Lại Mã OTP</h2>"
                        + "<p>Xin chào <strong>" + user.getFullname() + "</strong>,</p>"
                        + "<p>Mã OTP kích hoạt tài khoản mới của bạn là:</p>"
                        + "<div style='background: #f1f5f9; padding: 12px; border-radius: 6px; text-align: center; margin: 15px 0;'>"
                        + "<span style='font-size: 26px; font-weight: bold; letter-spacing: 5px; color: #0d6efd;'>" + newOtp + "</span>"
                        + "</div>"
                        + "</div>";

                EmailUtil.sendEmail(user.getEmail(), subject, body);
                req.setAttribute("message", "Đã gửi lại mã OTP thành công! Vui lòng kiểm tra email hoặc xem log console.");
            }
        }

        req.setAttribute("email", email);
        req.getRequestDispatcher("/views/verify-otp.jsp").include(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String otp = req.getParameter("otp");

        if (email == null || email.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Email không hợp lệ!");
            req.getRequestDispatcher("/views/verify-otp.jsp").include(req, resp);
            return;
        }

        if (otp == null || otp.trim().isEmpty()) {
            req.setAttribute("email", email);
            req.setAttribute("errorMessage", "Vui lòng nhập mã OTP gồm 6 chữ số");
            req.getRequestDispatcher("/views/verify-otp.jsp").include(req, resp);
            return;
        }

        boolean success = userService.activateAccount(email.trim(), otp.trim());
        if (success) {
            req.getSession().setAttribute("message", "Kích hoạt tài khoản thành công! Bạn có thể đăng nhập ngay bây giờ.");
            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
            req.setAttribute("email", email);
            req.setAttribute("errorMessage", "Mã OTP không chính xác hoặc đã hết hiệu lực. Vui lòng kiểm tra lại!");
            req.getRequestDispatcher("/views/verify-otp.jsp").include(req, resp);
        }
    }
}
