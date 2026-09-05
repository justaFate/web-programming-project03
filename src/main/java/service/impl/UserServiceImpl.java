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
    public boolean checkExistUsername(String username) {
        return userDao.checkExistUsername(username);
    }

    @Override
    public boolean checkExistPhone(String phone, int excludeUserId) {
        return userDao.checkExistPhone(phone, excludeUserId);
    }
}

