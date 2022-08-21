<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="message"/>
<fmt:setBundle basename="validation.validation" var="val"/>

<!DOCTYPE>
<html lang="${sessionScope.language}">
<head>
    <c:import url="meta.jsp"/>
    <title><fmt:message key="admin.patient.add"/></title>
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
            <%--<div class="alert alert-danger alert-dismissible fade m-4 hiding" id="fail">--%>
            <%--    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>--%>
            <%--</div>--%>
            <%--<div class="alert alert-success alert-dismissible fade show m-4" id="fail">--%>
            <%--    <strong>Error</strong>--%>
            <%--    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>--%>
            <%--</div>--%>
            <div class="container">
                <h3><fmt:message key="admin.patient.add"/></h3>
                <form method="POST" class="needs-validation">
                    <input type="hidden" name="action" value="add_patient">
                    <div class="row g-3 m-2">
                        <div class="col-md-4">
                            <label for="firstName"><strong><fmt:message key="signup.firstName"/></strong></label>
                            <input class="form-control" type="text" placeholder="<fmt:message key="signup.firstName"/>" onclick="deleteInvalid(this)"
                                   name="firstName" id="firstName"
                                   required>
                            <div class="text-danger invalid-feedback"></div>
                        </div>
                        <div class="col-md-4">
                            <label for="lastName"><strong><fmt:message key="signup.lastName"/></strong></label>
                            <input class="form-control" type="text" placeholder="<fmt:message key="signup.lastName"/>" onclick="deleteInvalid(this)"
                                   name="lastName" id="lastName" required>
                            <div class="text-danger invalid-feedback"></div>
                        </div>
                        <br>
                        <div class="col-md-4">
                            <label class="form-label" for="date_of_birth"><strong><fmt:message key="signup.bday"/></strong></label>
                            <input class="form-control" type="date" name="date_of_birth" onclick="deleteInvalid(this)"
                                   id="date_of_birth" required>
                            <div class="text-danger invalid-feedback"></div>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label" for="gender"><strong><fmt:message key="signup.gender"/></strong></label>
                            <select class="form-select required" name="gender" id="gender" required onclick="deleteInvalid(this)">
                                <option disabled selected value="<fmt:message key="signup.chose.gender"/>"><fmt:message
                                        key="signup.chose.gender"/></option>
                                <option value="MALE"><fmt:message key="signup.gender.male"/></option>
                                <option value="FEMALE"><fmt:message key="signup.gender.female"/></option>
                                <option value="OTHER"><fmt:message key="signup.gender.other"/></option>
                            </select>
                            <div class="text-danger invalid-feedback"></div>
                        </div>
                        <br>
                        <div class="col-md-4">
                            <label class="form-label" for="email"><strong><fmt:message key="signup.email"/></strong></label>
                            <input class="form-control" type="text" placeholder="<fmt:message key="signup.email.placeholder"/>" onclick="deleteInvalid(this)"
                                   name="email" id="email" required>
                            <div class="text-danger invalid-feedback"></div>
                        </div>
                        <div class="col-md-12">
                            <div class="clearfix">
                                <button type="submit" class="btn btn-outline-dark"><fmt:message
                                        key="admin.patient.add.patient"/></button>
                            </div>
                        </div>
                        <c:if test="${requestScope.success != null}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <strong><fmt:message key="${sessionScope.success}" /></strong>
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                    </div>
                </form>
                <c:if test="${sessionScope.userErrors != null}">
                    <div class="text-danger">
                        <c:forEach var="error" items="${sessionScope.userErrors}">
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
            </div>
    </div>
</div>
<c:import url="scripts.jsp"/>
</body>
</html>