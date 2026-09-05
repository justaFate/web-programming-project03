package dao;

import java.util.List;
import model.User;

public interface IUserDao {
    void insert(User user);
    void update(User user);
    void delete(int id) throws Exception;
    User findById(int id);
    User findByUsername(String username);
    List<User> findAll();
    boolean checkExistUsername(String username);
    boolean checkExistPhone(String phone, int excludeUserId);
}

