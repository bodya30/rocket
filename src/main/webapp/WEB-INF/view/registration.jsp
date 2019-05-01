<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:url var="registerUrl" value="/registration/register"/>
<c:url var="facebookRegUrl" value="/signin/facebook"/>
<c:url var="githubLogUrl" value="/signin/github"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
    <head>
        <title>Registration</title>
        <meta charset="UTF-8" content="text/html"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <sec:csrfMetaTags />
        <link rel="stylesheet" type="text/css" href="${contextPath}/static/css/registration.css"/>
        <link rel="shortcut icon" type="image/x-icon" href="${contextPath}/static/favicon.ico" />
    </head>
    <body>
        <div class="form-container">
            <form  id="registrationForm" action="${registerUrl}" method="post">
                <h1>Sign Up</h1>
                <label for="firstName">First name:</label>
                <input id="firstName" name="firstName" type="text" maxlength="255" placeholder="First name"/>
                <div class="js-error js-errors-firstName"></div>

                <label for="lastName">Last name:</label>
                <input id="lastName" name="lastName" type="text" maxlength="255" placeholder="Last name"/>
                <div class="js-error js-errors-lastName"></div>

                <label for="email">Email:</label>
                <input id="email" name="email" type="email" maxlength="255" placeholder="your@email.com"/>
                <div class="js-error js-errors-email"></div>

                <label for="password">Password:</label>
                <input id="password" name="password" type="password" maxlength="30" placeholder="Password"/>
                <div class="js-error js-errors-password"></div>

                <label for="confirmPassword">Confirm password:</label>
                <input id="confirmPassword" name="confirmPassword" type="password" maxlength="30" placeholder="Confirm password"/>
                <div class="js-error js-errors-confirmPassword"></div>

                <div class="js-error js-errors-message"></div>

                <button id="submitButton" type="submit">Sign up</button>
                <span class="js-register-message register-message">
                    Verification link was sent to your email
                </span>
            </form>
            <div class="button-container">
                <form:form cssClass="button-form" action="${facebookRegUrl}" method="post">
                    <button id="facebookButton" type="submit">
                        <img id="facebookIcon" src="${contextPath}/static/facebook-button.svg"/>
                        Sign up with Facebook
                    </button>
                </form:form>
                <form:form cssClass="button-form" action="${githubLogUrl}" method="post">
                    <button id="githubButton" type="submit">
                        <img id="githubIcon" src="${contextPath}/static/github-button.svg"/>
                        Sign up with Github
                    </button>
                </form:form>
            </div>
            <c:if test="${param.error}">
                <span>Your ${connection.key.providerId} account does not have public email.</span>
            </c:if>
        </div>
        <script src="${contextPath}/static/js/jquery-3.3.1.min.js"></script>
        <script src="${contextPath}/static/js/main.js"></script>
    </body>
</html>