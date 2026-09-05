<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chỉnh sửa Category</title>
</head>
<body>
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm border-0">
                <div class="card-header bg-warning-subtle border-bottom py-3">
                    <h4 class="mb-0 fw-bold text-dark"><i class="fa-solid fa-pen-to-square me-2 text-warning"></i>Chỉnh Sửa Danh Mục</h4>
                </div>
                <div class="card-body p-4">
                    <c:if test="${not empty generalError}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fa-solid fa-triangle-exclamation me-2"></i>${generalError}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="<c:url value='/admin/category/update'/>" method="post" enctype="multipart/form-data" novalidate>
                        <input type="hidden" name="categoryid" value="${cate.categoryid}" />

                        <div class="mb-3">
                            <label for="categoryname" class="form-label fw-semibold">
                                Tên danh mục <span class="text-danger">*</span>
                            </label>
                            <input type="text" 
                                   class="form-control ${not empty errors['categoryname'] ? 'is-invalid' : ''}" 
                                   id="categoryname" 
                                   name="categoryname" 
                                   value="${cate.categoryname}" 
                                   required />
                            <c:if test="${not empty errors['categoryname']}">
                                <div class="invalid-feedback d-block">
                                    <i class="fa-solid fa-circle-exclamation me-1"></i>${errors['categoryname']}
                                </div>
                            </c:if>
                        </div>

                        <div class="mb-3">
                            <label for="images" class="form-label fw-semibold">Đường dẫn ảnh Online (URL):</label>
                            <input type="text" 
                                   class="form-control" 
                                   id="images" 
                                   name="images" 
                                   value="${cate.images}" />
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold d-block">Hình ảnh hiện tại:</label>
                            <c:set var="imgUrl" value="${cate.images}" />
                            <c:if test="${not empty cate.images && !cate.images.startsWith('http')}">
                                <c:url value="/image?fname=${cate.images}" var="imgUrl" />
                            </c:if>
                            <img class="img-thumbnail shadow-sm mb-2" style="max-height: 120px;" src="${imgUrl}" alt="${cate.categoryname}" 
                                 onerror="this.onerror=null; this.src='https://placehold.co/150x100?text=No+Image';" />
                        </div>

                        <div class="mb-3">
                            <label for="images1" class="form-label fw-semibold">Tải ảnh mới thay thế (nếu muốn):</label>
                            <input type="file" 
                                   class="form-control ${not empty errors['images1'] ? 'is-invalid' : ''}" 
                                   id="images1" 
                                   name="images1" 
                                   accept="image/*" />
                            <c:if test="${not empty errors['images1']}">
                                <div class="invalid-feedback d-block">
                                    <i class="fa-solid fa-circle-exclamation me-1"></i>${errors['images1']}
                                </div>
                            </c:if>
                        </div>

                        <div class="mb-4">
                            <label class="form-label fw-semibold d-block">Trạng thái hoạt động:</label>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="status" id="statusActive" value="1" 
                                       ${cate.status == 1 ? 'checked' : ''} />
                                <label class="form-check-label text-success fw-semibold" for="statusActive">
                                    <i class="fa-solid fa-circle-check me-1"></i>Hoạt động
                                </label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="status" id="statusInactive" value="0" 
                                       ${cate.status != 1 ? 'checked' : ''} />
                                <label class="form-check-label text-secondary" for="statusInactive">
                                    <i class="fa-solid fa-lock me-1"></i>Khóa
                                </label>
                            </div>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-warning px-4 text-dark fw-semibold">
                                <i class="fa-solid fa-pen me-1"></i> Cập Nhật
                            </button>
                            <a href="<c:url value='/admin/categories'/>" class="btn btn-outline-secondary px-3">
                                <i class="fa-solid fa-arrow-left me-1"></i> Quay lại
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>

