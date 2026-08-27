<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Chỉnh sửa Category</title>
</head>
<body>
    <h2>Chỉnh sửa Category</h2>
    <form action="<c:url value='/admin/category/update'/>" method="post" enctype="multipart/form-data">
        <input type="hidden" name="categoryid" value="${cate.categoryid}" />
        
        <label>Category name:</label><br>
        <input type="text" name="categoryname" value="${cate.categoryname}" required /><br><br>
        
        <label>Link images:</label><br>
        <input type="text" name="images" value="${cate.images}" /><br><br>
        
        <c:set var="imgUrl" value="${cate.images}" />
        <c:if test="${!cate.images.startsWith('http')}">
            <c:url value="/image?fname=${cate.images}" var="imgUrl" />
        </c:if>
        <img height="100" width="150" src="${imgUrl}" alt="${cate.categoryname}" /><br><br>
        
        <label>Upload images mới:</label><br>
        <input type="file" name="images1" /><br><br>
        
        <label>Status:</label><br>
        <input type="radio" name="status" value="1" ${cate.status == 1 ? 'checked' : ''} /> Hoạt động
        <input type="radio" name="status" value="0" ${cate.status != 1 ? 'checked' : ''} /> Khóa
        <br><br>
        
        <input type="submit" value="Update" />
        <a href="<c:url value='/admin/categories'/>">Hủy</a>
    </form>
</body>
</html>