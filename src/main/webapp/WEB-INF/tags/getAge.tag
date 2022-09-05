<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="date" required="true" %>

<fmt:parseDate value="${date}" var="parsedDate" type="date"  pattern="yyyy-MM-dd" />
<jsp:useBean id="now" class="java.util.Date" />
<fmt:parseNumber type="number" integerOnly = "true"
                 value="${(now.time - parsedDate.time)/(1000 * 60 * 60 * 24 * 365)}" />
