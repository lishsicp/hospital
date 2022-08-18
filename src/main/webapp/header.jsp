<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ctg" uri="custom_tags" %>

<ctg:setLang />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="message" />

    <nav class="navbar navbar-expand-sm navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}"><fmt:message key="header.name" /></a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                    aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse flex-grow-1 text-right" id="myNavbar7">

                <div class="navbar-collapse collapse" id="collapseNavbar">
                    <ul class="navbar-nav ms-auto">
                        <li class="nav-item">
                            <a class="nav-link" href="" data-bs-target="#myModal" data-bs-toggle="modal"><fmt:message key="header.lang" /></a>
                        </li>
                    </ul>
                </div>

                <ul class="navbar-nav ms-auto flex-nowrap mx-2">
                    <form method="post" name="action" value="lang" >
                        <input type="hidden" name="action" value="lang">
                        <label for="language"></label>
                        <select id="language" name="language" onchange="submit()">
                            <option value="en" ${language == 'en' ? 'selected' : ''}>EN</option>
                            <option value="uk" ${language == 'uk' ? 'selected' : ''}>UA</option>
                        </select>
                    </form>
                    <c:if test="${current_user ne null}">
                        <div class="collapse navbar-collapse" id="navbarNavDropdown">
                            <ul class="navbar-nav">
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown"
                                       aria-expanded="false">
                                        <fmt:message key="header.menu"/>
                                    </a>
                                    <ul class="dropdown-menu dropdown-menu-end dropdown-menu-start">
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/controller?action=logout"><fmt:message key="header.logout" /></a></li>
                                        <li><a class="dropdown-item" href="#">Another action</a></li>
                                        <li><a class="dropdown-item" href="#">Something else here</a></li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </c:if>
                </ul>


            </div>
        </div>
    </nav>
