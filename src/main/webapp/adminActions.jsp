<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="message"/>
<div class="border-end bg-white" id="sidebar-wrapper">
    <div class="sidebar-heading border-bottom bg-light"><fmt:message key="header.name"/> | <fmt:message key="header.menu"/> </div>
    <div class="list-group list-group-flush">
        <a class="list-group-item list-group-item-action list-group-item-light p-3" href="${pageContext.request.contextPath}/controller?action=list_patients"><fmt:message key="admin.patient.list"/></a>
        <a class="list-group-item list-group-item-action list-group-item-light p-3" href="${pageContext.request.contextPath}/controller?action=list_doctors"><fmt:message key="admin.doctor.list"/></a>
        <a class="list-group-item list-group-item-action list-group-item-light p-3" href="${pageContext.request.contextPath}/controller?action=add_patient"><fmt:message key="admin.patient.add"/></a>
        <a class="list-group-item list-group-item-action list-group-item-light p-3" href="${pageContext.request.contextPath}/controller?action=add_doctor"><fmt:message key="admin.doctor.add"/></a>
    </div>
</div>
<script>
    window.addEventListener('DOMContentLoaded', event => {
        var url = window.location.href;
        //loop through `a` remove active class from all if any
        document.querySelectorAll('.list-group-flush a').forEach(function(el) {
            el.classList.remove("active")
        });
        document.querySelectorAll('.list-group-flush a').forEach(function(el) {
            //check if href matches url ..add active class theere
            if (url.startsWith('http://localhost:8080' + el.getAttribute('href'))) {
                el.classList.add("active")
            }
        })
    })
</script>