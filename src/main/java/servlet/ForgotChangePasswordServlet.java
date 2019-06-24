package servlet;

import bean.AuthenticationService;
import bean.ForgotService;
import bean.UserDataService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "ForgotChangePasswordServlet", urlPatterns = {"/forgotChangePassword"})
public class ForgotChangePasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String password = req.getParameter("password");
        String repeatedPassword = req.getParameter("rpassword");

        if (password != null && repeatedPassword != null) {

            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/forgotChangePassword.jsp");

            if (password.length() < 6) {
                req.setAttribute("lengthError", true);
                requestDispatcher.forward(req, resp);
                return;
            }

            if (!password.equals(repeatedPassword)) {
                req.setAttribute("passwordMismatch", true);
                requestDispatcher.forward(req, resp);
                return;
            }

            HttpSession httpSession = req.getSession();

            UserDataService userDataService = UserDataService.getInstance(httpSession);

            userDataService.changePassword((Long) httpSession.getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY), password);

            resp.sendRedirect("/EventsHere?message=Saved!");

        }

    }
}
