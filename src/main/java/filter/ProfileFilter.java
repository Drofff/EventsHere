package filter;

import dto.ProfileDto;
import dto.UserDto;
import entity.User;
import service.AuthenticationService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "ProfileFilter", urlPatterns = "/*")
public class ProfileFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String uri = httpServletRequest.getRequestURI();

        HttpSession session = httpServletRequest.getSession();

        UserDto userDto = UserDto.getInstance(session);
        ProfileDto profileDto = ProfileDto.getInstance(session);

        if (session.getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY) != null && !uri.matches(".*(/logout).*") && !uri.matches(".*(/profile).*")) {

            Long id = (Long) session.getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY);

                if (userDto.isActive(id) && profileDto.findByOwnerId(id) == null) {

                    request.getRequestDispatcher("/fillProfileData.jsp").include(httpServletRequest, response);
                    return;

                }

        }

        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}
