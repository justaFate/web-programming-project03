<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Danh sách Category</title></head>
<body>
    <h2>Quản lý danh mục</h2>
    <a href="${pageContext.request.contextPath}/admin/category/add">Thêm mới</a>
    <table border="1" cellpadding="5">
        <tr>
            <th>ID</th>
            <th>Tên</th>
            <th>Icon</th>
            <th>Hành động</th>
        </tr>
        <c:forEach items="${cateList}" var="cate">
            <tr>
                <td>${cate.id}</td>
                <td>${cate.name}</td>
                <td>${cate.icon}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/category/edit?id=${cate.id}">Sửa</a> |
                    <a href="${pageContext.request.contextPath}/admin/category/delete?id=${cate.id}">Xóa</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
