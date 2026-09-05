<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thêm Category Mới</title>
</head>
<body>
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm border-0">
                <div class="card-header bg-primary text-white py-3">
                    <h4 class="mb-0 fw-bold"><i class="fa-solid fa-folder-plus me-2"></i>Thêm Danh Mục Mới</h4>
                </div>
                <div class="card-body p-4">
                    <c:if test="${not empty generalError}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fa-solid fa-triangle-exclamation me-2"></i>${generalError}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="<c:url value='/admin/category/insert'/>" method="post" enctype="multipart/form-data" novalidate>
                        <div class="mb-3">
                            <label for="categoryname" class="form-label fw-semibold">
                                Tên danh mục <span class="text-danger">*</span>
                            </label>
                            <input type="text" 
                                   class="form-control ${not empty errors['categoryname'] ? 'is-invalid' : ''}" 
                                   id="categoryname" 
                                   name="categoryname" 
                                   value="${category.categoryname}" 
                                   placeholder="Ví dụ: Điện thoại, Laptop, Phụ kiện..." 
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
                                   value="${category.images}" 
                                   placeholder="https://example.com/image.jpg" />
                            <div class="form-text">Dùng đường dẫn ảnh trực tuyến nếu không tải lên từ máy tính.</div>
                        </div>

                        <div class="mb-3">
                            <label for="images1" class="form-label fw-semibold">Hoặc Tải ảnh lên từ máy tính (Multipart):</label>
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
                                       ${empty category.status || category.status == 1 ? 'checked' : ''} />
                                <label class="form-check-label text-success fw-semibold" for="statusActive">
                                    <i class="fa-solid fa-circle-check me-1"></i>Hoạt động
                                </label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="status" id="statusInactive" value="0" 
                                       ${category.status == 0 ? 'checked' : ''} />
                                <label class="form-check-label text-secondary" for="statusInactive">
                                    <i class="fa-solid fa-lock me-1"></i>Khóa
                                </label>
                            </div>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-primary px-4">
                                <i class="fa-solid fa-floppy-disk me-1"></i> Lưu Danh Mục
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

