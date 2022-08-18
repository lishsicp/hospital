<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="message"/>
<div class="border-end bg-white" id="sidebar-wrapper">
    <div class="sidebar-heading border-bottom bg-light"><fmt:message key="header.name"/> | <fmt:message key="admin.menu"/> </div>
    <div class="list-group list-group-flush">
        <a class="list-group-item list-group-item-action list-group-item-light p-3" href="${pageContext.request.contextPath}/controller?action=list_patients"><fmt:message key="admin.patient.list"/></a>
        <a class="list-group-item list-group-item-action list-group-item-light p-3" href="${pageContext.request.contextPath}/controller?action=list_doctors"><fmt:message key="admin.doctor.list"/></a>
        <a class="list-group-item list-group-item-action list-group-item-light p-3" href="${pageContext.request.contextPath}/controller?action=add_patient"><fmt:message key="admin.patient.add"/></a>
        <a class="list-group-item list-group-item-action list-group-item-light p-3" href="${pageContext.request.contextPath}/controller?action=add_doctor">><fmt:message key="admin.doctor.add"/></a>
        <a class="list-group-item list-group-item-action list-group-item-light p-3" href="#!"><fmt:message key="admin.doctor.to.patient"/></a>
    </div>
</div>