package filter;

import service.AuthenticationService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    private List<String> unsecuredUrls = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        unsecuredUrls.add("/login");
        unsecuredUrls.add("/registration");
        unsecuredUrls.add("/forgotPassword");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        HttpSession session = httpServletRequest.getSession(false);

        if (session == null) {
            session = httpServletRequest.getSession(true);
        }

        Cookie [] cookies = httpServletRequest.getCookies();

        Optional<Cookie> sessionTokenCookie = Optional.empty();

        if (cookies != null && cookies.length > 0) {
            sessionTokenCookie = Arrays.stream(cookies).filter(x -> x.getName().equals(AuthenticationService.REMEMBER_ME_KEY)).findFirst();
        }

        if (session.getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY) == null) {

            if (unsecuredUrls.stream().noneMatch(x -> httpServletRequest.getRequestURI().matches(".*(" + x + ").*"))) {
                session.setAttribute("redirect_to", httpServletRequest.getRequestURI());
            }

            if (sessionTokenCookie.isPresent()) {

                try {

                    String token = sessionTokenCookie.get().getValue();

                    AuthenticationService authenticationService = AuthenticationService.getInstance(session);

                    session.setAttribute(AuthenticationService.USER_AUTHENTICATION_KEY, authenticationService.authenticateByToken(token));

                } catch (SQLException e) {

                    e.printStackTrace();

                    httpServletRequest.getRequestDispatcher("/login").forward(httpServletRequest, response);

                    return;

                }

            } else if (unsecuredUrls.stream().noneMatch(x -> httpServletRequest.getRequestURI().matches(".*(" + x + ").*"))) {

                httpServletRequest.getRequestDispatcher("/login").forward(httpServletRequest, response);
                return;

            }


        }

        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}
