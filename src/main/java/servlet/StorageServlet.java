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
import java.nio.file.Paths;

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

        Integer count = storageService.getPhotosCount(username);

        req.setAttribute("count", count);
        req.setAttribute("limit", StorageService.STORAGE_LIMIT);

        if (count > 0) {

            req.setAttribute("photos", storageService.getAllPhotos(username));

        }

        req.getRequestDispatcher("/storagePage.jsp").include(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = UserRepository.getInstance(req.getSession()).findById( (Long) req.getSession().getAttribute(AuthenticationService.USER_AUTHENTICATION_KEY));

        try {

            Part part = req.getPart("photo");

            StorageService storageService = StorageService.getInstance();

            storageService.savePhoto(username, part);

        } catch (Exception e) {
            e.printStackTrace();
        }

        resp.sendRedirect(req.getHeader("referer"));

    }
}
