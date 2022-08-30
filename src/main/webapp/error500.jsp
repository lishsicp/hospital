<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.language}" />
<fmt:setBundle basename="message" />

<!DOCTYPE>
<html lang="${sessionScope.language}">
<head>
  <title>500 - Internal State Error</title>
  <%@ include file="meta.jsp" %>
</head>
<body>
<div class="m-4 p-4 fs-3">
  <div class="text-center"><h1 class="display-1">500</h1></div>
  <div class="text-center"><strong><fmt:message key="error.505.title"/></strong></div>
  <div class="text-danger text-center"><fmt:message key="error.505.header" /></div>
</div>
</body>
</html>
