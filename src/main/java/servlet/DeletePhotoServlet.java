package servlet;

import repository.UserRepository;
import service.AuthenticationService;
import service.StorageService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeletePhotoServlet", urlPatterns = {"/deletePhoto"})
public class DeletePhotoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            String fileName = req.getParameter("fileName");

            String username = UserRepository.getInstance(req.getSession()).findById((Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

            StorageService.getInstance().delete(username, fileName, req.getSession());

        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = req.getHeader("referer");

        resp.sendRedirect(url != null ? url : req.getContextPath());

    }
}
