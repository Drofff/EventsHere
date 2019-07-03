<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Events Here - Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
     <script
              src="https://code.jquery.com/jquery-3.4.1.js"
              integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
              crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
</head>
<body>
<script>

$(function() {

    $('select.dropdown')
      .dropdown()
    ;

});

</script>
<div class="ui stackable menu">
  <div class="item">
        <h4 class="ui header">EventsHere <b style="color: navy;">Admin</b></h4>
  </div>
  <a class="item" href="${pageContext.request.contextPath}">Back to Home</a>
</div>

<c:if test="${not empty msg}">
<div class='ui success message' style='margin-top: 2%; margin-bottom : 5%; width: 50%; margin-left: 40%;'>
    <div class='header'>
        ${msg}
    </div>
</div>
</c:if>

<div class="ui statistics" style="margin-left:30%; margin-top: 5%;">
  <div class="statistic">
    <div class="value">
      ${usersOnline}
    </div>
    <div class="label">
      Users Online
    </div>
  </div>
  <div class="statistic">
    <div class="value">
      ${usersInSystem}
    </div>
    <div class="label">
      Users in system
    </div>
  </div>
  <div class="statistic">
    <div class="value">
      ${actualEvents}
    </div>
    <div class="label">
      Actual events
    </div>
  </div>
   <div class="statistic">
      <div class="value">
        ${historyEvents}
      </div>
      <div class="label">
        Events finished
      </div>
    </div>
</div>

<div class="ui grid">
  <div class="six wide column" style="margin-top: 6%;">
    <h3 class="header" style="margin-left: 20%;">Block User</h3>
    <form method="get" action="${pageContext.request.contextPath}/admin/block" class="ui form" style="margin-left:20%; margin-top:4%;">
     <h4 class="header">Users</h4>
     <select class="ui search dropdown" name="id">
            <c:forEach var="user" items="${users}">
                <option value="${user.userId}">${user.firstName} ${user.lastName}</option>
            </c:forEach>
        </select>

        <div class="field" style="width: 50%; margin-bottom:10%; margin-top:10%;">
          <label>Reason</label>
          <input type="text" name="reason" placeholder="Reason">
        </div>

        <button type="submit" class="ui secondary button">
                  Block user
        </button>

    </form>
  </div>
  <div class="two wide column" style="margin-top:10%;">
     <div class="ui vertical divider">
              Or
            </div>
  </div>
  <div class="six wide column" style="margin-top: 5%;">
    <h3 class="header" style="margin-left: 20%;">Manage User's Events</h3>
    <div class="ui form" style="width: 50%; margin-left: 20%; margin-top:5%;">
    <form action="${pageContext.request.contextPath}/admin" method="get">
      <div class="field">
        <label>Users</label>
        <select class="ui search dropdown" name="id">
            <c:forEach var="user" items="${users}">
                <option <c:if test="${not empty oldId && oldId == user.userId}">selected</c:if> value="${user.userId}">${user.firstName} ${user.lastName}</option>
            </c:forEach>
        </select>
      </div>
      <button class="ui secondary button" type="submit">
        Show events
      </button>
    </form>
    </div>


  <c:if test="${not empty events && events.size() > 0}">

  <table class="ui celled table" style="margin-left: 20%; margin-top: 5%;">
    <thead>
      <tr><th>Name</th>
      <th>Description</th>
      <th>Date Time</th>
      <th></th>
      <th></th>
    </tr></thead>
    <tbody>

    <c:forEach var="event" items="${events}">

      <tr>
        <td>${event.name}</td>
        <td>${event.description}</td>
        <td>${event.getFormattedDateTime()}</td>
        <td><a href="${pageContext.request.contextPath}/save?id=${event.id}">Edit</a></td>
        <td><a href="${pageContext.request.contextPath}/delete?id=${event.id}">Delete</a></td>
      </tr>

    </c:forEach>

    </tbody>
  </table>

  </c:if>


  </div>
</div>


</body>
</html>