<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hồ sơ người dùng</title>
    <style>
        .profile-avatar-preview {
            width: 140px;
            height: 140px;
            object-fit: cover;
            border-radius: 50%;
            border: 4px solid #fff;
            box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
        }
    </style>
</head>
<body>
    <div class="row justify-content-center">
        <!-- Cột thông tin tóm tắt bên trái -->
        <div class="col-lg-4 mb-4">
            <div class="card shadow-sm border-0 text-center p-4">
                <div class="position-relative d-inline-block mx-auto mb-3">
                    <c:set var="avatarUrl" value="${user.images}" />
                    <c:if test="${empty user.images}">
                        <c:set var="avatarUrl" value="avatar.png" />
                    </c:if>
                    <c:if test="${!avatarUrl.startsWith('http')}">
                        <c:url value="/image?fname=${avatarUrl}" var="avatarUrl" />
                    </c:if>
                    <img id="avatarDisplay" src="${avatarUrl}" alt="Avatar" class="profile-avatar-preview"
                         onerror="this.onerror=null; this.src='https://ui-avatars.com/api/?name=${user.fullname}&size=140&background=0D8ABC&color=fff';" />
                </div>
                <h4 class="fw-bold mb-1">${user.fullname}</h4>
                <p class="text-muted mb-2">@${user.username}</p>
                <div>
                    <span class="badge ${user.role == 1 ? 'bg-primary' : 'bg-secondary'} px-3 py-2">
                        <i class="fa-solid ${user.role == 1 ? 'fa-shield-halved' : 'fa-user'} me-1"></i>
                        ${user.role == 1 ? 'Quản trị viên (Admin)' : 'Thành viên'}
                    </span>
                </div>
                <hr class="my-3">
                <div class="text-start small">
                    <div class="mb-2">
                        <i class="fa-solid fa-phone text-primary me-2"></i>
                        <strong>Số điện thoại:</strong> 
                        <span class="text-secondary">${not empty user.phone ? user.phone : 'Chưa cập nhật'}</span>
                    </div>
                    <div class="mb-2">
                        <i class="fa-solid fa-id-badge text-primary me-2"></i>
                        <strong>ID Người dùng:</strong> 
                        <span class="text-secondary">#${user.id}</span>
                    </div>
                </div>
            </div>
        </div>

        <!-- Cột form cập nhật thông tin bên phải -->
        <div class="col-lg-8">
            <div class="card shadow-sm border-0">
                <div class="card-header bg-white border-bottom py-3">
                    <h5 class="mb-0 fw-bold text-dark">
                        <i class="fa-solid fa-user-pen text-primary me-2"></i>Cập nhật thông tin hồ sơ
                    </h5>
                </div>
                <div class="card-body p-4">
                    <c:if test="${not empty message}">
                        <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
                            <i class="fa-solid fa-circle-check me-2"></i>${message}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <c:if test="${not empty generalError}">
                        <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
                            <i class="fa-solid fa-triangle-exclamation me-2"></i>${generalError}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="<c:url value='/profile/update'/>" method="post" enctype="multipart/form-data" novalidate>
                        <!-- Username (Readonly) -->
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Tên đăng nhập (Username):</label>
                            <input type="text" class="form-control bg-light" value="${user.username}" readonly />
                            <div class="form-text">Tên đăng nhập là định danh cố định không thể thay đổi.</div>
                        </div>

                        <!-- Fullname -->
                        <div class="mb-3">
                            <label for="fullname" class="form-label fw-semibold">
                                Họ và tên <span class="text-danger">*</span>:
                            </label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-signature"></i></span>
                                <input type="text" 
                                       class="form-control ${not empty errors['fullname'] ? 'is-invalid' : ''}" 
                                       id="fullname" 
                                       name="fullname" 
                                       value="${user.fullname}" 
                                       placeholder="Nhập họ và tên đầy đủ" 
                                       required />
                            </div>
                            <c:if test="${not empty errors['fullname']}">
                                <div class="invalid-feedback d-block">
                                    <i class="fa-solid fa-circle-exclamation me-1"></i>${errors['fullname']}
                                </div>
                            </c:if>
                        </div>

                        <!-- Phone -->
                        <div class="mb-3">
                            <label for="phone" class="form-label fw-semibold">Số điện thoại:</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fa-solid fa-phone"></i></span>
                                <input type="text" 
                                       class="form-control ${not empty errors['phone'] ? 'is-invalid' : ''}" 
                                       id="phone" 
                                       name="phone" 
                                       value="${user.phone}" 
                                       placeholder="Ví dụ: 0901234567" />
                            </div>
                            <c:if test="${not empty errors['phone']}">
                                <div class="invalid-feedback d-block">
                                    <i class="fa-solid fa-circle-exclamation me-1"></i>${errors['phone']}
                                </div>
                            </c:if>
                            <div class="form-text">Nhập số điện thoại 10 chữ số tại Việt Nam (đầu 03, 05, 07, 08, 09).</div>
                        </div>

                        <!-- Upload Avatar Multipart -->
                        <div class="mb-4">
                            <label for="imageFile" class="form-label fw-semibold">Thay đổi ảnh đại diện (Upload Multipart):</label>
                            <input type="file" 
                                   class="form-control ${not empty errors['imageFile'] ? 'is-invalid' : ''}" 
                                   id="imageFile" 
                                   name="imageFile" 
                                   accept="image/*" 
                                   onchange="previewImage(this);" />
                            <c:if test="${not empty errors['imageFile']}">
                                <div class="invalid-feedback d-block">
                                    <i class="fa-solid fa-circle-exclamation me-1"></i>${errors['imageFile']}
                                </div>
                            </c:if>
                            <div class="form-text">Hỗ trợ định dạng: JPG, PNG, WEBP, GIF (Tối đa 10MB).</div>
                        </div>

                        <div class="d-flex justify-content-end gap-2">
                            <a href="<c:url value='/'/>" class="btn btn-outline-secondary px-4">Hủy bỏ</a>
                            <button type="submit" class="btn btn-primary px-4 fw-semibold">
                                <i class="fa-solid fa-check me-1"></i> Lưu Thay Đổi
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Script xem trước ảnh khi chọn file -->
    <script>
        function previewImage(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    var display = document.getElementById('avatarDisplay');
                    if (display) {
                        display.src = e.target.result;
                    }
                }
                reader.readAsDataURL(input.files[0]);
            }
        }
    </script>
</body>
</html>

