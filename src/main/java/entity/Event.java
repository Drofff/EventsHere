package entity;

import repository.EventRepository;
import repository.HashTagRepository;
import repository.ProfileRepository;
import org.hibernate.validator.constraints.NotBlank;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Event {

    private Long id;

    @NotBlank(message = "Please, enter event's name")
    private String name;

    @NotBlank(message = "Describe your event")
    private String description;

    private List<String> hashTags;

    @NotNull(message = "Please, specify date")
    private LocalDateTime dateTime;

    private List<Profile> members;

    private Profile owner;

    private List<Profile> likes;

    @NotBlank(message = "Please, provide photo (url)")
    private String photoUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getFormattedDateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd' 'HH:mm:ss");
        return this.dateTime.format(dateTimeFormatter);
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDateTime(String dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd' 'HH:mm:ss");

        if (dateTime.matches(".*\\..*")) {
            dateTime = dateTime.split("\\.")[0];
        }

        this.dateTime = LocalDateTime.parse(dateTime, dateTimeFormatter);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<String> hashTags) {
        this.hashTags = hashTags;
    }

    public List<Profile> getMembers() {
        return members;
    }

    public void setMembers(List<Profile> members) {
        this.members = members;
    }

    public Profile getOwner() {
        return owner;
    }

    public void setOwner(Profile owner) {
        this.owner = owner;
    }

    public List<Profile> getLikes() {
        return likes;
    }

    public void setLikes(List<Profile> likes) {
        this.likes = likes;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public static Event parse(ResultSet resultSet, HttpSession session) throws SQLException {

        EventRepository eventRepository = EventRepository.getInstance(session);
        HashTagRepository hashTagRepository = HashTagRepository.getInstance(session);
        ProfileRepository profileRepository = ProfileRepository.getInstance(session);

        Long id = resultSet.getLong("id");

        Event event = new Event();

        event.setId(id);
        event.setDescription(resultSet.getString("description"));
        event.setName(resultSet.getString("name"));
        event.setOwner(profileRepository.findByOwnerId(resultSet.getLong("owner_id")));
        event.setPhotoUrl(resultSet.getString("photo_url"));
        event.setHashTags(hashTagRepository.findByEventId(id));
        event.setLikes(eventRepository.getLikes(id));
        event.setDateTime(resultSet.getString("date_time"));
        event.setMembers(eventRepository.getMembers(id));

        return event;
    }

    public static Event parse(HttpServletRequest req) {

        Event event = new Event();

        try {

            event.setId(Long.parseLong(req.getParameter("id")));

        } catch (Exception e) {}

        event.setName(req.getParameter("name"));
        event.setDescription(req.getParameter("description"));

        String dateTime = req.getParameter("dateTime");

        try {
            event.setDateTime(dateTime != null && !dateTime.isEmpty() ? LocalDateTime.parse(dateTime) : null);
        } catch (Exception e) {}

        event.setPhotoUrl(req.getParameter("photoUrl"));

        if (req.getParameter("hash") != null) {
            event.setHashTags(Arrays.asList(req.getParameterValues("hash")));
        }

        return event;
    }

}
