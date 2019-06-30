package servlet;

import dto.EventDto;
import dto.HashTagDto;
import dto.ProfileDto;
import entity.Profile;
import service.AuthenticationService;
import service.SearchService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "SearchServlet", urlPatterns = "/search")
public class SearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        SearchService searchService = SearchService.getInstance(session);
        HashTagDto hashTagDto = HashTagDto.getInstance(session);
        ProfileDto profileDto = ProfileDto.getInstance(session);

        req.setAttribute("events", searchService.search(req));

        req.setAttribute("tags", hashTagDto.findAll());

        Profile profile = profileDto.findByOwnerId((Long) session.getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        if (profile != null) {
            req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
            req.setAttribute("photoUrl", profile.getPhotoUrl());
        }

        req.getRequestDispatcher("/searchPage.jsp").include(req, resp);

    }

}
