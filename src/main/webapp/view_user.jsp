<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="custom_tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="message"/>
<html lang="${language}">
<head>
    <c:import url="meta.jsp" />
    <title>User Info</title>
</head>
<body>
<div class="container-fluid">
    <div class="row flex-nowrap">
        <div class="col py-3">
            <h1 class="align-content-center">User info</h1>
            <p>Name: ${viewUser.firstname}  ${viewUser.lastname}</p>
            <p>Login: ${viewUser.login}</p> Email: ${viewUser.email} Phone: ${viewUser.phone}
            <p>Gender ${viewUser.gender.toString()}</p>
            <p>Date of birth: <ctg:dateFormat date="${viewUser.dateOfBirth}" locale="${language}" /></p>
            <p>Address: </p>
            <p>Role: ${viewUser.role.name()}</p>
        </div>
    </div>
</div>
</body>
</html>
