package servlet;

import dto.EventDto;
import service.AuthenticationService;

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

        EventDto eventDto = EventDto.getInstance(req.getSession());

        if (id != null) {

            try {

                Long eventId = Long.parseLong(id);
                Long userId = (Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY);

                if (eventDto.findById(eventId).getMembers().stream().noneMatch(x -> x.getUserId().equals(userId))) {

                    eventDto.visit(eventId, userId);

                }


            } catch (Exception e) {}

        }

        String url = req.getHeader("referer");

        resp.sendRedirect(url != null? url : "/EventsHere");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("id");

        EventDto eventDto = EventDto.getInstance(req.getSession());

        try {

            Long eventId = Long.parseLong(id);

            eventDto.cancelVisit(eventId, (Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        } catch (Exception e) {}

        String url = req.getHeader("referer");

        resp.sendRedirect(url != null? url : "/EventsHere");
    }
}