<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="custom_tags" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="message"/>

<!DOCTYPE>
<html lang="${sessionScope.language}">
<head>
    <c:import url="meta.jsp"/>
    <title><fmt:message key="patient.list.patients"/></title>
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
                    <input type="hidden" name="action" value="list_doctors">
                    <label for="sorting_type" class="">Sort doctors by:: </label>
                    <select class="form-select" name="sorting_type" id="sorting_type" onchange="submit()">
                        <option value="" selected disabled>Select sorting:</option>
                        <option value="NAME" ${sessionScope.sortBy == 'NAME' ? 'selected' : ''}>name</option>
                        <option value="CATEGORY" ${sessionScope.sortBy == 'CATEGORY' ? 'selected' : ''}>category</option>
                        <option value="NUMBER_OF_PATIENTS" ${sessionScope.sortBy == 'NUMBER_OF_PATIENTS' ? 'selected' : ''}>number of patients</option>
                    </select>
                </form>
            </div>
            <h3><fmt:message key="patient.list.doctors"/></h3>
            <table class="table table-light table-striped table-hover" aria-describedby="">
                <thead>
                <tr>
                    <th scope="col"><fmt:message key="signup.firstName"/></th>
                    <th scope="col"><fmt:message key="signup.lastName"/></th>
                    <th scope="col"><fmt:message key="signup.bday"/></th>
                    <th scope="col"><fmt:message key="signup.gender"/></th>
                    <th scope="col"><fmt:message key="doctor.list.category"/></th>
                    <th scope="col"><fmt:message key="doctor.list.numberOfPatients"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${sessionScope.doctors}" var="d">
                    <tr>
                        <th scope="col">${d.user.firstname}</th>
                        <th scope="col">${d.user.lastname}</th>
                        <th scope="col"><ctg:dateFormat date="${d.user.dateOfBirth}" locale="${sessionScope.language}"/></th>
                        <th scope="col"><fmt:message key="gender.${d.user.gender}"/></th>
                        <th scope="col">${d.category.name}</th>
                        <th scope="col">${d.numberOfPatients}</th>
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
