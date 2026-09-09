<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thêm Người dùng Mới</title>
</head>
<body>
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 class="fw-bold mb-1"><i class="fa-solid fa-user-plus text-primary me-2"></i>Thêm Người dùng Mới</h2>
                    <p class="text-muted mb-0">Tạo mới tài khoản người dùng và thiết lập phân quyền quản trị</p>
                </div>
                <a href="<c:url value='/admin/users'/>" class="btn btn-outline-secondary">
                    <i class="fa-solid fa-arrow-left me-1"></i> Quay lại danh sách
                </a>
            </div>

            <div class="card shadow-sm border-0">
                <div class="card-body p-4">
                    <form action="<c:url value='/admin/user/insert'/>" method="post" enctype="multipart/form-data">
                        <div class="row g-3">
                            <!-- Username -->
                            <div class="col-md-6">
                                <label for="username" class="form-label fw-semibold">Tên đăng nhập <span class="text-danger">*</span></label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-user text-muted"></i></span>
                                    <input type="text" class="form-control ${not empty errors.username ? 'is-invalid' : ''}" 
                                           id="username" name="username" value="${user.username}" placeholder="Ví dụ: nguyenvana" required>
                                </div>
                                <c:if test="${not empty errors.username}">
                                    <div class="text-danger small mt-1"><i class="fa-solid fa-circle-exclamation me-1"></i>${errors.username}</div>
                                </c:if>
                            </div>

                            <!-- Password -->
                            <div class="col-md-6">
                                <label for="password" class="form-label fw-semibold">Mật khẩu <span class="text-danger">*</span></label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-lock text-muted"></i></span>
                                    <input type="password" class="form-control ${not empty errors.password ? 'is-invalid' : ''}" 
                                           id="password" name="password" placeholder="Tối thiểu 6 ký tự" required>
                                </div>
                                <c:if test="${not empty errors.password}">
                                    <div class="text-danger small mt-1"><i class="fa-solid fa-circle-exclamation me-1"></i>${errors.password}</div>
                                </c:if>
                            </div>

                            <!-- Fullname -->
                            <div class="col-md-12">
                                <label for="fullname" class="form-label fw-semibold">Họ và tên <span class="text-danger">*</span></label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-id-badge text-muted"></i></span>
                                    <input type="text" class="form-control ${not empty errors.fullname ? 'is-invalid' : ''}" 
                                           id="fullname" name="fullname" value="${user.fullname}" placeholder="Ví dụ: Nguyễn Văn A" required>
                                </div>
                                <c:if test="${not empty errors.fullname}">
                                    <div class="text-danger small mt-1"><i class="fa-solid fa-circle-exclamation me-1"></i>${errors.fullname}</div>
                                </c:if>
                            </div>

                            <!-- Email -->
                            <div class="col-md-6">
                                <label for="email" class="form-label fw-semibold">Email</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-envelope text-muted"></i></span>
                                    <input type="email" class="form-control ${not empty errors.email ? 'is-invalid' : ''}" 
                                           id="email" name="email" value="${user.email}" placeholder="example@domain.com">
                                </div>
                                <c:if test="${not empty errors.email}">
                                    <div class="text-danger small mt-1"><i class="fa-solid fa-circle-exclamation me-1"></i>${errors.email}</div>
                                </c:if>
                            </div>

                            <!-- Phone -->
                            <div class="col-md-6">
                                <label for="phone" class="form-label fw-semibold">Số điện thoại</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-phone text-muted"></i></span>
                                    <input type="tel" class="form-control ${not empty errors.phone ? 'is-invalid' : ''}" 
                                           id="phone" name="phone" value="${user.phone}" placeholder="Ví dụ: 0912345678">
                                </div>
                                <c:if test="${not empty errors.phone}">
                                    <div class="text-danger small mt-1"><i class="fa-solid fa-circle-exclamation me-1"></i>${errors.phone}</div>
                                </c:if>
                            </div>

                            <!-- Avatar Upload -->
                            <div class="col-md-12">
                                <label for="images1" class="form-label fw-semibold">Ảnh đại diện (Avatar)</label>
                                <input type="file" class="form-control" id="images1" name="images1" accept="image/*">
                                <div class="form-text">Hỗ trợ định dạng: JPG, PNG, WEBP, GIF. Kích thước tối đa 10MB.</div>
                            </div>

                            <!-- Role & Status -->
                            <div class="col-md-6">
                                <label class="form-label fw-semibold d-block">Vai trò (Role)</label>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="role" id="roleUser" value="0" ${user.role == 0 || empty user ? 'checked' : ''}>
                                    <label class="form-check-label" for="roleUser">
                                        <i class="fa-solid fa-user me-1 text-primary"></i>Khách hàng (User)
                                    </label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="role" id="roleAdmin" value="1" ${user.role == 1 ? 'checked' : ''}>
                                    <label class="form-check-label" for="roleAdmin">
                                        <i class="fa-solid fa-shield-halved me-1 text-danger"></i>Quản trị viên (Admin)
                                    </label>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-semibold d-block">Trạng thái tài khoản</label>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="status" id="statusActive" value="1" ${user.status == 1 || empty user ? 'checked' : ''}>
                                    <label class="form-check-label text-success fw-semibold" for="statusActive">
                                        <i class="fa-solid fa-check-circle me-1"></i>Kích hoạt (Active)
                                    </label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="status" id="statusInactive" value="0" ${user.status == 0 ? 'checked' : ''}>
                                    <label class="form-check-label text-danger fw-semibold" for="statusInactive">
                                        <i class="fa-solid fa-lock me-1"></i>Khóa (Inactive)
                                    </label>
                                </div>
                            </div>

                            <!-- Submit Button -->
                            <div class="col-12 mt-4 pt-2 border-top d-flex gap-2 justify-content-end">
                                <a href="<c:url value='/admin/users'/>" class="btn btn-light border px-4">Hủy bỏ</a>
                                <button type="submit" class="btn btn-primary px-4">
                                    <i class="fa-solid fa-save me-1"></i> Lưu Người dùng
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
