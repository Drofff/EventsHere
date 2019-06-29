package servlet;

import bean.AuthenticationService;
import bean.EventsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "VisitServlet", urlPatterns = {"/visit"})
public class VisitServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getQueryString();

        EventsService eventsService = EventsService.getInstance(req.getSession());

        if (id != null) {

            try {

                Long eventId = Long.parseLong(id);
                Long userId = (Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY);

                if (eventsService.findById(eventId).getMembers().stream().noneMatch(x -> x.getUserId().equals(userId))) {

                    EventsService.getInstance(req.getSession()).visit(eventId, userId);

                }


            } catch (Exception e) {}

        }

        String url = req.getHeader("referer");

        resp.sendRedirect(url != null? url : "/EventsHere");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("id");

        try {

            Long eventId = Long.parseLong(id);

            EventsService.getInstance(req.getSession()).cancelVisit(eventId, (Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        } catch (Exception e) {}

        String url = req.getHeader("referer");

        resp.sendRedirect(url != null? url : "/EventsHere");
    }
}