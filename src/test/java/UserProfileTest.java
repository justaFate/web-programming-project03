import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.User;
import service.IUserService;
import service.impl.UserServiceImpl;

@DisplayName("Test Chức Năng User Profile bằng JPA")
public class UserProfileTest {

    private IUserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
    }

    @Test
    @DisplayName("Cập nhật thông tin User Profile (fullname, phone, images) qua JPA")
    void testUpdateUserProfile() {
        String testUser = "profile_test_" + System.currentTimeMillis();
        String testEmail = testUser + "@gmail.com";

        User user = new User();
        user.setUsername(testUser);
        user.setPassword("123");
        user.setFullname("Tên Ban Đầu");
        user.setEmail(testEmail);
        user.setPhone("0901111111");
        user.setImages("old_avatar.png");
        user.setStatus(1);

        userService.register(user);

        // Đọc lại từ JPA
        User existing = userService.findByUsernameOrEmail(testUser);
        assertNotNull(existing);

        // Cập nhật Profile
        existing.setFullname("Tên Mới Sau Cập Nhật Profile");
        existing.setPhone("0988999888");
        existing.setImages("new_avatar_multipart.png");
        userService.update(existing);

        // Xác minh cập nhật
        User updated = userService.findByUsernameOrEmail(testUser);
        assertNotNull(updated);
        assertEquals("Tên Mới Sau Cập Nhật Profile", updated.getFullname());
        assertEquals("0988999888", updated.getPhone());
        assertEquals("new_avatar_multipart.png", updated.getImages());
    }
}
