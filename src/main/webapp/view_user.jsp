<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="custom_tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="message"/>
<c:set var="user" scope="page" value="${sessionScope.viewUser}"/>

<!DOCTYPE>
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
            <div class="container">
                <div class="row flex-nowrap">
                    <div class="col py-3">
                        <h1 class="align-content-center">User info</h1>
                        <p>Name: ${user.firstname}  ${viewUser.lastname}</p>
                        <p>Login: ${viewUser.login}</p> Email: ${viewUser.email} Phone: ${viewUser.phone}
                        <p>Gender ${viewUser.gender.toString()}</p>
                        <p>Date of birth: <ctg:dateFormat date="${viewUser.dateOfBirth}" locale="${language}" /></p>
                        <p>Address: </p>
                        <p>Role: ${viewUser.role.name()}</p>
                    </div>
                </div>
            </div>
    </div>
</div>
<c:import url="scripts.jsp"/>
</body>
</html>
