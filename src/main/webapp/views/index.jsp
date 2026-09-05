<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Trang chủ</title>
</head>
<body>
    <div class="p-5 mb-4 bg-primary text-white rounded-3 shadow-sm">
        <div class="container-fluid py-3">
            <h1 class="display-5 fw-bold"><i class="fa-solid fa-graduation-cap me-2"></i>Bài Tập 03 - Lập Trình Web</h1>
            <p class="col-md-9 fs-5">
                Dự án mẫu tích hợp đầy đủ <strong>SiteMesh Decorator 3</strong> với giao diện <strong>Bootstrap 5</strong>, 
                xử lý <strong>JPA Hibernate</strong>, chức năng <strong>User Profile (Multipart upload)</strong> và hệ thống <strong>Form Validation</strong>.
            </p>
            <div class="d-flex gap-2">
                <a class="btn btn-light btn-lg text-primary fw-semibold" href="<c:url value='/admin/categories'/>">
                    <i class="fa-solid fa-list me-1"></i> Quản lý Category
                </a>
                <a class="btn btn-outline-light btn-lg" href="<c:url value='/profile'/>">
                    <i class="fa-solid fa-id-badge me-1"></i> Xem Profile
                </a>
            </div>
        </div>
    </div>

    <div class="row g-4 mb-4">
        <div class="col-md-4">
            <div class="card h-100 border-0 shadow-sm">
                <div class="card-body">
                    <div class="text-primary fs-1 mb-2"><i class="fa-solid fa-palette"></i></div>
                    <h5 class="card-title fw-bold">SiteMesh 3 & Bootstrap</h5>
                    <p class="card-text text-secondary">
                        Áp dụng kiến trúc Decorator Pattern giúp thống nhất giao diện header, navigation, content và footer xuyên suốt ứng dụng.
                    </p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card h-100 border-0 shadow-sm">
                <div class="card-body">
                    <div class="text-success fs-1 mb-2"><i class="fa-solid fa-user-gear"></i></div>
                    <h5 class="card-title fw-bold">User Profile & JPA</h5>
                    <p class="card-text text-secondary">
                        Cập nhật họ tên, số điện thoại, tải lên ảnh đại diện qua MultipartConfig và đồng bộ trực tiếp vào cơ sở dữ liệu qua Hibernate JPA.
                    </p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card h-100 border-0 shadow-sm">
                <div class="card-body">
                    <div class="text-warning fs-1 mb-2"><i class="fa-solid fa-shield-check"></i></div>
                    <h5 class="card-title fw-bold">Form Validation</h5>
                    <p class="card-text text-secondary">
                        Kiểm soát chặt chẽ dữ liệu đầu vào cho các biểu mẫu (tên không rỗng, trùng lặp, định dạng SĐT Việt Nam, định dạng file ảnh).
                    </p>
                </div>
            </div>
        </div>
    </div>
</body>
</html>

