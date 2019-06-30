package servlet;

import dto.HashTagDto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "HashTagServlet", urlPatterns = {"/hash"})
public class HashTagServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HashTagDto hashTagDto = HashTagDto.getInstance(req.getSession());

        List<String> tags = hashTagDto.findAll();

        for (String tag : tags) {
            resp.getWriter().println("<option value='" + tag + "'>" + tag + "</option>\n");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String name = req.getParameter("name");

        HashTagDto hashTagDto = HashTagDto.getInstance(req.getSession());

        if (name != null && !name.isEmpty() && !hashTagDto.findAll().contains(name)) {

            hashTagDto.add(name);

            resp.getWriter().print(true);
            return;

        }

        resp.getWriter().print(false);

    }
}
