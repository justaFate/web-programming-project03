package service;

import java.util.List;
import model.Product;

public interface IProductService {
    void insert(Product product);
    void update(Product product);
    void delete(int productId) throws Exception;
    Product findById(int productId);
    List<Product> findAll();
    List<Product> findTop10Latest();
    List<Product> findAll(int page, int pageSize);
    int count();
    List<Product> findByCategoryId(int categoryId, int page, int pageSize);
    int countByCategoryId(int categoryId);
}
