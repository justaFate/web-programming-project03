import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.Category;
import model.Product;
import service.ICategoryService;
import service.IProductService;
import service.impl.CategoryServiceImpl;
import service.impl.ProductServiceImpl;

@DisplayName("Test Danh Mục & Quan Hệ 1 - N với Sản Phẩm")
public class CategoryRelationTest {

    private ICategoryService categoryService;
    private IProductService productService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl();
        productService = new ProductServiceImpl();
    }

    @Test
    @DisplayName("1. CRUD Danh mục Category")
    void testCategoryCrud() throws Exception {
        Category cate = new Category();
        cate.setCategoryname("Danh Mục Test JUnit");
        cate.setImages("cate_test.jpg");
        cate.setStatus(1);

        categoryService.insert(cate);
        assertTrue(cate.getCategoryid() > 0, "ID danh mục phải tự tăng");

        Category fetched = categoryService.findById(cate.getCategoryid());
        assertNotNull(fetched);
        assertEquals("Danh Mục Test JUnit", fetched.getCategoryname());

        fetched.setCategoryname("Danh Mục Đã Đổi Tên");
        categoryService.update(fetched);
        assertEquals("Danh Mục Đã Đổi Tên", categoryService.findById(cate.getCategoryid()).getCategoryname());

        categoryService.delete(cate.getCategoryid());
        assertNull(categoryService.findById(cate.getCategoryid()), "Danh mục phải bị xóa hoàn toàn");
    }

    @Test
    @DisplayName("2. Kiểm tra quan hệ 1 - N: Thêm sản phẩm vào danh mục và truy vấn hai chiều")
    void testOneToManyRelation() throws Exception {
        // Tạo category mới
        Category cate = new Category();
        cate.setCategoryname("Danh Mục 1-N Test");
        cate.setStatus(1);
        categoryService.insert(cate);

        // Tạo 2 sản phẩm thuộc category này
        Product p1 = new Product();
        p1.setProductName("SP Quan Hệ 1");
        p1.setPrice(100000.0);
        p1.setCategory(cate);
        p1.setCreateDate(new Date());
        productService.insert(p1);

        Product p2 = new Product();
        p2.setProductName("SP Quan Hệ 2");
        p2.setPrice(200000.0);
        p2.setCategory(cate);
        p2.setCreateDate(new Date());
        productService.insert(p2);

        // Kiểm tra Product -> Category (ManyToOne)
        Product fetchedP1 = productService.findById(p1.getProductId());
        assertNotNull(fetchedP1.getCategory());
        assertEquals(cate.getCategoryid(), fetchedP1.getCategory().getCategoryid());

        // Kiểm tra Category -> List<Product> (OneToMany) qua service
        List<Product> productsInCate = productService.findByCategoryId(cate.getCategoryid(), 1, 10);
        assertNotNull(productsInCate);
        assertEquals(2, productsInCate.size(), "Danh mục này phải có đúng 2 sản phẩm");

        long count = productService.countByCategoryId(cate.getCategoryid());
        assertEquals(2, count, "Đếm số sản phẩm trong danh mục phải ra 2");

        // Dọn dẹp
        productService.delete(p1.getProductId());
        productService.delete(p2.getProductId());
        categoryService.delete(cate.getCategoryid());
    }
}
