package servlet;

import dto.EventDto;
import dto.ProfileDto;
import dto.UserDto;
import entity.Profile;
import entity.User;
import service.AuthenticationService;

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
        UserDto userDto = UserDto.getInstance(req.getSession());

        Long userId = (Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY);

        try {

            Long id = Long.parseLong(req.getParameter("id"));

            if (!userDto.isAdmin(userId) && !eventDto.findById(id).getOwner().getUserId().equals(userId)) {
                throw new Exception("Invalid Permission");
            }

            eventDto.deleteById(id);

        } catch (Exception e) {}

        String url = req.getHeader("referer");

        resp.sendRedirect(url != null? url : req.getContextPath() + "/my");
    }
}
