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
        <div class="container-fluid">
                <form class="row g-3" action="${pageContext.request.contextPath}/controller?action=list_patients&page=${currentPageNo}" method="post">
                    <div class="col-4 p-2">
                        <input type="hidden" name="action" value="list_patients">
                        <label for="sorting_type" class=""><fmt:message key="patient.sort"/></label>
                        <select class="form-select" name="sorting_type" id="sorting_type">
                            <option value="NAME" disabled=""><fmt:message key="patient.select.sort"/></option>
                            <option value="NAME" ${sessionScope.sortBy == 'NAME' ? 'selected' : ''}><fmt:message key="patient.sort.name"/></option>
                            <option value="DATE" ${sessionScope.sortBy == 'DATE' ? 'selected' : ''}><fmt:message key="patient.sort.date"/></option>
                        </select>
                    </div>
                    <div class="pt-4 col-4">
                        <div class="">
                            <label for="recordsPerPage"><fmt:message key="items.per.page"/></label>
                            <input type="number" name="recordsPerPage" id="recordsPerPage" value="${recordsPerPage}" width="20 px">
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-group">
                            <label for="no_doctor"><fmt:message key="without.doctor"/></label>
                            <input type="checkbox" id="no_doctor" name="no_doctor" value="true" ${no_doctor == true ? 'checked' : ''}>
                            <input class="col-2 btn-outline-primary" type="submit" value="Submit">
                        </div>
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
                    <th scope="col"><fmt:message key="patient.list.status"/></th>
                    <th scope="col"><fmt:message key="patient.list.doctor"/></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${sessionScope.patients eq null or sessionScope.patients.isEmpty()}">
                        <th>
                            <div class="text-danger"><fmt:message key="patient.not_fount"/></div>
                        </th>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${sessionScope.patients}" var="p" varStatus="i">
                            <tr>
                                <th class="align-middle" scope="col">${p.firstname} ${p.lastname}</th>
                                <th class="align-middle" scope="col"><ctg:dateFormat date="${p.dateOfBirth}"
                                                                locale="${sessionScope.language}"/></th>
                                <th class="align-middle" scope="col"><fmt:message key="gender.${p.gender}"/></th>
                                <th class="align-middle" scope="col">${p.email}</th>
                                <th class="align-middle" scope="col"><fmt:message key="patient.status.${p.status}"/></th>
                                <th class="align-middle" scope="col">
                                    <c:choose>
                                        <c:when test="${p.doctor ne null}">
                                            ${p.doctor.user.firstname} ${p.doctor.user.lastname} - <fmt:message
                                                key="signup.category.${p.doctor.category.name}"/>
                                        </c:when>
                                        <c:when test="${p.doctor eq null}">
                                            <form class="form-group row" style="margin-bottom: 0;" action="${pageContext.request.contextPath}/controller">
                                                <input type="hidden" name="action" value="doctors_by_category">
                                                <input type="hidden" name="patient_id" value="${p.id}">
                                                <div class="col">
                                                    <label class="form-label" for="category"></label>
                                                    <select class="form-select form-select-sm required" style="margin-bottom: 0;" name="category_id"
                                                            id="category"
                                                            onclick="deleteInvalid(this)" required>
                                                        <option selected disabled value=""><fmt:message
                                                                key="signup.chose.category"/></option>
                                                        <c:forEach var="c" items="${sessionScope.categories}">
                                                            <option value="${c.id}"><fmt:message
                                                                    key="signup.category.${c.name}"/></option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                                <div class="col">
                                                    <!-- Button trigger modal -->
                                                    <button style="margin-bottom: 0; margin-top: 7px;" type="submit" class="btn btn-outline-warning btn-sm"
                                                            data-bs-toggle="modal"
                                                            onclick="<c:set var="p" value="${p}"/>"
                                                            data-bs-target="#doctorModal${i.index}">
                                                        <fmt:message key="patient.assign_doctor"/>
                                                    </button>
                                                </div>
                                            </form>
                                        </c:when>
                                    </c:choose>
                                </th>
                                <th class="align-middle" scope="col">
                                    <form action="${pageContext.request.contextPath}/controller" class="h-100" style="margin-bottom: 0;" method="post">
                                        <input class="btn btn-outline-danger btn-sm" type="submit" value="<fmt:message key="delete"/>"/>
                                        <input type="hidden" name="action" value="delete_patient"/>
                                        <input type="hidden" name="patientId" value="${p.id}"/>
                                    </form>
                                </th>
                                <th class="align-middle" scope="col">
                                    <!-- Button trigger modal -->
                                    <button type="button" class="btn btn-outline-primary btn-sm" data-bs-toggle="modal"
                                            data-bs-target="#exampleModal${i.index}" onclick="<c:set var="patient" value="${p}"/>"><fmt:message key="patient.edit"/>
                                    </button>
                                    <!-- Modal -->
                                    <div class="modal fade" id="exampleModal${i.index}" tabindex="-1"
                                         aria-labelledby="exampleModalLabel${i.index}"
                                         aria-hidden="true">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title" id="exampleModalLabel${i.index}"><fmt:message key="patient.edit"/></h5>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                            aria-label="Close"></button>
                                                </div>
                                                <form action="${pageContext.request.contextPath}/controller" method="post">
                                                    <input type="hidden" name="action" value="edit_patient">
                                                    <input type="hidden" name="id" value="${p.id}"/>
                                                    <input type="hidden" name="status" value="${p.status}">
                                                    <input type="hidden" name="doctor_id" value="${p.doctor.id}"/>
                                                    <div class="modal-body">
                                                        <div class="mb-3">
                                                            <label for="firstName"><fmt:message
                                                                    key="signup.firstName"/></label>
                                                            <input class="form-control" type="text"
                                                                   placeholder="<fmt:message key="signup.firstName"/>"
                                                                   onclick="deleteInvalid(this)"
                                                                   name="firstName" id="firstName"
                                                                   value="${patient.firstname}"
                                                                   required>
                                                            <div class="text-danger invalid-feedback"></div>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="lastName"><fmt:message
                                                                    key="signup.lastName"/></label>
                                                            <input class="form-control" type="text"
                                                                   placeholder="<fmt:message key="signup.lastName"/>"
                                                                   onclick="deleteInvalid(this)"
                                                                   name="lastName" id="lastName" value="${patient.lastname}"
                                                                   required>
                                                            <div class="text-danger invalid-feedback"></div>
                                                        </div>
                                                        <br>
                                                        <div class="mb-3">
                                                            <label class="form-label"
                                                                   for="date_of_birth"><fmt:message
                                                                    key="signup.bday"/></label>
                                                            <input class="form-control" type="date" name="date_of_birth"
                                                                   onclick="deleteInvalid(this)"
                                                                   id="date_of_birth" value="<ctg:dateFormat date="${p.dateOfBirth}" locale="uk-UA"/> "
                                                                   required>
                                                            <div class="text-danger invalid-feedback"></div>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label class="form-label" for="gender"><fmt:message
                                                                    key="signup.gender"/></label>
                                                            <select class="form-select required" name="gender"
                                                                    id="gender" required onclick="deleteInvalid(this)">
                                                                <option disabled selected
                                                                        value="<fmt:message key="signup.chose.gender"/>">
                                                                    <fmt:message
                                                                            key="signup.chose.gender"/></option>
                                                                <option value="MALE"><fmt:message
                                                                        key="signup.gender.male"/></option>
                                                                <option value="FEMALE"><fmt:message
                                                                        key="signup.gender.female"/></option>
                                                                <option value="OTHER"><fmt:message
                                                                        key="signup.gender.other"/></option>
                                                            </select>
                                                            <div class="text-danger invalid-feedback"></div>
                                                        </div>
                                                        <br>
                                                        <div class="mb-3">
                                                            <label class="form-label" for="email"><fmt:message
                                                                    key="signup.email"/></label>
                                                            <input class="form-control" type="text"
                                                                   placeholder="<fmt:message key="signup.email.placeholder"/>"
                                                                   onclick="deleteInvalid(this)"
                                                                   name="email" id="email" value="${patient.email}" required>
                                                            <div class="text-danger invalid-feedback"></div>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary"
                                                                data-bs-dismiss="modal"><fmt:message key="close"/>
                                                        </button>
                                                        <input class="btn btn-primary" type="submit" value="<fmt:message key="save.changes"/>"/>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
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
                               href="${pageContext.request.contextPath}/controller?action=list_patients&sorting_type=${sortBy}&page=${currentPageNo - 1}&recordsPerPage=${recordsPerPage}&no_doctor=${no_doctor}"
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
                                       href="${pageContext.request.contextPath}/controller?action=list_patients&sorting_type=${sortBy}&page=${i}&recordsPerPage=${recordsPerPage}&no_doctor=${no_doctor}">
                                            ${i}
                                    </a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <c:if test="${currentPageNo lt noOfPages}">
                        <li class="page-item">
                            <a class="page-link" href="${pageContext.request.contextPath}/controller?action=list_patients&sorting_type=${sortBy}&page=${currentPageNo + 1}&recordsPerPage=${recordsPerPage}&no_doctor=${no_doctor}" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </nav>
            <c:if test="${success != null}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong><fmt:message key="${success}" /></strong>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" onclick="${sessionScope.remove("success")}"></button>
                </div>
            </c:if>
            <c:if test="${fail != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong><fmt:message key="${fail}" /></strong>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" onclick="${sessionScope.remove("fail")}"></button>
                </div>
            </c:if>
            <c:if test="${patientUpdateErrors != null}">
                <div class="text-danger">
                    <c:forEach var="error" items="${patientUpdateErrors}">
                        <fmt:message key="${error.value}" bundle="${val}"/> <br>
                    </c:forEach>
                </div>
                <c:if test="${patientUpdateErrors[sql] ne null}">
                    <div class="text-danger" id="sql">
                        <fmt:message key="${patientUpdateErrors[sql]}"/>
                    </div>
                </c:if>
            </c:if>
        </div>
    </div>
</div>
<c:import url="scripts.jsp"/>
</body>
</html>