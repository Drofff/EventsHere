package servlet;

import bean.AuthenticationService;
import bean.EventsService;
import bean.ProfileService;
import entity.Event;
import entity.Profile;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "SearchServlet", urlPatterns = "/search")
public class SearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        EventsService eventsService = EventsService.getInstance(req.getSession());
        ProfileService profileService = ProfileService.getInstance(req.getSession());

        List<Event> events = eventsService.findAll(0);

        String name = req.getParameter("name");
        String hashTag = req.getParameter("hash");

        if (name != null && !name.isEmpty() && hashTag != null && !hashTag.isEmpty()) {

            events = eventsService.findByNameAndTag(name, parseTags(req));

        } else if (name != null && !name.isEmpty()) {

            events = eventsService.findByName(req.getParameter("name"));

            req.setAttribute("oldName", name);

        } else if (hashTag != null && !hashTag.isEmpty()) {

            events = eventsService.findByHashtag(parseTags(req));

        }

        req.setAttribute("events", events);

        req.setAttribute("tags", eventsService.findAllTags());

        Profile profile = profileService.findByOwnerId((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        if (profile != null) {
            req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
            req.setAttribute("photoUrl", profile.getPhotoUrl());
        }

        req.getRequestDispatcher("/searchPage.jsp").include(req, resp);

    }

    public List<String> parseTags(HttpServletRequest request) {
        List<String> tags = new LinkedList<>();

        Enumeration<String> params = request.getParameterNames();

        while (params.hasMoreElements()) {

            String currentParameterName = params.nextElement();

            if (currentParameterName.matches("(hash)(\\D)*")) {
                tags.add(request.getParameter(currentParameterName));
            }

        }

        return  tags;
    }

}
