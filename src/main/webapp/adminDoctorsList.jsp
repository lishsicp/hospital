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
            <form class="row g-3" action="${pageContext.request.contextPath}/controller">
                <div class="col-4">
                    <input type="hidden" name="action" value="list_doctors">
                    <label for="sorting_type" class=""><fmt:message key="doctor.sort"/> </label>
                    <select class="form-select" name="sorting_type" id="sorting_type" onchange="submit()">
                        <option value="" selected disabled><fmt:message key="patient.select.sort" /></option>
                        <option value="NAME" ${sessionScope.sortBy == 'NAME' ? 'selected' : ''}><fmt:message key="patient.sort.name" /></option>
                        <option value="CATEGORY" ${sessionScope.sortBy == 'CATEGORY' ? 'selected' : ''}><fmt:message key="doctor.sort.category" /></option>
                        <option value="NUMBER_OF_PATIENTS" ${sessionScope.sortBy == 'NUMBER_OF_PATIENTS' ? 'selected' : ''}><fmt:message key="doctor.sort.patients" /></option>
                    </select>
                </div>
                <div class="col-2">
                    <label for="recordsPerPage"><fmt:message key="items.per.page"/></label>
                    <input type="number" name="recordsPerPage" id="recordsPerPage" oninput="submit()" value="${recordsPerPage}">
                </div>
            </form>

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
                        <th scope="col"><fmt:message key="signup.category.${d.category.name}"/></th>
                        <th scope="col">${d.numberOfPatients}</th>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <nav aria-label="Page navigation example">
                <ul class="pagination pg-dark">
                    <c:if test="${currentPageNo != 1}">
                        <li class="page-item">
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/controller?action=list_doctors&sorting_type=${sortBy}&page=${currentPageNo - 1}&recordsPerPage=${recordsPerPage}"
                               aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </c:if>
                    <c:forEach begin="1" end="${noOfPages}" var="i">
                        <c:choose>
                            <c:when test="${currentPageNo eq i}">
                                <li class="page-item active"><a class="page-link active" href="#">${i}</a></li>
                            </c:when>
                            <c:otherwise>
                                <li class="page-item">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/controller?action=list_doctors&sorting_type=${sortBy}&page=${i}&recordsPerPage=${recordsPerPage}">
                                            ${i}
                                    </a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <c:if test="${currentPageNo lt noOfPages}">
                        <li class="page-item">
                            <a class="page-link" href="${pageContext.request.contextPath}/controller?action=list_doctors&sorting_type=${sortBy}&page=${currentPageNo + 1}&recordsPerPage=${recordsPerPage}" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </nav>
            <c:if test="${sql ne null}">
                <div class="text-danger" id="sql">
                    <fmt:message key="${sql}"/>
                </div>
            </c:if>
        </div>
    </div>
</div>
<c:import url="scripts.jsp"/>
</body>
</html>
