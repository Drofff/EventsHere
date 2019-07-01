package entity;

import dto.ProfileDto;
import org.hibernate.validator.constraints.NotBlank;
import service.ConnectionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Profile {

    private Long id;

    @NotBlank(message = "Please, input your first name")
    private String firstName;

    @NotBlank(message = "Enter your last name, please")
    private String lastName;

    private Long userId;

    private String status;

    @NotBlank(message = "Input your phone number")
    private String phoneNumber;

    @NotBlank(message = "You need to have a photo")
    private String photoUrl;

    private List<Profile> subscribers;

    private List<Profile> subscriptions;

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

    public List<Profile> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<Profile> subscribers) {
        this.subscribers = subscribers;
    }

    public List<Profile> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Profile> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static Profile parse(ResultSet resultSet) throws SQLException {

        Profile profile = new Profile();

        profile.setId(resultSet.getLong("id"));
        profile.setPhotoUrl(resultSet.getString("photo_url"));
        profile.setPhoneNumber(resultSet.getString("phone_number"));
        profile.setUserId(resultSet.getLong("user_id"));
        profile.setLastName(resultSet.getString("last_name"));
        profile.setFirstName(resultSet.getString("first_name"));
        profile.setStatus(resultSet.getString("status"));

        return profile;
    }

    public static Profile create(HttpServletRequest request) {

        Profile profile = new Profile();

        profile.setFirstName(request.getParameter("firstName"));
        profile.setLastName(request.getParameter("lastName"));
        profile.setStatus("Hey! I am new here!");
        profile.setPhoneNumber(request.getParameter("phoneNumber"));
        profile.setPhotoUrl(request.getParameter("photoUrl"));

        return profile;

    }


}
