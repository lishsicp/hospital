<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="custom_tags" %>


<html lang="${language}">
<head>
    <c:import url="meta.jsp"/>
    <title>Hospital</title>
</head>
<body>
<div class="d-flex" id="wrapper">
    <%-- Page actions --%>
    <c:import url="adminActions.jsp"/>
    <%-- Page content wrapper--%>
    <div id="page-content-wrapper">
        <%-- Top navigation--%>
        <c:import url="topnav.jsp"/>
        <%--Page Content--%>
        <c:import url="${sessionScope.currentPage}"/>
    </div>
</div>
<c:import url="scripts.jsp"/>
</body>
</html>