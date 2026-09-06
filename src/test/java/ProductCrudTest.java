import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.Category;
import model.Product;
import service.ICategoryService;
import service.IProductService;
import service.impl.CategoryServiceImpl;
import service.impl.ProductServiceImpl;

@DisplayName("Test CRUD & Validation Bảng Products")
public class ProductCrudTest {

    private IProductService productService;
    private ICategoryService categoryService;
    private Validator validator;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl();
        categoryService = new CategoryServiceImpl();
        ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("1. Thêm mới sản phẩm (Create)")
    void testInsertProduct() throws Exception {
        List<Category> categories = categoryService.findAll();
        assertFalse(categories.isEmpty(), "Phải có ít nhất 1 Category trong CSDL");
        Category defaultCate = categories.get(0);

        Product product = new Product();
        product.setProductName("Sản Phẩm Test JUnit 5");
        product.setDescription("Mô tả sản phẩm test trong bộ JUnit");
        product.setPrice(250000.0);
        product.setImages("test_junit.jpg");
        product.setStatus(1);
        product.setCreateDate(new Date());
        product.setCategory(defaultCate);

        productService.insert(product);
        assertTrue(product.getProductId() > 0, "ID sản phẩm phải tự động tăng (> 0)");

        // Dọn dẹp
        productService.delete(product.getProductId());
    }

    @Test
    @DisplayName("2. Đọc thông tin sản phẩm theo ID (Read)")
    void testFindProductById() throws Exception {
        List<Category> categories = categoryService.findAll();
        Category defaultCate = categories.get(0);

        Product product = new Product();
        product.setProductName("Sản Phẩm Test Read");
        product.setPrice(120000.0);
        product.setCategory(defaultCate);
        productService.insert(product);

        Product fetched = productService.findById(product.getProductId());
        assertNotNull(fetched, "Phải tìm thấy sản phẩm vừa thêm");
        assertEquals("Sản Phẩm Test Read", fetched.getProductName());
        assertEquals(120000.0, fetched.getPrice());

        productService.delete(product.getProductId());
    }

    @Test
    @DisplayName("3. Cập nhật thông tin sản phẩm (Update)")
    void testUpdateProduct() throws Exception {
        List<Category> categories = categoryService.findAll();
        Category defaultCate = categories.get(0);

        Product product = new Product();
        product.setProductName("Sản Phẩm Trước Cập Nhật");
        product.setPrice(50000.0);
        product.setCategory(defaultCate);
        productService.insert(product);

        product.setProductName("Sản Phẩm Sau Cập Nhật");
        product.setPrice(99000.0);
        product.setDescription("Mô tả mới");
        productService.update(product);

        Product updated = productService.findById(product.getProductId());
        assertNotNull(updated);
        assertEquals("Sản Phẩm Sau Cập Nhật", updated.getProductName());
        assertEquals(99000.0, updated.getPrice());
        assertEquals("Mô tả mới", updated.getDescription());

        productService.delete(product.getProductId());
    }

    @Test
    @DisplayName("4. Xóa sản phẩm khỏi CSDL (Delete)")
    void testDeleteProduct() throws Exception {
        List<Category> categories = categoryService.findAll();
        Category defaultCate = categories.get(0);

        Product product = new Product();
        product.setProductName("Sản Phẩm Sẽ Bị Xóa");
        product.setPrice(10000.0);
        product.setCategory(defaultCate);
        productService.insert(product);

        int id = product.getProductId();
        assertNotNull(productService.findById(id));

        productService.delete(id);
        assertNull(productService.findById(id), "Sản phẩm sau khi xóa phải trả về null");
    }

    @Test
    @DisplayName("5. Bean Validation cho Product: Tên rỗng hoặc Giá âm phải bị bắt lỗi")
    void testProductBeanValidation() {
        Product invalidProduct = new Product();
        invalidProduct.setProductName(""); // rỗng
        invalidProduct.setPrice(-5000.0);   // âm

        Set<ConstraintViolation<Product>> violations = validator.validate(invalidProduct);
        assertFalse(violations.isEmpty(), "Validation phải phát hiện các trường không hợp lệ");

        boolean hasNameError = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("productName"));
        boolean hasPriceError = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("price"));

        assertTrue(hasNameError, "Phải có lỗi validate tên sản phẩm rỗng");
        assertTrue(hasPriceError, "Phải có lỗi validate giá sản phẩm âm");
    }
}
