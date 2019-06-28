package bean;

import dto.Event;
import dto.Profile;

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
            String query = "select count(*) as num from events";
            return connection.createStatement().executeQuery(query).getLong("num") / 10;
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

        String query = "select * from events limit 10 offset ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, leftBound);

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
        String query = "select * from events where LOWER(name) like LOWER(CONCAT('%', ?, '%'))";

        List<Event> events = new LinkedList<>();

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);

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

        String query = "select * from events where id in ( select event_id from event_tags where tag_id = ( select id from hashtag where name = ? ) )";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            for (String tag : ids) {

                preparedStatement.setString(1, tag);

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

        return events;

    }

    public List<Profile> getMembers(Long id) {

        List<Profile> profiles = new ArrayList<>();

        String membersQuery = "select * from profile where id in (select profile_id from event_members where event_id = ?)";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(membersQuery);

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                profiles.add(parseProfile(resultSet));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return profiles;

    }

    public Event findById(Long id) {

        String query = "select * from events where id = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, id);

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

                profiles.add(parseProfile(resultSet));

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

    public Event parseEvent(ResultSet resultSet) throws SQLException {

        Long id = resultSet.getLong("id");

        Event event = new Event();

        event.setId(id);
        event.setDescription(resultSet.getString("description"));
        event.setName(resultSet.getString("name"));
        event.setOwner(profileService.findById(resultSet.getLong("owner_id")));
        event.setPhotoUrl(resultSet.getString("photo_url"));
        event.setHashTags(findHashtagByEventId(id));
        event.setLikes(getLikes(id));
        event.setDateTime(parseDate(resultSet.getString("date_time")));
        event.setMembers(getMembers(id));

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


    public Profile parseProfile(ResultSet resultSet) throws SQLException {

        Profile profile = new Profile();

        profile.setId(resultSet.getLong("id"));
        profile.setPhotoUrl(resultSet.getString("photo_url"));
        profile.setUserId(resultSet.getLong("user_id"));
        profile.setLastName(resultSet.getString("last_name"));
        profile.setFirstName(resultSet.getString("first_name"));

        return profile;
    }

}
