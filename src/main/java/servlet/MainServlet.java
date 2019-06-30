package servlet;

import dto.EventDto;
import dto.ProfileDto;
import entity.Profile;
import service.AuthenticationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MainServlet", urlPatterns = {""})
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pageNumber = req.getParameter("page");

        Integer page = 0;

        if (pageNumber != null) {
            page = Integer.parseInt(pageNumber);
        }

        EventDto eventDto = EventDto.getInstance(req.getSession());

        req.setAttribute("events", eventDto.findAll(page));

        Long pagesCount = eventDto.getPagesCount();

        if (pagesCount > page) {
            req.setAttribute("nextPage", page + 1);
        }

        if (page > 0 && pagesCount > 0) {
            req.setAttribute("prevPage", page - 1);
        }

        ProfileDto profileDto = ProfileDto.getInstance(req.getSession());

        Profile profile = profileDto.findByOwnerId((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        if (profile != null) {
            req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
            req.setAttribute("photoUrl", profile.getPhotoUrl());
        }

        req.getRequestDispatcher("/index.jsp").include(req, resp);

    }
}
