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
    <a class="item" href="${pageContext.request.contextPath}">Home</a>
    <c:if test="${not empty isAdmin && isAdmin}"><a class="item" href="${pageContext.request.contextPath}/admin">Admin Page</a></c:if>
    <a class="item" href="${pageContext.request.contextPath}/profile">Profile</a>
    <a class="item" href="${pageContext.request.contextPath}/my">My Events Here</a>
    <a class="item" href="${pageContext.request.contextPath}/popular">Popular</a>
    <a class="item" href="${pageContext.request.contextPath}/subscription">Subscription</a>
    <a class="item" href="${pageContext.request.contextPath}/logout">Logout</a>
</div>
<div class="ui segment" style="margin-left: 20%; margin-top: 5%; width: 70%;">

<div style="margin-top: 5%; margin-bottom: 5%; margin-left: 5%;  margin-right: 5%;">

    <div class="ui two column grid">
      <div class="row">
        <div class="column">
            <h3 class="ui header">Events Here</h3>
        </div>
        <div class="column">
            <form action="${pageContext.request.contextPath}/search" method="get">
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

                  <div class="item" style="margin-top:4%;">
                    <div class="ui large small">
                      <img src="${event.photoUrl}" style="max-width: 400px;">
                    </div>
                    <div class="content" style="margin-left: 5%;">
                      <a class="header" href="${pageContext.request.contextPath}/event?id=${event.id}" style="margin-bottom:4%; margin-top:2%;">${event.name}</a>
                      <div class="meta">
                        <span class="cinema">
                            <img class="ui avatar image" src="${event.owner.photoUrl}">
                            <span><a href="${pageContext.request.contextPath}/profile?id=${event.owner.id}">${event.owner.firstName} ${event.owner.lastName}</a></span>
                        </span>
                      </div>
                      <div class="ui label" style="margin-top: 4%; margin-bottom: 4%;">
                           <i class="calendar icon"></i> ${event.getFormattedDateTime()}
                      </div>
                      <div class="description" style="margin-bottom: 5%;">
                        ${event.description}
                      </div>
                      <div class="extra">
                        <c:forEach var="tag" items="${event.hashTags}">
                            <a href="${pageContext.request.contextPath}/search?tag=${tag}">
                                <div class="ui label"><i class="hashtag icon"></i> ${tag}</div>
                            </a>
                        </c:forEach>
                      </div>
                      <div class="extra" style="margin-bottom: 7%;">
                           <div class="ui label"><i class="users icon"></i> ${ fn:length(event.getMembers()) } Members</div>
                      </div>
                      <div class="extra">

                                 <div class="ui labeled button" tabindex="0">
                                     <a class="ui red button" href="${pageContext.request.contextPath}/like?id=${event.id}">
                                       <i class="heart icon"></i> Like
                                     </a>
                                     <a class="ui basic red left pointing label">
                                       ${ fn:length(event.getLikes()) }
                                     </a>
                                 </div>


                              <a class="ui right floated primary button" href="${pageContext.request.contextPath}/event?id=${event.id}">
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
                <a class="ui right labeled icon button" href="${pageContext.request.contextPath}?page=${nextPage}">
                  <i class="right arrow icon"></i>
                  Next
                </a>
            </c:if>
            <c:if test="${not empty prevPage}">
                <a class="ui right labeled icon button" href="${pageContext.request.contextPath}?page=${prevPage}">
                  <i class="left arrow icon"></i>
                  Previous
                </a>
            </c:if>

    </div>

</div>

</div>
</body>
</html>