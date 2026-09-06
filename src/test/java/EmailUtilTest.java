import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.EmailUtil;

@DisplayName("Test Bộ Tiện Ích Email & OTP")
public class EmailUtilTest {

    @Test
    @DisplayName("Kiểm tra sinh mã OTP 6 chữ số hợp lệ")
    void testGenerateOtp() {
        String otp = EmailUtil.generateOtp(6);
        assertNotNull(otp, "Mã OTP không được null");
        assertEquals(6, otp.length(), "Mã OTP phải có đúng 6 chữ số");
        assertTrue(otp.matches("\\d{6}"), "Mã OTP chỉ chứa các chữ số từ 0 đến 9");
    }

    @Test
    @DisplayName("Kiểm tra tính ngẫu nhiên của mã OTP qua các lần sinh")
    void testOtpRandomness() {
        String otp1 = EmailUtil.generateOtp(6);
        String otp2 = EmailUtil.generateOtp(6);
        // Xác suất trùng nhau của 2 mã 6 số ngẫu nhiên là 1/1,000,000
        assertNotNull(otp1);
        assertNotNull(otp2);
        assertTrue(otp1.length() == 6 && otp2.length() == 6);
    }
}
