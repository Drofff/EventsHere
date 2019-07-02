package servlet;

import dto.EventDto;
import dto.ProfileDto;
import dto.UserDto;
import service.AuthenticationService;
import service.OnlineService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        EventDto eventDto = EventDto.getInstance(req.getSession());
        UserDto userDto = UserDto.getInstance(req.getSession());
        ProfileDto profileDto = ProfileDto.getInstance(req.getSession());

        req.setAttribute( "isAdmin", UserDto.getInstance(req.getSession()).isAdmin((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY)));

        try {

            Long id = Long.parseLong(req.getParameter("id"));

            req.setAttribute("oldId", id);
            req.setAttribute("events", eventDto.findByOwner(id));

        } catch (Exception e) {}

        req.setAttribute("msg", req.getParameter("msg"));

        req.setAttribute("usersOnline", OnlineService.getInstance().getOnlineCount());
        req.setAttribute("usersInSystem", userDto.count());
        req.setAttribute("actualEvents", eventDto.countActual());
        req.setAttribute("historyEvents", eventDto.countHistory());
        req.setAttribute("users", profileDto.findAll());

        req.getRequestDispatcher("/adminPage.jsp").include(req, resp);

    }

}
