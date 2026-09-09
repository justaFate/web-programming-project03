<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Danh sách Category</title>
</head>
<body>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="fw-bold mb-1"><i class="fa-solid fa-list-check text-primary me-2"></i>Quản lý Danh mục</h2>
            <p class="text-muted mb-0">Quản lý và cập nhật danh sách thể loại sản phẩm</p>
        </div>
        <a href="<c:url value='/admin/category/add'/>" class="btn btn-primary">
            <i class="fa-solid fa-plus-circle me-1"></i> Thêm Danh mục Mới
        </a>
    </div>

    <c:if test="${not empty message}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
            <i class="fa-solid fa-circle-check me-2"></i>${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${not empty errorMessage || not empty sessionScope.errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
            <i class="fa-solid fa-circle-exclamation me-2"></i>${not empty errorMessage ? errorMessage : sessionScope.errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="errorMessage" scope="session" />
    </c:if>

    <!-- Search Bar -->
    <div class="card shadow-sm border-0 mb-4">
        <div class="card-body p-3">
            <form action="<c:url value='/admin/categories'/>" method="get" class="row g-2 align-items-center">
                <div class="col-md-9 col-sm-8">
                    <div class="input-group">
                        <span class="input-group-text bg-light border-end-0">
                            <i class="fa-solid fa-magnifying-glass text-muted"></i>
                        </span>
                        <input type="text" name="keyword" class="form-control border-start-0 ps-0" 
                               placeholder="Tìm kiếm danh mục theo tên..." value="${keyword}">
                    </div>
                </div>
                <div class="col-md-3 col-sm-4 d-flex gap-2">
                    <button type="submit" class="btn btn-primary flex-grow-1">
                        <i class="fa-solid fa-search me-1"></i>Tìm kiếm
                    </button>
                    <c:if test="${not empty keyword}">
                        <a href="<c:url value='/admin/categories'/>" class="btn btn-outline-secondary" title="Đặt lại">
                            <i class="fa-solid fa-rotate-left"></i>
                        </a>
                    </c:if>
                </div>
            </form>
            <c:if test="${not empty keyword}">
                <div class="small text-muted mt-2">
                    <i class="fa-solid fa-filter me-1"></i>Kết quả tìm kiếm cho từ khóa: <strong class="text-dark">"${keyword}"</strong> (Tìm thấy <strong>${listcate.size()}</strong> danh mục)
                </div>
            </c:if>
        </div>
    </div>

    <div class="card shadow-sm border-0">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th class="text-center" style="width: 5%;">#</th>
                            <th style="width: 20%;">Hình ảnh</th>
                            <th style="width: 35%;">Tên danh mục</th>
                            <th class="text-center" style="width: 15%;">Trạng thái</th>
                            <th class="text-center" style="width: 25%;">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${listcate}" var="cate" varStatus="STT">
                            <tr>
                                <td class="text-center fw-bold text-secondary">${STT.index + 1}</td>
                                <td>
                                    <c:set var="imgUrl" value="${cate.images}" />
                                    <c:if test="${not empty cate.images && !cate.images.startsWith('http')}">
                                        <c:url value="/image?fname=${cate.images}" var="imgUrl" />
                                    </c:if>
                                    <img class="table-avatar shadow-sm border" src="${imgUrl}" alt="${cate.categoryname}" 
                                         onerror="this.onerror=null; this.src='https://placehold.co/120x80?text=No+Image';" />
                                </td>
                                <td>
                                    <span class="fw-semibold text-dark">${cate.categoryname}</span>
                                    <div class="small text-muted">ID: ${cate.categoryid}</div>
                                </td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${cate.status == 1}">
                                            <span class="badge bg-success-subtle text-success border border-success-subtle px-3 py-2">
                                                <i class="fa-solid fa-check-circle me-1"></i>Hoạt động
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger-subtle text-danger border border-danger-subtle px-3 py-2">
                                                <i class="fa-solid fa-lock me-1"></i>Khóa
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center">
                                    <div class="btn-group" role="group">
                                        <a href="<c:url value='/admin/category/edit?id=${cate.categoryid}'/>" class="btn btn-sm btn-outline-warning">
                                            <i class="fa-solid fa-pen-to-square me-1"></i>Sửa
                                        </a>
                                        <a href="<c:url value='/admin/category/delete?id=${cate.categoryid}'/>" 
                                           class="btn btn-sm btn-outline-danger" 
                                           onclick="return confirm('Bạn có chắc chắn muốn xóa danh mục [${cate.categoryname}] không?');">
                                            <i class="fa-solid fa-trash me-1"></i>Xóa
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listcate}">
                            <tr>
                                <td colspan="5" class="text-center py-4 text-muted">
                                    <i class="fa-solid fa-inbox fa-2x mb-2 d-block"></i>
                                    Chưa có danh mục nào. Hãy bấm "Thêm Danh mục Mới" để tạo.
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

