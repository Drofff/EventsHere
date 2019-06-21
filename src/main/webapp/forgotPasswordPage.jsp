<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Events Here - Forgot Password</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
</head>
<body>
<div class="ui left fixed vertical menu">
    <div class="item">
        <img class="ui mini image" src="http://www.housingeurope.eu/image/167/sectionheaderpng/events.png">
    </div>
    <a class="item" href="/EventsHere/registration">Registration</a>
    <a class="item" href="/EventsHere/login">Login</a>
</div>
<form style="margin-left: 20%;" action="/EventsHere/forgotPassword" method="post">
    <%
        String msg = (String)request.getAttribute("message");

        if (msg != null) {
            out.println("<div class='ui negative message' style='margin-top: 5%; margin-bottom : 5%;'><div class='header'>" + msg + "</div></div>");
            request.removeAttribute("message");
        }
    %>

    <h3 class="header">Please input your email address</h3>
    <div class="ui input focus">
        <input type="text" name="email" placeholder="Email">
    </div>
    <button class="ui toggle button">
        Restore password
    </button>


</form>
</body>
</html>