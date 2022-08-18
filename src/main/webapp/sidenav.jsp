<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ctg" uri="custom_tags" %>
<ctg:setLang/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="message"/>

<div class="col-auto col-md-3 col-xl-2 px-sm-2 px-0 bg-dark">
    <div class="d-flex flex-column align-items-center align-items-sm-start px-3 pt-2 text-white min-vh-100">
        <a href="/" class="d-flex align-items-center pb-3 mb-md-0 me-md-auto text-white text-decoration-none">
            <span class="fs-5 d-none d-sm-inline"><fmt:message key="header.name"/> | <fmt:message key="header.name"/></span>
        </a>
        <a href="/" class="d-flex align-items-center pb-3 mb-md-0 me-md-auto text-white text-decoration-none">
            <span class="fs-5 d-none d-sm-inline">Menu</span>
        </a>
        <a href="/" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none">
            Sidebar
        </a>
        <ul class="nav nav-pills flex-column mb-sm-auto mb-0 align-items-center align-items-sm-start" id="menu">
            <li class="nav-item active">
                <a href="#" class="nav-link align-middle px-0">
                    <em class="fs-4 bi-house"></em> <span class="ms-1 d-none d-sm-inline">Home</span>
                </a>
            </li>
            <li>
                <a href="#" class="nav-link px-0 align-middle">
                    <em class="fs-4 bi-table"></em> <span class="ms-1 d-none d-sm-inline">Orders</span></a>
            </li>
            <li>
                <a href="#" class="nav-link px-0 align-middle">
                    <em class="fs-4 bi-people"></em> <span class="ms-1 d-none d-sm-inline">Customers</span> </a>
            </li>
        </ul>

        <form method="post" action="">
            <input type="hidden" name="action" value="lang">
            <label for="language"><fmt:message key="header.lang"/></label>
            <select id="language" name="language" onchange="submit()">
                <option value="en" ${language == 'en' ? 'selected' : ''}>EN</option>
                <option value="uk" ${language == 'uk' ? 'selected' : ''}>UA</option>
            </select>
        </form>
        <hr>
        <div class="dropdown pb-4">
            <a href="#" class="dropdown-toggle d-flex align-items-center text-white text-decoration-none"
               id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">
                <span class="d-none d-sm-inline mx-1">${current_user.firstname} ${current_user.lastname}</span>
            </a>
            <ul class="dropdown-menu dropdown-menu-dark text-small shadow" aria-labelledby="dropdownUser1">
                <li><a class="dropdown-item" href="#">New project...</a></li>
                <li><a class="dropdown-item" href="#">Settings</a></li>
                <li><a class="dropdown-item" href="#">Profile</a></li>
                <li>
                    <hr class="dropdown-divider">
                </li>
                <li><a class="dropdown-item" href="#">Sign out</a></li>
            </ul>
        </div>
    </div>
</div>


