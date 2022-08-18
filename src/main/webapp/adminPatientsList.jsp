<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="custom_tags"%>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="message"/>

<!DOCTYPE>
<html lang="${sessionScope.language}">
<head>
    <c:import url="meta.jsp"/>
    <title><fmt:message key="patient.list.patients" /></title>
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
                <div class="col-4">
                    <form action="${pageContext.request.contextPath}/controller">
                        <input type="hidden" name="action" value="list_patients">
                        <label for="sorting_type" class="">Sort patients by: </label>
                        <select class="form-select" name="sorting_type" id="sorting_type" onchange="submit()">
                            <option value="" selected disabled>Select sorting: </option>
                            <option value="NAME" ${sessionScope.sortBy == 'NAME' ? 'selected' : ''}>name</option>
                            <option value="DATE" ${sessionScope.sortBy == 'DATE' ? 'selected' : ''}>date of birth</option>
                        </select>
                    </form>
                </div>
                <h3><fmt:message key="patient.list.patients" /></h3>
                <table class="table table-light table-striped table-hover" aria-describedby="">
                    <thead>
                    <tr>
                        <th scope="col"><fmt:message key="signup.firstName"/></th>
                        <th scope="col"><fmt:message key="signup.lastName"/></th>
                        <th scope="col"><fmt:message key="signup.bday"/></th>
                        <th scope="col"><fmt:message key="signup.gender"/></th>
                        <th scope="col"><fmt:message key="patient.list.status"/></th>
                        <th scope="col"><fmt:message key="patient.list.doctor"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${sessionScope.patients}" var="p">
                        <tr>
                            <th scope="col">${p.firstname}</th>
                            <th scope="col">${p.lastname}</th>
                            <th scope="col"><ctg:dateFormat date="${p.dateOfBirth}" locale="${sessionScope.language}"/></th>
                            <th scope="col"><fmt:message key="gender.${p.gender}" /></th>
                            <th scope="col">${p.status}</th>
                            <th scope="col">${p.doctor.id}</th>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
    </div>
</div>
<c:import url="scripts.jsp"/>
</body>
</html>