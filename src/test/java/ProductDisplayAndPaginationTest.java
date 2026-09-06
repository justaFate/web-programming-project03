import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.Product;
import service.IProductService;
import service.impl.ProductServiceImpl;

@DisplayName("Test Hiển Thị 10 Sản Phẩm Mới Nhất & Phân Trang 6 sp/trang")
public class ProductDisplayAndPaginationTest {

    private IProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl();
    }

    @Test
    @DisplayName("1. Hiển thị 10 sản phẩm mới nhất (Trang chủ): Tối đa 10 sp, sắp xếp mới nhất trước")
    void testFindTop10Latest() {
        List<Product> top10 = productService.findTop10Latest();
        assertNotNull(top10, "Danh sách top 10 sản phẩm mới nhất không được null");
        assertTrue(top10.size() <= 10, "Số lượng sản phẩm lấy ra không được vượt quá 10 (Hiện có: " + top10.size() + ")");

        // Kiểm tra thứ tự sắp xếp giảm dần (mới nhất trước)
        for (int i = 0; i < top10.size() - 1; i++) {
            Product curr = top10.get(i);
            Product next = top10.get(i + 1);
            if (curr.getCreateDate() != null && next.getCreateDate() != null) {
                assertFalse(curr.getCreateDate().before(next.getCreateDate()), 
                        "Sản phẩm trước phải có thời gian tạo mới hơn hoặc bằng sản phẩm sau");
            }
        }
    }

    @Test
    @DisplayName("2. Phân trang 6 sản phẩm / trang: Trang 1 và Trang 2 có đúng tối đa 6 sp và không trùng nhau")
    void testPagination6ProductsPerPage() {
        int pageSize = 6;
        long totalProducts = productService.count();
        assertTrue(totalProducts > 0, "Cơ sở dữ liệu phải có sản phẩm để kiểm thử phân trang");

        // Trang 1
        List<Product> page1 = productService.findAll(1, pageSize);
        assertNotNull(page1, "Trang 1 không được null");
        assertTrue(page1.size() <= 6, "Trang 1 phải có tối đa đúng 6 sản phẩm (Thực tế: " + page1.size() + ")");

        if (totalProducts > 6) {
            // Trang 2
            List<Product> page2 = productService.findAll(2, pageSize);
            assertNotNull(page2, "Trang 2 không được null");
            assertTrue(page2.size() <= 6, "Trang 2 phải có tối đa đúng 6 sản phẩm (Thực tế: " + page2.size() + ")");

            // Kiểm tra tính rời rạc (không trùng lặp sản phẩm giữa trang 1 và trang 2)
            Set<Integer> page1Ids = new HashSet<>();
            for (Product p : page1) {
                page1Ids.add(p.getProductId());
            }

            for (Product p : page2) {
                assertFalse(page1Ids.contains(p.getProductId()), 
                        "Sản phẩm ID " + p.getProductId() + " xuất hiện ở cả trang 1 và trang 2!");
            }
        }
    }

    @Test
    @DisplayName("3. Xem chi tiết 01 sản phẩm: Đầy đủ các trường thông tin tên, giá, ảnh, danh mục")
    void testProductDetail() {
        List<Product> list = productService.findAll(1, 1);
        assertFalse(list.isEmpty(), "Cần ít nhất 1 sản phẩm để test chi tiết");

        int sampleId = list.get(0).getProductId();
        Product detail = productService.findById(sampleId);

        assertNotNull(detail, "Phải tìm thấy sản phẩm ID = " + sampleId);
        assertNotNull(detail.getProductName(), "Tên sản phẩm không được rỗng");
        assertFalse(detail.getProductName().trim().isEmpty());
        assertTrue(detail.getPrice() >= 0, "Giá sản phẩm phải >= 0");
        assertNotNull(detail.getCategory(), "Sản phẩm phải có liên kết danh mục");
    }
}
