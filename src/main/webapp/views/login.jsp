<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head><title>LOGIN</title></head>
<body>
    <h1>Login</h1>
    <!-- Gửi thông tin bằng phương thức POST[cite: 4] -->
    <form action="${pageContext.request.contextPath}/login" method="POST">
        Username: <input type="text" name="username"><br><br>
        Password: <input type="password" name="password"><br><br>
        <button type="submit">Login</button>
    </form>
</body>
</html>