package servlet;

import dto.ProfileDto;
import entity.Profile;
import service.AuthenticationService;
import service.ValidationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "EditProfileServlet", urlPatterns = {"/editProfile"})
public class EditProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        Long id = (Long) session.getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY);

        Profile profile = ProfileDto.getInstance(req.getSession()).findByOwnerId(id);

        req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
        req.setAttribute("photoUrl", profile.getPhotoUrl());

        req.setAttribute("oldData", profile);

        req.getRequestDispatcher("/editProfilePage.jsp").include(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Profile profile = Profile.create(req);

        ProfileDto profileDto = ProfileDto.getInstance(req.getSession());

        HttpSession session = req.getSession();

        Profile currentProfile = profileDto.findByOwnerId((Long) session.getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        req.setAttribute("name", currentProfile.getFirstName() + " " + currentProfile.getLastName());
        req.setAttribute("photoUrl", currentProfile.getPhotoUrl());

        String status = req.getParameter("status");

        profile.setStatus(status != null || status.isEmpty() ? status : "Using app");

        Map<String, String> errors = ValidationService.getInstance(session).validate(profile);

        if (errors.size() > 0) {

            errors.entrySet().stream().forEach(x -> req.setAttribute(x.getKey(), x.getValue()));

            req.setAttribute("oldData", profile);

            req.getRequestDispatcher("/editProfilePage.jsp").include(req, resp);
            return;

        }

        profile.setUserId((Long) session.getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));
        profile.setId(currentProfile.getId());

        profileDto.save(profile);

        resp.sendRedirect("/EventsHere");

    }
}
