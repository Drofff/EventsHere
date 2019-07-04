<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Events Here - Add Event</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script
          src="https://code.jquery.com/jquery-3.4.1.js"
          integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
          crossorigin="anonymous"></script>
          <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
</head>
<script>

    $(function() {

        <c:if test="${not empty oldData && not empty oldData.description}">
                   $("#desc_text").val("${oldData.description}");
                   $("#desc_field").val("${oldData.description}");
        </c:if>

        $('#multi-select').dropdown();

        $('.ui.fluid.selection.dropdown')
          .dropdown()
        ;


        <c:if test="${not empty oldData && not empty oldData.getHashTags()}">

            var tags = [];

            <c:forEach var="tag" items="${oldData.getHashTags()}">
                tags.push('${tag}');
            </c:forEach>

            if (tags.length > 0) {

                $('#multi-select').dropdown('set selected', tags);

             }


        </c:if>

        $("#desc_text").keyup(function() {

            $("#desc_field").val( $("#desc_text").val() );

        });


    });

    function add_hash() {

        $('.ui.medium.modal')
          .modal('show')
        ;

    }

    function add_tag() {

        var tag = $("#hash_tag").val();

        if (tag.length < 2) {
            $("#msg").html("<h4 class='header' style='color: red'>Invalid tag name</h4>");
        } else {

            $("#msg").html('');

            $.post('${pageContext.request.contextPath}/hash', {name : tag}, function(result) {

                if (result == 'true') {

                    $("#msg").html("<h4 class='header' style='color: green'>Saved</h4>");
                    $("#hash_tag").val('');

                    $.get('${pageContext.request.contextPath}/hash', function(result) {

                        $("#multi-select").html(result);

                    });

                } else {

                    $("#msg").html("<h4 class='header' style='color: red'>This tag already exists!</h4>");

                }

            });

        }

    }

</script>
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

<div class="ui segment" style="margin-left: 20%; margin-top: 3%; width: 70%;">
    <div style="margin-top: 5%; margin-bottom: 5%; margin-left: 5%;  margin-right: 5%;">

    <form class="ui form" action="${pageContext.request.contextPath}/save" method="post">
      <h4 class="ui dividing header">Information about event</h4>
       <c:if test="${not empty oldData.id}">
          <input type="hidden" value="${oldData.id}" name="id">
       </c:if>
      <div class="field">
        <label>Name</label>
        <div class="two fields">
          <div class="field <c:if test='${not empty nameError}'>error</c:if>">
            <input type="text" name="name" <c:if test="${not empty oldData && not empty oldData.name}">value="${oldData.name}"</c:if> placeholder="Event's name">
          <c:if test="${not empty nameError}">
            <h4 class="header" style="color:red; margin-bottom: 10%;">${nameError}</h4>
          </c:if>
          </div>
        </div>
      </div>
      <div class="field">
         <div class="two fields">

            <div class="field">
            <label>Select photo</label>
            <div class="ui fluid selection dropdown" style="height: 20px; width: 70%; margin-right: 3%; margin-left:1%;">
              <input type="hidden" name="photoUrl">
              <i class="dropdown icon"></i>
              <div class="default text">Select photo</div>
              <div class="menu">
                <c:forEach var="photo" items="${storagePhotos}">
                    <div class="item" data-value="${photo.key}">
                      <img class="ui mini avatar image" src="${photo.value}">
                      ${photo.key}
                    </div>
                </c:forEach>
              </div>
            </div>
            </div>

            <div class="field">
             <input type="hidden" name="description" id="desc_field">
             <label>Description</label>
             <textarea id="desc_text"></textarea>
             <c:if test="${not empty descriptionError}">
                 <h4 class="header" style="color:red; margin-bottom: 10%;">${descriptionError}</h4>
             </c:if>
            </div>
         </div>
      </div>
          <div class="field  <c:if test='${not empty dateTimeError}'>error</c:if>" style="width:50%;">
            <label>Date and time of event</label>
            <input type="datetime-local" <c:if test="${not empty oldData && not empty oldData.dateTime}">value="${oldData.dateTime}"</c:if> name="dateTime">
            <c:if test="${not empty dateTimeError}">
                <h4 class="header" style="color:red; margin-bottom: 10%;">${dateTimeError}</h4>
            </c:if>
          </div>
      <div class="two fields">
          <div class="field" style="width: 50%;">
                <label>Hash tags</label>
              <select class="ui fluid search dropdown" id = "multi-select" multiple="" name="hash">
                 <c:forEach var="tag" items="${tags}">
                    <option value="${tag}">${tag}</option>
                 </c:forEach>
              </select>
                <c:if test="${not empty tagError}">
                    <h4 class="header" style="color:red; margin-bottom: 10%;">${tagError}</h4>
                </c:if>
           </div>
           <div class="field" style="margin-top:5%;">
            <div class="circular ui icon button" onclick="add_hash()">
                <i class="plus icon"></i>
            </div>
           </div>
      </div>
      <button class="ui primary button" type="submit" style="margin-top: 5%;">
        Submit
      </button>
    </form>

    <div class="ui medium modal">
      <i class="close icon"></i>
      <div class="header">
        Create your own <i class="slack hash icon"></i> tag!
      </div>
      <div class="image content">
        <div class="ui medium image">
          <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSuJDe_5aE92E0UIVh7n0no7uJnwduZ3Y5y3BLPNgUs86XVjvgN2Q">
        </div>
        <div class="description">
          <div class="ui right labeled input">
            <label for="hash_tag" class="ui label">#</label>
            <input type="text" placeholder="Hashtag" id="hash_tag">
            <button class="ui teal right labeled icon button" onclick="add_tag()">
                Add
                <i class="plus icon"></i>
            </button>
          </div>
          <div id="msg"></div>
        </div>
      </div>
      <div class="actions">
        <div class="ui black deny button">
          Cancel
        </div>
      </div>
    </div>


    </div>
</div>
</body>
</html>