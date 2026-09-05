<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property='title'/> - Bài Tập 03</title>

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

    <style>
        body {
            background-color: #f8f9fa;
            font-family: system-ui, -apple-system, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
        }
        .navbar-brand {
            letter-spacing: 0.5px;
        }
        .card {
            border-radius: 0.75rem;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
        }
        .table-avatar {
            width: 80px;
            height: 60px;
            object-fit: cover;
            border-radius: 6px;
        }
    </style>

    <sitemesh:write property='head'/>
</head>
<body class="d-flex flex-column min-vh-100">

    <!-- Header Navigation -->
    <%@ include file="/views/layout/header.jsp" %>

    <!-- Main Content Container -->
    <main class="container my-4 flex-grow-1">
        <sitemesh:write property='body'/>
    </main>

    <!-- Footer -->
    <%@ include file="/views/layout/footer.jsp" %>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

