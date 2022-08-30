<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="custom_tags" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="message"/>
<fmt:setBundle basename="validation.validation" var="val"/>

<div class="container-fluid">
  <form method="post" action="${pageContext.request.contextPath}/controller?action=${current_user.role == 'DOCTOR' ? 'appointments' : 'nurse_appointments'}&page=${currentPageNo}">
    <input type="hidden" name="action" value="${current_user.role == 'DOCTOR' ? 'appointments' : 'nurse_appointments'}">
    <div class="row mb-3">
      <div class="col-sm-10">
        <label for="recordsPerPage" class="col-sm-2 col-form-label"><fmt:message key="items.per.page"/></label>
        <input type="number" name="recordsPerPage" id="recordsPerPage" value="${recordsPerPage}" width="20 px">
      </div>
    </div>
    <fieldset class="row mb-3">
      <div class="col-2">
        <legend class="col-form-label col-sm-12 pt-0"><fmt:message key="filted.appointments"/>:</legend>
        <div class="form-check">
          <input class="form-check-input" type="checkbox" name="PROCEDURE" id="procedure" value="true" ${procedure == true ? 'checked' : ''}>
          <label class="form-check-label" for="procedure">
            <fmt:message key="doctor.view_patient.procedure"/>
          </label>
        </div>
        <div class="form-check">
          <input class="form-check-input" type="checkbox" name="MEDICATION" id="medication" value="true" ${medication == true ? 'checked' : ''}>
          <label class="form-check-label" for="medication">
            <fmt:message key="doctor.view_patient.medication"/>
          </label>
        </div>
        <c:if test="${current_user.role == 'DOCTOR'}">
          <div class="form-check">
            <input class="form-check-input" type="checkbox" name="OPERATION" id="operation" value="true" ${operation == true ? 'checked' : ''}>
            <label class="form-check-label" for="operation">
              <fmt:message key="doctor.view_patient.operation"/>
            </label>
          </div>
        </c:if>
      </div>
      <div class="col-sm-2">
        <p class="col-form-label col-sm-12 pt-0"><fmt:message key="doctor.my.patients.assign.status"/>:</p>
        <div class="form-check">
          <input class="form-check-input" type="checkbox" name="status" id="done" value="DONE">
          <label class="form-check-label" for="done">
            <fmt:message key="doctor.my.patients.assign.status.DONE"/>
          </label>
        </div>
        <div class="form-check">
          <input class="form-check-input" type="checkbox" name="status" id="ongoing" value="ONGOING">
          <label class="form-check-label" for="ongoing">
            <fmt:message key="doctor.my.patients.assign.status.ONGOING"/>
          </label>
        </div>
      </div>
    </fieldset>

    <div class="row">
      <div class="col-12">
        <input class="col-2 btn btn-outline-primary" type="submit" value="Submit">
      </div>
    </div>
  </form>
</div>
<table class="table table-light table-striped table-hover" aria-describedby="">
  <thead>
  <tr>
    <th scope="col"><fmt:message key="signup.fullName"/></th>
    <th scope="col"><fmt:message key="signup.gender"/></th>
    <th scope="col"><fmt:message key="patient.list.status"/></th>
    <th scope="col"><fmt:message key="doctor.my.patients.diagnose"/></th>
    <th scope="col"><fmt:message key="doctor.view_patient.assign.start_date"/></th>
    <th scope="col"><fmt:message key="doctor.my.patients.appointment.type"/></th>
    <th scope="col"><fmt:message key="doctor.my.patients.appointment.description"/></th>
    <th scope="col"><fmt:message key="doctor.my.patients.assign.status"/></th>
    <th scope="col"></th>
  </tr>
  </thead>
  <tbody>
  <c:choose>
    <c:when test="${sessionScope.appointments eq null or sessionScope.appointments.isEmpty()}">
      <th>
        <div class="text-danger"><fmt:message key="appointments.not_found"/></div>
      </th>
    </c:when>
    <c:otherwise>
      <c:forEach items="${sessionScope.appointments}" var="a">
        <tr>
          <th class="align-middle"
              scope="col">${a.hospitalCard.patient.firstname} ${a.hospitalCard.patient.lastname}</th>
          <th class="align-middle" scope="col"><fmt:message key="gender.${a.hospitalCard.patient.gender}"/></th>
          <th class="align-middle" scope="col"><fmt:message key="patient.status.${a.hospitalCard.patient.status}"/></th>
          <th class="align-middle" scope="col">${a.hospitalCard.diagnosis}</th>
          <th class="align-middle" scope="col"><ctg:dateFormat date="${a.startDate}" locale="${language}"/></th>
          <th class="align-middle" scope="col"><fmt:message key="doctor.my.patients.assign.type.${a.type}"/></th>
          <th class="align-middle" scope="col">${a.title}</th>
          <th class="align-middle" scope="col"><fmt:message key="doctor.my.patients.assign.status.${a.status}"/></th>
          <th class="align-middle" scope="col">
            <form>
              <input type="hidden" name="action" value="make_appointment">
              <input type="hidden" name="appointmentId" value="${a.id}">
              <input type="hidden" name="userId" value="${current_user.id}">
              <button type="submit" class="btn btn-secondary" ${a.status=='DONE' or (current_user.role=='NURSE' and a.type=='OPERATION') ? 'disabled' : ''}><fmt:message key="appointments.make"/></button>
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
           href="${pageContext.request.contextPath}/controller?action=appointments&page=${currentPageNo - 1}&recordsPerPage=${recordsPerPage}"
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
               href="${pageContext.request.contextPath}/controller?action=appointments&page=${i}&recordsPerPage=${recordsPerPage}">
                ${i}
            </a>
          </li>
        </c:otherwise>
      </c:choose>
    </c:forEach>
    <c:if test="${currentPageNo lt noOfPages}">
      <li class="page-item">
        <a class="page-link" href="${pageContext.request.contextPath}/controller?action=appointments&page=${currentPageNo + 1}&recordsPerPage=${recordsPerPage}" aria-label="Next">
          <span aria-hidden="true">&raquo;</span>
        </a>
      </li>
    </c:if>
  </ul>
</nav>
</div>
