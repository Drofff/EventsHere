package servlet;

import bean.AuthenticationService;
import bean.EventsService;
import bean.ProfileService;
import entity.Profile;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MyEventsServlet", urlPatterns = {"/my"})
public class MyEventsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        EventsService eventsService = EventsService.getInstance(req.getSession());

        Long userId = (Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY);

        req.setAttribute("events", eventsService.findByOwner(userId));

        req.setAttribute("history", eventsService.getHistoryOf(userId));

        ProfileService profileService = ProfileService.getInstance(req.getSession());

        Profile profile = profileService.findByOwnerId(userId);

        if (profile != null) {
            req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
            req.setAttribute("photoUrl", profile.getPhotoUrl());
        }

        req.getRequestDispatcher("/myEventsPage.jsp").include(req, resp);

    }
}
