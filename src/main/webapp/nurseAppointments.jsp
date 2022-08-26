<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="custom_tags" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="message"/>
<fmt:setBundle basename="validation.validation" var="val"/>

<!DOCTYPE html>
<html lang="${sessionScope.language}">
<head>
  <c:import url="meta.jsp" />
  <title>Hospital</title>
</head>
<body>
<div class="d-flex" id="wrapper">
  <%-- Page actions --%>
  <c:import url="nurseActions.jsp"/>
  <%-- Page content wrapper--%>
  <div id="page-content-wrapper">
    <%-- Top navigation--%>
    <c:import url="topnav.jsp"/>
    <%--Page Content--%>
    <c:import url="appointmentList.jsp"/>
  </div>
</div>
<c:import url="scripts.jsp"/>
</body>
</html>