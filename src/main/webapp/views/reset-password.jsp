<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đặt lại mật khẩu mới</title>
</head>
<body>
    <div class="row justify-content-center my-5">
        <div class="col-md-6 col-lg-5">
            <div class="card shadow border-0">
                <div class="card-header bg-dark text-white text-center py-3">
                    <h4 class="mb-0 fw-bold"><i class="fa-solid fa-key me-2 text-warning"></i>Đặt Lại Mật Khẩu</h4>
                    <p class="small text-muted mb-0 mt-1">Nhập mã OTP và thiết lập mật khẩu mới</p>
                </div>
                <div class="card-body p-4">
                    <c:if test="${param.msg == 'otp_sent'}">
                        <div class="alert alert-info alert-dismissible fade show" role="alert">
                            <i class="fa-solid fa-envelope-circle-check me-2"></i>Mã OTP đã được gửi đến email của bạn. Vui lòng kiểm tra và nhập bên dưới.
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fa-solid fa-circle-exclamation me-2"></i>${errorMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/reset-password" method="POST" class="needs-validation" novalidate>
                        <div class="mb-3">
                            <label for="email" class="form-label fw-semibold">Địa chỉ Email:</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                                <input type="email" class="form-control" 
                                       id="email" name="email" value="${not empty email ? email : param.email}" 
                                       placeholder="example@gmail.com" required ${not empty email or not empty param.email ? 'readonly' : ''} />
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="otp" class="form-label fw-semibold">Mã OTP (6 chữ số):</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-shield-halved"></i></span>
                                <input type="text" class="form-control fw-bold text-center letter-spacing-lg" 
                                       id="otp" name="otp" maxlength="6" placeholder="000000" 
                                       style="letter-spacing: 0.3em;" required autofocus />
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="newPassword" class="form-label fw-semibold">Mật khẩu mới:</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-lock"></i></span>
                                <input type="password" class="form-control" 
                                       id="newPassword" name="newPassword" placeholder="Nhập tối thiểu 3 ký tự" required />
                            </div>
                        </div>

                        <div class="mb-4">
                            <label for="confirmPassword" class="form-label fw-semibold">Xác nhận mật khẩu mới:</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-lock"></i></span>
                                <input type="password" class="form-control" 
                                       id="confirmPassword" name="confirmPassword" placeholder="Nhập lại mật khẩu mới" required />
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold mb-3">
                            <i class="fa-solid fa-check me-1"></i> Cập nhật mật khẩu mới
                        </button>
                    </form>

                    <div class="text-center pt-2 border-top">
                        <a href="${pageContext.request.contextPath}/login" class="text-decoration-none small text-secondary">
                            <i class="fa-solid fa-arrow-left me-1"></i>Quay lại trang Đăng nhập
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
