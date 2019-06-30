package servlet;

import dto.EventDto;
import dto.ProfileDto;
import entity.Event;
import entity.Profile;
import service.AuthenticationService;

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

        EventDto eventDto = EventDto.getInstance(req.getSession());
        ProfileDto profileDto = ProfileDto.getInstance(req.getSession());

        try {

            Long id = Long.parseLong(req.getParameter("id"));

            Event currentEvent = eventDto.findById(id);

            req.setAttribute("event", currentEvent);

            Profile profile = profileDto.findByOwnerId( (Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

            if (profile != null) {

                req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
                req.setAttribute("photoUrl", profile.getPhotoUrl());

                if (currentEvent.getMembers().stream().anyMatch(x -> x.getId().equals(profile.getId()))) {
                    req.setAttribute("member", true);
                }

            }

            req.getRequestDispatcher("/eventPage.jsp").include(req, resp);

        } catch (Exception e) {
            resp.sendRedirect("/EventsHere");
        }

    }
}
