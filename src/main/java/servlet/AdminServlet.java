package servlet;

import repository.EventRepository;
import repository.ProfileRepository;
import repository.UserRepository;
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

        EventRepository eventRepository = EventRepository.getInstance(req.getSession());
        UserRepository userRepository = UserRepository.getInstance(req.getSession());
        ProfileRepository profileRepository = ProfileRepository.getInstance(req.getSession());

        req.setAttribute( "isAdmin", UserRepository.getInstance(req.getSession()).isAdmin((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY)));

        try {

            Long id = Long.parseLong(req.getParameter("id"));

            req.setAttribute("oldId", id);
            req.setAttribute("events", eventRepository.findByOwner(id));

        } catch (Exception e) {}

        req.setAttribute("msg", req.getParameter("msg"));

        req.setAttribute("usersOnline", OnlineService.getInstance().getOnlineCount());
        req.setAttribute("usersInSystem", userRepository.count());
        req.setAttribute("actualEvents", eventRepository.countActual());
        req.setAttribute("historyEvents", eventRepository.countHistory());
        req.setAttribute("users", profileRepository.findAll());

        req.getRequestDispatcher("/adminPage.jsp").include(req, resp);

    }

}
