<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Danh sách Category</title>
</head>
<body>
    <a href="<c:url value='/admin/category/add'/>">Add Category</a><br>
    <hr>
    <table border="1" width="100%">
        <tr>
            <th>STT</th>
            <th>Images</th>
            <th>Category name</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        <c:forEach items="${listcate}" var="cate" varStatus="STT">
            <tr>
                <td>${STT.index + 1}</td>
                <c:set var="imgUrl" value="${cate.images}" />
                <c:if test="${!cate.images.startsWith('http')}">
                    <c:url value="/image?fname=${cate.images}" var="imgUrl" />
                </c:if>
                <td><img height="100" width="150" src="${imgUrl}" alt="${cate.categoryname}" /></td>
                <td>${cate.categoryname}</td>
                <td>${cate.status == 1 ? 'Hoạt động' : 'Khóa'}</td>
                <td>
                    <a href="<c:url value='/admin/category/edit?id=${cate.categoryid}'/>">Sửa</a> |
                    <a href="<c:url value='/admin/category/delete?id=${cate.categoryid}'/>" onclick="return confirm('Bạn có chắc muốn xóa?');">Xóa</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>