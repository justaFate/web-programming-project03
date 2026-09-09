package service;

import model.User;

public interface IUserService {
    User findById(int id);
    User findByUsername(String username);
    User findByEmail(String email);
    User findByUsernameOrEmail(String value);
    void insert(User user);
    void update(User user);
    User updateProfile(int id, String fullname, String phone, String images);
    User login(String username, String password);
    boolean checkExistUsername(String username);
    boolean checkExistEmail(String email);
    boolean checkExistPhone(String phone, int excludeUserId);
    boolean register(User user);
    boolean activateAccount(String email, String otp);
    boolean sendForgotPasswordOtp(String emailOrUsername);
    boolean resetPassword(String email, String otp, String newPassword);
    java.util.List<User> findAll();
    java.util.List<User> search(String keyword);
    void delete(int id) throws Exception;
}

