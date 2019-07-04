<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Events Here - My Storage</title>
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

function add_photo() {

    $('.ui.modal')
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
<div class="ui segment" style="margin-left: 20%; margin-top: 5%; width: 70%;">

    <div style="margin-top: 5%; margin-bottom: 5%; margin-left: 5%;  margin-right: 5%;">

        <div class="ui grid" style="margin-bottom: 5%;">
          <div class="four wide column">
            <h3 class="header">My Storage</h3>
          </div>
          <div class="four wide column">
             <c:if test="${not empty count && count < 5}">
                <a class="fluid ui button" onclick="add_photo()" style="margin-left: 100%; width: 50%;">Add photo</a>
             </c:if>
          </div>
        </div>

        <c:choose>

            <c:when test="${ not empty photos }">

             <div class="ui cards">

                    <c:forEach var="photo" items="${photos}">

                     <form action="${pageContext.request.contextPath}/deletePhoto" method="post" style="margin-left: 10%; margin-top: 5%;">

                          <div class="ui fluid card">
                            <div class="ui medium image">
                              <img src="${photo.value}">
                            </div>
                            <div class="content">
                             <input type="hidden" value="${photo.key}" name="fileName">
                            <button type="submit" class="ui button" style="margin-top: 7%;">
                                Delete
                            </button>
                            </div>
                          </div>
                      </form>

                    </c:forEach>

                        </div>


            </c:when>

            <c:otherwise>

             <h4 class="header">Nothing here..</h4>

            </c:otherwise>

         </c:choose>

    </div>

    <div class="ui modal">
      <form action="${pageContext.request.contextPath}/storage" method="post" enctype="multipart/form-data">
        <div style="margin-top: 5%; margin-bottom: 5%; margin-left: 5%; margin-right: 5%;">

          <div class="description">
            <div class="ui header">Add photo</div>
            <p><input type="file" name="photo" accept=".jpg, .jpeg, .png" style="margin-top: 5%; margin-bottom: 2%;"></p>
            Maximum you can have 5 photos
          </div>
          <div class="actions" style="margin-top: 5%;">
            <div class="ui black deny button">
              Cancel
            </div>
            <button type="submit" class="ui positive right labeled icon button">
              Save
              <i class="checkmark icon"></i>
            </button>
          </div>
         </div>
      </form>
    </div>

</div>
</body>
</html>