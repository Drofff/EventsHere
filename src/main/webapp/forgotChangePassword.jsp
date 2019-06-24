<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
<div style="margin-left: 20%; margin-top: 10%; margin-right: 20%;" class="ui segment">

<h2 class="ui header">
  <i class="green check icon"></i>
  <div class="content">
    Successfully authenticated
  </div>
</h2>

<h4 class="ui header">Please enter new password</h4>


<form class="ui form" method="post" action="/EventsHere/forgotChangePassword">
  <div class='field <c:if test="${not empty lengthError || not empty passwordMismatch}">error</c:if>'>
    <label>New password</label>
    <input type="password" name="password" required placeholder="New Password">

    <c:if test="${not empty lengthError}">
         <div class="ui error message">
            <div class="header"><c:out value="${lengthError}"></c:out></div>
          </div>
        </c:if>

  </div>

  <div class='field <c:if test="${not empty passwordMismatch}">error</c:if>'>
    <label>Repeat password</label>
    <input type="password" name="rpassword" required placeholder="Confirm password">

    <c:if test="${not empty passwordMismatch}">
       <div class="ui error message">
          <div class="header"><c:out value="${passwordMismatch}"></c:out></div>
        </div>
      </c:if>

  </div>


  <button class="ui button" type="submit">Change</button>
</form>

</div>
</form>
</body>
</html>