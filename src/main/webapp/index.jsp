<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Events Here - Main</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
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
<div class="ui segment" style="margin-left: 20%; margin-top: 10%; width: 50%;">

<div style="margin-top: 5%; margin-bottom: 5%; margin-left: 5%;  margin-right: 5%;">

    <div class="ui two column grid">
      <div class="row">
        <div class="column">
            <h3 class="ui header">Events Here</h3>
        </div>
        <div class="column">
            <form action="/EventsHere/search" method="get">
                <div class="ui icon input">
                  <input type="text" name="name" placeholder="Search...">
                  <i class="search icon"></i>
                </div>
            </form>
        </div>
      </div>
    </div>

    <div class="ui divided items" style="margin-top: 5%;">

    <c:choose>

        <c:when test="${not empty events}">
              <c:forEach var="event" items="${events}">

                  <div class="item">
                    <div class="image">
                      <img src="${event.photoUrl}">
                    </div>
                    <div class="content">
                      <a class="header" href="/EventsHere/event?id=${event.id}">${event.name}</a>
                      <div class="meta">
                        <span class="cinema">
                            <img class="ui avatar image" src="${event.owner.photoUrl}">
                            <span>${event.owner.firstName} ${event.owner.lastName}</span>
                        </span>
                      </div>
                      <div class="description">
                        ${event.description}
                      </div>
                      <div class="extra">
                        <c:forEach var="tag" items="${event.hashTags}">
                            <a href="/EventsHere/search?tag=${tag}">
                                <div class="ui label"><i class="hashtag icon"></i> ${tag}</div>
                            </a>
                        </c:forEach>
                      </div>
                      <div class="extra">
                           <div class="ui label"><i class="users icon"></i> ${ fn:length(event.getMembers()) } Members</div>
                      </div>
                      <div class="extra">

                                 <div class="ui labeled button" tabindex="0">
                                     <a class="ui red button" href="/EventsHere/like?id=${event.id}">
                                       <i class="heart icon"></i> Like
                                     </a>
                                     <a class="ui basic red left pointing label">
                                       ${ fn:length(event.getLikes()) }
                                     </a>
                                 </div>


                              <a class="ui right floated primary button" href="/EventsHere/event?id=${event.id}">
                                Visit
                                <i class="right chevron icon"></i>
                              </a>
                            </div>
                    </div>
                  </div>

              </c:forEach>

       </c:when>
       <c:otherwise>
             <h3 class="ui header">Sorry, we haven't any events yet..</h3>
       </c:otherwise>

    </c:choose>

     <c:if test="${not empty nextPage}">
                        <a class="ui right labeled icon button" href="/EventsHere?page=${nextPage}">
                          <i class="right arrow icon"></i>
                          Next
                        </a>
                    </c:if>
            <c:if test="${not empty prevPage}">
                <a class="ui right labeled icon button" href="/EventsHere?page=${prevPage}">
                  <i class="right arrow icon"></i>
                  Next
                </a>
            </c:if>

    </div>

</div>

</div>
</body>
</html>