<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Events Here - ${event.name}</title>
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
    <a class="item" href="/EventsHere/my">My Events Here</a>
    <a class="item" href="/EventsHere/popular">Popular</a>
    <a class="item" href="/EventsHere/subscription">Subscription</a>
    <a class="item" href="/EventsHere/logout">Logout</a>
</div>
<div class="ui segment" style="margin-left: 20%; margin-top: 3%; width: 50%;">

<div style="margin-top: 5%; margin-bottom: 5%; margin-left: 5%;  margin-right: 5%;">


 <div class="item">
      <div class="image">
      <img src="${event.photoUrl}">
 </div>
 <div class="content">
   <h2 class="header" style="margin-top: 5%; margin-bottom: 5%;">${event.name}</h2>
     <div class="meta" style="margin-bottom: 5%;">
       <span class="cinema">
        <img class="ui avatar image" src="${event.owner.photoUrl}">
        <span>Provided by ${event.owner.firstName} ${event.owner.lastName}</span>
       </span>
     </div>
     <div class="description" style="margin-bottom: 5%;">
        ${event.description}
     </div>
     <div class="extra">
      <c:forEach var="tag" items="${event.hashTags}">
       <a href="/EventsHere/search?tag=${tag}">
         <div class="ui label"><i class="hashtag icon"></i> ${tag}</div>
       </a>
      </c:forEach>
     </div>
    </div>

    <h4 class="header">Members:</h4>
    <div class="ui middle aligned selection list" style="margin-bottom: 5%;">
    <c:forEach var="member" items="${event.getMembers()}">
      <a class="item" href="/EventsHere/profile/${member.id}">
        <img class="ui avatar image" src="${member.photoUrl}">
        <div class="content">
          <div class="header">${member.firstName} ${member.lastName}</div>
        </div>
      </a>
    </c:forEach>
    </div>


     <div class="ui labeled button" tabindex="0">
        <a class="ui red button" href="/EventsHere/like?id=${event.id}">
            <i class="heart icon"></i> Like
        </a>
        <a class="ui basic red left pointing label">
            ${ fn:length(event.getLikes()) }
        </a>
     </div>


   <div class="ui right floated primary button">
       Visit
    <i class="right chevron icon"></i>
   </div>


</div>
</body>
</html>