package bean;

import entity.Event;
import entity.Profile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class EventsService implements Serializable {

    private static EventsService eventsService;

    private static Connection connection;

    private static ProfileService profileService;

    private static final Integer elementsPerPage = 10;

    private static DateTimeFormatter dateFormatter;

    private EventsService() {}

    public static EventsService getInstance(HttpSession session) {
        if (eventsService == null) {
            eventsService = new EventsService();
            profileService = ProfileService.getInstance(session);
            connection = (Connection) session.getAttribute(ConnectionService.CONNECTION_KEY);

            dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd' 'HH:mm:ss");

        }
        return eventsService;
    }

    public LocalDateTime parseDate(String date) {

        if (date.matches(".*\\..*")) {
            date = date.split("\\.")[0];
        }

        return LocalDateTime.parse(date, dateFormatter);

    }

    public Long getPagesCount() {
        try {
            String query = "select count(*) as num from events where date_time >= ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));

            return preparedStatement.executeQuery().getLong("num") / 10;
        } catch (Exception e) {
            return 0l;
        }
    }

    public List<Event> findByNameAndTag(String name, List<String> tags) {
        return findByName(name).stream().filter(x -> x.getHashTags().containsAll(tags)).collect(Collectors.toList());
    }

    public List<Event> findAll(Integer pageNumber) {

        List<Event> events = new LinkedList<>();

        Integer leftBound = pageNumber * elementsPerPage;

        String query = "select * from events where date_time >= ? limit 10 offset ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setInt(2, leftBound);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                events.add(parseEvent(resultSet));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (events.size() > 0) {

            Collections.sort(events, (x, y) -> (int) (x.getDateTime().toEpochSecond(ZonedDateTime.now().getOffset()) - y.getDateTime().toEpochSecond(ZonedDateTime.now().getOffset())));
        }

        return events;
    }

    public List<Event> findByName(String name) {
        String query = "select * from events where date_time >= ? and LOWER(name) like LOWER(CONCAT('%', ?, '%'))";

        List<Event> events = new LinkedList<>();

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(2, name);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                events.add(parseEvent(resultSet));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        ZoneOffset zoneOffset = ZonedDateTime.now().getOffset();

        events.sort((x, y) -> (int) (x.getDateTime().toEpochSecond(zoneOffset) - y.getDateTime().toEpochSecond(zoneOffset)));

        return events;

    }

    public List<String> findAllTags() {

        List<String> tags = new ArrayList<>();

        String query = "select * from hashtag";

        try {

            ResultSet resultSet = connection.createStatement().executeQuery(query);

            while (resultSet.next()) {
                tags.add(resultSet.getString("name"));
            }

        } catch (Exception e) {}

        return tags;

    }

    public List<Event> findByHashtag(List<String> ids) {

        List<Event> events = new LinkedList<>();

        String query = "select * from events where date_time >= ? and id in ( select event_id from event_tags where tag_id = ( select id from hashtag where name = ? ) )";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            for (String tag : ids) {

                preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.setString(2, tag);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {

                    events.add(parseEvent(resultSet));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        ZoneOffset zoneOffset = ZonedDateTime.now().getOffset();

        events.sort((x, y) -> (int) (x.getDateTime().toEpochSecond(zoneOffset) - y.getDateTime().toEpochSecond(zoneOffset)));

        events.sort((x, y) -> x.getHashTags().size() - y.getHashTags().size());

        return events;

    }

    public void cancelVisit(Long eventId, Long userId) {

        String query = "delete from event_members where event_id = ? and profile_id = (select id from profile where user_id = ?) ";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, eventId);
            preparedStatement.setLong(2, userId);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void visit(Long eventId, Long userId) {

        String query = "insert into event_members (event_id, profile_id) values (?, ( select id from profile where user_id = ? ) )";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, eventId);
            preparedStatement.setLong(2, userId);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Profile> getMembers(Long id) {

        List<Profile> profiles = new ArrayList<>();

        String membersQuery = "select * from profile where id in (select profile_id from event_members where event_id = ?)";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(membersQuery);

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                profiles.add(ProfileService.parseProfile(resultSet));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return profiles;

    }

    public Event findById(Long id) {

        String query = "select * from events where id = ? and date_time >= ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, id);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return parseEvent(resultSet);
            }

        } catch (Exception e) {}

        return null;

    }

    public List<Profile> getLikes(Long id) {

        List<Profile> profiles = new ArrayList<>();

        String likesQuery = "select * from profile where id in (select profile_id from event_likes where event_id = ?)";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(likesQuery);

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                profiles.add(ProfileService.parseProfile(resultSet));

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return profiles;
    }

    public List<String> findHashtagByEventId(Long id) {

        List<String> tags = new ArrayList<>();

        String hashTagsQuery = "select name from hashtag where id in (select tag_id from event_tags where event_id = ?)";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(hashTagsQuery);

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tags.add(resultSet.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tags;

    }

    public void save(Event event) {

        String checkQuery = "select * from events where id = ?";

        String addQuery = "insert into events (name, description, date_time, owner_id, photo_url) values (?, ?, ?, ?, ?) returning id";
        String addHashTagQuery = "insert into event_tags (event_id, tag_id) values (?, ( select id from hashtag where name = ?) )";

        String updateQuery = "update events set name = ?, description = ?, date_time = ?, owner_id = ?, photo_url = ? where id = ?";

        String deleteTag = "delete from event_tags where event_id = ?";

        try {

            boolean create = true;

            if (event.getId() != null) {

                PreparedStatement preparedStatement = connection.prepareStatement(checkQuery);

                preparedStatement.setLong(1, event.getId());

                if (preparedStatement.executeQuery().next()) {
                    create = false;
                }

            }

            Long eventId = event.getId();

            if (create) {

                PreparedStatement addEventStatement = connection.prepareStatement(addQuery);

                addEventStatement.setString(1, event.getName());
                addEventStatement.setString(2, event.getDescription());
                addEventStatement.setTimestamp(3, Timestamp.valueOf(event.getDateTime()));
                addEventStatement.setLong(4, event.getOwner().getUserId());
                addEventStatement.setString(5, event.getPhotoUrl());

                ResultSet resultSet = addEventStatement.executeQuery();

                if (resultSet.next()) {

                    eventId = resultSet.getLong("id");

                }

            } else {

                PreparedStatement updatePreparedStatement = connection.prepareStatement(updateQuery);

                updatePreparedStatement.setString(1, event.getName());
                updatePreparedStatement.setString(2, event.getDescription());
                updatePreparedStatement.setTimestamp(3, Timestamp.valueOf(event.getDateTime()));
                updatePreparedStatement.setLong(4, event.getOwner().getUserId());
                updatePreparedStatement.setString(5, event.getPhotoUrl());
                updatePreparedStatement.setLong(6, event.getId());

                updatePreparedStatement.executeUpdate();

                PreparedStatement deleteOldTags = connection.prepareStatement(deleteTag);

                deleteOldTags.setLong(1, event.getId());

                deleteOldTags.executeUpdate();

            }

            PreparedStatement addHashtagStatement = connection.prepareStatement(addHashTagQuery);

            for (String tag : event.getHashTags()) {

                addHashtagStatement.setLong(1, eventId);
                addHashtagStatement.setString(2, tag);
                addHashtagStatement.executeUpdate();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void delete(Event event) {
        deleteById(event.getId());
    }

    public void deleteById(Long id) {

        List<String> queries = Arrays.asList("delete from events where id = ?", "delete from event_tags where event_id = ?",
                                            "delete from event_members where event_id = ?", "delete from event_likes where event_id = ?");

        try {

            for (String query : queries) {

                PreparedStatement preparedStatement = connection.prepareStatement(query);

                preparedStatement.setLong(1, id);

                preparedStatement.executeUpdate();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addTag(String name) {

        String query = "insert into hashtag (name) values (?)";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Event> getHistoryOf(Long userId) {

        String query = "select * from events where owner_id = ? and date_time < ?";

        List<Event> events = new LinkedList<>();

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, userId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                events.add(parseEvent(resultSet));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        ZoneOffset zoneOffset = ZonedDateTime.now().getOffset();

        events.sort((x, y) -> (int) (x.getDateTime().toEpochSecond(zoneOffset) - y.getDateTime().toEpochSecond(zoneOffset)));

        return events;

    }

    public List<Event> findByOwner(Long ownerId) {

        List<Event> events = new LinkedList<>();

        String query = "select * from events where owner_id = ? and date_time >= ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, ownerId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                events.add(parseEvent(resultSet));
            }

        } catch (Exception e) {e.printStackTrace();}

        return events;

    }

    public Event parseEvent(ResultSet resultSet) throws SQLException {

        Long id = resultSet.getLong("id");

        Event event = new Event();

        event.setId(id);
        event.setDescription(resultSet.getString("description"));
        event.setName(resultSet.getString("name"));
        event.setOwner(profileService.findByOwnerId(resultSet.getLong("owner_id")));
        event.setPhotoUrl(resultSet.getString("photo_url"));
        event.setHashTags(findHashtagByEventId(id));
        event.setLikes(getLikes(id));
        event.setDateTime(parseDate(resultSet.getString("date_time")));
        event.setMembers(getMembers(id));

        return event;

    }

    public Event parseEvent(HttpServletRequest req) {

        Event event = new Event();

        try {

            event.setId(Long.parseLong(req.getParameter("id")));

        } catch (Exception e) {}

        event.setName(req.getParameter("name"));
        event.setDescription(req.getParameter("description"));

        String dateTime = req.getParameter("dateTime");
        event.setDateTime(dateTime != null && !dateTime.isEmpty()? LocalDateTime.parse(dateTime) : null);

        event.setPhotoUrl(req.getParameter("photoUrl"));

        event.setHashTags(Arrays.asList(req.getParameterValues("hash")));

        return event;

    }

    public void likeEvent(Long eventId, Long userId) {

        String checkQuery = "select * from event_likes where event_id = ? and profile_id = (select id from profile where user_id = ?)";

        String likeQuery = "insert into event_likes (event_id, profile_id) values (?, (select id from profile where user_id = ?) )";

        String unlikeQuery = "delete from event_likes where event_id = ? and profile_id = (select id from profile where user_id = ?)";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(checkQuery);

            preparedStatement.setLong(1, eventId);
            preparedStatement.setLong(2, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                PreparedStatement unlikeStatement = connection.prepareStatement(unlikeQuery);

                unlikeStatement.setLong(1, eventId);
                unlikeStatement.setLong(2, userId);

                unlikeStatement.executeUpdate();

            } else {

                PreparedStatement likeStatement = connection.prepareStatement(likeQuery);

                likeStatement.setLong(1, eventId);
                likeStatement.setLong(2, userId);

                likeStatement.executeUpdate();

            }

        } catch (Exception e) {
           e.printStackTrace();
        }

    }


}
