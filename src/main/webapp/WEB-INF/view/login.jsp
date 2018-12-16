<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "sec" uri="http://www.springframework.org/security/tags" %>

<c:url var="loginUrl" value="/login"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <meta charset="UTF-8" content="text/html"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <sec:csrfMetaTags />
    <link rel="stylesheet" type="text/css" href="${contextPath}/static/css/registration.css"/>
    <link rel='shortcut icon' type='image/x-icon' href='${contextPath}/static/favicon.ico' />
</head>
<body>
    <form id="registrationForm" action="${loginUrl}" method="post">
        <h1>Login</h1>
        <label for="email">Email:</label>
        <input id="email" name="email" type="email" maxlength="255" placeholder="your@email.com"/>

        <label for="password">Password:</label>
        <input id="password" name="password" type="password" maxlength="30" placeholder="Password"/>

        <div class="js-error js-errors-message"></div>

        <button type="submit">Sign Up</button>
    </form>
    <script src="${contextPath}/static/js/jquery-3.3.1.min.js"></script>
    </body>
</html>