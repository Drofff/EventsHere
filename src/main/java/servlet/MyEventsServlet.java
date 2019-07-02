package servlet;

import dto.EventDto;
import dto.ProfileDto;
import dto.UserDto;
import entity.Profile;
import service.AuthenticationService;

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

        EventDto eventDto = EventDto.getInstance(req.getSession());

        Long userId = (Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY);

        req.setAttribute("events", eventDto.findByOwner(userId));

        req.setAttribute("history", eventDto.getHistoryOf(userId));

        ProfileDto profileDto = ProfileDto.getInstance(req.getSession());

        Profile profile = profileDto.findByOwnerId(userId);

        if (profile != null) {
            req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
            req.setAttribute("photoUrl", profile.getPhotoUrl());
        }

        req.setAttribute( "isAdmin", UserDto.getInstance(req.getSession()).isAdmin((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY)));

        req.getRequestDispatcher("/myEventsPage.jsp").include(req, resp);

    }
}
