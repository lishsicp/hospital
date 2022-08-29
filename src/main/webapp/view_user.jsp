<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="custom_tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <c:import
            url="${current_user.role == 'ADMIN' ? 'adminActions.jsp' : current_user.role == 'DOCTOR' ? 'doctorActions.jsp' : 'nurseActions.jsp'}"/>
    <div id="page-content-wrapper">
        <%-- Top navigation--%>
        <c:import url="topnav.jsp"/>
        <%--Page Content--%>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-5">
                    <h2 class="p-2"><fmt:message key="user.details"/></h2>
                    <div class="card mb-3">
                        <div class="card-body">
                            <div class="row">
                                <div class="col-sm-3">
                                    <h6 class="mb-0"><fmt:message key="signup.fullName"/></h6>
                                </div>
                                <div class="col-sm-9 text-secondary">
                                    ${viewUser.firstname} ${viewUser.lastname}
                                </div>
                            </div>
                            <hr>
                            <div class="row">
                                <div class="col-sm-3">
                                    <h6 class="mb-0"><fmt:message key="signup.login"/></h6>
                                </div>
                                <div class="col-sm-9 text-secondary">
                                    ${viewUser.login}
                                </div>
                            </div>
                            <hr>
                            <div class="row">
                                <div class="col-sm-3">
                                    <h6 class="mb-0"><fmt:message key="signup.email"/></h6>
                                </div>
                                <div class="col-sm-9 text-secondary">
                                    ${viewUser.email}
                                </div>
                            </div>
                            <hr>
                            <div class="row">
                                <div class="col-sm-3">
                                    <h6 class="mb-0"><fmt:message key="signup.phone"/></h6>
                                </div>
                                <div class="col-sm-9 text-secondary">
                                    ${viewUser.phone}
                                </div>
                            </div>
                            <hr>
                            <div class="row">
                                <div class="col-sm-3">
                                    <h6 class="mb-0"><fmt:message key="patient.age"/></h6>
                                </div>
                                <div class="col-sm-9 text-secondary">
                                    <ctg:getAge></ctg:getAge>
                                </div>
                            </div>
                            <hr>
                            <div class="row">
                                <div class="col-sm-3">
                                    <h6 class="mb-0"><fmt:message key="signup.gender"/></h6>
                                </div>
                                <div class="col-sm-9 text-secondary">
                                    <fmt:message key="gender.${viewUser.gender}"/>
                                </div>
                            </div>
                            <hr>
                            <c:if test="${viewUser.address != null}">
                                <div class="row">
                                    <div class="col-sm-3">
                                        <h6 class="mb-0"><fmt:message key="signup.address"/></h6>
                                    </div>
                                    <div class="col-sm-9 text-secondary">
                                        ${viewUser.address}
                                    </div>
                                </div>
                                <hr>
                            </c:if>
                            <div class="row">
                                <div class="col-sm-3">
                                    <h6 class="mb-0"><fmt:message key="signup.role"/></h6>
                                </div>
                                <div class="col-sm-9 text-secondary">
                                    <fmt:message key="role.${viewUser.role}"/>
                                </div>
                            </div>
                            <hr>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <c:import url="scripts.jsp"/>
</body>
</html>
