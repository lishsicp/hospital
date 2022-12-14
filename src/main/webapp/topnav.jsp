<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="custom_tags" %>

<%--<ctg:setLang/>--%>
<%--<fmt:setLocale value="${sessionScope.current_user ne null ? sessionScope.current_user.locale : sessionScope.language}"/>--%>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="message"/>

<nav class="navbar navbar-expand-lg navbar-light bg-light border-bottom">
    <div class="container-fluid">
        <c:if test="${sessionScope.current_user ne null}">
            <button class="btn" id="sidebarToggle" data-bs-toggle="tooltip" data-bs-placement="bottom"
                    title="<fmt:message key="header.toggle.tooltip"/>"><h3 style="margin-bottom: 0;"><i id="menuToggle" class="bi bi-toggle-on"></i></h3>
            </button>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent"
                    aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"><span
                    class="navbar-toggler-icon"></span></button>
        </c:if>
        <c:if test="${sessionScope.current_user eq null}">
            <h3 class=""><fmt:message key="header.name"/> </h3>
        </c:if>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav ms-auto mt-2 mt-lg-0">
                <li class="nav-item h-100">
                    <p class="nav-link" style="margin-bottom: 0;"><fmt:message key="header.lang"/>: </p>
                </li>
                <li class="nav-item h-100">
                    <form class="nav-link" style="margin-bottom: 0;">
                        <label for="language"></label>
                        <select class="" id="language" name="language" onchange="submit()">
                            <option value="en-US" ${sessionScope.language == 'en-US' ? 'selected' : ''}>EN</option>
                            <option value="uk-UA" ${sessionScope.language == 'uk-UA' ? 'selected' : ''}>UA</option>
                        </select>
                    </form>
                </li>
                <c:if test="${sessionScope.current_user ne null}">
                    <li class="nav-item h-100 dropdown">
                        <a class="nav-link dropdown-toggle" style="margin-bottom: 0;" id="navbarDropdown" href="#" role="button"
                           data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">${sessionScope.current_user.email}</a>
                        <div class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/controller?action=view_user"><fmt:message key="profile"/></a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/controller?action=logout"><fmt:message
                                    key="header.logout"/></a>
                        </div>
                    </li>
                </c:if>
            </ul>
        </div>
    </div>
</nav>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"
        integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF"
        crossorigin="anonymous"></script>

