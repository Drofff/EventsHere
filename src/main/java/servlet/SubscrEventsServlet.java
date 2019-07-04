package servlet;

import repository.EventRepository;
import repository.ProfileRepository;
import entity.Event;
import entity.Profile;
import repository.UserRepository;
import service.AuthenticationService;
import service.StorageService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "SubscrEventsServlet", urlPatterns = "/subscription")
public class SubscrEventsServlet extends HttpServlet {

    public static final String NOTIFY_ME_KEY = "yes_notify";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        Long userId = (Long) session.getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY);

        EventRepository eventRepository = EventRepository.getInstance(session);
        ProfileRepository profileRepository = ProfileRepository.getInstance(session);

        Profile profile = profileRepository.findByOwnerId(userId);

        req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
        req.setAttribute("photoUrl", profile.getPhotoUrl());
        req.setAttribute("notifyMe", profile.getNotifyMe());

        req.setAttribute("isAdmin", UserRepository.getInstance(req.getSession()).isAdmin(userId));

        List<Event> events = new LinkedList<>();

        List<Profile> subscriptions = profileRepository.getSubscriptions(profile.getId());

        subscriptions.stream().forEach(x -> events.addAll(eventRepository.findByOwner(x.getUserId())));

        ZoneOffset zoneOffset = ZonedDateTime.now().getOffset();

        events.sort((x, y) -> (int) (x.getDateTime().toEpochSecond(zoneOffset) - y.getDateTime().toEpochSecond(zoneOffset)));

        req.setAttribute("events", StorageService.putPhotos(events, req));

        req.getRequestDispatcher("/subscrEventsPage.jsp").include(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            HttpSession session = req.getSession();

            Boolean notifyMe = req.getParameter("notify").equals(NOTIFY_ME_KEY);

            Long userId = (Long) session.getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY);

            ProfileRepository.getInstance(session).setEmailNotification(userId, notifyMe);

        } catch (Exception e) {}


        String url = req.getHeader("referer");
        resp.sendRedirect(url != null ? url : req.getContextPath() + "/subscription");

    }
}
