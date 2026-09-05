<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/">
            <i class="fa-solid fa-cubes text-info me-2"></i>Bài Tập 03
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/">
                        <i class="fa-solid fa-house me-1"></i> Trang chủ
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/categories">
                        <i class="fa-solid fa-list me-1"></i> Quản lý Danh mục
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/profile">
                        <i class="fa-solid fa-id-card me-1"></i> User Profile
                    </a>
                </li>
            </ul>

            <div class="d-flex align-items-center">
                <c:choose>
                    <c:when test="${not empty sessionScope.username}">
                        <div class="dropdown">
                            <button class="btn btn-outline-light btn-sm dropdown-toggle d-flex align-items-center gap-2" type="button" data-bs-toggle="dropdown">
                                <i class="fa-solid fa-circle-user fa-lg text-info"></i>
                                <span>${sessionScope.user.fullname != null ? sessionScope.user.fullname : sessionScope.username}</span>
                            </button>
                            <ul class="dropdown-menu dropdown-menu-end shadow">
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                        <i class="fa-solid fa-user-pen me-2 text-primary"></i>Hồ sơ cá nhân
                                    </a>
                                </li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                        <i class="fa-solid fa-right-from-bracket me-2"></i>Đăng xuất
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-sm">
                            <i class="fa-solid fa-right-to-bracket me-1"></i> Đăng nhập
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>

