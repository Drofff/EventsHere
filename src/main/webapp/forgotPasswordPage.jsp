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
<body style="background-image : url(https://visiteurope.com/wp-content/uploads/Events4.jpg);  background-size: 100% 100%;">
<div class="ui left fixed vertical menu">
    <div class="item">
        <img class="ui mini image" src="http://www.housingeurope.eu/image/167/sectionheaderpng/events.png">
    </div>
    <a class="item" href="/EventsHere/registration">Registration</a>
    <a class="item" href="/EventsHere/login">Login</a>
</div>
<form style="margin-left: 20%; margin-top: 10%; margin-right: 20%;" action="/EventsHere/forgotPassword" method="post">
<div class="ui segment">
    <div style="margin-top: 5%; margin-bottom: 5%; margin-left: 5%; margin-right: 5%;">
    <%
        String msg = (String)request.getParameter("message");

        if (msg != null) {
            out.println("<div class='ui negative message' style='margin-top: 5%; margin-bottom : 5%;'><div class='header'>" + msg + "</div></div>");
        }
    %>

    <h3 class="header">Please input your email address</h3>
    <div class="ui input focus" style="margin-top: 5%;">
        <input type="text" required name="email" placeholder="Email" style="width: 300px;">
    </div>
    <br/>
    <button class="ui toggle button" style="margin-top: 5%;">
        Restore password
    </button>

</div>
</div>
</form>
</body>
</html>