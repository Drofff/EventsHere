package servlet;

import bean.AuthenticationService;
import bean.EventsService;
import bean.ProfileService;
import dto.Profile;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MainServlet", urlPatterns = {""})
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pageNumber = req.getParameter("page");

        Integer page = 0;

        if (pageNumber != null) {
            page = Integer.parseInt(pageNumber);
        }

        ProfileService profileService = ProfileService.getInstance(req.getSession());

        Profile profile = profileService.findById((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        if (profile != null) {
            req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
            req.setAttribute("photoUrl", profile.getPhotoUrl());
        }

        EventsService eventsService = EventsService.getInstance(req.getSession());

        req.setAttribute("events", eventsService.findAll(page));

        Long pagesCount = eventsService.getPagesCount();

        if (pagesCount > page) {
            req.setAttribute("nextPage", page + 1);
        }

        if (page > 0 && pagesCount > 0) {
            req.setAttribute("prevPage", page - 1);
        }

        req.getRequestDispatcher("/index.jsp").include(req, resp);

    }
}
