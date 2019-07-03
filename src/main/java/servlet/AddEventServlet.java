package servlet;

import repository.EventRepository;
import repository.HashTagRepository;
import repository.ProfileRepository;
import repository.UserRepository;
import entity.Event;
import entity.Profile;
import service.AuthenticationService;
import service.ValidationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "AddEventServlet", urlPatterns = {"/save"})
public class AddEventServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HashTagRepository hashTagRepository = HashTagRepository.getInstance(req.getSession());
        EventRepository eventRepository = EventRepository.getInstance(req.getSession());

        ProfileRepository profileRepository = ProfileRepository.getInstance(req.getSession());
        Profile profile = profileRepository.findByOwnerId((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        if (profile != null) {
            req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
            req.setAttribute("photoUrl", profile.getPhotoUrl());
        }

        Boolean isAdmin = UserRepository.getInstance(req.getSession()).isAdmin((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        req.setAttribute( "isAdmin", isAdmin);

        req.setAttribute("tags", hashTagRepository.findAll());

        if (req.getParameter("id") != null) {

            try {

                Long eventId = Long.parseLong(req.getParameter("id"));

                Event currentEvent = eventRepository.findById(eventId);

                String oldDescription = currentEvent.getDescription();
                currentEvent.setDescription(oldDescription.replace("\"", "\'\'"));

                req.setAttribute("oldData", currentEvent);

                if (!currentEvent.getOwner().getUserId().equals(profile.getUserId()) && !isAdmin) {
                    resp.sendRedirect(req.getContextPath());
                    return;
                }


            } catch (Exception e) {}

        }

        req.getRequestDispatcher("/addEventPage.jsp").include(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        ValidationService validationService = ValidationService.getInstance(session);
        HashTagRepository hashTagRepository = HashTagRepository.getInstance(session);
        EventRepository eventRepository = EventRepository.getInstance(session);
        ProfileRepository profileRepository = ProfileRepository.getInstance(session);
        UserRepository userRepository = UserRepository.getInstance(session);

        Long userId = (Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY);

        Profile profile = profileRepository.findByOwnerId(userId);
        req.setAttribute( "isAdmin", UserRepository.getInstance(req.getSession()).isAdmin(userId));

        if (profile != null) {
            req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
            req.setAttribute("photoUrl", profile.getPhotoUrl());
        }

        req.setAttribute("tags", hashTagRepository.findAll());

        try {

            Event event = Event.parse(req);

            Map<String, String> errors = validationService.validate(event);

            if (errors.size() == 0) {

                event.setOwner(profile);

                if (!userRepository.isAdmin(profile.getUserId()) && !event.getOwner().getUserId().equals(profile.getUserId())) {
                    resp.sendRedirect(req.getContextPath());
                }

                eventRepository.save(event);
                resp.sendRedirect(req.getContextPath() + "/my");
                return;

            } else {

                String oldDescription = event.getDescription();
                event.setDescription(oldDescription.replace("\"", "\'\'"));

                req.setAttribute("oldData", event);

                for (Map.Entry<String, String> error : errors.entrySet()) {
                    req.setAttribute(error.getKey(), error.getValue());
                }

                req.getRequestDispatcher("/addEventPage.jsp").include(req, resp);
                return;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = req.getHeader("referer");

        resp.sendRedirect(url != null ? url : req.getContextPath() + "/my");

    }
}
