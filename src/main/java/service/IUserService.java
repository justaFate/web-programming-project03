package service;

import model.User;

public interface IUserService {
    User findById(int id);
    User findByUsername(String username);
    void insert(User user);
    void update(User user);
    User updateProfile(int id, String fullname, String phone, String images);
    User login(String username, String password);
    boolean checkExistUsername(String username);
    boolean checkExistPhone(String phone, int excludeUserId);
}

