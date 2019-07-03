package service;

import repository.EventRepository;
import entity.Event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<Event> filterFrom(String from, List<Event> eventList) throws Exception {
        LocalDate fromDateTime = LocalDate.parse(from);
        return  eventList.stream().filter(x -> ( x.getDateTime().toLocalDate().isAfter(fromDateTime) || x.getDateTime().toLocalDate().isEqual(fromDateTime)))
                .collect(Collectors.toList());
    }

    public List<Event> filterTo(String to, List<Event> eventList) throws Exception {
        LocalDate toDateTime = LocalDate.parse(to);
        return eventList.stream().filter(x -> x.getDateTime().toLocalDate().isBefore(toDateTime) || x.getDateTime().toLocalDate().isEqual(toDateTime))
                .collect(Collectors.toList());
    }

    public List<Event> search(HttpServletRequest req) {

        String name = req.getParameter("name");
        String hashTags = req.getParameter("hash");

        EventRepository eventRepository = EventRepository.getInstance(session);

        List<Event> events = eventRepository.findAll(0);

        if (name != null && !name.isEmpty() && hashTags != null && !hashTags.isEmpty()) {

            events = eventRepository.findByNameAndTag(name, parseTags(req));

        } else if (name != null && !name.isEmpty()) {

            events = eventRepository.findByName(req.getParameter("name"));

            req.setAttribute("oldName", name);

        } else if (hashTags != null && !hashTags.isEmpty()) {

            events = eventRepository.findByHashtag(parseTags(req));

        }

        return events;

    }

    public static List<String> parseTags(HttpServletRequest request) {

        String [] tags = request.getParameterValues("hash");

        if (tags == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(tags);

    }

}
