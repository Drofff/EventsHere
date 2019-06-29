package servlet;

import bean.AuthenticationService;
import bean.EventsService;
import bean.ProfileService;
import bean.ValidationService;
import entity.Event;
import entity.Profile;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "AddEventServlet", urlPatterns = {"/save"})
public class AddEventServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ProfileService profileService = ProfileService.getInstance(req.getSession());
        EventsService eventsService = EventsService.getInstance(req.getSession());

        Profile profile = profileService.findByOwnerId((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        if (profile != null) {
            req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
            req.setAttribute("photoUrl", profile.getPhotoUrl());
        }

        req.setAttribute("tags", EventsService.getInstance(req.getSession()).findAllTags());

        if (req.getParameter("id") != null) {

            try {

                Long eventId = Long.parseLong(req.getParameter("id"));

                req.setAttribute("oldData", eventsService.findById(eventId));


            } catch (Exception e) {}

        }

        req.getRequestDispatcher("/addEventPage.jsp").include(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ProfileService profileService = ProfileService.getInstance(req.getSession());
        ValidationService validationService = ValidationService.getInstance(req.getSession());
        EventsService eventsService = EventsService.getInstance(req.getSession());

        Profile profile = profileService.findByOwnerId((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        if (profile != null) {
            req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
            req.setAttribute("photoUrl", profile.getPhotoUrl());
        }

        req.setAttribute("tags", EventsService.getInstance(req.getSession()).findAllTags());

        try {

            Event event = eventsService.parseEvent(req);

            Map<String, String> errors = validationService.validateEvent(event);

            if (errors.size() == 0) {

                event.setOwner(profile);

                eventsService.save(event);
                resp.sendRedirect("/EventsHere/my");
                return;

            } else {

                req.setAttribute("oldData", event);

                errors.entrySet().stream().forEach(x -> req.setAttribute(x.getKey(), x.getValue()));

                req.getRequestDispatcher("/addEventPage.jsp").include(req, resp);
                return;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = req.getHeader("referer");

        resp.sendRedirect(url != null ? url : "/EventsHere/my");

    }
}
