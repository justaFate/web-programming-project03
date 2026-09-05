<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>${product.productName} - Chi tiết sản phẩm</title>
</head>
<body>
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb bg-light p-3 rounded shadow-sm">
            <li class="breadcrumb-item"><a href="<c:url value='/home'/>" class="text-decoration-none"><i class="fa-solid fa-house me-1"></i>Trang chủ</a></li>
            <li class="breadcrumb-item"><a href="<c:url value='/product'/>" class="text-decoration-none">Sản phẩm</a></li>
            <c:if test="${not empty product.category}">
                <li class="breadcrumb-item">
                    <a href="<c:url value='/product?cid=${product.category.categoryid}'/>" class="text-decoration-none">
                        ${product.category.categoryname}
                    </a>
                </li>
            </c:if>
            <li class="breadcrumb-item active text-truncate" style="max-width: 300px;" aria-current="page">${product.productName}</li>
        </ol>
    </nav>

    <!-- Product Detail Container -->
    <div class="card shadow-sm border-0 mb-5">
        <div class="card-body p-4 p-md-5">
            <div class="row g-5">
                <!-- Cột Trái: Ảnh Sản Phẩm -->
                <div class="col-md-5 text-center">
                    <c:set var="imgUrl" value="${product.images}" />
                    <c:if test="${not empty product.images && !product.images.startsWith('http')}">
                        <c:url value="/image?fname=${product.images}" var="imgUrl" />
                    </c:if>
                    <div class="border rounded p-3 bg-white shadow-sm position-relative overflow-hidden mb-3">
                        <img src="${imgUrl}" alt="${product.productName}" 
                             class="img-fluid rounded" style="max-height: 400px; object-fit: contain;" 
                             onerror="this.onerror=null; this.src='https://placehold.co/500x500?text=No+Image';" />
                        <c:if test="${product.status == 1}">
                            <span class="position-absolute top-0 start-0 badge bg-success m-3 py-2 px-3 shadow-sm">
                                <i class="fa-solid fa-circle-check me-1"></i>Còn hàng
                            </span>
                        </c:if>
                        <c:if test="${product.status == 0}">
                            <span class="position-absolute top-0 start-0 badge bg-secondary m-3 py-2 px-3 shadow-sm">
                                <i class="fa-solid fa-circle-pause me-1"></i>Tạm ngưng bán
                            </span>
                        </c:if>
                    </div>
                </div>

                <!-- Cột Phải: Thông Tin Sản Phẩm -->
                <div class="col-md-7">
                    <c:if test="${not empty product.category}">
                        <div class="mb-2">
                            <span class="badge bg-primary-subtle text-primary border border-primary-subtle px-3 py-2">
                                <i class="fa-solid fa-tag me-1"></i>${product.category.categoryname}
                            </span>
                        </div>
                    </c:if>

                    <h2 class="fw-bold text-dark mb-3">${product.productName}</h2>

                    <div class="text-muted small mb-3">
                        <span class="me-3"><i class="fa-solid fa-barcode me-1"></i>Mã SP: <strong>#${product.productId}</strong></span>
                        <span><i class="fa-regular fa-clock me-1"></i>Ngày đăng: <fmt:formatDate value="${product.createDate}" pattern="dd/MM/yyyy HH:mm" /></span>
                    </div>

                    <!-- Đơn giá -->
                    <div class="bg-light p-3 rounded mb-4">
                        <span class="text-muted small d-block mb-1">Giá bán niêm yết:</span>
                        <span class="display-6 fw-bold text-danger">
                            <fmt:formatNumber value="${product.price}" pattern="#,##0" /> ₫
                        </span>
                    </div>

                    <!-- Mô tả sản phẩm -->
                    <div class="mb-4">
                        <h5 class="fw-semibold text-dark mb-2"><i class="fa-solid fa-align-left me-2 text-primary"></i>Mô tả chi tiết:</h5>
                        <div class="text-secondary leading-relaxed p-3 bg-light rounded" style="white-space: pre-line;">
                            ${not empty product.description ? product.description : 'Chưa có mô tả chi tiết cho sản phẩm này.'}
                        </div>
                    </div>

                    <!-- Nút thao tác -->
                    <div class="d-flex gap-3 flex-wrap pt-2 border-top">
                        <button class="btn btn-primary btn-lg px-4" type="button" onclick="alert('Đã thêm [${product.productName}] vào giỏ hàng!');">
                            <i class="fa-solid fa-cart-plus me-2"></i>Thêm vào giỏ hàng
                        </button>
                        <button class="btn btn-danger btn-lg px-4" type="button" onclick="alert('Chức năng mua ngay đang được cập nhật!');">
                            <i class="fa-solid fa-bolt me-2"></i>Mua ngay
                        </button>
                        <a href="<c:url value='/product'/>" class="btn btn-outline-secondary btn-lg px-4">
                            <i class="fa-solid fa-arrow-left me-1"></i> Quay lại
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Sản phẩm liên quan cùng danh mục -->
    <c:if test="${not empty relatedProducts}">
        <div class="mb-5">
            <h4 class="fw-bold mb-4 pb-2 border-bottom">
                <i class="fa-solid fa-link me-2 text-primary"></i>Sản Phẩm Cùng Danh Mục
            </h4>
            <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-4">
                <c:forEach items="${relatedProducts}" var="rp">
                    <div class="col">
                        <div class="card h-100 shadow-sm border-0 product-card hover-shadow transition">
                            <c:set var="rpImg" value="${rp.images}" />
                            <c:if test="${not empty rp.images && !rp.images.startsWith('http')}">
                                <c:url value="/image?fname=${rp.images}" var="rpImg" />
                            </c:if>
                            <div class="position-relative overflow-hidden" style="height: 180px;">
                                <a href="<c:url value='/product/detail?id=${rp.productId}'/>">
                                    <img src="${rpImg}" class="card-img-top w-100 h-100 object-fit-cover" alt="${rp.productName}"
                                         onerror="this.onerror=null; this.src='https://placehold.co/300x200?text=No+Image';" />
                                </a>
                            </div>
                            <div class="card-body p-3 d-flex flex-column">
                                <h6 class="card-title fw-bold text-truncate-2 mb-2">
                                    <a href="<c:url value='/product/detail?id=${rp.productId}'/>" class="text-dark text-decoration-none">
                                        ${rp.productName}
                                    </a>
                                </h6>
                                <div class="mt-auto d-flex justify-content-between align-items-center">
                                    <span class="fw-bold text-danger">
                                        <fmt:formatNumber value="${rp.price}" pattern="#,##0" /> ₫
                                    </span>
                                    <a href="<c:url value='/product/detail?id=${rp.productId}'/>" class="btn btn-sm btn-outline-primary">
                                        <i class="fa-solid fa-eye"></i>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:if>
</body>
</html>
