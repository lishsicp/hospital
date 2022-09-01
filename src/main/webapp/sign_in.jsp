<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="custom_tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%--<ctg:setLang />--%>
<fmt:setLocale value="${sessionScope.language}" />
<fmt:setBundle basename="message" />
<fmt:setBundle basename="validation.validation" var="v" />

<!DOCTYPE>
<html lang="${sessionScope.language}">
<head>
    <c:import url="meta.jsp"/>
    <script src='https://www.google.com/recaptcha/api.js?hl=${sessionScope.language eq 'uk-UA' ? 'ru' : 'en'}'></script>
    <title><fmt:message key="signin.title"/></title>
</head>
<body>
<c:import url="topnav.jsp"/>
<div class="container">
    <h1 class="m-2"><fmt:message key="signin.signin"/></h1>
    <form action="${pageContext.request.contextPath}/controller" class="needs-validation" method="post">
        <input type="hidden" name="action" value="sign_in">
        <div class="row g-3 justify-content-center align-content-center">
            <div class="col-md-12">
                <div class="col-md-4">
                    <c:set var="error" scope="page" value="${sessionScope.errors.contains('signin.error.email_not_found') ? 'is-invalid' : ''}" />
                    <label class="col-sm-2 col-form-label" for="email"><strong><fmt:message key="signin.email" /></strong></label>
                    <input class="form-control ${error}}" type="email" placeholder="<fmt:message key="signin.email.placeholder" />" onclick="deleteInvalid(this)" name="email" id="email" required>
                    <div class="text-danger py-2 mx-1"></div>
                </div>
            </div>
            <div class="col-md-12">
                <div class="col-md-4">
                    <c:set var="error" scope="page" value="${sessionScope.errors.contains('signin.error.wrong_password') ? 'is-invalid' : ''}" />
                    <label class="form-label" for="psw"><strong><fmt:message key="signin.password"/></strong></label>
                    <input class="form-control ${error}" type="password" placeholder="<fmt:message key="signin.password.placeholder"/>"
                           name="psw" id="psw" onclick="deleteInvalid(this)" required>
                    <div class="text-danger"></div>
                </div>
            </div>
            <div class="col-md-12">
                <div class="text-danger small m-1">
                    <c:if test="${sessionScope.errors != null}">
                        <fmt:message key="${sessionScope.errors[0]}" />
                    </c:if>
                </div>
                <div class="clearfix">
                    <button type="submit" class="btn btn-primary"><fmt:message key="signin.signin" /></button>
                </div>
            </div>
            <div class="col-md-12">
                <div class="col-md-4">
                    <!-- reCAPTCHA -->
                    <div class="g-recaptcha"
                         data-sitekey="6LeKHr4hAAAAAA3eXh8oC86Haxrqeb73Hx_Xt-f0"></div>
                </div>
            </div>
        </div>
    </form>
    <c:if test="${sql ne null}">
        <p class="text-danger"><fmt:message key="${sql}"/></p>
    </c:if>
    <c:if test="${captchaError ne null}">
        <p class="text-danger"><fmt:message key="${captchaError}"/></p>
    </c:if>
<%--    <p><fmt:message key="signin.account" /> <a class="link-info text-decoration-none" href="${pageContext.request.contextPath}/sign_up.jsp"><fmt:message key="header.signup" /></a></p>--%>
</div>
<script>
    window.onload = function() {
        var $recaptcha = document.querySelector('#g-recaptcha-response');

        if($recaptcha) {
            $recaptcha.setAttribute("required", "required");
        }
    };
</script>
<c:import url="scripts.jsp"/>
<c:import url="footer.jsp" />

</body>
</html>