<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chỉnh sửa Người dùng</title>
</head>
<body>
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 class="fw-bold mb-1"><i class="fa-solid fa-user-pen text-warning me-2"></i>Chỉnh sửa Người dùng</h2>
                    <p class="text-muted mb-0">Cập nhật thông tin, thay đổi phân quyền và trạng thái tài khoản</p>
                </div>
                <a href="<c:url value='/admin/users'/>" class="btn btn-outline-secondary">
                    <i class="fa-solid fa-arrow-left me-1"></i> Quay lại danh sách
                </a>
            </div>

            <div class="card shadow-sm border-0">
                <div class="card-body p-4">
                    <form action="<c:url value='/admin/user/update'/>" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="id" value="${user.id}">

                        <div class="row g-3">
                            <!-- Username (Readonly) -->
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Tên đăng nhập</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-user text-muted"></i></span>
                                    <input type="text" class="form-control bg-light" value="${user.username}" readonly>
                                </div>
                                <div class="form-text text-muted">Tên đăng nhập không thể thay đổi sau khi tạo.</div>
                            </div>

                            <!-- New Password (Optional) -->
                            <div class="col-md-6">
                                <label for="password" class="form-label fw-semibold">Mật khẩu mới (Nếu muốn đổi)</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa-solid fa-key text-muted"></i></span>
                                    <input type="password" class="form-control ${not empty errors.password ? 'is-invalid' : ''}" 
                                           id="password" name="password" placeholder="Để trống nếu giữ mật khẩu cũ">
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
                                           id="fullname" name="fullname" value="${user.fullname}" required>
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
                                           id="email" name="email" value="${user.email}">
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
                                           id="phone" name="phone" value="${user.phone}">
                                </div>
                                <c:if test="${not empty errors.phone}">
                                    <div class="text-danger small mt-1"><i class="fa-solid fa-circle-exclamation me-1"></i>${errors.phone}</div>
                                </c:if>
                            </div>

                            <!-- Current Avatar & Upload New Avatar -->
                            <div class="col-md-12">
                                <label class="form-label fw-semibold">Ảnh đại diện hiện tại</label>
                                <div class="d-flex align-items-center gap-3 mb-2">
                                    <c:set var="imgUrl" value="${user.images}" />
                                    <c:if test="${not empty user.images && !user.images.startsWith('http')}">
                                        <c:url value="/image?fname=${user.images}" var="imgUrl" />
                                    </c:if>
                                    <img src="${imgUrl}" alt="${user.username}" class="rounded-circle border shadow-sm" 
                                         style="width: 64px; height: 64px; object-fit: cover;"
                                         onerror="this.onerror=null; this.src='https://placehold.co/80x80?text=User';" />
                                    <div class="flex-grow-1">
                                        <label for="images1" class="form-label small text-muted mb-1">Tải lên ảnh mới (Nếu muốn thay đổi):</label>
                                        <input type="file" class="form-control" id="images1" name="images1" accept="image/*">
                                    </div>
                                </div>
                            </div>

                            <!-- Role & Status -->
                            <div class="col-md-6">
                                <label class="form-label fw-semibold d-block">Vai trò (Role)</label>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="role" id="roleUser" value="0" ${user.role == 0 ? 'checked' : ''}>
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
                                    <input class="form-check-input" type="radio" name="status" id="statusActive" value="1" ${user.status == 1 ? 'checked' : ''}>
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
                                <button type="submit" class="btn btn-warning px-4">
                                    <i class="fa-solid fa-check me-1"></i> Cập nhật Thông tin
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
