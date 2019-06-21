package servlet;

import bean.AuthenticationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getSession().removeAttribute(AuthenticationService.USER_AUTHENTICATION_KEY);

        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals(AuthenticationService.REMEMBER_ME_KEY)) {
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
            }
        }

        resp.sendRedirect("/EventsHere/login");

    }
}
