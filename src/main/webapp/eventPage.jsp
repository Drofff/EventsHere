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
<script>

function visit() {

    $('.ui.basic.modal')
      .modal('show')
    ;

}

function unvisit() {

        $('.ui.tiny.modal')
          .modal('show')
        ;

}

</script>
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
<div class="ui segment" style="margin-left: 20%; margin-top: 3%; width: 70%;">

<div style="margin-top: 5%; margin-bottom: 5%; margin-left: 5%;  margin-right: 5%;">


 <div class="item">
      <img class="ui large rounded image" src="${eventPhoto}">
 <div class="content">
   <h2 class="header" style="margin-top: 5%; margin-bottom: 5%;">${event.name}</h2>
     <div class="meta" style="margin-bottom: 5%;">
       <span class="cinema">
        <img class="ui avatar image" src="${event.owner.photoUrl}">
        <span>Provided by <a href="${pageContext.request.contextPath}/profile?id=${event.owner.id}">${event.owner.firstName} ${event.owner.lastName}</a></span>
       </span>
     </div>
     <c:if test="${not empty member && member}">
        <div class="ui left icon input" style="margin-bottom:5%;">
          <input type="text" readonly value="${event.owner.phoneNumber}" placeholder="Contact phone">
          <i class="mobile icon"></i>
        </div>
     </c:if>
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
    </div>

    <div class="ui label" style="margin-bottom: 5%; margin-top: 5%;">
                <i class="calendar icon"></i> ${event.getFormattedDateTime()}
              </div>

    <h4 class="header">Members:</h4>
    <div class="ui middle aligned selection list" style="margin-bottom: 5%;">
    <c:forEach var="member" items="${event.getMembers()}">
      <a class="item" href="${pageContext.request.contextPath}/profile?id=${member.id}">
        <img class="ui avatar image" src="${member.photoUrl}">
        <div class="content">
          <div class="header">${member.firstName} ${member.lastName}</div>
        </div>
      </a>
    </c:forEach>
    </div>


     <div class="ui labeled button" tabindex="0">
        <a class="ui red button" href="${pageContext.request.contextPath}/like?id=${event.id}">
            <i class="heart icon"></i> Like
        </a>
        <a class="ui basic red left pointing label">
            ${ fn:length(event.getLikes()) }
        </a>
     </div>

    <c:if test="${empty member}">
       <div class="ui right floated primary button" onclick="visit()" >
           Visit
        <i class="right chevron icon"></i>
       </div>
   </c:if>

   <c:if test="${not empty member}">
        <div class="ui right floated primary button" onclick="unvisit()" >
           You are already member
        </div>
   </c:if>

   <div class="ui basic modal">
     <div class="ui icon header">
       <i class="user plus icon"></i>
       Join ${event.name}
     </div>
     <div class="content">
       <p>Please, mention that by accepting this you accept that your membership in this event will be displayed to other users.
        Also, you have to understand that administrators of this event expect your attendance</p>
     </div>
     <div class="actions">
       <div class="ui red basic cancel inverted button">
         <i class="remove icon"></i>
         Cancel
       </div>
       <a class="ui green ok inverted button" href="${pageContext.request.contextPath}/visit?${event.id}">
         <i class="checkmark icon"></i>
         Confirm
       </a>
     </div>
   </div>

   <div class="ui tiny modal">
     <i class="close icon"></i>
     <div class="header">
       Cancel visit
     </div>
     <div class="image content">
       <div class="description">
         <div class="ui header">You do not want to visit this event anymore?</div>
       </div>
     </div>
     <form action="${pageContext.request.contextPath}/visit" method="post" style="margin-bottom: 3%; margin-left: 6%;">
     <div class="actions">
       <div class="ui black deny button">
         Cancel
       </div>
            <input type="hidden" name="id" value="${event.id}"/>
           <button class="ui positive right labeled icon button" type="submit">
             Yep, I will not go
             <i class="checkmark icon"></i>
           </button>
     </div>
      </form>
   </div>


</div>
</body>
</html>