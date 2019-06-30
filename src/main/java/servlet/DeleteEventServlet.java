package servlet;

import dto.EventDto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteEventServlet", urlPatterns = {"/delete"})
public class DeleteEventServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        EventDto eventDto = EventDto.getInstance(req.getSession());

        try {

            Long id = Long.parseLong(req.getParameter("id"));

            eventDto.deleteById(id);

        } catch (Exception e) {}

        String url = req.getHeader("referer");

        resp.sendRedirect(url != null? url : "/EventsHere/my");
    }
}
