<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Thêm Category</title>
</head>
<body>
    <h2>Thêm Category Mới</h2>
    <form action="<c:url value='/admin/category/insert'/>" method="post" enctype="multipart/form-data">
        <label>Category name:</label><br>
        <input type="text" name="categoryname" required /><br><br>
        
        <label>Link images (URL online):</label><br>
        <input type="text" name="images" /><br><br>
        
        <label>Upload images:</label><br>
        <input type="file" name="images1" /><br><br>
        
        <label>Status:</label><br>
        <input type="radio" name="status" value="1" checked /> Hoạt động
        <input type="radio" name="status" value="0" /> Khóa
        <br><br>
        
        <input type="submit" value="Insert" />
        <a href="<c:url value='/admin/categories'/>">Hủy</a>
    </form>
</body>
</html>