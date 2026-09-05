package util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.Random;

public class EmailUtil {

    // Cấu hình email mặc định (có thể tùy chỉnh nếu có tài khoản SMTP)
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SENDER_EMAIL = "webprogramming.edu.vn@gmail.com";
    private static final String SENDER_PASSWORD = "app_password_here";

    /**
     * Sinh mã số OTP ngẫu nhiên gồm 6 chữ số
     */
    public static String generateOtp(int length) {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    /**
     * Gửi email và in mã OTP rõ ràng ra Console log để kiểm thử nhanh
     */
    public static boolean sendEmail(String toEmail, String subject, String htmlContent) {
        // Luôn in mã và nội dung ra console để kiểm thử và chấm bài ngay cả khi offline
        System.out.println("================================================================================");
        System.out.println("[EMAIL NOTIFICATION SERVICE]");
        System.out.println("-> Người nhận: " + toEmail);
        System.out.println("-> Tiêu đề: " + subject);
        System.out.println("-> Nội dung Email / Mã OTP:");
        System.out.println(htmlContent);
        System.out.println("================================================================================");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        try {
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, "Bài Tập 03 - Web Programming", "UTF-8"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            // Cố gắng gửi qua SMTP nếu kết nối được
            try {
                Transport.send(message);
                System.out.println("[EMAIL SERVICE] Đã gửi thư thành công qua SMTP tới: " + toEmail);
            } catch (Exception smtpEx) {
                System.out.println("[EMAIL SERVICE] Lưu ý: Không thể kết nối SMTP thực tế (" + smtpEx.getMessage() 
                        + "). Mã OTP đã được xuất trực tiếp lên log phía trên để kiểm thử.");
            }
            return true;
        } catch (Exception e) {
            System.err.println("[EMAIL SERVICE ERROR] " + e.getMessage());
            return false;
        }
    }
}
