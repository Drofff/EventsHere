package servlet;

import dto.ProfileDto;
import dto.UserDto;
import entity.Profile;
import service.AuthenticationService;
import service.MailService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "BlockServlet", urlPatterns = {"/admin/block"})
public class BlockServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long userId = (Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY);

        try {

            Long id = Long.parseLong(req.getParameter("id"));

            String reason = req.getParameter("reason");

            ProfileDto profileDto = ProfileDto.getInstance(req.getSession());

            Profile profile = profileDto.findByOwnerId(id);

            if (profile != null && !id.equals(userId)) {

                UserDto userDto = UserDto.getInstance(req.getSession());

                MailService.getInstance().sendBlockedWarning(UserDto.getInstance(req.getSession()).findById(id), reason);

                userDto.delete(id);

                resp.sendRedirect(req.getContextPath() + "/admin?msg=User blocked successfully");
                return;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        resp.sendRedirect(req.getContextPath());

    }
}
