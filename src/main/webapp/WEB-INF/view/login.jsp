<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix = "form" uri="http://www.springframework.org/tags/form" %>

<c:url var="loginUrl" value="/login"/>
<c:url var="facebookLogUrl" value="/signin/facebook"/>
<c:url var="githubLogUrl" value="/signin/github"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <meta charset="UTF-8" content="text/html"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <sec:csrfMetaTags />
    <link rel="stylesheet" type="text/css" href="${contextPath}/static/css/registration.css"/>
    <link rel="shortcut icon" type="image/x-icon" href="${contextPath}/static/favicon.ico" />
</head>
<body>
    <div class="form-container">
        <form:form id="loginForm" action="${loginUrl}" method="post">
            <h1>Login</h1>
            <label for="email">Email:</label>
            <input id="email" name="email" type="email" maxlength="255" placeholder="your@email.com"/>

            <label for="password">Password:</label>
            <input id="password" name="password" type="password" maxlength="30" placeholder="Password"/>

            <div class="js-error js-errors-message"></div>

            <button type="submit">Sign in</button>
            <c:if test="${param.error != null}">
                <span>Invalid email or passowrd</span>
            </c:if>
        </form:form>
        <form:form action="${facebookLogUrl}" method="post">
            <button id="facebookButton" type="submit">
                <img id="facebookIcon" src="${contextPath}/static/facebook-button.svg"/>
                Sign in with Facebook
            </button>
        </form:form>
        <form:form action="${githubLogUrl}" method="post">
            <button id="githubButton" type="submit">
                <img id="githubIcon" src="${contextPath}/static/github-button.svg"/>
                Sign in with Github
            </button>
        </form:form>
        <c:if test="${param.socialerror != null}">
            <span>No such social account found</span>
        </c:if>
    </div>

    <script src="${contextPath}/static/js/jquery-3.3.1.min.js"></script>
    </body>
</html>