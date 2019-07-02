<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Events Here - Registration</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
</head>
<body style="background-image : url(https://visiteurope.com/wp-content/uploads/Events4.jpg);  background-size: 100% 100%;">
<div class="ui left fixed vertical menu">
    <div class="item">
        <img class="ui mini image" src="http://www.housingeurope.eu/image/167/sectionheaderpng/events.png">
    </div>
    <a class="item" href="${pageContext.request.contextPath}/login">Login</a>
</div>

<div style="margin-left: 20%; margin-top: 10%; margin-right: 20%;" class="ui segment">
<div style="margin-left: 5%;  margin-top: 5%; margin-right: 5%; margin-bottom: 5%;">

<c:if test="${not empty message}">
    <div class='ui negative message' style='margin-top: 5%; margin-bottom : 5%;'>
        <div class='header'>
            <c:out value="${message}"></c:out>
        </div>
    </div>
</c:if>

<h1 class="ui header">Registration</h1>
<form class="ui form" action="${pageContext.request.contextPath}/registration" method="post" style="margin-top: 5%;">
  <div class='field <c:if test="${not empty userExists || not empty emailError}">error</c:if>' style="margin-bottom: 5%;">
    <label>Email</label>
    <input type="text" required name="email" placeholder="Email" <c:if test="${not empty oldEmail}"> value="${oldEmail}" </c:if> >
    <c:if test="${not empty userExists}">
        <div class="header red" style="margin-top: 2%;">User with such email already exists</div>
    </c:if>
     <c:if test="${not empty emailError}">
            <div class="header red" style="margin-top: 2%;">Invalid email address</div>
     </c:if>
  </div>
  <div class='field <c:if test="${not empty lengthError || not empty passwordMismatch}">error</c:if>' style="margin-bottom: 5%;">
    <label>Password</label>
    <input type="password" required name="password" placeholder="Password" <c:if test="${not empty oldPassword}"> value="${oldPassword}" </c:if> >
     <c:if test="${not empty lengthError}">
                <div class="header red" style="margin-top: 2%;">Minimum password length is 5 symbols</div>
        </c:if>
  </div>
   <div class='field <c:if test="${not empty passwordMismatch}">error</c:if>' style="margin-bottom: 5%;" >
      <label>Confirm password</label>
      <input type="password" required name="rpassword" placeholder="Confirm password" <c:if test="${not empty oldRepeatedPassword}"> value="${oldRepeatedPassword}" </c:if>>
       <c:if test="${not empty passwordMismatch}">
                <div class="header red" style="margin-top: 2%;">Password mismatch. Please make sure that repeated password is equals to new password</div>
          </c:if>
    </div>

  <button class="ui button" type="submit">Submit</button>
</form>
</div>
</div>
</body>
</html>