<%--
  Created by IntelliJ IDEA.
  User: Eilas
  Date: 2021/1/23
  Time: 17:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
</head>
<body>
<form action="/login" method="get">
  <button>get</button>
</form>
<form action="/login" method="post">
    user:<input name="id" type="text">
    password:<input name="pwd" type="password">
    <button>post</button>
</form>
</body>
</html>
