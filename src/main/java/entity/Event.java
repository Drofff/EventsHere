package entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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

}
