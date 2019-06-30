package entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Profile {

    private Long id;

    private String firstName;

    private String lastName;

    private Long userId;

    private String phoneNumber;

    private String photoUrl;

    private List<User> subscribers;

    private List<User> subscriptions;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<User> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<User> subscribers) {
        this.subscribers = subscribers;
    }

    public List<User> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<User> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public static Profile parse(ResultSet resultSet) throws SQLException {

        Profile profile = new Profile();

        profile.setId(resultSet.getLong("id"));
        profile.setPhotoUrl(resultSet.getString("photo_url"));
        profile.setPhoneNumber(resultSet.getString("phone_number"));
        profile.setUserId(resultSet.getLong("user_id"));
        profile.setLastName(resultSet.getString("last_name"));
        profile.setFirstName(resultSet.getString("first_name"));

        return profile;
    }

}
