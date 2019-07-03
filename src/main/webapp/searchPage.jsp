<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Events Here - Search</title>
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
    $(document).ready(function() {
        $('#multi-select').dropdown();

        <c:if test="${not empty oldTags && oldTags.size() > 0}">

            var tags = [];

            <c:forEach var="tag" items="${oldTags}">
                tags.push('${tag}');
            </c:forEach>

            if (tags.length > 0) {

                $('#multi-select').dropdown('set selected', tags);

             }


        </c:if>

    });

    function today() {
        var today_date = "${today}";
        $("#from").val(today_date);
        $("#to").val(today_date);

        $("#today_item").attr("class", "item active");
        $("#week_item").attr("class", "item");
        $("#all_item").attr("class", "item");
        $("#select_item").attr("class", "item");

    }

    function this_week() {

        var today_date = "${today}";
        var next_sunday = "${nextWeekBound}";

        $("#from").val(today_date);
        $("#to").val(next_sunday);

        $("#today_item").attr("class", "item");
        $("#week_item").attr("class", "item active");
        $("#all_item").attr("class", "item");
        $("#select_item").attr("class", "item");

    }

    function select_date() {
        $('.ui.modal')
          .modal('show')
        ;
    }

    function submit() {
        $("#from").val($("#from_select").val());
        $("#to").val($("#to_select").val());

        $("#today_item").attr("class", "item");
        $("#week_item").attr("class", "item");
        $("#all_item").attr("class", "item");
        $("#select_item").attr("class", "item active");
    }

    function all_dates() {
        $("#from").val('');
        $("#to").val('');

        $("#all_item").attr("class", "item active");
        $("#today_item").attr("class", "item");
        $("#week_item").attr("class", "item");
        $("#select_item").attr("class", "item");
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
    <a class="item" href="${pageContext.request.contextPath}/popular">Popular</a>
    <a class="item" href="${pageContext.request.contextPath}/subscription">Subscription</a>
    <a class="item" href="${pageContext.request.contextPath}/logout">Logout</a>
</div>
<div class="ui segment" style="margin-left: 20%; margin-top: 5%; width: 70%;">

<div style="margin-top: 5%; margin-bottom: 5%; margin-left: 5%;  margin-right: 5%;">

<form action="${pageContext.request.contextPath}/search" method="get">

    <div class="ui two column grid">
      <div class="row">
        <div class="column">

            <div class="ui form">
              <div class="field">
                <select class="ui fluid search dropdown" id ="multi-select" multiple="" name="hash">
                   <c:forEach var="tag" items="${tags}">
                      <option value="${tag}">${tag}</option>
                   </c:forEach>
                </select>
              </div>
            </div>

        </div>
        <div class="column">
                <div class="ui icon input">
                  <input type="text" name="name" <c:if test="${not empty oldName}">value='${oldName}'</c:if> placeholder="Name">
                </div>

        </div>
      </div>
    </div>

    <div class="ui four item menu" style="width:50%;">
      <a class="item" id="all_item" onclick="all_dates()">All Dates</a>
      <a class="item" id="today_item" onclick="today()">Today</a>
      <a class="item" id="week_item" onclick="this_week()">This week</a>
      <a class="item" id="select_item" onclick="select_date()" id="select_date">Select date</a>

        <input type="hidden" id="from" name="from">
        <input type="hidden" id="to" name="to">

    </div>

    <button type="submit" class="ui secondary button" style="margin-top: 2%;">
      Find
    </button>

</form>

    <div class="ui divided items" style="margin-top: 5%;">

    <c:choose>

        <c:when test="${not empty events}">
              <c:forEach var="event" items="${events}">

                  <div class="item">
                    <div class="image">
                      <img src="${event.photoUrl}">
                    </div>
                    <div class="content">
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
                      <div class="extra">
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


                              <a class="ui right floated primary button" href="${pageContext.request.contextPath}/event?id=${event.id}" >
                                Visit
                                <i class="right chevron icon"></i>
                              </a>
                            </div>
                    </div>
                  </div>

              </c:forEach>

       </c:when>
       <c:otherwise>
             <h3 class="ui header">Sorry, we can't find any events by your query</h3>
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
                  <i class="right arrow icon"></i>
                  Next
                </a>
            </c:if>

    </div>

</div>

<div class="ui modal">
  <div class="ui icon header">
    Select Date
  </div>
  <div class="content">
    <div class="ui form">
        <div class="ui grid" style="margin-top: 5%; margin-bottom: 5%;">
          <div class="four wide column" style="margin-left: 23%;">
            <h4 class="header">Date from</h4>
            <input type='date' id="from_select">
          </div>
          <div class="four wide column" style="margin-left: 5%;">
            <h4 class="header">Date to</h4>
            <input type='date' id="to_select">
          </div>
        </div>
    </div>
  </div>
  <div class="actions">
   <div class="ui black deny button">
         Cancel
       </div>
    <a class="ui green ok inverted button" style="margin-left: 5%; margin-right: 5%;" onclick="submit()">
      <i class="checkmark icon"></i>
      Submit
    </a>
  </div>
</div>

</div>
</body>
</html>