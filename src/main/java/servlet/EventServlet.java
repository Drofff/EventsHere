package servlet;

import entity.Event;
import entity.Profile;
import repository.EventRepository;
import repository.ProfileRepository;
import repository.UserRepository;
import service.AuthenticationService;
import service.PropertiesService;
import service.StorageService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "EventServlet", urlPatterns = {"/event"})
public class EventServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        EventRepository eventRepository = EventRepository.getInstance(req.getSession());
        ProfileRepository profileRepository = ProfileRepository.getInstance(req.getSession());
        StorageService storageService = StorageService.getInstance();
        PropertiesService propertiesService = PropertiesService.getInstance();
        UserRepository userRepository = UserRepository.getInstance(req.getSession());

        try {

            Long id = Long.parseLong(req.getParameter("id"));

            Event currentEvent = eventRepository.findById(id);

            currentEvent.setDescription(currentEvent.getDescription().replace("\n", "<br/>"));

            req.setAttribute("event", currentEvent);
            req.setAttribute("map_url", propertiesService.getGoogleMapsUrl() + currentEvent.getCity() + ", " + currentEvent.getAddress());
            req.setAttribute("eventPhoto", storageService.getPhoto(userRepository.findById(currentEvent.getOwner().getUserId()), currentEvent.getPhotoUrl()));

            Profile profile = profileRepository.findByOwnerId( (Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

            req.setAttribute( "isAdmin", UserRepository.getInstance(req.getSession()).isAdmin((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY)));

            if (profile != null) {

                req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
                req.setAttribute("photoUrl", profile.getPhotoUrl());

                if (currentEvent.getMembers().stream().anyMatch(x -> x.getId().equals(profile.getId()))) {
                    req.setAttribute("member", true);
                }

            }

            req.getRequestDispatcher("/eventPage.jsp").include(req, resp);

        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath());
        }

    }
}
