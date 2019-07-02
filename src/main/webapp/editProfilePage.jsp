<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Events Here - Edit Profile</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
     <script
              src="https://code.jquery.com/jquery-3.4.1.js"
              integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
              crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
</head>
<body style="background-image : url(https://visiteurope.com/wp-content/uploads/Events4.jpg);  background-size: 100% 100%;">

<script>

    $(function() {

        $("#photo").change(function() {

            $("#photo_frame").html('<img class="ui medium rounded image" src="' + $("#photo").val() + '">');

        });


    });


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
       <a class="item" href="${pageContext.request.contextPath}/popular">Popular</a>
       <a class="item" href="${pageContext.request.contextPath}/subscription">Subscription</a>
       <a class="item" href="${pageContext.request.contextPath}/logout">Logout</a>
</div>

<div style="margin-left: 20%; margin-top: 10%; margin-right: 20%;" class="ui segment">
<div style="margin-left: 5%;  margin-top: 5%; margin-right: 5%; margin-bottom: 5%;">

<c:if test="${not empty message}">
    <div class='ui negative message' style='margin-top: 5%; margin-bottom : 5%;'>
        <div class='header'>
            <c:out value="${message}"></c:out>
        </div>
    </div>
</c:if>

<h3 class="ui header">Edit Profile</h3>
<form class="ui form" action="${pageContext.request.contextPath}/editProfile" method="post" style="margin-top: 5%;">
    <div class="field <c:if test='${not empty firstNameError}'>error</c:if>">
      <label>First Name</label>
      <input type="text" name="firstName" placeholder="First Name" <c:if test="${not empty oldData && not empty oldData.firstName}">value="${oldData.firstName}"</c:if>>
       <c:if test="${not empty firstNameError}">
          <h4 class="header" style="color:red; margin-bottom: 5%;">${firstNameError}</h4>
        </c:if>
    </div>
    <div class="field <c:if test='${not empty lastNameError}'>error</c:if>">
      <label>Last Name</label>
      <input type="text" name="lastName" placeholder="Last Name" <c:if test="${not empty oldData && not empty oldData.lastName}">value="${oldData.lastName}"</c:if>>
       <c:if test="${not empty lastNameError}">
        <h4 class="header" style="color:red; margin-bottom: 5%;">${lastNameError}</h4>
      </c:if>
    </div>
    <div class="field <c:if test='${not empty statusError}'>error</c:if>">
      <label>Status</label>
      <input type="text" name="status" placeholder="Your status" <c:if test="${not empty oldData && not empty oldData.status}">value="${oldData.status}"</c:if>>
       <c:if test="${not empty statusError}">
        <h4 class="header" style="color:red; margin-bottom: 5%;">${statusError}</h4>
      </c:if>
    </div>
    <div class="field <c:if test='${not empty phoneNumberError}'>error</c:if>">
        <div class="ui right labeled input">
          <label for="amount" class="ui label">+</label>
          <input type="text" name="phoneNumber" placeholder="Phone number" <c:if test="${not empty oldData && not empty oldData.phoneNumber}">value="${oldData.phoneNumber}"</c:if>>
        </div>
        <c:if test="${not empty phoneNumberError}">
                      <h4 class="header" style="color:red; margin-bottom: 5%;">${phoneNumberError}</h4>
                    </c:if>
    </div>
     <div class="field <c:if test='${not empty photoUrlError}'>error</c:if>">
         <input type="text" <c:if test="${not empty oldData && not empty oldData.photoUrl}">value="${oldData.photoUrl}"</c:if> name="photoUrl" id="photo" placeholder="Photo URL">
         <c:if test="${not empty photoUrlError}">
              <h4 class="header" style="color:red; margin-bottom: 5%;">${photoUrlError}</h4>
         </c:if>
         <div id="photo_frame"></div>
       </div>
     <div class="field">
        <button class="ui primary button" type="submit">
          Save
        </button>
     </div>
</form>
</div>
</div>
</body>
</html>