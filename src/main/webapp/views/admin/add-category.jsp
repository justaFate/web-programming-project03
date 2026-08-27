<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Thêm mới danh mục</title>
</head>
<body>
    <h2>Thêm Danh Mục Mới</h2>
    <form action="${pageContext.request.contextPath}/admin/category/add" method="post">
        <div>
            <label>Tên danh mục:</label>
            <input type="text" name="name" required />
        </div>
        <br>
        <div>
            <label>Tên file Icon (ví dụ: phone.png):</label>
            <input type="text" name="icon" />
        </div>
        <br>
        <button type="submit">Thêm</button>
        <a href="${pageContext.request.contextPath}/admin/category/list">Hủy</a>
    </form>
</body>
</html>