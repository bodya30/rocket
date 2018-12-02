<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "form" uri="http://www.springframework.org/tags/form" %>

<c:url var="registerUrl" value="/registration/validate"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
    <head>
        <title>Registration</title>
        <meta charset="UTF-8" content="text/html"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <link rel="stylesheet" type="text/css" href="${contextPath}/static/css/registration.css"/>
        <link rel='shortcut icon' type='image/x-icon' href='${contextPath}/static/favicon.ico' />
    </head>
    <body>
        <form:form  id="registrationForm" action="${registerUrl}" method="post" modelAttribute="registrationForm">
            <h1>Sign Up</h1>
            <form:label path="firstName">First name:</form:label>
            <form:input path="firstName" type="text" maxlength="255"/>
            <div class="js-errors-firstName"></div>

            <form:label path="lastName">Last name:</form:label>
            <form:input path="lastName" type="text" maxlength="255"/>
            <div class="js-errors-lastName"></div>

            <form:label path="email">Email:</form:label>
            <form:input path="email" type="email" maxlength="255"/>
            <div class="js-errors-email"></div>

            <button type="submit">Sign Up</button>

        </form:form>
        <script src="${contextPath}/static/js/jquery-3.3.1.min.js"></script>
        <script src="${contextPath}/static/js/main.js"></script>
    </body>
</html>