<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý Sản phẩm</title>
</head>
<body>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="fw-bold mb-1"><i class="fa-solid fa-boxes-stacked text-primary me-2"></i>Quản lý Sản phẩm</h2>
            <p class="text-muted mb-0">Quản lý kho hàng, giá cả và danh mục của sản phẩm</p>
        </div>
        <a href="<c:url value='/admin/product/add'/>" class="btn btn-primary">
            <i class="fa-solid fa-plus-circle me-1"></i> Thêm Sản phẩm Mới
        </a>
    </div>

    <c:if test="${not empty message || not empty sessionScope.message}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
            <i class="fa-solid fa-circle-check me-2"></i>${not empty message ? message : sessionScope.message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="message" scope="session" />
    </c:if>

    <c:if test="${not empty errorMessage || not empty sessionScope.errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
            <i class="fa-solid fa-circle-exclamation me-2"></i>${not empty errorMessage ? errorMessage : sessionScope.errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="errorMessage" scope="session" />
    </c:if>

    <div class="card shadow-sm border-0">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th class="text-center" style="width: 5%;">#</th>
                            <th style="width: 12%;">Hình ảnh</th>
                            <th style="width: 28%;">Tên sản phẩm</th>
                            <th style="width: 15%;">Danh mục</th>
                            <th style="width: 15%;">Đơn giá</th>
                            <th class="text-center" style="width: 10%;">Trạng thái</th>
                            <th class="text-center" style="width: 15%;">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${productList}" var="p" varStatus="stt">
                            <tr>
                                <td class="text-center fw-bold text-secondary">${stt.index + 1}</td>
                                <td>
                                    <c:set var="imgUrl" value="${p.images}" />
                                    <c:if test="${not empty p.images && !p.images.startsWith('http')}">
                                        <c:url value="/image?fname=${p.images}" var="imgUrl" />
                                    </c:if>
                                    <img src="${imgUrl}" alt="${p.productName}" class="rounded border shadow-sm" 
                                         style="width: 60px; height: 60px; object-fit: cover;" 
                                         onerror="this.onerror=null; this.src='https://placehold.co/80x80?text=No+Image';" />
                                </td>
                                <td>
                                    <span class="fw-semibold text-dark">${p.productName}</span>
                                    <div class="small text-muted">ID: #${p.productId}</div>
                                </td>
                                <td>
                                    <span class="badge bg-secondary-subtle text-secondary border">
                                        <i class="fa-solid fa-folder me-1"></i>${not empty p.category ? p.category.categoryname : 'Chưa phân loại'}
                                    </span>
                                </td>
                                <td>
                                    <span class="fw-bold text-danger">
                                        <fmt:formatNumber value="${p.price}" pattern="#,##0" /> ₫
                                    </span>
                                </td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${p.status == 1}">
                                            <span class="badge bg-success-subtle text-success border border-success-subtle px-2 py-1">
                                                <i class="fa-solid fa-check-circle me-1"></i>Đang bán
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger-subtle text-danger border border-danger-subtle px-2 py-1">
                                                <i class="fa-solid fa-pause me-1"></i>Tạm dừng
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center">
                                    <div class="btn-group" role="group">
                                        <a href="<c:url value='/admin/product/edit?id=${p.productId}'/>" class="btn btn-sm btn-outline-warning">
                                            <i class="fa-solid fa-pen-to-square me-1"></i>Sửa
                                        </a>
                                        <a href="<c:url value='/admin/product/delete?id=${p.productId}'/>" 
                                           class="btn btn-sm btn-outline-danger" 
                                           onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm [${p.productName}] không?');">
                                            <i class="fa-solid fa-trash me-1"></i>Xóa
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty productList}">
                            <tr>
                                <td colspan="7" class="text-center py-5 text-muted">
                                    <i class="fa-solid fa-box-open fa-3x mb-2 d-block text-secondary"></i>
                                    Chưa có sản phẩm nào. Hãy bấm "Thêm Sản phẩm Mới" để tạo!
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
