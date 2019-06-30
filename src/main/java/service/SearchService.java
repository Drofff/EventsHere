package service;

import dto.EventDto;
import entity.Event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class SearchService implements Serializable {

    private static SearchService searchService;

    private static HttpSession session;

    private SearchService() {}

    public static SearchService getInstance(HttpSession httpSession) {

        if (searchService == null) {
            searchService = new SearchService();
            session = httpSession;
        }

        return searchService;
    }

    public List<Event> search(HttpServletRequest req) {

        String name = req.getParameter("name");
        String hashTags = req.getParameter("hash");

        EventDto eventDto = EventDto.getInstance(session);

        List<Event> events = eventDto.findAll(0);

        if (name != null && !name.isEmpty() && hashTags != null && !hashTags.isEmpty()) {

            events = eventDto.findByNameAndTag(name, parseTags(req));

        } else if (name != null && !name.isEmpty()) {

            events = eventDto.findByName(req.getParameter("name"));

            req.setAttribute("oldName", name);

        } else if (hashTags != null && !hashTags.isEmpty()) {

            events = eventDto.findByHashtag(parseTags(req));

        }

        return events;

    }

    public static List<String> parseTags(HttpServletRequest request) {

        List<String> tags = new LinkedList<>();

        Enumeration<String> params = request.getParameterNames();

        while (params.hasMoreElements()) {

            String currentParameterName = params.nextElement();

            if (currentParameterName.matches("(hash)(\\D)*")) {
                tags.add(request.getParameter(currentParameterName));
            }

        }

        return tags;
    }

}
