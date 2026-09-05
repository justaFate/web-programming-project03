<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng nhập hệ thống</title>
</head>
<body>
    <div class="row justify-content-center my-5">
        <div class="col-md-5 col-lg-4">
            <div class="card shadow border-0">
                <div class="card-header bg-dark text-white text-center py-3">
                    <h4 class="mb-0 fw-bold"><i class="fa-solid fa-lock me-2 text-info"></i>Đăng Nhập</h4>
                </div>
                <div class="card-body p-4">
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fa-solid fa-circle-exclamation me-2"></i>${errorMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/login" method="POST" class="needs-validation" novalidate>
                        <div class="mb-3">
                            <label for="username" class="form-label fw-semibold">Tên đăng nhập:</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-user"></i></span>
                                <input type="text" class="form-control ${not empty errors['username'] ? 'is-invalid' : ''}" 
                                       id="username" name="username" value="${username}" placeholder="Nhập username" required />
                            </div>
                            <c:if test="${not empty errors['username']}">
                                <div class="text-danger small mt-1">${errors['username']}</div>
                            </c:if>
                        </div>

                        <div class="mb-4">
                            <label for="password" class="form-label fw-semibold">Mật khẩu:</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-key"></i></span>
                                <input type="password" class="form-control ${not empty errors['password'] ? 'is-invalid' : ''}" 
                                       id="password" name="password" placeholder="Nhập mật khẩu" required />
                            </div>
                            <c:if test="${not empty errors['password']}">
                                <div class="text-danger small mt-1">${errors['password']}</div>
                            </c:if>
                        </div>

                        <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold">
                            <i class="fa-solid fa-arrow-right-to-bracket me-1"></i> Đăng nhập
                        </button>
                    </form>
                </div>
                <div class="card-footer bg-light text-center py-2 text-muted small">
                    Gợi ý: Đăng nhập tài khoản mặc định <code>trung</code> / <code>123</code> hoặc tài khoản JPA của bạn.
                </div>
            </div>
        </div>
    </div>
</body>
</html>

