<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Xác nhận mã OTP</title>
</head>
<body>
    <div class="row justify-content-center my-5">
        <div class="col-md-6 col-lg-5">
            <div class="card shadow border-0">
                <div class="card-header bg-dark text-white text-center py-3">
                    <h4 class="mb-0 fw-bold"><i class="fa-solid fa-shield-halved me-2 text-info"></i>Xác Nhận OTP</h4>
                    <p class="small text-muted mb-0 mt-1">Nhập mã xác thực để kích hoạt tài khoản</p>
                </div>
                <div class="card-body p-4">
                    <c:if test="${not empty message}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <i class="fa-solid fa-circle-check me-2"></i>${message}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fa-solid fa-circle-exclamation me-2"></i>${errorMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <p class="text-secondary text-center">
                        Mã xác nhận 6 chữ số đã được gửi tới email: <br/>
                        <strong class="text-primary">${email}</strong>
                    </p>

                    <form action="${pageContext.request.contextPath}/verify-otp" method="POST" class="needs-validation" novalidate>
                        <input type="hidden" name="email" value="${email}" />

                        <div class="mb-4">
                            <label for="otp" class="form-label fw-semibold text-center w-100">Mã Xác Nhận (OTP):</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-key"></i></span>
                                <input type="text" class="form-control text-center fw-bold fs-4 letter-spacing-lg" 
                                       id="otp" name="otp" maxlength="6" placeholder="000000" 
                                       style="letter-spacing: 0.35em;" required autofocus />
                            </div>
                            <div class="form-text text-center mt-2">Vui lòng kiểm tra hộp thư đến hoặc thư rác (Spam).</div>
                        </div>

                        <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold mb-3">
                            <i class="fa-solid fa-check me-1"></i> Kích hoạt tài khoản
                        </button>
                    </form>

                    <div class="text-center pt-2 border-top">
                        <span class="text-muted small">Chưa nhận được mã OTP? </span>
                        <a href="${pageContext.request.contextPath}/verify-otp?action=resend&email=${email}" class="text-decoration-none fw-semibold small">
                            <i class="fa-solid fa-rotate-right me-1"></i>Gửi lại mã
                        </a>
                    </div>
                </div>
                <div class="card-footer bg-light text-center py-2 text-muted small">
                    Đã kích hoạt? <a href="${pageContext.request.contextPath}/login" class="text-decoration-none fw-semibold">Đăng nhập ngay</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
