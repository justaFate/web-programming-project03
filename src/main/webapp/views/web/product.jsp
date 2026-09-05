<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Danh sách Sản phẩm</title>
</head>
<body>
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb bg-light p-3 rounded shadow-sm">
            <li class="breadcrumb-item"><a href="<c:url value='/home'/>" class="text-decoration-none"><i class="fa-solid fa-house me-1"></i>Trang chủ</a></li>
            <li class="breadcrumb-item active" aria-current="page">Sản phẩm</li>
            <c:if test="${not empty selectedCid}">
                <c:forEach items="${categories}" var="c">
                    <c:if test="${c.categoryid == selectedCid}">
                        <li class="breadcrumb-item active text-primary fw-semibold">${c.categoryname}</li>
                    </c:if>
                </c:forEach>
            </c:if>
        </ol>
    </nav>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fa-solid fa-circle-exclamation me-2"></i>${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="row">
        <!-- Sidebar Danh mục -->
        <div class="col-lg-3 mb-4">
            <div class="card shadow-sm border-0 sticky-top" style="top: 20px;">
                <div class="card-header bg-dark text-white py-3">
                    <h5 class="mb-0 fw-bold"><i class="fa-solid fa-layer-group me-2 text-info"></i>Danh Mục</h5>
                </div>
                <div class="list-group list-group-flush">
                    <a href="<c:url value='/product'/>" 
                       class="list-group-item list-group-item-action d-flex justify-content-between align-items-center ${empty selectedCid ? 'active fw-bold' : ''}">
                        <span><i class="fa-solid fa-border-all me-2"></i>Tất cả sản phẩm</span>
                    </a>
                    <c:forEach items="${categories}" var="c">
                        <a href="<c:url value='/product?cid=${c.categoryid}'/>" 
                           class="list-group-item list-group-item-action d-flex justify-content-between align-items-center ${selectedCid == c.categoryid ? 'active fw-bold' : ''}">
                            <span><i class="fa-solid fa-angle-right me-2 small"></i>${c.categoryname}</span>
                        </a>
                    </c:forEach>
                </div>
            </div>
        </div>

        <!-- Danh sách Sản phẩm (6 sp / trang) -->
        <div class="col-lg-9">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h4 class="fw-bold mb-0 text-dark">
                    <i class="fa-solid fa-boxes-stacked text-primary me-2"></i>
                    <c:choose>
                        <c:when test="${not empty selectedCid}">
                            <c:forEach items="${categories}" var="c">
                                <c:if test="${c.categoryid == selectedCid}">${c.categoryname}</c:if>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>Tất Cả Sản Phẩm</c:otherwise>
                    </c:choose>
                </h4>
                <div class="text-muted small">
                    Tổng cộng: <strong>${totalProducts}</strong> sản phẩm | Phân trang: <strong>6 sp/trang</strong>
                </div>
            </div>

            <c:choose>
                <c:when test="${not empty products}">
                    <!-- Lưới 6 sản phẩm -->
                    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-4 mb-4">
                        <c:forEach items="${products}" var="p">
                            <div class="col">
                                <div class="card h-100 shadow-sm border-0 product-card hover-shadow transition">
                                    <c:set var="imgUrl" value="${p.images}" />
                                    <c:if test="${not empty p.images && !p.images.startsWith('http')}">
                                        <c:url value="/image?fname=${p.images}" var="imgUrl" />
                                    </c:if>

                                    <div class="position-relative overflow-hidden" style="height: 210px;">
                                        <a href="<c:url value='/product/detail?id=${p.productId}'/>">
                                            <img src="${imgUrl}" class="card-img-top w-100 h-100 object-fit-cover" alt="${p.productName}"
                                                 onerror="this.onerror=null; this.src='https://placehold.co/300x200?text=No+Image';" />
                                        </a>
                                        <c:if test="${p.status == 0}">
                                            <span class="position-absolute top-0 end-0 badge bg-secondary m-2 shadow-sm">
                                                Tạm hết
                                            </span>
                                        </c:if>
                                    </div>

                                    <div class="card-body d-flex flex-column p-3">
                                        <c:if test="${not empty p.category}">
                                            <small class="text-muted mb-1 text-truncate">
                                                <i class="fa-solid fa-tag me-1"></i>${p.category.categoryname}
                                            </small>
                                        </c:if>

                                        <h5 class="card-title fw-bold fs-6 text-truncate-2 mb-2" style="min-height: 44px;">
                                            <a href="<c:url value='/product/detail?id=${p.productId}'/>" class="text-dark text-decoration-none hover-primary">
                                                ${p.productName}
                                            </a>
                                        </h5>

                                        <p class="card-text small text-secondary text-truncate-2 mb-3">
                                            ${p.description}
                                        </p>

                                        <div class="mt-auto pt-2 border-top d-flex justify-content-between align-items-center">
                                            <span class="fw-bold text-danger fs-5">
                                                <fmt:formatNumber value="${p.price}" pattern="#,##0" /> ₫
                                            </span>
                                            <a href="<c:url value='/product/detail?id=${p.productId}'/>" class="btn btn-sm btn-primary">
                                                <i class="fa-solid fa-circle-info me-1"></i>Chi tiết
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Phân trang Bootstrap 5 (Pagination) -->
                    <c:if test="${totalPages > 1}">
                        <nav aria-label="Page navigation" class="mt-4">
                            <ul class="pagination justify-content-center">
                                <!-- First Page -->
                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <c:url value="/product" var="firstUrl">
                                        <c:param name="page" value="1" />
                                        <c:if test="${not empty selectedCid}">
                                            <c:param name="cid" value="${selectedCid}" />
                                        </c:if>
                                    </c:url>
                                    <a class="page-item page-link" href="${firstUrl}">
                                        <i class="fa-solid fa-angles-left"></i>
                                    </a>
                                </li>

                                <!-- Previous Page -->
                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <c:url value="/product" var="prevUrl">
                                        <c:param name="page" value="${currentPage - 1}" />
                                        <c:if test="${not empty selectedCid}">
                                            <c:param name="cid" value="${selectedCid}" />
                                        </c:if>
                                    </c:url>
                                    <a class="page-link" href="${prevUrl}">
                                        <i class="fa-solid fa-angle-left"></i>
                                    </a>
                                </li>

                                <!-- Page Numbers -->
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <c:url value="/product" var="numUrl">
                                        <c:param name="page" value="${i}" />
                                        <c:if test="${not empty selectedCid}">
                                            <c:param name="cid" value="${selectedCid}" />
                                        </c:if>
                                    </c:url>
                                    <li class="page-item ${currentPage == i ? 'active fw-bold' : ''}">
                                        <a class="page-link" href="${numUrl}">${i}</a>
                                    </li>
                                </c:forEach>

                                <!-- Next Page -->
                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <c:url value="/product" var="nextUrl">
                                        <c:param name="page" value="${currentPage + 1}" />
                                        <c:if test="${not empty selectedCid}">
                                            <c:param name="cid" value="${selectedCid}" />
                                        </c:if>
                                    </c:url>
                                    <a class="page-link" href="${nextUrl}">
                                        <i class="fa-solid fa-angle-right"></i>
                                    </a>
                                </li>

                                <!-- Last Page -->
                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <c:url value="/product" var="lastUrl">
                                        <c:param name="page" value="${totalPages}" />
                                        <c:if test="${not empty selectedCid}">
                                            <c:param name="cid" value="${selectedCid}" />
                                        </c:if>
                                    </c:url>
                                    <a class="page-link" href="${lastUrl}">
                                        <i class="fa-solid fa-angles-right"></i>
                                    </a>
                                </li>
                            </ul>
                        </nav>
                    </c:if>
                </c:when>

                <c:otherwise>
                    <div class="card shadow-sm border-0 py-5 text-center">
                        <div class="card-body">
                            <i class="fa-solid fa-box-open fa-4x text-secondary mb-3"></i>
                            <h5 class="fw-bold text-dark">Chưa có sản phẩm nào!</h5>
                            <p class="text-muted">Không tìm thấy sản phẩm trong danh mục này.</p>
                            <a href="<c:url value='/product'/>" class="btn btn-outline-primary">
                                <i class="fa-solid fa-arrow-left me-1"></i>Xem tất cả sản phẩm
                            </a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
