<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quên mật khẩu</title>
</head>
<body>
    <div class="row justify-content-center my-5">
        <div class="col-md-5 col-lg-4">
            <div class="card shadow border-0">
                <div class="card-header bg-dark text-white text-center py-3">
                    <h4 class="mb-0 fw-bold"><i class="fa-solid fa-unlock-keyhole me-2 text-warning"></i>Quên Mật Khẩu</h4>
                    <p class="small text-muted mb-0 mt-1">Khôi phục mật khẩu thông qua mã OTP Email</p>
                </div>
                <div class="card-body p-4">
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fa-solid fa-circle-exclamation me-2"></i>${errorMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/forgot-password" method="POST" class="needs-validation" novalidate>
                        <div class="mb-4">
                            <label for="emailOrUsername" class="form-label fw-semibold">Tên đăng nhập hoặc Email:</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-user-tag"></i></span>
                                <input type="text" class="form-control" 
                                       id="emailOrUsername" name="emailOrUsername" value="${emailOrUsername}" 
                                       placeholder="Nhập username hoặc email" required autofocus />
                            </div>
                            <div class="form-text mt-2">Hệ thống sẽ gửi mã OTP 6 chữ số đến email đã đăng ký của bạn.</div>
                        </div>

                        <button type="submit" class="btn btn-warning text-dark w-100 py-2 fw-semibold mb-3">
                            <i class="fa-solid fa-paper-plane me-1"></i> Gửi mã OTP xác nhận
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
