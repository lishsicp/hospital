<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ctg" uri="custom_tags" %>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="message" var="message"/>
<fmt:setBundle basename="validation.validation" var="val"/>



<!DOCTYPE html>
<html lang="${language}">
<head>
    <c:import url="meta.jsp"/>
    <title><fmt:message key="admin.doctor.add" bundle="${message}"/></title>
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
            <h3 class="m-1"><fmt:message key="admin.doctor.add" bundle="${message}"/> </h3>
            <form action="${pageContext.request.contextPath}/controller" name="form" class="needs-validation"
                  method="post">
                <input type="hidden" name="action" value="add_doctor">
                <div class="row g-3">
                    <div class="col-md-4">
                        <label class="form-label" for="login"><strong><fmt:message
                                key="signup.login" bundle="${message}"/></strong></label>
                        <input class="form-control" type="text" onclick="deleteInvalid(this)"
                               placeholder="<fmt:message key="signup.login.placeholder" bundle="${message}"/>" name="login" id="login"
                               required>
                        <div class="text-danger invalid-feedback"></div>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label" for="email"><strong><fmt:message key="signup.email" bundle="${message}"/></strong></label>
                        <input class="form-control" type="text" onclick="deleteInvalid(this)"
                               placeholder="<fmt:message key="signup.email.placeholder" bundle="${message}"/>" name="email" id="email"
                               required>
                        <div class="text-danger invalid-feedback"></div>
                    </div>
                    <br>
                    <div class="col-md-4">
                        <label class="form-label" for="psw"><strong><fmt:message
                                key="signup.password" bundle="${message}"/></strong></label>
                        <input class="form-control" type="password" onclick="deleteInvalid(this)"
                               placeholder="<fmt:message key="signup.password.placeholder" bundle="${message}"/>"
                               name="psw" id="psw" required>
                        <div class="text-danger invalid-feedback"></div>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label" for="psw-repeat"><strong><fmt:message
                                key="signup.password.repeat" bundle="${message}"/></strong></label>
                        <input class="form-control" type="password" onclick="deleteInvalid(this)"
                               placeholder="<fmt:message key="signup.password.repeat" bundle="${message}"/>"
                               name="psw-repeat" id="psw-repeat" required>
                        <div class="text-danger invalid-feedback"></div>
                    </div>
                    <br>
                    <div class="col-md-4">
                        <label for="firstName"><strong><fmt:message key="signup.firstName" bundle="${message}"/></strong></label>
                        <input class="form-control" type="text" placeholder="<fmt:message key="signup.firstName" bundle="${message}"/>"
                               name="firstName" id="firstName" onclick="deleteInvalid(this)"
                               required>
                        <div class="text-danger invalid-feedback"></div>
                    </div>
                    <div class="col-md-4">
                        <label for="lastName"><strong><fmt:message key="signup.lastName" bundle="${message}"/></strong></label>
                        <input class="form-control" type="text" placeholder="<fmt:message key="signup.lastName" bundle="${message}"/>"
                               name="lastName" id="lastName" onclick="deleteInvalid(this)" required>
                        <div class="text-danger invalid-feedback"></div>
                    </div>
                    <br>
                    <div class="col-md-4">
                        <label class="form-label" for="date_of_birth"><strong><fmt:message key="signup.bday" bundle="${message}"/></strong></label>
                        <input class="form-control" type="date" name="date_of_birth" onclick="deleteInvalid(this)"
                               id="date_of_birth" required>
                        <div class="text-danger invalid-feedback"></div>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label" for="gender"><strong><fmt:message
                                key="signup.gender" bundle="${message}"/></strong></label>
                        <select class="form-select required" name="gender" id="gender" onclick="deleteInvalid(this)" required>
                            <option disabled selected value=""><fmt:message
                                    key="signup.chose.gender" bundle="${message}"/></option>
                            <option value="MALE"><fmt:message key="signup.gender.male" bundle="${message}"/></option>
                            <option value="FEMALE"><fmt:message key="signup.gender.female" bundle="${message}"/></option>
                            <option value="OTHER"><fmt:message key="signup.gender.other" bundle="${message}"/></option>
                        </select>
                        <div class="text-danger invalid-feedback"></div>
                    </div>
                    <br>
                    <div class="col-md-4">
                        <label class="form-label" for="phone"><strong><fmt:message key="signup.phone" bundle="${message}"/> </strong>
                            (XXX)-XXX-XX-XX</label>
                        <input class="form-control" type="text" placeholder="(XXX)-XXX-XX-XX" name="phone" onclick="deleteInvalid(this)" id="phone">
                        <div class="text-danger invalid-feedback"></div>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label" for="address"><strong><fmt:message
                                key="signup.address" bundle="${message}"/></strong> <fmt:message key="signup.optional" bundle="${message}"/></label>
                        <input class="form-control" type="text"
                               placeholder="<fmt:message key="signup.address.placeholder" bundle="${message}"/>" name="address"
                               id="address">
                    </div>
                    <br>
                    <div class="col-md-4">
                        <label class="form-label" for="category"><strong><fmt:message
                                key="signup.category" bundle="${message}"/></strong></label>
                        <select class="form-select required" name="category" id="category" onclick="deleteInvalid(this)" required>
                            <option disabled selected value=""><fmt:message
                                    key="signup.chose.category" bundle="${message}"/></option>
                            <c:forEach var="c" items="${sessionScope.categories}">
                                <ctg:checkKey key="signup.category.${c.name}"/>
                                <c:choose>
                                    <c:when test="${pageScope.contains eq 'true'}">
                                        <option value="${c.id}"><fmt:message key="signup.category.${c.name}" bundle="${message}"/></option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${c.id}">${c.name}</option>
                                    </c:otherwise>
                                </c:choose>
<%--                                ${pageScope.remove('contains')}--%>
                            </c:forEach>
                        </select>
                        <div class="text-danger invalid-feedback"></div>
                    </div>
                    <div class="col-md-12">
                        <div class="clearfix">
                            <button type="submit" class="btn btn-outline-dark"><fmt:message key="signup.addDoctor" bundle="${message}"/></button>
                        </div>
                    </div>
                    <c:if test="${sessionScope.success != null}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <strong><fmt:message key="${sessionScope.success}" bundle="${message}" /></strong>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" onclick="${sessionScope.remove("success")}"></button>
                        </div>
                    </c:if>
                </div>
            </form>
        </div>
    </div>
</div>
<c:if test="${requestScope.doctorErrors != null}">
    <div class="text-danger">
        <c:forEach var="error" items="${requestScope.doctorErrors}">
            <script>
                var key = '${error.key}';
                var message = "<fmt:message key="${error.value}" bundle="${val}"/>";
                console.log(key + " " + message);
                var input = document.getElementById(key);
                input.classList.add("is-invalid")
                input.parentElement.querySelector(".text-danger").innerHTML = message;
            </script>
        </c:forEach>
    </div>
</c:if>
<c:if test="${requestScope.sql ne null}}"><div class="text-danger"><fmt:message key="${requestScope.sql}"/></div></c:if>

<c:import url="scripts.jsp"/>
</body>
</html>