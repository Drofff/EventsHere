package servlet;

import repository.EventRepository;
import repository.ProfileRepository;
import repository.UserRepository;
import entity.Profile;
import service.AuthenticationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PopularServlet", urlPatterns = {"/popular"})
public class PopularServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            String type = req.getQueryString();

            ProfileRepository profileRepository = ProfileRepository.getInstance(req.getSession());
            EventRepository eventRepository = EventRepository.getInstance(req.getSession());

            Profile profile = profileRepository.findByOwnerId((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

            req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
            req.setAttribute("photoUrl", profile.getPhotoUrl());

            req.setAttribute( "isAdmin", UserRepository.getInstance(req.getSession()).isAdmin((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY)));

            if (type == null || type.isEmpty()) {

                req.setAttribute("byLikes", true);
                req.setAttribute("eventsByLikes", eventRepository.findPopularByLikes());

            } else if (type.equals("tags")) {

                req.setAttribute("byTags", true);
                req.setAttribute("eventsByTags", eventRepository.findPopularByTags());

            } else {
                resp.sendRedirect(req.getContextPath());
                return;
            }

            req.getRequestDispatcher("/popularEventsPage.jsp").include(req, resp);

    }
}
