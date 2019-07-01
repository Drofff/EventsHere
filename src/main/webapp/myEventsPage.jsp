<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Events Here - My Events</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script
          src="https://code.jquery.com/jquery-3.4.1.js"
          integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
          crossorigin="anonymous"></script>
          <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
</head>
<body style="background-image : url(https://visiteurope.com/wp-content/uploads/Events4.jpg); background-size: 100% 100%;">
<div class="ui left fixed vertical menu">
    <c:choose>
        <c:when test="${not empty name}">
            <div class="item">
                <img class="ui tiny circular image" src="${photoUrl}" style="margin-top: 5%; margin-bottom: 5%; margin-left: 20%; margin-right: 5%;">
                <h3 class="ui header" style="margin-left: 3%;">${name}</h3>
            </div>
        </c:when>
        <c:otherwise>
             <div class="item">
                    <img class="ui mini image" src="http://www.housingeurope.eu/image/167/sectionheaderpng/events.png">
             </div>
        </c:otherwise>
    </c:choose>
    <a class="item" href="/EventsHere">Home</a>
    <a class="item" href="/EventsHere/profile">Profile</a>
    <a class="item" href="/EventsHere/my">My Events Here</a>
    <a class="item" href="/EventsHere/popular">Popular</a>
    <a class="item" href="/EventsHere/subscription">Subscription</a>
    <a class="item" href="/EventsHere/logout">Logout</a>
</div>
<div class="ui segment" style="margin-left: 20%; margin-top: 3%; width: 70%;">

<div style="margin-top: 5%; margin-bottom: 5%; margin-left: 5%;  margin-right: 5%;">

<a class="ui animated button" tabindex="0" href="/EventsHere/save" style="margin-left: 80%;">
  <div class="visible content">Add event</div>
  <div class="hidden content">
    <i class="right arrow icon"></i>
  </div>
</a>

<h3 class="header" style="margin-top: 10%;">Current events:</h3>


<c:choose>

<c:when test="${not empty events && events.size() > 0}">

<div class="ui cards">

<c:forEach var="event" items="${events}">

  <div class="card">
    <div class="content">
      <img class="right floated tiny ui image" src="${event.photoUrl}">
      <div class="header">
         ${event.name}
      </div>
      <div class="meta">
        ${event.dateTime}
      </div>
      <div class="description">
        ${event.description}
      </div>
    </div>
    <div class="extra content">
      <div class="ui two buttons">
        <a class="ui basic green button" href="/EventsHere/save?id=${event.id}">Edit</a>
        <a class="ui basic red button" href="/EventsHere/delete?id=${event.id}">Delete</a>
      </div>
    </div>
  </div>

</c:forEach>

</div>

</c:when>

<c:otherwise>
<h4 class="header">You haven't any events now</h4>
</c:otherwise>

</c:choose>

<h3 class="header" style="margin-top: 10%;">History:</h3>

<c:choose>

<c:when test="${not empty history && history.size() > 0}">

<div class="ui list">

<c:forEach var="event" items="${history}">
  <div class="item">
    <i class="map marker icon"></i>
    <div class="content">
      <a class="header">${event.name}</a>
      <div class="description" style="margin-top: 2%;">
        <div class="ui label"><i class="users icon"></i> ${ fn:length(event.getMembers()) } Members</div>
        <div class="ui label"><i class="heart icon"></i> ${ fn:length(event.getLikes()) } Likes</div>
      </div>
    </div>
  </div>
</c:forEach>

</div>

</c:when>

<c:otherwise>

<h4 class="header">Start your own history by adding new events! Common! <i class="smile outline icon yellow"></i></h4>

</c:otherwise>

</c:choose>


</div>
</body>
</html>