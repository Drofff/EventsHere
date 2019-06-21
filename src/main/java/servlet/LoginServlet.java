package servlet;

import bean.AuthenticationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

        Boolean rememberMe = req.getParameter("remember_me") != null;

        AuthenticationService authenticationService = AuthenticationService.getInstance(req.getSession());

        Long id = authenticationService.authenticate(username, password);

        if (id == AuthenticationService.FAILURE_CODE) {

            req.setAttribute("message", "Invalid credentials");

            req.getRequestDispatcher("/login").forward(req, resp);

        }

        req.getSession().setAttribute(AuthenticationService.USER_AUTHENTICATION_KEY, id);

        if (rememberMe) {

            String token = authenticationService.getToken(id);

            if (token != null) {

                Cookie cookie = new Cookie(AuthenticationService.REMEMBER_ME_KEY, token);

                resp.addCookie(cookie);

            } else {
                //DEBUG
                //TODO REMOVE
                System.out.println("ERROR WITH REMEMBER ME");
            }

        }

        req.getRequestDispatcher("/").forward(req, resp);

    }
}
