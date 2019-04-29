<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "form" uri="http://www.springframework.org/tags/form" %>

<c:url var="logoutUrl" value="/logout"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
    <head>
        <title>Rocket homepage</title>
        <meta charset="UTF-8" content="text/html"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <link rel="stylesheet" type="text/css" href="${contextPath}/static/css/main.css"/>
        <link rel="stylesheet" type="text/css" href="${contextPath}/static/css/registration.css"/>
        <link rel='shortcut icon' type='image/x-icon' href='favicon.ico' />
    </head>
    <body>
        <div class="form-container">
            <form:form id="logoutForm" action="${logoutUrl}" method="post">
                <h1>You are successfully authenticated!</h1>
                <button type="submit">Logout</button>
            </form:form>
        </div>
    </body>
</html>
