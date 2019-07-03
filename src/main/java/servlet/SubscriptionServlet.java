package servlet;

import repository.ProfileRepository;
import entity.Profile;
import service.AuthenticationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "SubscriptionServlet", urlPatterns = {"/subscribe"})
public class SubscriptionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            Long id = Long.parseLong(req.getParameter("id"));

            HttpSession session = req.getSession();
            ProfileRepository profileRepository = ProfileRepository.getInstance(session);

            Profile currentProfile = profileRepository.findByOwnerId((Long) session.getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

            if (profileRepository.getSubscribers(id).stream().anyMatch(x -> x.getId().equals(currentProfile.getId()))) {
                profileRepository.unsubscribe(currentProfile.getId(), id);
            } else {
                profileRepository.subscribe(currentProfile.getId(), id);
            }

        } catch (Exception e) {}


        String url = req.getHeader("referer");
        resp.sendRedirect(url != null ? url : req.getContextPath());

    }
}
