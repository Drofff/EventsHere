package filter;

import dto.UserDto;
import service.AuthenticationService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "AdminFilter", urlPatterns = {"/admin", "/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        Long id = (Long) httpServletRequest.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY);
        UserDto userDto = UserDto.getInstance(httpServletRequest.getSession());

        if (id != null && !userDto.isAdmin(id)) {
            ((HttpServletResponse) response).sendRedirect(httpServletRequest.getContextPath());
            return;
        }

        chain.doFilter(httpServletRequest, response);

    }

    @Override
    public void destroy() {

    }
}
