package controller;

import jakarta.servlet.http.HttpSession;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.IUserService;
import util.Constant;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class UserAdminController {

    @Autowired
    private IUserService userService;

    @GetMapping("/users")
    public String listUsers(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<User> list;
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = userService.search(keyword.trim());
            model.addAttribute("keyword", keyword.trim());
        } else {
            list = userService.findAll();
        }
        model.addAttribute("userList", list);
        return "admin/user-list";
    }

    @GetMapping("/user/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/user-add";
    }

    @PostMapping("/user/insert")
    public String insertUser(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("fullname") String fullname,
                             @RequestParam(value = "email", required = false) String email,
                             @RequestParam(value = "phone", required = false) String phone,
                             @RequestParam(value = "role", defaultValue = "0") int role,
                             @RequestParam(value = "status", defaultValue = "1") int status,
                             @RequestParam(value = "images1", required = false) MultipartFile file,
                             Model model, HttpSession session) {
        Map<String, String> errors = new HashMap<>();

        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "Tên đăng nhập không được để trống!");
        } else if (username.trim().length() < 3 || username.trim().length() > 50) {
            errors.put("username", "Tên đăng nhập phải từ 3 đến 50 ký tự!");
        } else if (userService.checkExistUsername(username.trim())) {
            errors.put("username", "Tên đăng nhập đã tồn tại trong hệ thống!");
        }

        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "Mật khẩu không được để trống!");
        } else if (password.trim().length() < 6) {
            errors.put("password", "Mật khẩu phải có ít nhất 6 ký tự!");
        }

        if (fullname == null || fullname.trim().isEmpty()) {
            errors.put("fullname", "Họ và tên không được để trống!");
        }

        if (email != null && !email.trim().isEmpty()) {
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                errors.put("email", "Email không đúng định dạng!");
            } else if (userService.checkExistEmail(email.trim())) {
                errors.put("email", "Email này đã được sử dụng!");
            }
        }

        if (phone != null && !phone.trim().isEmpty()) {
            if (!phone.matches("^(0[3|5|7|8|9][0-9]{8})$")) {
                errors.put("phone", "Số điện thoại không hợp lệ (gồm 10 số, bắt đầu bằng 03, 05, 07, 08, 09)!");
            } else if (userService.checkExistPhone(phone.trim(), 0)) {
                errors.put("phone", "Số điện thoại đã được đăng ký bởi tài khoản khác!");
            }
        }

        User user = new User();
        user.setUsername(username != null ? username.trim() : "");
        user.setPassword(password != null ? password.trim() : "");
        user.setFullname(fullname != null ? fullname.trim() : "");
        user.setEmail(email != null ? email.trim() : "");
        user.setPhone(phone != null ? phone.trim() : "");
        user.setRole(role);
        user.setStatus(status);

        if (!errors.isEmpty()) {
            model.addAttribute("user", user);
            model.addAttribute("errors", errors);
            return "admin/user-add";
        }

        String fname = handleFileUpload(file, null);
        user.setImages(fname);

        userService.insert(user);
        session.setAttribute("message", "Thêm người dùng [" + user.getUsername() + "] thành công!");
        return "redirect:/admin/users";
    }

    @GetMapping("/user/edit")
    public String editUserForm(@RequestParam("id") int id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        return "admin/user-edit";
    }

    @PostMapping("/user/update")
    public String updateUser(@RequestParam("id") int id,
                             @RequestParam("fullname") String fullname,
                             @RequestParam(value = "password", required = false) String password,
                             @RequestParam(value = "email", required = false) String email,
                             @RequestParam(value = "phone", required = false) String phone,
                             @RequestParam(value = "role", defaultValue = "0") int role,
                             @RequestParam(value = "status", defaultValue = "1") int status,
                             @RequestParam(value = "images1", required = false) MultipartFile file,
                             Model model, HttpSession session) {
        User user = userService.findById(id);
        if (user == null) {
            session.setAttribute("errorMessage", "Không tìm thấy người dùng!");
            return "redirect:/admin/users";
        }

        Map<String, String> errors = new HashMap<>();

        if (fullname == null || fullname.trim().isEmpty()) {
            errors.put("fullname", "Họ và tên không được để trống!");
        }

        if (email != null && !email.trim().isEmpty()) {
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                errors.put("email", "Email không đúng định dạng!");
            } else {
                User existing = userService.findByEmail(email.trim());
                if (existing != null && existing.getId() != id) {
                    errors.put("email", "Email này đã được sử dụng bởi tài khoản khác!");
                }
            }
        }

        if (phone != null && !phone.trim().isEmpty()) {
            if (!phone.matches("^(0[3|5|7|8|9][0-9]{8})$")) {
                errors.put("phone", "Số điện thoại không hợp lệ (gồm 10 số, bắt đầu bằng 03, 05, 07, 08, 09)!");
            } else if (userService.checkExistPhone(phone.trim(), id)) {
                errors.put("phone", "Số điện thoại đã được đăng ký bởi tài khoản khác!");
            }
        }

        if (password != null && !password.trim().isEmpty() && password.trim().length() < 6) {
            errors.put("password", "Mật khẩu mới phải từ 6 ký tự trở lên!");
        }

        if (!errors.isEmpty()) {
            user.setFullname(fullname);
            user.setEmail(email);
            user.setPhone(phone);
            user.setRole(role);
            user.setStatus(status);
            model.addAttribute("user", user);
            model.addAttribute("errors", errors);
            return "admin/user-edit";
        }

        user.setFullname(fullname.trim());
        user.setEmail(email != null ? email.trim() : "");
        user.setPhone(phone != null ? phone.trim() : "");
        user.setRole(role);
        user.setStatus(status);

        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(password.trim());
        }

        if (file != null && !file.isEmpty()) {
            if (user.getImages() != null && !user.getImages().startsWith("http")) {
                deleteFileQuietly(Constant.DIR + File.separator + user.getImages());
            }
            String fname = handleFileUpload(file, null);
            user.setImages(fname);
        }

        userService.update(user);
        session.setAttribute("message", "Cập nhật thông tin người dùng [" + user.getUsername() + "] thành công!");
        return "redirect:/admin/users";
    }

    @GetMapping("/user/delete")
    public String deleteUser(@RequestParam("id") int id, HttpSession session) {
        try {
            User user = userService.findById(id);
            if (user != null) {
                if (user.getImages() != null && !user.getImages().startsWith("http")) {
                    deleteFileQuietly(Constant.DIR + File.separator + user.getImages());
                }
                userService.delete(id);
                session.setAttribute("message", "Đã xóa người dùng [" + user.getUsername() + "] thành công!");
            } else {
                session.setAttribute("errorMessage", "Không tìm thấy người dùng để xóa!");
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Không thể xóa người dùng: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    private String handleFileUpload(MultipartFile file, String defaultImage) {
        if (file != null && !file.isEmpty()) {
            String uploadPath = Constant.DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String originalFilename = file.getOriginalFilename();
            String ext = ".png";
            if (originalFilename != null && originalFilename.lastIndexOf(".") > 0) {
                ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fname = "user_" + System.currentTimeMillis() + ext;
            try {
                file.transferTo(new File(uploadPath + File.separator + fname));
                return fname;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (defaultImage != null && !defaultImage.trim().isEmpty()) {
            return defaultImage.trim();
        }
        return "avatar.png";
    }

    private void deleteFileQuietly(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (Exception ignored) {}
    }
}
