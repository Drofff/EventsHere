package service;

import entity.Event;
import org.apache.http.client.utils.URIBuilder;
import repository.EventRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchService implements Serializable {

    private static SearchService searchService;

    private static HttpSession session;

    public static final Integer PAGE_SIZE = 5;

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

        Integer pageNumber = 0;

        try {
            pageNumber = Integer.parseInt(req.getParameter("page"));

        } catch (Exception e) {}

        if (pageNumber < 0) {
            return Collections.emptyList();
        }

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

        Integer pageCount = events.size() / PAGE_SIZE;

        if (pageNumber < (pageCount - 1)) {
            req.setAttribute("next", getPageRedirectUrl(req, pageNumber + 1));
        }

        if (pageNumber > 0) {
            req.setAttribute("previous", getPageRedirectUrl(req, pageNumber - 1));
        }

        Integer page = pageNumber * PAGE_SIZE;

        List<Event> pagedEvents = new LinkedList<>();

        for (int i = page; i < (page + PAGE_SIZE); i++) {
            if (i < events.size()) {
                pagedEvents.add(events.get(i));
            }
        }

        return pagedEvents;

    }

    private String getPageRedirectUrl(HttpServletRequest request, Integer pageNumber) {

        String strUrl = request.getRequestURI();

        if (request.getQueryString() != null) {
            strUrl += "?" + request.getQueryString();
        }

        try {

            URIBuilder uriBuilder = new URIBuilder(strUrl)
                    .setParameter("page", pageNumber + "");

            return uriBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return request.getContextPath();
        }

    }

    public static List<String> parseTags(HttpServletRequest request) {

        String [] tags = request.getParameterValues("hash");

        if (tags == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(tags);

    }

}
