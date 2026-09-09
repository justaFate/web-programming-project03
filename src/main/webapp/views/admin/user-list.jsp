<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý Người dùng</title>
</head>
<body>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="fw-bold mb-1"><i class="fa-solid fa-users text-primary me-2"></i>Quản lý Người dùng</h2>
            <p class="text-muted mb-0">Quản lý tài khoản, phân quyền Role và trạng thái hoạt động của hệ thống</p>
        </div>
        <a href="<c:url value='/admin/user/add'/>" class="btn btn-primary">
            <i class="fa-solid fa-user-plus me-1"></i> Thêm Người dùng Mới
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

    <!-- Search Bar -->
    <div class="card shadow-sm border-0 mb-4">
        <div class="card-body p-3">
            <form action="<c:url value='/admin/users'/>" method="get" class="row g-2 align-items-center">
                <div class="col-md-9 col-sm-8">
                    <div class="input-group">
                        <span class="input-group-text bg-light border-end-0">
                            <i class="fa-solid fa-magnifying-glass text-muted"></i>
                        </span>
                        <input type="text" name="keyword" class="form-control border-start-0 ps-0" 
                               placeholder="Tìm theo Username, Họ tên, Email hoặc Số điện thoại..." value="${keyword}">
                    </div>
                </div>
                <div class="col-md-3 col-sm-4 d-flex gap-2">
                    <button type="submit" class="btn btn-primary flex-grow-1">
                        <i class="fa-solid fa-search me-1"></i>Tìm kiếm
                    </button>
                    <c:if test="${not empty keyword}">
                        <a href="<c:url value='/admin/users'/>" class="btn btn-outline-secondary" title="Đặt lại">
                            <i class="fa-solid fa-rotate-left"></i>
                        </a>
                    </c:if>
                </div>
            </form>
            <c:if test="${not empty keyword}">
                <div class="small text-muted mt-2">
                    <i class="fa-solid fa-filter me-1"></i>Kết quả tìm kiếm cho từ khóa: <strong class="text-dark">"${keyword}"</strong> (Tìm thấy <strong>${userList.size()}</strong> tài khoản)
                </div>
            </c:if>
        </div>
    </div>

    <!-- User Table -->
    <div class="card shadow-sm border-0">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th class="text-center" style="width: 5%;">#</th>
                            <th style="width: 10%;">Avatar</th>
                            <th style="width: 25%;">Tài khoản & Họ tên</th>
                            <th style="width: 25%;">Liên hệ (Email / SĐT)</th>
                            <th class="text-center" style="width: 12%;">Vai trò</th>
                            <th class="text-center" style="width: 10%;">Trạng thái</th>
                            <th class="text-center" style="width: 13%;">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${userList}" var="u" varStatus="stt">
                            <tr>
                                <td class="text-center fw-bold text-secondary">${stt.index + 1}</td>
                                <td>
                                    <c:set var="imgUrl" value="${u.images}" />
                                    <c:if test="${not empty u.images && !u.images.startsWith('http')}">
                                        <c:url value="/image?fname=${u.images}" var="imgUrl" />
                                    </c:if>
                                    <img src="${imgUrl}" alt="${u.username}" class="rounded-circle border shadow-sm" 
                                         style="width: 48px; height: 48px; object-fit: cover;"
                                         onerror="this.onerror=null; this.src='https://placehold.co/80x80?text=User';" />
                                </td>
                                <td>
                                    <div class="fw-bold text-dark">${u.fullname}</div>
                                    <div class="small text-muted"><i class="fa-solid fa-at me-1"></i>${u.username} (ID: ${u.id})</div>
                                </td>
                                <td>
                                    <div class="small text-dark"><i class="fa-solid fa-envelope me-1 text-secondary"></i>${not empty u.email ? u.email : '<span class="text-muted fst-italic">Chưa cập nhật</span>'}</div>
                                    <div class="small text-dark mt-1"><i class="fa-solid fa-phone me-1 text-secondary"></i>${not empty u.phone ? u.phone : '<span class="text-muted fst-italic">Chưa cập nhật</span>'}</div>
                                </td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${u.role == 1}">
                                            <span class="badge bg-danger-subtle text-danger border border-danger-subtle px-2 py-1">
                                                <i class="fa-solid fa-shield-halved me-1"></i>Admin
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-primary-subtle text-primary border border-primary-subtle px-2 py-1">
                                                <i class="fa-solid fa-user me-1"></i>User
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${u.status == 1}">
                                            <span class="badge bg-success-subtle text-success border border-success-subtle px-2 py-1">
                                                <i class="fa-solid fa-check-circle me-1"></i>Hoạt động
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary-subtle text-secondary border border-secondary-subtle px-2 py-1">
                                                <i class="fa-solid fa-lock me-1"></i>Khóa
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center">
                                    <div class="btn-group" role="group">
                                        <a href="<c:url value='/admin/user/edit?id=${u.id}'/>" class="btn btn-sm btn-outline-warning" title="Chỉnh sửa">
                                            <i class="fa-solid fa-pen-to-square"></i>
                                        </a>
                                        <a href="<c:url value='/admin/user/delete?id=${u.id}'/>" 
                                           class="btn btn-sm btn-outline-danger" 
                                           title="Xóa"
                                           onclick="return confirm('Bạn có chắc chắn muốn xóa tài khoản [${u.username}] không?');">
                                            <i class="fa-solid fa-trash"></i>
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty userList}">
                            <tr>
                                <td colspan="7" class="text-center py-4 text-muted">
                                    <i class="fa-solid fa-user-slash fa-2x mb-2 d-block"></i>
                                    Không tìm thấy người dùng nào phù hợp.
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
