<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="custom_tags" %>

<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="message"/>
<fmt:setBundle basename="validation.validation" var="val"/>


<!DOCTYPE html>
<html lang="${sessionScope.language}">
<head>
    <c:import url="meta.jsp"/>
    <title><fmt:message key="doctor.view_patient.patientDetails"/></title>
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
        <div class="container-fluid">
            <c:if test="${sessionScope.hospitalCard != null}">
            <div class="row">
                <div class="col-md-5">
                    <h2 class="p-2"><fmt:message key="doctor.view_patient.patientDetails"/></h2>
                    <div class="card mb-3">
                        <div class="card-body">
                            <div class="row">
                                <div class="col-sm-3">
                                    <h6 class="mb-0"><fmt:message key="signup.fullName"/></h6>
                                </div>
                                <div class="col-sm-9 text-secondary">
                                        ${hospitalCard.patient.firstname} ${hospitalCard.patient.lastname}
                                </div>
                            </div>
                            <hr>
                            <div class="row">
                                <div class="col-sm-3">
                                    <h6 class="mb-0"><fmt:message key="signup.email"/></h6>
                                </div>
                                <div class="col-sm-9 text-secondary">
                                        ${hospitalCard.patient.email}
                                </div>
                            </div>
                            <hr>
                            <div class="row">
                                <div class="col-sm-3">
                                    <h6 class="mb-0"><fmt:message key="patient.age"/></h6>
                                </div>
                                <div class="col-sm-9 text-secondary">
                                    <ctg:getAge date="${hospitalCard.patient.dateOfBirth}"/>
                                </div>
                            </div>
                            <hr>
                            <div class="row">
                                <div class="col-sm-3">
                                    <h6 class="mb-0"><fmt:message key="signup.gender"/></h6>
                                </div>
                                <div class="col-sm-9 text-secondary">
                                    <fmt:message key="gender.${hospitalCard.patient.gender}"/>
                                </div>
                            </div>
                            <hr>
                            <div class="row">
                                <div class="col-sm-3">
                                    <h6 class="mb-0"><fmt:message key="patient.list.status"/></h6>
                                </div>
                                <div class="col-sm-9 text-secondary">
                                    <fmt:message key="patient.status.${hospitalCard.patient.status}"/>
                                </div>
                            </div>
                            <hr>
                        </div>
                    </div>
                    <c:if test="${hospitalCard.patient.status != 'DISCHARGED'}">
                        <h5 class="p-2"><fmt:message key="assign.appointment"/></h5>
                        <div class="card mb-3">
                            <div class="card-body">
                                <form action="${pageContext.request.contextPath}/controller" method="post">
                                    <input type="hidden" name="action" value="assign_appointment">
                                    <input type="hidden" name="cardId" value="${hospitalCard.id}">
                                    <div class="row">
                                        <div class="col-sm-3">
                                            <h6 class="mb-0"><fmt:message key="doctor.my.patients.appointment.type"/></h6>
                                        </div>
                                        <div class="col-sm-9 text-secondary">
                                            <label class="form-label" for="type" style="display: none;"></label>
                                            <select class="form-select" name="appointment" id="type">
                                                <option disabled value=""><fmt:message
                                                        key="doctor.view_patient.select.type"/></option>
                                                <option value="PROCEDURE"><fmt:message
                                                        key="doctor.view_patient.procedure"/></option>
                                                <option value="MEDICATION"><fmt:message
                                                        key="doctor.view_patient.medication"/></option>
                                                <option value="OPERATION"><fmt:message
                                                        key="doctor.view_patient.operation"/></option>
                                            </select>
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <div class="col-sm-3">
                                            <h6 class="mb-0"><fmt:message
                                                    key="doctor.my.patients.appointment.description"/></h6>
                                        </div>
                                        <div class="col-sm-9 text-secondary">
                                            <label for="description"><fmt:message
                                                    key="doctor.view_patient.add.description"/></label>
                                            <textarea rows="2" class="form-control" id="description" name="description"
                                                      required></textarea>
                                        </div>
                                    </div>
                                    <hr>
                                    <button type="submit" class="btn btn-secondary"><fmt:message key="assign"/></button>
                                </form>
                            </div>
                        </div>
                    </c:if>
                    <div class="form-group row">
                        <c:if test="${hospitalCard.diagnosis ne null && sessionScope.allDone }">
                            <form class="col-4" action="${pageContext.request.contextPath}/controller" method="post">
                                <input type="hidden" name="action" value="discharge_patient">
                                <input type="hidden" name="discharge" value="${hospitalCard.patient.id}">
                                <input type="hidden" name="cardId" value="${hospitalCard.id}">
                                <button class="btn btn-secondary" id="discharge" ${hospitalCard.patient.status eq 'DISCHARGED' ? 'disabled' : ''}><fmt:message key="doctor.view_patient.discharge"/></button>
                            </form>
                        </c:if>
                        <c:if test="${hospitalCard.patient.status eq 'DISCHARGED'}">
                            <form class="col-6" action="${pageContext.request.contextPath}/controller" method="post">
                                <input type="hidden" name="action" value="download">
                                <input type="hidden" name="hospitalCardId" value="${hospitalCard.id}">
                                <button class="btn btn-secondary" id="pdf"><em class="bi bi-download m-1"></em><fmt:message key="doctor.view_patient.download.info"/></button>
                            </form>
                        </c:if>
                    </div>
                    <c:if test="${requestScope.errors != null}">
                        <div class="text-danger">
                            <c:forEach var="error" items="${requestScope.errors}">
                                <fmt:message key="${error.value}" bundle="${val}"/>
                            </c:forEach>
                        </div>
                    </c:if>
                    <c:if test="${dischargeInfo ne null}">
                        <div class="text-danger">
                            <fmt:message key="${dischargeInfo}"/>
                        </div>
                    </c:if>
                    <c:if test="${sql ne null}">
                        <div class="text-danger" id="sql">
                            <fmt:message key="${sql}"/>
                        </div>
                    </c:if>
                    <c:if test="${sessionScope.success != null}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <strong><fmt:message key="${sessionScope.success}"/></strong>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"
                                    onclick="${sessionScope.remove("success")}"></button>
                        </div>
                    </c:if>
                    <c:if test="${requestScope.fail != null}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <strong><fmt:message key="${sessionScope.success}"/></strong>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"
                                    onclick="${requestScope.remove("fail")}"></button>
                        </div>
                    </c:if>
                </div>
                </c:if>
                <div class="col-md-7">
                    <h2 class="p-2"><fmt:message key="doctor.view_patient.medicalInfo"/></h2>
                    <div class="card mb-3">
                        <div class="card-body">
                            <c:if test="${hospitalCard.diagnosis eq null}">
                                <div class="row">
                                    <div class="col-sm-3">
                                        <h4 class="mb-0"><fmt:message key="doctor.my.patients.diagnose"/></h4>
                                    </div>
                                    <div class="col-sm-9 text-secondary">
                                        <form class="needs-validation" method="post" id="update_diagnosis"
                                              action="${pageContext.request.contextPath}/controller">
                                            <label class="form-label" for="diagnosis"><fmt:message
                                                    key="doctor.my.patient.set.diagnosis"/>:</label>

                                            <input type="hidden" name="action" value="update_diagnosis">
                                            <input type="hidden" name="cardId" value="${hospitalCard.id}">
                                            <textarea rows="2" class="form-control" id="diagnosis" name="diagnosis"
                                                       required></textarea>
                                            <br>
                                        </form>
                                    </div>
                                    <hr>
                                    <button type="submit" class="btn btn-secondary mx-3 col-sm-2"
                                            form="update_diagnosis"><fmt:message
                                            key="doctor.my.patients.save"/></button>
                                    <div class="text-danger invalid-feedback"></div>
                                </div>
                            </c:if>
                            <c:if test="${hospitalCard.diagnosis ne null}">
                                <div class="row">
                                    <div class="col-sm-3">
                                        <h6 class="mb-0"><fmt:message key="doctor.my.patients.diagnose"/></h6>
                                    </div>
                                    <div class="col-sm-9 text-secondary">
                                            ${hospitalCard.diagnosis}
                                    </div>
                                </div>
                                <hr>
                                <!-- button trigger modal -->
                                <button type="button" class="btn btn-outline-primary mx-3 col-sm-3"
                                        data-bs-toggle="modal"
                                        data-bs-target="#diagnoseModal"
                                        ${hospitalCard.patient.status eq 'DISCHARGED' ? 'disabled' : ''}
                                        onclick="<c:set var="h" value="${hospitalCard}"/>"><fmt:message
                                        key="diagnosis.edit"/>
                                </button>
                                <!-- Modal -->
                                <div class="modal fade" id="diagnoseModal" tabindex="-1"
                                     aria-labelledby="diagnoseModalLabel"
                                     aria-hidden="true">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="diagnoseModalLabel"><fmt:message
                                                        key="diagnosis.edit"/></h5>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                        aria-label="Close"></button>
                                            </div>
                                            <form action="${pageContext.request.contextPath}/controller" method="post">
                                                <input type="hidden" name="action" value="update_diagnosis">
                                                <input type="hidden" name="cardId" value="${h.id}">
                                                <div class="modal-body">
                                                    <div class="mb-3">
                                                        <label for="diagnosis"><fmt:message
                                                                key="doctor.my.patients.diagnose"/>:</label>
                                                        <textarea class="form-control"
                                                                  name="diagnosis" id="diagnosis"
                                                                  required>${h.diagnosis}</textarea>
                                                        <div class="text-danger invalid-feedback"></div>
                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary"
                                                            data-bs-dismiss="modal"><fmt:message key="close"/>
                                                    </button>
                                                    <input class="btn btn-primary" type="submit"
                                                           value="<fmt:message key="save.changes"/>"/>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                            <hr>
                        <div class="col-sm-5">
                            <h4 class="mb-3"><fmt:message key="doctor.view_patient.appointmentInfo"/></h4>
                        </div>
                            <c:if test="${appointments != null}">
                            <c:forEach items="${appointments}" var="appointment" varStatus="i">
                                <div class="row">
                                    <div class="col-sm-3">
                                        <h6 class="mb-0"><fmt:message key="doctor.view_patient.assign.start_date"/></h6>
                                    </div>
                                    <div class="col-sm-9 text-secondary">
                                        <ctg:dateFormat date="${appointment.startDate}" locale="${language}"/>
                                    </div>
                                </div>
                                <hr>
                                <c:if test="${appointment.endDate ne null}">
                                    <div class="row">
                                        <div class="col-sm-3">
                                            <h6 class="mb-0"><fmt:message key="doctor.view_patient.assign.end_date"/></h6>
                                        </div>
                                        <div class="col-sm-9 text-secondary">
                                            <ctg:dateFormat date="${appointment.endDate}" locale="${language}"/>
                                        </div>
                                    </div>
                                    <hr>
                                </c:if>
                                <div class="row">
                                    <div class="col-sm-3">
                                        <h6 class="mb-0"><fmt:message key="doctor.my.patients.appointment.description"/></h6>
                                    </div>
                                    <div class="col-sm-9 text-secondary">
                                            ${appointment.title}
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-sm-3">
                                        <h6 class="mb-0"><fmt:message key="doctor.my.patients.assign.status"/></h6>
                                    </div>
                                    <div class="col-sm-9 text-secondary">
                                        <fmt:message key="doctor.my.patients.assign.status.${appointment.status}"/>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-sm-3">
                                        <h6 class="mb-0"><fmt:message key="doctor.my.patients.assign.type"/></h6>
                                    </div>
                                    <div class="col-sm-9 text-secondary">
                                        <fmt:message key="doctor.my.patients.assign.type.${appointment.type}"/>
                                    </div>
                                </div>
                                <hr>
                                <!-- button trigger modal -->
                                <button type="button" class="btn btn-outline-primary mx-3 col-sm-3"
                                        data-bs-toggle="modal"
                                        data-bs-target="#appointmentModal${i.index}"
                                        ${appointment.endDate != null ? 'disabled' : ''}><fmt:message
                                        key="patient.edit"/>
                                </button>
                                <hr>
                                <!-- Modal -->
                                <div class="modal fade" id="appointmentModal${i.index}" tabindex="-1"
                                     aria-labelledby="appointmentModalLabel${i.index}"
                                     aria-hidden="true">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="appointmentModalLabel${i.index}"><fmt:message
                                                        key="appointment.edit"/></h5>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                        aria-label="Close"></button>
                                            </div>
                                            <form action="${pageContext.request.contextPath}/controller" method="post">
                                                <input type="hidden" name="action" value="update_appointment">
                                                <input type="hidden" name="appointmentId" value="${appointment.id}">
                                                <div class="modal-body">
                                                    <label class="form-label" for="appointmentType"><fmt:message key="doctor.my.patients.appointment.type"/></label>
                                                    <select class="form-select" name="appointment" id="appointmentType">
                                                        <option disabled value=""><fmt:message
                                                                key="doctor.view_patient.select.type"/></option>
                                                        <option ${appointment.type eq 'PROCEDURE' ? 'selected' : ''} value="PROCEDURE"><fmt:message
                                                                key="doctor.view_patient.procedure"/></option>
                                                        <option ${appointment.type eq 'MEDICATION' ? 'selected' : ''} value="MEDICATION"><fmt:message
                                                                key="doctor.view_patient.medication"/></option>
                                                        <option ${appointment.type eq 'OPERATION' ? 'selected' : ''} value="OPERATION"><fmt:message
                                                                key="doctor.view_patient.operation"/></option>
                                                    </select>
                                                    <br>
                                                    <label for="desc"><fmt:message
                                                            key="doctor.view_patient.add.description"/></label>
                                                    <textarea rows="2" class="form-control" id="desc" name="description"
                                                               required></textarea>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary"
                                                            data-bs-dismiss="modal"><fmt:message key="close"/>
                                                    </button>
                                                    <input class="btn btn-primary" type="submit"
                                                           value="<fmt:message key="save.changes"/>"/>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                                <br>
                            </c:forEach>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<c:import url="scripts.jsp"/>
</body>
</html>