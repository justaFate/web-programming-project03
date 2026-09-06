import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.User;
import service.IUserService;
import service.impl.UserServiceImpl;

@DisplayName("Test Xác Thực Tài Khoản & Kích Hoạt OTP Email")
public class UserAuthAndOtpTest {

    private IUserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
    }

    @Test
    @DisplayName("1. Đăng ký tài khoản mới: status = 0 (chưa kích hoạt) và có mã OTP 6 số")
    void testRegisterInactiveWithOtp() {
        String uniqueUser = "reg_test_" + System.currentTimeMillis();
        String uniqueEmail = uniqueUser + "@test.com";

        User user = new User();
        user.setUsername(uniqueUser);
        user.setPassword("Password123");
        user.setFullname("Người Dùng Test Đăng Ký");
        user.setEmail(uniqueEmail);
        user.setPhone("0912345678");

        boolean registered = userService.register(user);
        assertTrue(registered, "Đăng ký tài khoản mới phải thành công");

        User saved = userService.findByUsernameOrEmail(uniqueUser);
        assertNotNull(saved, "Phải tìm thấy user vừa đăng ký trong CSDL");
        assertEquals(0, saved.getStatus(), "Tài khoản mới đăng ký phải ở trạng thái chưa kích hoạt (status = 0)");
        assertNotNull(saved.getCode(), "Tài khoản mới phải có mã OTP xác thực");
        assertEquals(6, saved.getCode().length(), "Mã OTP phải gồm 6 chữ số");
    }

    @Test
    @DisplayName("2. Kích hoạt tài khoản bằng mã OTP sai -> thất bại")
    void testActivateWithWrongOtp() {
        String uniqueUser = "wrong_otp_" + System.currentTimeMillis();
        String uniqueEmail = uniqueUser + "@test.com";

        User user = new User();
        user.setUsername(uniqueUser);
        user.setPassword("Password123");
        user.setFullname("Test OTP Sai");
        user.setEmail(uniqueEmail);
        user.setPhone("0912345678");
        userService.register(user);

        boolean activated = userService.activateAccount(uniqueEmail, "000000");
        assertFalse(activated, "Kích hoạt bằng OTP sai phải trả về false");

        User saved = userService.findByUsernameOrEmail(uniqueUser);
        assertEquals(0, saved.getStatus(), "Tài khoản vẫn phải ở trạng thái chưa kích hoạt (status = 0)");
    }

    @Test
    @DisplayName("3. Kích hoạt tài khoản bằng mã OTP đúng -> thành công, status = 1, code = null")
    void testActivateWithCorrectOtp() {
        String uniqueUser = "correct_otp_" + System.currentTimeMillis();
        String uniqueEmail = uniqueUser + "@test.com";

        User user = new User();
        user.setUsername(uniqueUser);
        user.setPassword("Password123");
        user.setFullname("Test OTP Đúng");
        user.setEmail(uniqueEmail);
        user.setPhone("0912345678");
        userService.register(user);

        User saved = userService.findByUsernameOrEmail(uniqueUser);
        String correctOtp = saved.getCode();
        assertNotNull(correctOtp);

        boolean activated = userService.activateAccount(uniqueEmail, correctOtp);
        assertTrue(activated, "Kích hoạt bằng OTP đúng phải thành công");

        User activatedUser = userService.findByUsernameOrEmail(uniqueUser);
        assertEquals(1, activatedUser.getStatus(), "Tài khoản sau khi kích hoạt phải có status = 1");
        assertNull(activatedUser.getCode(), "Mã OTP phải được xóa (null) sau khi kích hoạt");
    }

    @Test
    @DisplayName("4. Kiểm tra logic đăng nhập: Tài khoản kích hoạt (status = 1) đăng nhập thành công")
    void testLoginWithActivatedAccount() {
        String uniqueUser = "login_test_" + System.currentTimeMillis();
        String uniqueEmail = uniqueUser + "@test.com";
        String pass = "SecurePass123";

        User user = new User();
        user.setUsername(uniqueUser);
        user.setPassword(pass);
        user.setFullname("Test Đăng Nhập");
        user.setEmail(uniqueEmail);
        user.setPhone("0909090909");
        userService.register(user);

        // Kích hoạt
        User saved = userService.findByUsernameOrEmail(uniqueUser);
        userService.activateAccount(uniqueEmail, saved.getCode());

        // Đăng nhập
        User loginUser = userService.login(uniqueUser, pass);
        assertNotNull(loginUser, "Đăng nhập phải thành công với tài khoản đã kích hoạt");
        assertEquals(1, loginUser.getStatus(), "User đăng nhập phải có status = 1");
    }

    @Test
    @DisplayName("5. Quên mật khẩu & Đặt lại mật khẩu mới bằng OTP")
    void testForgotPasswordAndResetPassword() {
        String uniqueUser = "forgot_test_" + System.currentTimeMillis();
        String uniqueEmail = uniqueUser + "@test.com";
        String oldPass = "OldPass123";
        String newPass = "NewPass456";

        User user = new User();
        user.setUsername(uniqueUser);
        user.setPassword(oldPass);
        user.setFullname("Test Quên Mật Khẩu");
        user.setEmail(uniqueEmail);
        user.setPhone("0933333333");
        userService.register(user);

        // Kích hoạt trước
        User saved = userService.findByUsernameOrEmail(uniqueUser);
        userService.activateAccount(uniqueEmail, saved.getCode());

        // Yêu cầu quên mật khẩu
        boolean otpSent = userService.sendForgotPasswordOtp(uniqueEmail);
        assertTrue(otpSent, "Gửi yêu cầu OTP quên mật khẩu phải thành công");

        User withOtp = userService.findByUsernameOrEmail(uniqueEmail);
        assertNotNull(withOtp.getCode(), "Phải sinh mã OTP mới khi yêu cầu quên mật khẩu");

        // Đổi mật khẩu với OTP sai
        boolean resetWrong = userService.resetPassword(uniqueEmail, "888888", newPass);
        assertFalse(resetWrong, "Đổi mật khẩu với OTP sai phải thất bại");

        // Đổi mật khẩu với OTP đúng
        boolean resetSuccess = userService.resetPassword(uniqueEmail, withOtp.getCode(), newPass);
        assertTrue(resetSuccess, "Đổi mật khẩu với OTP đúng phải thành công");

        // Mật khẩu cũ không còn dùng được
        User oldLogin = userService.login(uniqueUser, oldPass);
        assertNull(oldLogin, "Mật khẩu cũ không được phép đăng nhập");

        // Mật khẩu mới dùng được
        User newLogin = userService.login(uniqueUser, newPass);
        assertNotNull(newLogin, "Mật khẩu mới phải đăng nhập thành công");
    }
}
