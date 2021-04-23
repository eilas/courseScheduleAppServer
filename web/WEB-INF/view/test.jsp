<%--
  Created by IntelliJ IDEA.
  User: Eilas
  Date: 2021/4/17
  Time: 16:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>TEST</title>
    <script src="/js/jquery-3.5.1.js" type="text/javascript"></script>
</head>
<script>
    $(function () {
        $("button:eq(0)").click(function () {
            $.get("/SQLtest?check=mysql", function (data) {
                $("#result").html(data)
            })
        })
        $("button:eq(1)").click(function () {
            $.get("/MQtest?action=prod&message=" + $("#message").val(), function (data) {
                $("#result").html(data)
            })
        })
        $("button:eq(2)").click(function () {
            $.get("/MQtest?action=cons", function (data) {
                $("#result").html(data)
            })
        })
        $("button:eq(3)").click(function () {
            $.get("/MQtest?action=onlyone", function (data) {
                $("#result").html(data)
            })
        })
    })
</script>
<body>
<button>测试MySQL</button>
<br>
<input name="message" type="text" id="message">
<button>测试RabbitMQ~生产M</button>
<button>测试RabbitMQ~消费M</button>
<button>测试RabbitMQ~一键测试</button>
<div id="result"></div>
</body>
</html>
