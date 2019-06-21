<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Events Here - Login</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
</head>
<body>
<div class="ui left fixed vertical menu">
    <div class="item">
        <img class="ui mini image" src="http://www.housingeurope.eu/image/167/sectionheaderpng/events.png">
    </div>
    <a class="item" href="/registration">Registration</a>
</div>
<%

    String message = request.getParameter("message");

    if (message != null && ! message.isEmpty()) {

        out.println("<div class='ui negative message' style='margin-left: 20%;'><div class='header'>" + message + "</div></div>");

    }

%>
<form class="ui form" method="post" action="/login" style="margin-left: 20%; margin-top: 10%;">
    <div class="field">
        <label>Email</label>
        <input type="text" name="username" placeholder="your@mail.com">
    </div>
    <div class="field">
        <label>Password</label>
        <input type="password" name="password" placeholder="Password">
    </div>
    <div class="field">
        <div class="ui checkbox">
            <input type="checkbox" tabindex="0" name="remember_me" values="remember_me" class="hidden">
            <label>Remember me</label>
        </div>
    </div>
    <button class="ui button" type="submit">Login</button>
</form>
</body>
</html>