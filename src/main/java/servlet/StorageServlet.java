package servlet;

import entity.Profile;
import repository.ProfileRepository;
import repository.UserRepository;
import service.AuthenticationService;
import service.StorageService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@WebServlet(name = "StorageServlet", urlPatterns = {"/storage"})
@MultipartConfig
public class StorageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        StorageService storageService = StorageService.getInstance();

        UserRepository userRepository = UserRepository.getInstance(req.getSession());

        Long userId = (Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY);

        String username = userRepository.findById(userId);

        Profile profile = ProfileRepository.getInstance(req.getSession()).findByOwnerId(userId);

        req.setAttribute("name", profile.getFirstName() + " " + profile.getLastName());
        req.setAttribute("photoUrl", profile.getPhotoUrl());

        req.setAttribute("message", req.getParameter("message"));

        req.setAttribute("isAdmin", UserRepository.getInstance(req.getSession()).isAdmin(userId));

        Integer count = storageService.getPhotosCount(username);

        req.setAttribute("count", count);
        req.setAttribute("limit", StorageService.STORAGE_LIMIT);

        if (count > 0) {

            req.setAttribute("photos", storageService.getAllPhotosWithInfo(username, req.getSession()));

        }

        req.getRequestDispatcher("/storagePage.jsp").include(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = UserRepository.getInstance(req.getSession()).findById( (Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        try {

            Part part = req.getPart("photo");

            StorageService storageService = StorageService.getInstance();

            if (storageService.getAllPhotos(username).entrySet().stream().anyMatch(x -> x.getKey().equals(part.getSubmittedFileName()))) {

                resp.sendRedirect(req.getContextPath() + "/storage?message=File with such name already exists");
                return;

            }

            storageService.savePhoto(username, part);



        } catch (Exception e) {
            e.printStackTrace();
        }

        resp.sendRedirect(req.getHeader("referer"));

    }
}
