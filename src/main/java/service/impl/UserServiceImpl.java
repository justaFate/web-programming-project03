package service.impl;

import dao.IUserDao;
import dao.impl.UserDaoImpl;
import model.User;
import service.IUserService;

public class UserServiceImpl implements IUserService {
    private IUserDao userDao = new UserDaoImpl();

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public void insert(User user) {
        userDao.insert(user);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public User updateProfile(int id, String fullname, String phone, String images) {
        User user = userDao.findById(id);
        if (user != null) {
            user.setFullname(fullname);
            user.setPhone(phone);
            if (images != null && !images.trim().isEmpty()) {
                user.setImages(images);
            }
            userDao.update(user);
            return user;
        }
        return null;
    }

    @Override
    public User login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        try {
            User user = userDao.findByUsername(username);
            if (user != null) {
                if (password.equals(user.getPassword())) {
                    return user;
                }
                return null;
            }

            // Hỗ trợ tạo tài khoản mặc định nếu đăng nhập lần đầu với trung / 123
            if ("trung".equals(username) && "123".equals(password)) {
                User defaultUser = new User();
                defaultUser.setUsername("trung");
                defaultUser.setPassword("123");
                defaultUser.setFullname("Nguyễn Văn Trung");
                defaultUser.setPhone("0901234567");
                defaultUser.setImages("avatar.png");
                defaultUser.setRole(1);
                userDao.insert(defaultUser);
                return defaultUser;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu có lỗi DB (như chưa bật server SQL), vẫn cho fallback tài khoản mẫu để test logic giao diện
            if ("trung".equals(username) && "123".equals(password)) {
                User fallback = new User();
                fallback.setId(1);
                fallback.setUsername("trung");
                fallback.setPassword("123");
                fallback.setFullname("Nguyễn Văn Trung");
                fallback.setPhone("0901234567");
                fallback.setImages("avatar.png");
                fallback.setRole(1);
                return fallback;
            }
        }

        return null;
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User findByUsernameOrEmail(String value) {
        return userDao.findByUsernameOrEmail(value);
    }

    @Override
    public boolean checkExistUsername(String username) {
        return userDao.checkExistUsername(username);
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userDao.checkExistEmail(email);
    }

    @Override
    public boolean checkExistPhone(String phone, int excludeUserId) {
        return userDao.checkExistPhone(phone, excludeUserId);
    }

    @Override
    public boolean register(User user) {
        try {
            user.setStatus(0); // Chưa kích hoạt
            String otp = util.EmailUtil.generateOtp(6);
            user.setCode(otp);
            if (user.getImages() == null || user.getImages().trim().isEmpty()) {
                user.setImages("avatar.png");
            }
            userDao.insert(user);

            String subject = "[Bài Tập 03] Mã OTP kích hoạt tài khoản";
            String body = "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;'>"
                    + "<h2 style='color: #0d6efd;'>Kích Hoạt Tài Khoản</h2>"
                    + "<p>Xin chào <strong>" + user.getFullname() + "</strong>,</p>"
                    + "<p>Cảm ơn bạn đã đăng ký tài khoản tại hệ thống của chúng tôi.</p>"
                    + "<p>Mã OTP kích hoạt tài khoản của bạn là:</p>"
                    + "<div style='background: #f1f5f9; padding: 12px; border-radius: 6px; text-align: center; margin: 15px 0;'>"
                    + "<span style='font-size: 26px; font-weight: bold; letter-spacing: 5px; color: #0d6efd;'>" + otp + "</span>"
                    + "</div>"
                    + "<p style='color: #6c757d; font-size: 13px;'>Mã xác thực có hiệu lực trong 5 phút. Vui lòng không cung cấp mã này cho người khác.</p>"
                    + "</div>";

            util.EmailUtil.sendEmail(user.getEmail(), subject, body);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean activateAccount(String email, String otp) {
        if (email == null || otp == null) return false;
        try {
            User user = userDao.findByEmail(email.trim());
            if (user != null && otp.trim().equals(user.getCode())) {
                user.setStatus(1); // Kích hoạt thành công
                user.setCode(null); // Xóa OTP
                userDao.update(user);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean sendForgotPasswordOtp(String emailOrUsername) {
        if (emailOrUsername == null || emailOrUsername.trim().isEmpty()) return false;
        try {
            User user = userDao.findByUsernameOrEmail(emailOrUsername.trim());
            if (user != null && user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
                String otp = util.EmailUtil.generateOtp(6);
                user.setCode(otp);
                userDao.update(user);

                String subject = "[Bài Tập 03] Mã OTP đặt lại mật khẩu";
                String body = "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;'>"
                        + "<h2 style='color: #dc3545;'>Đặt Lại Mật Khẩu</h2>"
                        + "<p>Xin chào <strong>" + user.getFullname() + "</strong>,</p>"
                        + "<p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản: <strong>" + user.getUsername() + "</strong>.</p>"
                        + "<p>Mã xác thực OTP của bạn là:</p>"
                        + "<div style='background: #fff5f5; padding: 12px; border-radius: 6px; text-align: center; margin: 15px 0;'>"
                        + "<span style='font-size: 26px; font-weight: bold; letter-spacing: 5px; color: #dc3545;'>" + otp + "</span>"
                        + "</div>"
                        + "<p style='color: #6c757d; font-size: 13px;'>Nếu bạn không yêu cầu điều này, xin vui lòng bỏ qua email.</p>"
                        + "</div>";

                util.EmailUtil.sendEmail(user.getEmail(), subject, body);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean resetPassword(String email, String otp, String newPassword) {
        if (email == null || otp == null || newPassword == null) return false;
        try {
            User user = userDao.findByEmail(email.trim());
            if (user != null && otp.trim().equals(user.getCode())) {
                user.setPassword(newPassword.trim());
                user.setCode(null); // Xóa OTP sau khi đổi mật khẩu
                userDao.update(user);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

