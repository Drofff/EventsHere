package servlet;

import repository.HashTagRepository;
import repository.ProfileRepository;
import repository.UserRepository;
import entity.Event;
import entity.Profile;
import service.AuthenticationService;
import service.SearchService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@WebServlet(name = "SearchServlet", urlPatterns = "/search")
public class SearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        SearchService searchService = SearchService.getInstance(session);
        HashTagRepository hashTagRepository = HashTagRepository.getInstance(session);
        ProfileRepository profileRepository = ProfileRepository.getInstance(session);

        List<Event> eventList = searchService.search(req);

        try {

            String from = req.getParameter("from");
            String to = req.getParameter("to");

            if (from != null && !from.isEmpty() && to != null && !to.isEmpty()) {

                eventList = searchService.filterFrom(from, eventList);
                eventList = searchService.filterTo(to, eventList);

            } else if (from != null && !from.isEmpty()) {

                eventList = searchService.filterFrom(from, eventList);

            } else if (to != null && !to.isEmpty()) {

                eventList = searchService.filterTo(to, eventList);

            }

        } catch (Exception e) {}

        req.setAttribute("events", eventList);
        req.setAttribute("tags", hashTagRepository.findAll());

        req.setAttribute("oldTags", SearchService.parseTags(req));

        req.setAttribute("today", LocalDate.now());
        req.setAttribute("nextWeekBound", LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)));

        Profile profile = profileRepository.findByOwnerId((Long) session.getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        if (profile != null) {
            req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
            req.setAttribute("photoUrl", profile.getPhotoUrl());
        }

        req.setAttribute( "isAdmin", UserRepository.getInstance(req.getSession()).isAdmin((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY)));

        req.getRequestDispatcher("/searchPage.jsp").include(req, resp);

    }

}
