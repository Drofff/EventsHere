package servlet;

import dto.EventDto;
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

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        ProfileDto profileDto = ProfileDto.getInstance(session);

        Long userId = (Long) session.getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY);
        Profile currentProfile = profileDto.findByOwnerId(userId);

        if (currentProfile == null) {
            resp.sendRedirect("/EventsHere");
            return;
        }

        req.setAttribute("name", currentProfile.getFirstName() + " " + currentProfile.getLastName());
        req.setAttribute("photoUrl", currentProfile.getPhotoUrl());

        String profileId = req.getParameter("id");

        Profile profile = null;

        if (profileId == null) {

            profile = currentProfile;
            req.setAttribute("me", true);

        } else {

            try {

                profile = profileDto.findById(Long.parseLong(profileId));

                if (profile.getId().equals(currentProfile.getId())) {

                    req.setAttribute("me", true);

                } else if (profileDto.getSubscribers(profile.getId()).stream().anyMatch(x -> x.getId().equals(currentProfile.getId()))) {

                    req.setAttribute("subscriber", true);

                }

            } catch (Exception e) {
                e.printStackTrace();
                resp.sendRedirect("/EventsHere");
                return;
            }

        }

        profile.setSubscribers(profileDto.getSubscribers(profile.getId()));
        profile.setSubscriptions(profileDto.getSubscriptions(profile.getId()));

        req.setAttribute("profile", profile);

        if (profile != null) {
            EventDto eventDto = EventDto.getInstance(session);
            req.setAttribute("history", eventDto.getHistoryOf(profile.getUserId()));
            req.setAttribute("events", eventDto.findByOwner(profile.getUserId()));
        }

        req.getRequestDispatcher("/profilePage.jsp").include(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Profile profile = Profile.create(req);
        HttpSession session = req.getSession();

        Map<String, String> errors = ValidationService.getInstance(session).validate(profile);

        if (errors.size() > 0) {

            errors.entrySet().stream().forEach(x -> req.setAttribute(x.getKey(), x.getValue()));

            req.setAttribute("oldData", profile);

            req.getRequestDispatcher("/fillProfileData.jsp").include(req, resp);
            return;

        }

        profile.setUserId((Long) session.getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        ProfileDto profileDto = ProfileDto.getInstance(req.getSession());

        profileDto.save(profile);

        resp.sendRedirect("/EventsHere");

    }
}
