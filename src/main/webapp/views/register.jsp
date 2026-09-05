<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng ký tài khoản</title>
</head>
<body>
    <div class="row justify-content-center my-4">
        <div class="col-md-7 col-lg-6">
            <div class="card shadow border-0">
                <div class="card-header bg-dark text-white text-center py-3">
                    <h4 class="mb-0 fw-bold"><i class="fa-solid fa-user-plus me-2 text-info"></i>Đăng Ký Tài Khoản</h4>
                    <p class="small text-muted mb-0 mt-1">Kích hoạt tài khoản bằng mã xác nhận OTP qua Email</p>
                </div>
                <div class="card-body p-4">
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fa-solid fa-circle-exclamation me-2"></i>${errorMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/register" method="POST" class="needs-validation" novalidate>
                        <!-- Username -->
                        <div class="mb-3">
                            <label for="username" class="form-label fw-semibold">Tên đăng nhập <span class="text-danger">*</span>:</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-user"></i></span>
                                <input type="text" class="form-control ${not empty errors['username'] ? 'is-invalid' : ''}" 
                                       id="username" name="username" value="${username}" placeholder="Nhập username từ 3-50 ký tự" required />
                            </div>
                            <c:if test="${not empty errors['username']}">
                                <div class="text-danger small mt-1">${errors['username']}</div>
                            </c:if>
                        </div>

                        <!-- Fullname -->
                        <div class="mb-3">
                            <label for="fullname" class="form-label fw-semibold">Họ và tên <span class="text-danger">*</span>:</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-signature"></i></span>
                                <input type="text" class="form-control ${not empty errors['fullname'] ? 'is-invalid' : ''}" 
                                       id="fullname" name="fullname" value="${fullname}" placeholder="Nhập họ và tên đầy đủ" required />
                            </div>
                            <c:if test="${not empty errors['fullname']}">
                                <div class="text-danger small mt-1">${errors['fullname']}</div>
                            </c:if>
                        </div>

                        <!-- Email -->
                        <div class="mb-3">
                            <label for="email" class="form-label fw-semibold">Địa chỉ Email <span class="text-danger">*</span>:</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                                <input type="email" class="form-control ${not empty errors['email'] ? 'is-invalid' : ''}" 
                                       id="email" name="email" value="${email}" placeholder="example@gmail.com" required />
                            </div>
                            <c:if test="${not empty errors['email']}">
                                <div class="text-danger small mt-1">${errors['email']}</div>
                            </c:if>
                            <div class="form-text">Mã OTP kích hoạt sẽ được gửi tới địa chỉ email này.</div>
                        </div>

                        <!-- Phone -->
                        <div class="mb-3">
                            <label for="phone" class="form-label fw-semibold">Số điện thoại:</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-phone"></i></span>
                                <input type="text" class="form-control ${not empty errors['phone'] ? 'is-invalid' : ''}" 
                                       id="phone" name="phone" value="${phone}" placeholder="Ví dụ: 0901234567" />
                            </div>
                            <c:if test="${not empty errors['phone']}">
                                <div class="text-danger small mt-1">${errors['phone']}</div>
                            </c:if>
                        </div>

                        <!-- Password -->
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="password" class="form-label fw-semibold">Mật khẩu <span class="text-danger">*</span>:</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fa-solid fa-lock"></i></span>
                                    <input type="password" class="form-control ${not empty errors['password'] ? 'is-invalid' : ''}" 
                                           id="password" name="password" placeholder="Tối thiểu 3 ký tự" required />
                                </div>
                                <c:if test="${not empty errors['password']}">
                                    <div class="text-danger small mt-1">${errors['password']}</div>
                                </c:if>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label for="confirmPassword" class="form-label fw-semibold">Xác nhận mật khẩu <span class="text-danger">*</span>:</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fa-solid fa-lock"></i></span>
                                    <input type="password" class="form-control ${not empty errors['confirmPassword'] ? 'is-invalid' : ''}" 
                                           id="confirmPassword" name="confirmPassword" placeholder="Nhập lại mật khẩu" required />
                                </div>
                                <c:if test="${not empty errors['confirmPassword']}">
                                    <div class="text-danger small mt-1">${errors['confirmPassword']}</div>
                                </c:if>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold mt-2">
                            <i class="fa-solid fa-paper-plane me-1"></i> Đăng Ký & Nhận Mã OTP
                        </button>
                    </form>

                    <div class="text-center mt-3 small">
                        <span>Đã có tài khoản? </span>
                        <a href="${pageContext.request.contextPath}/login" class="text-decoration-none fw-semibold">Đăng nhập ngay</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
