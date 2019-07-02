package servlet;

import dto.UserDto;
import service.AuthenticationService;
import service.MailService;
import service.TokenService;

import javax.mail.MessagingException;
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

        String token = req.getParameter("token");

        if (token != null) {

            TokenService tokenService = TokenService.getInstance();
            UserDto userDto = UserDto.getInstance(req.getSession());

            String email = tokenService.use(token);

            if (email != null && !email.isEmpty()) {

                Long userId = userDto.findByUsername(email);

                if (userId != null) {

                    req.getSession().setAttribute(AuthenticationService.USER_AUTHENTICATION_KEY, userId);

                    req.getRequestDispatcher("/forgotChangePassword.jsp").include(req, resp);
                    return;

                }

            }

            resp.sendRedirect(req.getContextPath() + "/forgotPassword?message=Wrong recovery token");
            return;

        }

        req.getRequestDispatcher("/forgotPasswordPage.jsp").include(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter("email");

        AuthenticationService authenticationService = AuthenticationService.getInstance(req.getSession());

        if (email != null && authenticationService.userExists(email)) {

            TokenService tokenService = TokenService.getInstance();

            MailService mailService = MailService.getInstance();

            String token = tokenService.generate(email);

            try {

                mailService.sendRecoveryToken(email, token);

            } catch (MessagingException e) {

                resp.sendRedirect(req.getContextPath() + "/forgotPassword?message=Sorry, SMTP Server Error. Please, try later");
                e.printStackTrace();
                return;

            }

            req.getRequestDispatcher("/checkYourMail.html").include(req, resp);
            return;

        }

        resp.sendRedirect(req.getContextPath() + "/forgotPassword?message=User with such email do not exists");

    }
}
