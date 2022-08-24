<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="custom_tags" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="message"/>
<fmt:setBundle basename="validation.validation" var="val"/>

<!DOCTYPE html>
<html lang="${sessionScope.language}">
<head>
  <c:import url="meta.jsp" />
  <title>My Patients</title>
</head>
<body>
<div class="d-flex" id="wrapper">
  <%-- Page actions --%>
  <c:import url="doctorActions.jsp"/>
  <%-- Page content wrapper--%>
  <div id="page-content-wrapper">
    <%-- Top navigation--%>
    <c:import url="topnav.jsp"/>
    <%--Page Content--%>
    <div class="container">
      <form action="${pageContext.request.contextPath}/controller">
        <div class="">
          <input type="hidden" name="action" value="my_patients">
          <label for="recordsPerPage"><fmt:message key="items.per.page"/></label>
          <input type="number" name="recordsPerPage" id="recordsPerPage" value="${recordsPerPage}" onchange="submit()">
        </div>
      </form>
      <h3><fmt:message key="patient.list.patients"/></h3>

      <table class="table table-light table-striped table-hover" aria-describedby="">
        <thead>
        <tr>
          <th scope="col"><fmt:message key="signup.fullName"/></th>
          <th scope="col"><fmt:message key="signup.bday"/></th>
          <th scope="col"><fmt:message key="signup.gender"/></th>
          <th scope="col"><fmt:message key="signup.email"/></th>
          <th scope="col"><fmt:message key="doctor.my.patients.diagnose"/></th>
          <th scope="col"><fmt:message key="doctor.my.patients.details"/></th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
          <c:when test="${sessionScope.hospitalCards eq null or sessionScope.hospitalCards.isEmpty()}">
            <th>
              <div class="text-danger"><fmt:message key="patient.not_fount"/></div>
            </th>
          </c:when>
          <c:otherwise>
            <c:forEach items="${sessionScope.hospitalCards}" var="card" varStatus="i">
              <tr>
                <th class="align-middle" scope="col">${card.patient.firstname} ${card.patient.lastname}</th>
                <th class="align-middle" scope="col"><ctg:dateFormat date="${card.patient.dateOfBirth}"
                                                                     locale="${sessionScope.language}"/></th>
                <th class="align-middle" scope="col"><fmt:message key="gender.${card.patient.gender}"/></th>
                <th class="align-middle" scope="col">${card.patient.email}</th>
                <th class="align-middle" scope="col">
                  <c:if test="${card.diagnosis == null}">
                    <fmt:message key="no.diagnosis"/>
                  </c:if>
                  ${card.diagnosis}
                </th>
                <th class="align-middle" scope="col">
                  <form>
                    <input class="btn btn-secondary btn-sm" type="submit" value="View"/>
                    <input type="hidden" name="action" value="view_patient"/>
                    <input type="hidden" name="cardId" value="${card.id}"/>
                  </form>
                </th>
              </tr>
            </c:forEach>
          </c:otherwise>
        </c:choose>
        </tbody>
      </table>
      <nav aria-label="Page navigation example">
        <ul class="pagination">
          <c:if test="${currentPageNo != 1}">
            <li class="page-item">
              <a class="page-link"
                 href="${pageContext.request.contextPath}/controller?action=my_patients&sorting_type=${sortBy}&page=${currentPageNo - 1}&recordsPerPage=${recordsPerPage}&no_doctor=${no_doctor}"
                 aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
              </a>
            </li>
          </c:if>
          <c:forEach begin="1" end="${noOfPages}" var="i">
            <c:choose>
              <c:when test="${currentPageNo eq i}">
                <li class="page-item"><a class="page-link active" href="#">${i}</a></li>
              </c:when>
              <c:otherwise>
                <li class="page-item">
                  <a class="page-link"
                     href="${pageContext.request.contextPath}/controller?action=my_patients&sorting_type=${sortBy}&page=${i}&recordsPerPage=${recordsPerPage}&no_doctor=${no_doctor}">
                      ${i}
                  </a>
                </li>
              </c:otherwise>
            </c:choose>
          </c:forEach>
          <c:if test="${currentPageNo lt noOfPages}">
            <li class="page-item">
              <a class="page-link" href="${pageContext.request.contextPath}/controller?action=my_patients&sorting_type=${sortBy}&page=${currentPageNo + 1}&recordsPerPage=${recordsPerPage}&no_doctor=${no_doctor}" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
              </a>
            </li>
          </c:if>
        </ul>
      </nav>
      <div class="text-danger">
        <c:if test="${sessionScope.sql ne null}">
          <fmt:message key="${sql}"/>
        </c:if>
      </div>
    </div>
  </div>
</div>
<c:import url="scripts.jsp"/>
</body>
</html>