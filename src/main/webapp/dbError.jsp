<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="message"/>

<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<!DOCTYPE>
<html lang="${sessionScope.language}">
<head>
    <title>Database error</title>
</head>
<body>
<h1>Error: <fmt:message key="${sql}"/></h1>
</body>
</html>
