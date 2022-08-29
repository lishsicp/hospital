<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<!DOCTYPE>
<html lang="${sessionScope.language}">
<head>
    <title>Access error</title>
</head>
<body>
  <h1>Error: don't have rights to access resource</h1>
  <button onclick="window.history.go(-1); return false;"
  type="submit">Return to previous page</button>
</body>
</html>
