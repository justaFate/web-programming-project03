<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Trang chủ - Cửa Hàng Trực Tuyến</title>
</head>
<body>
    <!-- Banner Hero -->
    <div class="p-5 mb-4 bg-primary text-white rounded-3 shadow-sm">
        <div class="container-fluid py-3">
            <h1 class="display-5 fw-bold"><i class="fa-solid fa-graduation-cap me-2"></i>Bài Tập 03 - Lập Trình Web</h1>
            <p class="col-md-9 fs-5">
                Dự án mẫu tích hợp đầy đủ <strong>SiteMesh Decorator 3</strong> với giao diện <strong>Bootstrap 5</strong>, 
                xử lý <strong>JPA Hibernate</strong>, quản trị sản phẩm phân trang và hệ thống xác thực tài khoản qua <strong>Email OTP</strong>.
            </p>
            <div class="d-flex gap-2 flex-wrap">
                <a class="btn btn-light btn-lg text-primary fw-semibold" href="<c:url value='/product'/>">
                    <i class="fa-solid fa-bag-shopping me-1"></i> Mua Sắm Ngay
                </a>
                <a class="btn btn-outline-light btn-lg" href="<c:url value='/admin/products'/>">
                    <i class="fa-solid fa-boxes-stacked me-1"></i> Quản lý Sản phẩm
                </a>
                <a class="btn btn-outline-light btn-lg" href="<c:url value='/profile'/>">
                    <i class="fa-solid fa-id-badge me-1"></i> Xem Profile
                </a>
            </div>
        </div>
    </div>

    <!-- Section: 10 Sản phẩm Mới Nhất -->
    <div class="mb-5">
        <div class="d-flex justify-content-between align-items-center mb-4 pb-2 border-bottom">
            <div>
                <h3 class="fw-bold mb-0 text-dark">
                    <i class="fa-solid fa-fire text-danger me-2"></i>Top 10 Sản Phẩm Mới Nhất
                </h3>
                <small class="text-muted">Các mặt hàng vừa được cập nhật tại cửa hàng</small>
            </div>
            <a href="<c:url value='/product'/>" class="btn btn-outline-primary btn-sm">
                Xem tất cả <i class="fa-solid fa-arrow-right ms-1"></i>
            </a>
        </div>

        <c:choose>
            <c:when test="${not empty topProducts}">
                <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-5 g-4">
                    <c:forEach items="${topProducts}" var="p">
                        <div class="col">
                            <div class="card h-100 shadow-sm border-0 product-card hover-shadow transition">
                                <c:set var="imgUrl" value="${p.images}" />
                                <c:if test="${not empty p.images && !p.images.startsWith('http')}">
                                    <c:url value="/image?fname=${p.images}" var="imgUrl" />
                                </c:if>
                                
                                <div class="position-relative overflow-hidden" style="height: 190px;">
                                    <a href="<c:url value='/product/detail?id=${p.productId}'/>">
                                        <img src="${imgUrl}" class="card-img-top w-100 h-100 object-fit-cover" alt="${p.productName}"
                                             onerror="this.onerror=null; this.src='https://placehold.co/300x200?text=No+Image';" />
                                    </a>
                                    <span class="position-absolute top-0 start-0 badge bg-danger m-2 shadow-sm">
                                        <i class="fa-solid fa-bolt me-1"></i>Mới
                                    </span>
                                </div>

                                <div class="card-body d-flex flex-column p-3">
                                    <c:if test="${not empty p.category}">
                                        <small class="text-muted mb-1 text-truncate">
                                            <i class="fa-solid fa-tag me-1"></i>${p.category.categoryname}
                                        </small>
                                    </c:if>

                                    <h6 class="card-title fw-bold text-truncate-2 mb-2" style="min-height: 40px;">
                                        <a href="<c:url value='/product/detail?id=${p.productId}'/>" class="text-dark text-decoration-none hover-primary">
                                            ${p.productName}
                                        </a>
                                    </h6>

                                    <div class="mt-auto pt-2 border-top d-flex justify-content-between align-items-center">
                                        <span class="fw-bold text-danger fs-6">
                                            <fmt:formatNumber value="${p.price}" pattern="#,##0" /> ₫
                                        </span>
                                        <a href="<c:url value='/product/detail?id=${p.productId}'/>" class="btn btn-sm btn-outline-primary" title="Xem chi tiết">
                                            <i class="fa-solid fa-eye"></i>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-light text-center py-5 shadow-sm border">
                    <i class="fa-solid fa-box-open fa-3x text-secondary mb-3"></i>
                    <p class="mb-0 text-muted">Chưa có sản phẩm nào được hiển thị. Vui lòng vào Quản lý Sản phẩm để thêm dữ liệu.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Feature Cards -->
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
                    <div class="text-success fs-1 mb-2"><i class="fa-solid fa-shield-halved"></i></div>
                    <h5 class="card-title fw-bold">Xác Thực OTP Email</h5>
                    <p class="card-text text-secondary">
                        Tích hợp gửi mã kích hoạt tài khoản và cấp lại mật khẩu mới qua Email với mã OTP an toàn.
                    </p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card h-100 border-0 shadow-sm">
                <div class="card-body">
                    <div class="text-warning fs-1 mb-2"><i class="fa-solid fa-boxes-packing"></i></div>
                    <h5 class="card-title fw-bold">Quản Lý Sản Phẩm</h5>
                    <p class="card-text text-secondary">
                        Phân loại danh mục 1 - N, tải ảnh sản phẩm bằng Multipart và phân trang 6 sản phẩm / trang tại trang mua sắm.
                    </p>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
