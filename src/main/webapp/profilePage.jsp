<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Events Here - ${profile.firstName} ${profile.lastName}</title>
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
    <a class="item" href="${pageContext.request.contextPath}">Home</a>
    <c:if test="${not empty isAdmin && isAdmin}"><a class="item" href="${pageContext.request.contextPath}/admin">Admin Page</a></c:if>
    <a class="item" href="${pageContext.request.contextPath}/profile">Profile</a>
    <a class="item" href="${pageContext.request.contextPath}/my">My Events Here</a>
    <a class="item" href="${pageContext.request.contextPath}/popular">Popular</a>
    <a class="item" href="${pageContext.request.contextPath}/subscription">Subscription</a>
    <a class="item" href="${pageContext.request.contextPath}/logout">Logout</a>
</div>

    <div class="ui segment" style="margin-left: 20%; margin-top: 3%; width: 70%;">
        <div style="margin-top: 5%; margin-bottom: 5%; margin-left: 5%;  margin-right: 5%;">

        <div class="ui grid">

          <div class="six wide column">
             <div class="ui card">
              <a class="image" href="#">
                <img src="${profile.photoUrl}">
              </a>
              <div class="content">
                <a class="header" href="#">${profile.firstName} ${profile.lastName}</a>
                <div class="meta">
                  <a>${profile.status}</a>
                </div>
              </div>
            </div>

            <c:choose>
                            <c:when test="${not empty subscriber && subscriber}">
                                <a class="ui green button" href="${pageContext.request.contextPath}/subscribe?id=${profile.id}">
                                        Already subscriber
                                </a>
                            </c:when>
                            <c:when test="${not empty me && me}">
                                <a href="${pageContext.request.contextPath}/editProfile">Edit</a>
                            </c:when>
                            <c:otherwise>
                                 <a class="ui primary button" href="${pageContext.request.contextPath}/subscribe?id=${profile.id}">
                                        Subscribe
                                 </a>
                            </c:otherwise>
                        </c:choose>

          </div>

           <div class="six wide column">
                   <div class="ui statistics">
                     <div class="statistic">
                           <div class="value">
                             ${ fn:length(profile.getSubscribers()) }
                           </div>
                           <div class="label">
                             Subscribers
                           </div>
                         </div>
                     <div class="statistic">
                         <div class="value">
                           ${ fn:length(profile.getSubscriptions()) }
                         </div>
                         <div class="label">
                           Subscriptions
                         </div>
                     </div>
                   </div>

            <c:if test="${not empty events && events.size() > 0}">

            <h4 class="header"  style="margin-top: 13%;">Current events:</h4>

            <div class="ui list" style="margin-top: 5%;">

                <c:forEach var="event" items="${events}">

                      <div class="item" style="margin-top: 5%;">
                        <img class="ui avatar image" src="${event.photoUrl}">
                        <div class="content">
                          <a class="header" href="${pageContext.request.contextPath}/event?id=${event.id}">${event.name}</a>
                          <div class="description">${fn:length(event.getMembers())} Members</div>
                        </div>
                      </div>

                </c:forEach>

            </div>
            </div>

            </c:if>

            <c:if test="${not empty history && history.size() > 0}">

             <h4 class="header" style="margin-top: 12%;">History:</h4>

                <div class="ui list" style="margin-top: 5%;">

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

            </c:if>

          </div>
        </div>
      </div>
    </div>
</body>
</html>