package util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

public class EmailUtil {

    private static String smtpHost = "smtp.gmail.com";
    private static String smtpPort = "587";
    private static String senderEmail = "webprogramming.edu.vn@gmail.com";
    private static String senderPassword = "app_password_here";

    static {
        loadConfig();
    }

    public static void loadConfig() {
        try (InputStream input = EmailUtil.class.getClassLoader().getResourceAsStream("email.properties")) {
            if (input != null) {
                Properties prop = new Properties();
                prop.load(input);
                if (prop.getProperty("SMTP_HOST") != null && !prop.getProperty("SMTP_HOST").isBlank()) {
                    smtpHost = prop.getProperty("SMTP_HOST").trim();
                }
                if (prop.getProperty("SMTP_PORT") != null && !prop.getProperty("SMTP_PORT").isBlank()) {
                    smtpPort = prop.getProperty("SMTP_PORT").trim();
                }
                if (prop.getProperty("SMTP_EMAIL") != null && !prop.getProperty("SMTP_EMAIL").isBlank()) {
                    senderEmail = prop.getProperty("SMTP_EMAIL").trim();
                }
                if (prop.getProperty("SMTP_PASSWORD") != null && !prop.getProperty("SMTP_PASSWORD").isBlank()) {
                    senderPassword = prop.getProperty("SMTP_PASSWORD").trim();
                }
            }
        } catch (Exception e) {
            System.err.println("[EMAIL CONFIG] Không thể nạp email.properties: " + e.getMessage());
        }

        if (System.getenv("SMTP_HOST") != null && !System.getenv("SMTP_HOST").isBlank()) {
            smtpHost = System.getenv("SMTP_HOST");
        }
        if (System.getenv("SMTP_PORT") != null && !System.getenv("SMTP_PORT").isBlank()) {
            smtpPort = System.getenv("SMTP_PORT");
        }
        if (System.getenv("SMTP_EMAIL") != null && !System.getenv("SMTP_EMAIL").isBlank()) {
            senderEmail = System.getenv("SMTP_EMAIL");
        } else if (System.getenv("SENDER_EMAIL") != null && !System.getenv("SENDER_EMAIL").isBlank()) {
            senderEmail = System.getenv("SENDER_EMAIL");
        }
        if (System.getenv("SMTP_PASSWORD") != null && !System.getenv("SMTP_PASSWORD").isBlank()) {
            senderPassword = System.getenv("SMTP_PASSWORD");
        } else if (System.getenv("SENDER_PASSWORD") != null && !System.getenv("SENDER_PASSWORD").isBlank()) {
            senderPassword = System.getenv("SENDER_PASSWORD");
        }
    }

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
        // Luôn in mã và nội dung ra console để kiểm thử 
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
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        try {
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail, "Bài Tập 03 - Web Programming", "UTF-8"));
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
