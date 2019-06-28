package filter;

import bean.AuthenticationService;
import bean.UserDataService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "ActivationFilter", urlPatterns = {"/*"})
public class ActivationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        HttpSession session = httpServletRequest.getSession(false);

        if (session == null) {
            session = httpServletRequest.getSession(true);
        }

        if (session.getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY) != null && !httpServletRequest.getRequestURI().matches(".*(/logout).*")) {

            Long id = (Long) session.getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY);

            UserDataService userDataService = UserDataService.getInstance(session);

            if (!userDataService.isActive(id)) {

                httpServletRequest.getRequestDispatcher("/activatePage.html").include(httpServletRequest, response);
                return;

            }

        }

        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}
