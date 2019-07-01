package servlet;

import dto.EventDto;
import dto.HashTagDto;
import dto.ProfileDto;
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

        HashTagDto hashTagDto = HashTagDto.getInstance(req.getSession());
        EventDto eventDto = EventDto.getInstance(req.getSession());

        ProfileDto profileDto = ProfileDto.getInstance(req.getSession());
        Profile profile = profileDto.findByOwnerId((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        if (profile != null) {
            req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
            req.setAttribute("photoUrl", profile.getPhotoUrl());
        }

        req.setAttribute("tags", hashTagDto.findAll());

        if (req.getParameter("id") != null) {

            try {

                Long eventId = Long.parseLong(req.getParameter("id"));

                req.setAttribute("oldData", eventDto.findById(eventId));


            } catch (Exception e) {}

        }

        req.getRequestDispatcher("/addEventPage.jsp").include(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        ValidationService validationService = ValidationService.getInstance(session);
        HashTagDto hashTagDto = HashTagDto.getInstance(session);
        EventDto eventDto = EventDto.getInstance(session);
        ProfileDto profileDto = ProfileDto.getInstance(session);

        Profile profile = profileDto.findByOwnerId((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        if (profile != null) {
            req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
            req.setAttribute("photoUrl", profile.getPhotoUrl());
        }

        req.setAttribute("tags", hashTagDto.findAll());

        try {

            Event event = Event.parse(req);

            Map<String, String> errors = validationService.validate(event);

            if (errors.size() == 0) {

                event.setOwner(profileDto.findByOwnerId((Long) session.getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY)));

                eventDto.save(event);
                resp.sendRedirect("/EventsHere/my");
                return;

            } else {

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

        resp.sendRedirect(url != null ? url : "/EventsHere/my");

    }
}
