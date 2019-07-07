<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Events Here - Subscription</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script
              src="https://code.jquery.com/jquery-3.4.1.js"
              integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
              crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
</head>
<script>

    $(function() {

        <c:if test="${not empty notifyMe && notifyMe}">

            $("#email_notif").attr("checked", true);

        </c:if>

        $("#email_notif").change(function () {

            var notify = $('.ui.checkbox').checkbox('is checked');

            var message = "not_notify";

            if (notify == true) {

                message = "yes_notify";

            }

            $.post("${pageContext.request.contextPath}/subscription", { notify : message });

        });


    });


</script>
<body style="background : url(https://visiteurope.com/wp-content/uploads/Events4.jpg); background-size: 100% 100%;">
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

        <div style="margin-top: 5%; margin-bottom: 5%; margin-left: 5%;  margin-right: 5%;">

            <h2 class="header" style="margin-bottom: 5%;">Events by subscription <div class="ui toggle checkbox" style="margin-left: 52%;">
               <input type="checkbox" id="email_notif" name="public">
               <label>Email notifications</label>
             </div></h2>

           <div class="ui cards">

            <c:choose>

              <c:when test="${ not empty events && events.size() > 0 }">

                <c:forEach var="event" items="${events}">

                  <div class="ui card" style="margin-right:5%;">
                    <div class="content">
                      <img class="ui avatar image" src="${event.key.owner.photoUrl}"> <a href="${pageContext.request.contextPath}/profile?id=${event.key.owner.id}"> ${event.key.owner.firstName} ${event.key.owner.lastName} </a>
                    </div>
                    <div class="image">
                      <img src="${event.value}">
                    </div>
                    <div class="content">
                      <a class="header" href="${pageContext.request.contextPath}/event?id=${event.key.id}" style="margin-bottom: 5%;">${event.key.name}</a>
                      <span class="right floated">
                      <a href="${pageContext.request.contextPath}/like?id=${event.key.id}">
                        <i class="heart outline like icon"></i>
                      </a>
                        ${ fn:length(event.key.getLikes()) }
                      </span>
                      <i class="users icon"></i>
                      ${ fn:length(event.key.getMembers()) } Members
                    </div>
                    <div class="extra content">
                      <c:forEach var="tag" items="${event.key.hashTags}">
                             <a href="${pageContext.request.contextPath}/search?hash=${tag}">
                               <div class="ui label"><i class="hashtag icon"></i> ${tag}</div>
                             </a>
                            </c:forEach>
                    </div>
                  </div>

                </c:forEach>


              </c:when>
              <c:otherwise>

                <h4 class="header">People you subscribed on haven't any events</h4>

              </c:otherwise>

            </c:choose>

         </div>

        </div>
    </div>
</body>
</html>