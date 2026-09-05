<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thêm Sản phẩm Mới</title>
</head>
<body>
    <div class="row justify-content-center my-4">
        <div class="col-lg-8">
            <div class="card shadow-sm border-0">
                <div class="card-header bg-dark text-white py-3">
                    <h4 class="mb-0 fw-bold"><i class="fa-solid fa-plus-circle me-2 text-info"></i>Thêm Sản Phẩm Mới</h4>
                </div>
                <div class="card-body p-4">
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fa-solid fa-circle-exclamation me-2"></i>${errorMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="<c:url value='/admin/product/insert'/>" method="POST" enctype="multipart/form-data" class="needs-validation" novalidate>
                        <!-- Tên sản phẩm -->
                        <div class="mb-3">
                            <label for="productName" class="form-label fw-semibold">Tên sản phẩm <span class="text-danger">*</span>:</label>
                            <input type="text" class="form-control ${not empty errors['productName'] ? 'is-invalid' : ''}" 
                                   id="productName" name="productName" value="${product.productName}" 
                                   placeholder="Nhập tên sản phẩm (2-200 ký tự)" required />
                            <c:if test="${not empty errors['productName']}">
                                <div class="invalid-feedback d-block">${errors['productName']}</div>
                            </c:if>
                        </div>

                        <div class="row">
                            <!-- Danh mục -->
                            <div class="col-md-6 mb-3">
                                <label for="categoryId" class="form-label fw-semibold">Danh mục <span class="text-danger">*</span>:</label>
                                <select class="form-select ${not empty errors['categoryId'] ? 'is-invalid' : ''}" id="categoryId" name="categoryId" required>
                                    <option value="">-- Chọn danh mục --</option>
                                    <c:forEach items="${categories}" var="c">
                                        <option value="${c.categoryid}" ${product.category != null && product.category.categoryid == c.categoryid ? 'selected' : ''}>
                                            ${c.categoryname}
                                        </option>
                                    </c:forEach>
                                </select>
                                <c:if test="${not empty errors['categoryId']}">
                                    <div class="invalid-feedback d-block">${errors['categoryId']}</div>
                                </c:if>
                            </div>

                            <!-- Đơn giá -->
                            <div class="col-md-6 mb-3">
                                <label for="price" class="form-label fw-semibold">Đơn giá (VNĐ) <span class="text-danger">*</span>:</label>
                                <div class="input-group">
                                    <input type="number" step="1000" min="0" class="form-control ${not empty errors['price'] ? 'is-invalid' : ''}" 
                                           id="price" name="price" value="${product.price > 0 ? product.price : ''}" 
                                           placeholder="Ví dụ: 250000" required />
                                    <span class="input-group-text">₫</span>
                                </div>
                                <c:if test="${not empty errors['price']}">
                                    <div class="invalid-feedback d-block">${errors['price']}</div>
                                </c:if>
                            </div>
                        </div>

                        <!-- Trạng thái -->
                        <div class="mb-3">
                            <label class="form-label fw-semibold d-block">Trạng thái kinh doanh:</label>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="status" id="status1" value="1" 
                                       ${empty product || product.status == 1 ? 'checked' : ''} />
                                <label class="form-check-label text-success fw-semibold" for="status1">
                                    <i class="fa-solid fa-check me-1"></i>Đang bán
                                </label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="status" id="status0" value="0" 
                                       ${product != null && product.status == 0 ? 'checked' : ''} />
                                <label class="form-check-label text-secondary fw-semibold" for="status0">
                                    <i class="fa-solid fa-pause me-1"></i>Tạm dừng
                                </label>
                            </div>
                        </div>

                        <!-- Hình ảnh Multipart -->
                        <div class="mb-3">
                            <label for="imageFile" class="form-label fw-semibold">Tải ảnh sản phẩm (File):</label>
                            <input type="file" class="form-control ${not empty errors['imageFile'] ? 'is-invalid' : ''}" 
                                   id="imageFile" name="imageFile" accept="image/*" />
                            <div class="form-text">Hỗ trợ các định dạng JPG, PNG, WEBP, GIF (tối đa 10MB).</div>
                            <c:if test="${not empty errors['imageFile']}">
                                <div class="invalid-feedback d-block">${errors['imageFile']}</div>
                            </c:if>
                        </div>

                        <!-- Hoặc URL Ảnh online -->
                        <div class="mb-3">
                            <label for="images" class="form-label fw-semibold">Hoặc liên kết ảnh Online (URL):</label>
                            <input type="text" class="form-control" id="images" name="images" 
                                   value="${product.images}" placeholder="https://example.com/image.jpg" />
                            <div class="form-text">Nếu không tải file, có thể dán đường dẫn ảnh trực tuyến tại đây.</div>
                        </div>

                        <!-- Mô tả chi tiết -->
                        <div class="mb-4">
                            <label for="description" class="form-label fw-semibold">Mô tả sản phẩm:</label>
                            <textarea class="form-control" id="description" name="description" rows="4" 
                                      placeholder="Nhập thông tin chi tiết, xuất xứ, tính năng sản phẩm...">${product.description}</textarea>
                        </div>

                        <!-- Buttons -->
                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-primary px-4">
                                <i class="fa-solid fa-floppy-disk me-1"></i> Lưu Sản Phẩm
                            </button>
                            <a href="<c:url value='/admin/products'/>" class="btn btn-outline-secondary px-4">
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
