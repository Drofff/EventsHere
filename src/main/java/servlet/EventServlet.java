package servlet;

import bean.EventsService;
import dto.Event;

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

        EventsService eventsService = EventsService.getInstance(req.getSession());

        try {

            Long id = Long.parseLong(req.getParameter("id"));

            Event currentEvent = eventsService.findById(id);

            req.setAttribute("event", currentEvent);

            req.getRequestDispatcher("/eventPage.jsp").include(req, resp);

        } catch (Exception e) {
            resp.sendRedirect("/EventsHere");
        }

    }
}
