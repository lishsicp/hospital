<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="custom_tags" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="message"/>
<fmt:setBundle basename="validation.validation" var="val"/>

<!DOCTYPE>
<html lang="${sessionScope.language}">
<head>
    <c:import url="meta.jsp" />
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
            <h2><fmt:message key="assign.doctor.for"/>: <em>${sessionScope.patient.firstname} ${sessionScope.patient.lastname}</em></h2>
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
                    <th scope="col"><fmt:message key="submit"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${sessionScope.doctorsByCategory}" var="d">
                    <tr>
                        <th scope="col">${d.user.firstname}</th>
                        <th scope="col">${d.user.lastname}</th>
                        <th scope="col"><ctg:dateFormat date="${d.user.dateOfBirth}" locale="${sessionScope.language}"/></th>
                        <th scope="col"><fmt:message key="gender.${d.user.gender}"/></th>
                        <th scope="col"><fmt:message key="signup.category.${d.category.name}"/></th>
                        <th scope="col">${d.numberOfPatients}</th>
                        <th scope="col">
                            <form action="">
                                <input type="hidden" name="action" value="assign_doctor">
                                <input type="hidden" name="doctor_id" value="${d.id}">
                                <input type="hidden" name="patient_id" value="${patient.id}">
                            <button class="btn btn-secondary" type="submit"><fmt:message key="patient.assign_doctor"/></button>
                            </form>
                        </th>
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
