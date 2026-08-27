package service;

import java.util.List;
import model.Category;

public interface ICategoryService {
    void insert(Category category);
    void update(Category category);
    void delete(int id);
    Category findById(int id);
    Category findByCategoryname(String name);
    List<Category> findAll();
    List<Category> findAll(int page, int pagesize);
    List<Category> searchByName(String keyword);
    int count();
}