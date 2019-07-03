package servlet;

import repository.UserRepository;
import entity.User;
import service.ActivationService;
import service.AuthenticationService;
import service.EncryptingService;
import service.ValidationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegistrationServlet", urlPatterns = {"/registration"})
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String token = req.getParameter("token");

        if (token != null) {

            ActivationService activationService = ActivationService.getInstance();

            String email = activationService.use(token);

            if (!email.equals("")) {

                UserRepository userRepository = UserRepository.getInstance(req.getSession());

                Long id = userRepository.findByUsername(email);

                if (id != null) {

                    userRepository.activate(email);
                    req.getSession().setAttribute(AuthenticationService.USER_AUTHENTICATION_KEY, id);

                    resp.sendRedirect(req.getContextPath());

                    return;

                }

            }

            req.setAttribute("message", "Wrong activation token");

        }

        req.getRequestDispatcher("/registrationPage.jsp").include(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String repeated_password = req.getParameter("rpassword");

        ValidationService validationService = ValidationService.getInstance(req.getSession());
        AuthenticationService authenticationService = AuthenticationService.getInstance(req.getSession());
        UserRepository userRepository = UserRepository.getInstance(req.getSession());

        if (validationService.validateEmail(email)) {

            if (!authenticationService.userExists(email)) {

                if (password.length() >= 6) {

                    if (password.equals(repeated_password)) {

                        User user = new User();
                        user.setUsername(email);
                        user.setPassword(EncryptingService.getInstance().encrypt(password));

                        userRepository.save(user);

                        ActivationService activationService = ActivationService.getInstance();
                        activationService.sendActivationMail(user.getUsername());

                        req.getRequestDispatcher("/activatePage.html").include(req, resp);
                        return;

                    }

                      req.setAttribute("passwordMismatch", true);

                } else {

                    req.setAttribute("lengthError", true);

                }

            } else {
                req.setAttribute("userExists", true);
            }

        } else {
            req.setAttribute("emailError", true);
        }

        req.setAttribute("oldEmail", email);
        req.setAttribute("oldPassword", password);
        req.setAttribute("oldRepeatedPassword", repeated_password);

        req.getRequestDispatcher("/registrationPage.jsp").include(req, resp);


    }
}
