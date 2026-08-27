<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Chỉnh sửa danh mục</title>
</head>
<body>
    <h2>Chỉnh Sửa Danh Mục</h2>
    <form action="${pageContext.request.contextPath}/admin/category/edit" method="post">
        <input type="hidden" name="id" value="${category.id}" />
        <div>
            <label>Tên danh mục:</label>
            <input type="text" name="name" value="${category.name}" required />
        </div>
        <br>
        <div>
            <label>Icon:</label>
            <input type="text" name="icon" value="${category.icon}" />
        </div>
        <br>
        <button type="submit">Cập nhật</button>
        <a href="${pageContext.request.contextPath}/admin/category/list">Hủy</a>
    </form>
</body>
</html>