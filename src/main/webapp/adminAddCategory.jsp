<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="custom_tags" %>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="message"/>
<fmt:setBundle basename="validation.validation" var="val"/>

<!DOCTYPE html>
<html lang="${language}">
<head>
  <c:import url="meta.jsp"/>
  <title><fmt:message key="admin.category.add"/></title>
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
      <h3 class="m-1"><fmt:message key="admin.category.add"/> </h3>
        <form action="${pageContext.request.contextPath}/controller" name="form" class="needs-validation"
              method="post">
          <div class="col-md-4">
          <input type="hidden" name="action" value="add_category">
          <label for="categoryName" class="form-label"></label>
          <input type="text" class="form-control" name="categoryName" id="categoryName" placeholder="<fmt:message key="admin.category.add"/> " onclick="deleteInvalid(this)" required>
          <br>
          <input type="submit" class="btn btn-outline-dark" value="<fmt:message key="admin.category.add"/> ">
          <div class="text-danger invalid-feedback"></div>
          </div>
        </form>
      <c:if test="${sessionScope.success != null}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
          <strong><fmt:message key="${sessionScope.success}" /></strong>
          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" onclick="${sessionScope.remove("success")}"></button>
        </div>
      </c:if>
    </div>
  </div>
</div>
<c:if test="${requestScope.categoryErrors != null}">
  <div class="text-danger">
    <c:forEach var="error" items="${requestScope.categoryErrors}">
      <script>
        var key = '${error.key}';
        var message = "<fmt:message key="${error.value}" bundle="${val}"/>";
        console.log(key + " " + message);
        var input = document.getElementById(key);
        input.classList.add("is-invalid")
        input.parentElement.querySelector(".text-danger").innerHTML = message;
      </script>
    </c:forEach>
    <c:if test="${sessionScope.sql ne null}}"><div class="text-danger"><fmt:message key="${sessionScope.sql}"/></div></c:if>
  </div>
</c:if>

<c:import url="scripts.jsp"/>
</body>
</html>

