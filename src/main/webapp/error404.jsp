<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${language}" />
<fmt:setBundle basename="message" />

<html lang="${language}">
<head>
    <title><fmt:message key="error.404.title" /></title>
    <%@ include file="meta.jsp" %>
</head>
<body>
    <div class="m-4 p-4 fs-3">
        <div class="text-center"><h1 class="display-1">404</h1></div>
        <div class="text-center"><strong><fmt:message key="error.404.title"/></strong></div>
        <div class="text-danger text-center"><fmt:message key="error.404.header" /></div>
    </div>
</body>
</html>
