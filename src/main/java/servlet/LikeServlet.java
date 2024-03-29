package servlet;

import repository.EventRepository;
import service.AuthenticationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LikeServlet", urlPatterns = {"/like"})
public class LikeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            Long eventId = Long.parseLong(req.getParameter("id"));

            EventRepository eventRepository = EventRepository.getInstance(req.getSession());

            eventRepository.like(eventId, (Long)req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        } catch (Exception e) {}

        if (req.getHeader("referer") != null) {
            resp.sendRedirect(req.getHeader("referer"));
        } else {
            resp.sendRedirect(req.getContextPath());
        }

    }
}
