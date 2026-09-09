import java.util.Date;
import java.util.List;

import model.Category;
import model.Product;
import model.User;
import service.ICategoryService;
import service.IProductService;
import service.IUserService;
import service.impl.CategoryServiceImpl;
import service.impl.ProductServiceImpl;
import service.impl.UserServiceImpl;

public class FullSystemTestSuite {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("          BẮT ĐẦU CHẠY BỘ KIỂM THỬ TOÀN DIỆN (JAVA TEST)          ");
        System.out.println("=================================================================\n");

        ICategoryService categoryService = new CategoryServiceImpl();
        IProductService productService = new ProductServiceImpl();
        IUserService userService = new UserServiceImpl();

        testCategoryAndRelation(categoryService, productService);
        testProductCrud(productService, categoryService);
        testTop10LatestProducts(productService);
        testProductPagination(productService);
        testProductDetail(productService);
        testUserRegisterAndOtpActivation(userService);
        testForgotPasswordAndResetOtp(userService);
        testCategorySearch(categoryService);
        testAdminUserCrudAndSearch(userService);

        System.out.println("\n=================================================================");
        System.out.println("                    KẾT QUẢ TỔNG HỢP KIỂM THỬ                   ");
        System.out.println("=================================================================");
        System.out.println("-> TỔNG SỐ TEST THÀNH CÔNG (PASSED): " + testsPassed);
        System.out.println("-> TỔNG SỐ TEST THẤT BẠI   (FAILED): " + testsFailed);
        if (testsFailed == 0) {
            System.out.println("\n>>> [CHÚC MỪNG] TOÀN BỘ CÁC CHỨC NĂNG ĐỀU ĐẠT CHUẨN 100%! <<<");
        } else {
            System.err.println("\n>>> [CẢNH BÁO] CÓ TEST CASE BỊ LỖI, VUI LÒNG KIỂM TRA LẠI! <<<");
        }
        System.out.println("=================================================================");
    }

    private static void assertTrue(String testName, boolean condition, String details) {
        if (condition) {
            System.out.println("[PASS] " + testName + (details != null ? " - " + details : ""));
            testsPassed++;
        } else {
            System.err.println("[FAIL] " + testName + (details != null ? " - " + details : ""));
            testsFailed++;
        }
    }

    // 1. Kiểm tra Category và quan hệ 1-N với Product
    private static void testCategoryAndRelation(ICategoryService cateService, IProductService prodService) {
        System.out.println("--- [TEST 1] Kiểm tra Quan hệ 1 - N giữa Category và Product ---");
        try {
            List<Category> categories = cateService.findAll();
            assertTrue("Category.findAll()", !categories.isEmpty(), "Số lượng danh mục hiện tại: " + categories.size());

            Category sampleCate = categories.get(0);
            List<Product> productsInCate = prodService.findByCategoryId(sampleCate.getCategoryid(), 1, 10);
            assertTrue("Category.ProductRelation", productsInCate != null, 
                    "Lấy danh sách sản phẩm thuộc danh mục [" + sampleCate.getCategoryname() + "]: " + productsInCate.size() + " sp");

        } catch (Exception e) {
            assertTrue("testCategoryAndRelation", false, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 2. Kiểm tra CRUD cho bảng Products
    private static void testProductCrud(IProductService prodService, ICategoryService cateService) {
        System.out.println("\n--- [TEST 2] Kiểm tra CRUD Bảng Products ---");
        try {
            List<Category> categories = cateService.findAll();
            Category defaultCate = categories.get(0);

            // C - Create
            Product newProd = new Product();
            newProd.setProductName("Sản Phẩm Test CRUD Tự Động");
            newProd.setDescription("Mô tả sản phẩm test phục vụ kiểm thử đơn vị");
            newProd.setPrice(150000.0);
            newProd.setImages("test-crud.png");
            newProd.setStatus(1);
            newProd.setCreateDate(new Date());
            newProd.setCategory(defaultCate);

            prodService.insert(newProd);
            int generatedId = newProd.getProductId();
            assertTrue("Product.Insert", generatedId > 0, "Đã thêm sản phẩm thành công với ID = " + generatedId);

            // R - Read
            Product fetched = prodService.findById(generatedId);
            assertTrue("Product.FindById", fetched != null && fetched.getProductName().equals("Sản Phẩm Test CRUD Tự Động"), 
                    "Đọc lại sản phẩm từ DB thành công: " + (fetched != null ? fetched.getProductName() : "null"));

            // U - Update
            fetched.setProductName("Sản Phẩm Test Đã Được Update");
            fetched.setPrice(199000.0);
            prodService.update(fetched);

            Product updated = prodService.findById(generatedId);
            assertTrue("Product.Update", updated != null && updated.getPrice() == 199000.0, 
                    "Cập nhật sản phẩm thành công, giá mới: " + (updated != null ? updated.getPrice() : 0));

            // D - Delete
            prodService.delete(generatedId);
            Product deleted = prodService.findById(generatedId);
            assertTrue("Product.Delete", deleted == null, "Xóa sản phẩm ID " + generatedId + " thành công!");

        } catch (Exception e) {
            assertTrue("testProductCrud", false, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 3. Kiểm tra Top 10 sản phẩm mới nhất lên trang chủ
    private static void testTop10LatestProducts(IProductService prodService) {
        System.out.println("\n--- [TEST 3] Kiểm tra Hiển thị 10 Sản phẩm Mới Nhất (Trang chủ) ---");
        try {
            List<Product> top10 = prodService.findTop10Latest();
            assertTrue("Product.Top10.NotNull", top10 != null, "Danh sách top 10 không rỗng");
            assertTrue("Product.Top10.MaxCount", top10.size() <= 10, "Số lượng sản phẩm lấy ra: " + top10.size() + " (<= 10)");

            boolean sortedCorrectly = true;
            for (int i = 0; i < top10.size() - 1; i++) {
                Product p1 = top10.get(i);
                Product p2 = top10.get(i + 1);
                if (p1.getCreateDate() != null && p2.getCreateDate() != null) {
                    if (p1.getCreateDate().before(p2.getCreateDate())) {
                        sortedCorrectly = false;
                        break;
                    }
                }
            }
            assertTrue("Product.Top10.SortedByLatest", sortedCorrectly, "Các sản phẩm được sắp xếp đúng thứ tự mới nhất trước");

        } catch (Exception e) {
            assertTrue("testTop10LatestProducts", false, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 4. Kiểm tra Phân trang 6 sản phẩm/trang (/product)
    private static void testProductPagination(IProductService prodService) {
        System.out.println("\n--- [TEST 4] Kiểm tra Phân Trang 6 Sản Phẩm / Trang (/product) ---");
        try {
            int pageSize = 6;
            long total = prodService.count();
            assertTrue("Product.TotalCount", total > 0, "Tổng số sản phẩm trong cơ sở dữ liệu: " + total);

            List<Product> page1 = prodService.findAll(1, pageSize);
            assertTrue("Product.Paging.Page1.NotNull", page1 != null, "Trang 1 lấy được dữ liệu");
            assertTrue("Product.Paging.Page1.Size", page1.size() <= 6, "Trang 1 có số lượng: " + page1.size() + " sp (tối đa 6)");

            if (total > 6) {
                List<Product> page2 = prodService.findAll(2, pageSize);
                assertTrue("Product.Paging.Page2.NotNull", page2 != null, "Trang 2 lấy được dữ liệu");
                assertTrue("Product.Paging.Page2.Size", page2.size() <= 6, "Trang 2 có số lượng: " + page2.size() + " sp (tối đa 6)");

                // Kiểm tra không trùng lặp giữa trang 1 và trang 2
                boolean duplicate = false;
                for (Product p1 : page1) {
                    for (Product p2 : page2) {
                        if (p1.getProductId() == p2.getProductId()) {
                            duplicate = true;
                            break;
                        }
                    }
                }
                assertTrue("Product.Paging.NoDuplicates", !duplicate, "Trang 1 và Trang 2 không bị trùng lặp sản phẩm");
            }

        } catch (Exception e) {
            assertTrue("testProductPagination", false, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 5. Kiểm tra Chi tiết sản phẩm
    private static void testProductDetail(IProductService prodService) {
        System.out.println("\n--- [TEST 5] Kiểm tra Xem Chi Tiết 01 Sản Phẩm ---");
        try {
            List<Product> all = prodService.findAll(1, 1);
            if (!all.isEmpty()) {
                Product sample = all.get(0);
                Product detail = prodService.findById(sample.getProductId());

                assertTrue("Product.Detail.NotNull", detail != null, "Tìm thấy sản phẩm ID = " + sample.getProductId());
                assertTrue("Product.Detail.HasName", detail.getProductName() != null && !detail.getProductName().isEmpty(), 
                        "Tên sản phẩm: " + detail.getProductName());
                assertTrue("Product.Detail.HasPrice", detail.getPrice() >= 0, "Giá sản phẩm: " + detail.getPrice() + " VND");
                assertTrue("Product.Detail.HasCategory", detail.getCategory() != null, 
                        "Danh mục: " + (detail.getCategory() != null ? detail.getCategory().getCategoryname() : "none"));
            }
        } catch (Exception e) {
            assertTrue("testProductDetail", false, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 6. Kiểm tra Đăng ký và Kích hoạt OTP
    private static void testUserRegisterAndOtpActivation(IUserService userService) {
        System.out.println("\n--- [TEST 6] Kiểm tra Đăng Ký & Kích Hoạt OTP Qua Email ---");
        try {
            String testUsername = "autouser_" + System.currentTimeMillis();
            String testEmail = testUsername + "@gmail.com";

            User user = new User();
            user.setUsername(testUsername);
            user.setPassword("Secret123");
            user.setFullname("Hệ Thống Test OTP");
            user.setEmail(testEmail);
            user.setPhone("0901234567");

            // Đăng ký
            boolean registered = userService.register(user);
            assertTrue("User.Register", registered, "Đăng ký tài khoản mới thành công!");

            // Kiểm tra trạng thái ban đầu: status = 0, code != null
            User saved = userService.findByUsernameOrEmail(testUsername);
            assertTrue("User.InitialStatusInactive", saved != null && saved.getStatus() == 0, 
                    "Tài khoản chưa kích hoạt (status = 0)");
            assertTrue("User.HasOtpCode", saved != null && saved.getCode() != null && saved.getCode().length() == 6, 
                    "Mã OTP 6 số đã được sinh và lưu: " + (saved != null ? saved.getCode() : "null"));

            // Kích hoạt bằng mã sai
            boolean wrongOtp = userService.activateAccount(testEmail, "000000");
            assertTrue("User.ActivateWithWrongOtp", !wrongOtp, "Kích hoạt bằng OTP sai bị từ chối thành công");

            // Kích hoạt bằng mã đúng
            String correctOtp = saved.getCode();
            boolean activateSuccess = userService.activateAccount(testEmail, correctOtp);
            assertTrue("User.ActivateWithCorrectOtp", activateSuccess, "Kích hoạt với OTP đúng thành công!");

            // Kiểm tra trạng thái sau kích hoạt: status = 1, code = null
            User activated = userService.findByUsernameOrEmail(testUsername);
            assertTrue("User.StatusActive", activated != null && activated.getStatus() == 1, 
                    "Tài khoản đã kích hoạt (status = 1)");
            assertTrue("User.OtpCleared", activated != null && activated.getCode() == null, 
                    "Mã OTP đã được xóa sau khi kích hoạt thành công");

            // Kiểm tra logic đăng nhập với status
            User loginUser = userService.login(testUsername, "Secret123");
            assertTrue("User.LoginAfterActivation", loginUser != null && loginUser.getStatus() == 1, 
                    "Đăng nhập thành công với tài khoản đã kích hoạt!");

        } catch (Exception e) {
            assertTrue("testUserRegisterAndOtpActivation", false, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 7. Kiểm tra Quên mật khẩu và Đặt lại mật khẩu bằng OTP
    private static void testForgotPasswordAndResetOtp(IUserService userService) {
        System.out.println("\n--- [TEST 7] Kiểm tra Quên Mật Khẩu & Đặt Lại Mật Khẩu Qua OTP ---");
        try {
            String testUsername = "forgotuser_" + System.currentTimeMillis();
            String testEmail = testUsername + "@gmail.com";

            User user = new User();
            user.setUsername(testUsername);
            user.setPassword("OldPass123");
            user.setFullname("Quên Mật Khẩu Test");
            user.setEmail(testEmail);
            user.setPhone("0988776655");
            userService.register(user);

            // Kích hoạt tài khoản trước
            User saved = userService.findByUsernameOrEmail(testUsername);
            userService.activateAccount(testEmail, saved.getCode());

            // Yêu cầu OTP Quên mật khẩu
            boolean forgotSent = userService.sendForgotPasswordOtp(testEmail);
            assertTrue("User.ForgotPassword.SendOtp", forgotSent, "Đã gửi yêu cầu OTP quên mật khẩu");

            User withOtp = userService.findByUsernameOrEmail(testEmail);
            assertTrue("User.ForgotPassword.OtpGenerated", withOtp != null && withOtp.getCode() != null, 
                    "OTP mới đã được tạo cho quên mật khẩu: " + (withOtp != null ? withOtp.getCode() : "null"));

            // Đổi mật khẩu với OTP sai
            boolean resetWrong = userService.resetPassword(testEmail, "999999", "NewPass456");
            assertTrue("User.ResetPassword.WrongOtpRejected", !resetWrong, "Đổi mật khẩu với OTP sai bị từ chối");

            // Đổi mật khẩu với OTP đúng
            boolean resetSuccess = userService.resetPassword(testEmail, withOtp.getCode(), "NewPass456");
            assertTrue("User.ResetPassword.Success", resetSuccess, "Đổi mật khẩu mới thành công!");

            // Đăng nhập lại với mật khẩu cũ -> phải thất bại
            User oldLogin = userService.login(testUsername, "OldPass123");
            assertTrue("User.OldPasswordInvalidated", oldLogin == null, "Mật khẩu cũ không còn đăng nhập được");

            // Đăng nhập lại với mật khẩu mới -> phải thành công
            User newLogin = userService.login(testUsername, "NewPass456");
            assertTrue("User.NewPasswordWorking", newLogin != null, "Đăng nhập thành công với mật khẩu mới!");

        } catch (Exception e) {
            assertTrue("testForgotPasswordAndResetOtp", false, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 8. Kiểm tra Tìm kiếm Category
    private static void testCategorySearch(ICategoryService cateService) {
        System.out.println("\n--- [TEST 8] Kiểm tra Chức năng Tìm kiếm Category ---");
        try {
            List<Category> all = cateService.findAll();
            if (!all.isEmpty()) {
                String name = all.get(0).getCategoryname();
                String searchKw = (name != null && name.length() > 2) ? name.substring(0, 3) : name;
                List<Category> results = cateService.searchByName(searchKw);
                assertTrue("Category.SearchByName", results != null && !results.isEmpty(), 
                        "Tìm kiếm theo từ khóa [" + searchKw + "] trả về: " + (results != null ? results.size() : 0) + " danh mục");
            } else {
                assertTrue("Category.SearchByName", true, "Chưa có danh mục nào để tìm kiếm");
            }
        } catch (Exception e) {
            assertTrue("testCategorySearch", false, "Exception: " + e.getMessage());
        }
    }

    // 9. Kiểm tra Admin CRUD & Tìm kiếm User
    private static void testAdminUserCrudAndSearch(IUserService userService) {
        System.out.println("\n--- [TEST 9] Kiểm tra CRUD và Tìm kiếm User trong Role Admin ---");
        String uniqueSuffix = String.valueOf(System.currentTimeMillis() % 100000);
        String username = "admin_u_" + uniqueSuffix;
        String email = "adminuser" + uniqueSuffix + "@gmail.com";

        try {
            // Thêm mới
            User user = new User();
            user.setUsername(username);
            user.setPassword("AdminPass123");
            user.setFullname("Quản Trị Viên Test " + uniqueSuffix);
            user.setEmail(email);
            user.setPhone("0987" + String.format("%06d", Integer.parseInt(uniqueSuffix)));
            user.setRole(1); // Admin
            user.setStatus(1); // Active
            user.setImages("avatar.png");

            userService.insert(user);
            User created = userService.findByUsername(username);
            assertTrue("AdminUser.Insert", created != null && created.getRole() == 1, 
                    "Thêm mới Admin User thành công, ID: " + (created != null ? created.getId() : "null"));

            if (created != null) {
                // Tìm kiếm theo username
                List<User> searchByUsername = userService.search(username);
                assertTrue("AdminUser.SearchByUsername", searchByUsername != null && !searchByUsername.isEmpty(), 
                        "Tìm thấy user qua username [" + username + "]");

                // Tìm kiếm theo họ tên
                List<User> searchByName = userService.search("Quản Trị Viên Test");
                assertTrue("AdminUser.SearchByName", searchByName != null && !searchByName.isEmpty(), 
                        "Tìm thấy user qua họ tên");

                // Cập nhật thông tin và role
                created.setFullname("Đã Đổi Tên " + uniqueSuffix);
                created.setRole(0); // đổi sang user
                userService.update(created);

                User updated = userService.findById(created.getId());
                assertTrue("AdminUser.Update", updated != null && ("Đã Đổi Tên " + uniqueSuffix).equals(updated.getFullname()) && updated.getRole() == 0,
                        "Cập nhật thông tin và phân quyền thành công");

                // Xóa
                userService.delete(created.getId());
                User deleted = userService.findById(created.getId());
                assertTrue("AdminUser.Delete", deleted == null, "Xóa Admin User thành công khỏi hệ thống");
            }
        } catch (Exception e) {
            assertTrue("testAdminUserCrudAndSearch", false, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
