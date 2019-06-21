package filter;

import bean.AuthenticationService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        HttpSession session = httpServletRequest.getSession(true);

        Cookie [] cookies = httpServletRequest.getCookies();

        Optional<Cookie> sessionTokenCookie = Arrays.stream(cookies).filter(x -> x.getName().equals(AuthenticationService.REMEMBER_ME_KEY)).findFirst();

        if (session.getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY) == null) {

            if (sessionTokenCookie.isPresent()) {

                try {

                    String token = sessionTokenCookie.get().getValue();

                    AuthenticationService authenticationService = AuthenticationService.getInstance(session);

                    session.setAttribute(AuthenticationService.USER_AUTHENTICATION_KEY, authenticationService.authenticateByToken(token));

                } catch (SQLException e) {

                    e.printStackTrace();

                    request.getRequestDispatcher("/login").forward(request, response);

                }

            } else if (!httpServletRequest.getRequestURI().matches("(/login).*")) {

                request.getRequestDispatcher("/login").forward(request, response);
            }


        }

        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}
