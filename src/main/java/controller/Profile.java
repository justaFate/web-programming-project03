package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.User;
import service.IUserService;
import service.impl.UserServiceImpl;
import util.Constant;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 50
)
@WebServlet(urlPatterns = {"/profile", "/profile/update"})
public class Profile extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); 
        if (session == null || session.getAttribute("username") == null) { 
            response.sendRedirect(request.getContextPath() + "/login"); 
            return;
        }

        String username = (String) session.getAttribute("username");
        User user = userService.findByUsername(username);

        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setFullname(session.getAttribute("name") != null ? (String) session.getAttribute("name") : username);
            user.setPassword("123");
            user.setPhone("0901234567");
            user.setImages("avatar.png");
            user.setRole(1);
            try {
                userService.insert(user);
            } catch (Exception ignored) {}
        }

        request.setAttribute("user", user);
        session.setAttribute("user", user);
        request.getRequestDispatcher("/views/user/profile.jsp").include(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String username = (String) session.getAttribute("username");
        User user = userService.findByUsername(username);
        if (user == null) {
            user = (User) session.getAttribute("user");
        }

        String fullname = request.getParameter("fullname");
        String phone = request.getParameter("phone");
        Part filePart = request.getPart("imageFile");

        Map<String, String> errors = new HashMap<>();

        if (fullname == null || fullname.trim().isEmpty()) {
            errors.put("fullname", "Họ và tên không được để trống");
        } else if (fullname.trim().length() < 2 || fullname.trim().length() > 100) {
            errors.put("fullname", "Họ và tên phải từ 2 đến 100 ký tự");
        }

        if (phone != null && !phone.trim().isEmpty()) {
            phone = phone.trim();
            if (!phone.matches("^0[3|5|7|8|9][0-9]{8}$")) {
                errors.put("phone", "Số điện thoại không hợp lệ (phải gồm 10 chữ số, bắt đầu bằng 03, 05, 07, 08, 09)");
            } else if (user != null && user.getId() > 0 && userService.checkExistPhone(phone, user.getId())) {
                errors.put("phone", "Số điện thoại này đã được sử dụng bởi tài khoản khác");
            }
        }

        String newFileName = null;
        if (filePart != null && filePart.getSize() > 0) {
            String submittedFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String lower = submittedFileName.toLowerCase();
            if (!lower.endsWith(".jpg") && !lower.endsWith(".jpeg") && !lower.endsWith(".png") 
                    && !lower.endsWith(".webp") && !lower.endsWith(".gif")) {
                errors.put("imageFile", "Định dạng file không hợp lệ! Vui lòng chọn file ảnh (JPG, PNG, GIF, WEBP)");
            }
        }

        if (!errors.isEmpty()) {
            User tempUser = new User();
            if (user != null) {
                tempUser.setId(user.getId());
                tempUser.setUsername(user.getUsername());
                tempUser.setImages(user.getImages());
                tempUser.setRole(user.getRole());
            }
            tempUser.setFullname(fullname);
            tempUser.setPhone(phone);

            request.setAttribute("user", tempUser);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/views/user/profile.jsp").include(request, response);
            return;
        }

        try {
            if (filePart != null && filePart.getSize() > 0) {
                String submittedFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                int dotIndex = submittedFileName.lastIndexOf(".");
                String extension = (dotIndex >= 0) ? submittedFileName.substring(dotIndex) : ".png";

                newFileName = "user_" + (user != null ? user.getId() : "0") + "_" + System.currentTimeMillis() + extension;

                String uploadPath = Constant.DIR;
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                filePart.write(uploadPath + File.separator + newFileName);

                if (user != null && user.getImages() != null && !user.getImages().equals("avatar.png") 
                        && !user.getImages().startsWith("http")) {
                    File oldFile = new File(uploadPath + File.separator + user.getImages());
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.put("imageFile", "Lỗi khi lưu file: " + e.getMessage());
            request.setAttribute("user", user);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/views/user/profile.jsp").include(request, response);
            return;
        }

        try {
            if (user != null) {
                User updatedUser = userService.updateProfile(user.getId(), fullname.trim(), 
                        (phone != null ? phone.trim() : null), newFileName);
                if (updatedUser != null) {
                    user = updatedUser;
                } else {
                    user.setFullname(fullname.trim());
                    user.setPhone(phone != null ? phone.trim() : null);
                    if (newFileName != null) user.setImages(newFileName);
                }
            }
            session.setAttribute("user", user);
            session.setAttribute("name", user != null ? user.getFullname() : fullname);
            request.setAttribute("user", user);
            request.setAttribute("message", "Cập nhật thông tin cá nhân thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("generalError", "Có lỗi xảy ra khi lưu vào CSDL: " + e.getMessage());
            request.setAttribute("user", user);
        }

        request.getRequestDispatcher("/views/user/profile.jsp").include(request, response);
    }
}
