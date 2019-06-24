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
<body style="background-image : url(https://visiteurope.com/wp-content/uploads/Events4.jpg); background-size: 100% 100%;">
<div class="ui left fixed vertical menu">
    <div class="item">
        <img class="ui mini image" src="http://www.housingeurope.eu/image/167/sectionheaderpng/events.png">
    </div>
    <a class="item" href="/EventsHere/registration">Registration</a>
</div>
<form class="ui form" method="post" action="/EventsHere/login" style="margin-left: 20%; margin-top: 10%; width: 50%;">
<div class="ui segment">
    <div style="margin-top: 5%; margin-bottom: 5%; margin-left: 5%; margin-right: 5%;">
<%! Integer count = 0; %>
<%

    String queryString = request.getQueryString();

    if (queryString != null && queryString.equals("error")) {

        count++;
        out.println("<div class='ui negative message' style='margin-top: 5%; margin-bottom : 5%;'><div class='header'>Invalid credentials</div></div>");

    } else {
        count = 0;
    }

%>
    <div class="field" style="margin-bottom: 5%;">
        <label>Email</label>
        <input type="text" required name="username" placeholder="your@mail.com">
    </div>
    <div class="field" style="margin-bottom: 5%;">
        <label>Password</label>
        <input type="password" required name="password" placeholder="Password">
        <%

                if (count >= 2) {
                    out.println("<h4 class='header'><a href='/EventsHere/forgotPassword'>Forgot your password? We can help!</a></div>");
                }

        %>
    </div>
    <div class="field" style="margin-left: 5%;">
        <div class="ui checkbox" >
            <input type="checkbox" name="remember_me" values="remember_me" style="margin-top: 7%; margin-bottom: 7%;">
            <label>Remember me</label>
        </div>
    </div>
    <br/>
    <button class="ui button" type="submit" style="margin-top: 7%; margin-left: 5%;">Login</button>
   </div>
</div>
</form>
</body>
</html>