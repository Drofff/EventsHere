package servlet;

import bean.AuthenticationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ForgotServlet", urlPatterns = {"/forgotPassword"})
public class ForgotServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/forgotPasswordPage.jsp").include(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        AuthenticationService authenticationService = AuthenticationService.getInstance(req.getSession());

        if (email != null && authenticationService.userExists(email)) {

        }

        req.setAttribute("message", "User with such email do not exists");
        req.getRequestDispatcher("/forgotPassword").forward(req, resp);
    }
}
