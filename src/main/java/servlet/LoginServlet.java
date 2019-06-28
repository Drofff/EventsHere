package servlet;

import bean.AuthenticationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/loginPage.jsp").include(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        HttpSession session = req.getSession(false);

        Boolean rememberMe = req.getParameter("remember_me") != null;

        AuthenticationService authenticationService = AuthenticationService.getInstance(req.getSession());

        Long id = authenticationService.authenticate(username, password);

        if (id == AuthenticationService.FAILURE_CODE) {

            resp.sendRedirect("/EventsHere/login?error");
            return;

        }

        session.setAttribute(AuthenticationService.USER_AUTHENTICATION_KEY, id);

        if (rememberMe) {

            String token = authenticationService.getToken(id);

            if (token != null) {

                Cookie cookie = new Cookie(AuthenticationService.REMEMBER_ME_KEY, token);

                resp.addCookie(cookie);

            }

        }

        String redirectUrl = (String)session.getAttribute("redirect_to");

        if (redirectUrl != null) {
            session.removeAttribute("redirect_to");
            resp.sendRedirect(redirectUrl);
        } else {
            resp.sendRedirect("/EventsHere");
        }

    }
}
