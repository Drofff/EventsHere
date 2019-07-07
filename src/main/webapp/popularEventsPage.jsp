<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Events Here - Popular</title>
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
    <a class="item" href="${pageContext.request.contextPath}/storage">My Storage</a>
    <a class="item" href="${pageContext.request.contextPath}/popular">Popular</a>
    <a class="item" href="${pageContext.request.contextPath}/subscription">Subscription</a>
    <a class="item" href="${pageContext.request.contextPath}/logout">Logout</a>
</div>
    <div class="ui segment" style="margin-top: 5%; margin-left: 20%; width: 70%;">

        <div class="ui tabular menu">
              <a class='<c:if test="${ not empty byLikes && byLikes }">active</c:if> item' href="${pageContext.request.contextPath}/popular">
                By likes
              </a>
              <a class='<c:if test="${ not empty byTags && byTags }">active</c:if>  item' href="${pageContext.request.contextPath}/popular?tags">
                By tags
              </a>
            </div>

        <div style="margin-top: 5%; margin-bottom: 5%; margin-left: 5%;  margin-right: 5%;">

            <div class="ui items">

            <c:choose>

              <c:when test="${ not empty byLikes && byLikes }">

                <c:forEach var="event" items="${eventsByLikes}">

                  <div class="item" style="margin-bottom: 5%;">
                    <div class="ui small image">
                      <img src="${event.key}">
                    </div>
                    <div class="middle aligned content">
                      <div class="header">
                        ${event.value.name}
                      </div>
                      <div class="description">
                        <div class="ui label"><i class="users icon"></i> ${ fn:length(event.value.getMembers()) } Members</div>
                        <div class="ui label"><i class="heart icon"></i> ${ fn:length(event.value.getLikes()) } Likes</div>
                      </div>
                      <div class="extra">
                        <a class="ui right floated button" href="${pageContext.request.contextPath}/event?id=${event.value.id}">
                          Details
                        </a>
                      </div>
                    </div>
                  </div>

                </c:forEach>

                <c:if test="${eventsByLikes.size() == 0}">

                    <h4 class="header">Nothing here..</h4>

                </c:if>

              </c:when>
              <c:otherwise>

                <div class="ui link cards">

                <c:forEach var="event" items="${eventsByTags}">

                      <div class="card" style="margin-left: 5%;">
                        <div class="image">
                          <img src="${event.key}" style="max-height: 200px;">
                        </div>
                        <div class="content">
                          <a class="header" href="${pageContext.request.contextPath}/event?id=${event.value.value.id}">${event.value.value.name}</a>
                          <div class="meta">
                            <a>${event.value.value.getFormattedDateTime()}</a>
                          </div>
                          <a class="ui red ribbon label" style="margin-top: 5%;" href="${pageContext.request.contextPath}/search?hash=${event.value.key}">${event.value.key}</a>
                        </div>
                        <div class="extra content">
                          <span class="right floated">
                            ${ fn:length(event.value.value.getLikes()) } Likes
                          </span>
                          <span>
                            <i class="user icon"></i>
                            ${ fn:length(event.value.value.getMembers()) } Members
                          </span>
                        </div>
                      </div>

                </c:forEach>

                 </div>

                 <c:if test="${eventsByTags.size() == 0}">

                     <h4 class="header">Nothing here..</h4>

                 </c:if>

              </c:otherwise>

            </c:choose>

         </div>

        </div>
    </div>
</body>
</html>