package servlet;

import entity.Profile;
import repository.EventRepository;
import repository.ProfileRepository;
import repository.UserRepository;
import service.AuthenticationService;
import service.StorageService;

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

        EventRepository eventRepository = EventRepository.getInstance(req.getSession());

        req.setAttribute("message", req.getParameter("message"));

        req.setAttribute("events", StorageService.putPhotos(eventRepository.findAll(page), req));


        Long pagesCount = eventRepository.getPagesCount();

        if (pagesCount > page + 1) {
            req.setAttribute("nextPage", page + 1);
        }

        if (page > 0 && pagesCount > 0) {
            req.setAttribute("prevPage", page - 1);
        }

        ProfileRepository profileRepository = ProfileRepository.getInstance(req.getSession());

        Profile profile = profileRepository.findByOwnerId((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        if (profile != null) {
            req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
            req.setAttribute("photoUrl", profile.getPhotoUrl());
        }

        req.setAttribute( "isAdmin", UserRepository.getInstance(req.getSession()).isAdmin((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY)));

        req.getRequestDispatcher("/index.jsp").include(req, resp);

    }
}
