package bean;

import entity.Profile;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileService implements Serializable {

    private static ProfileService profileService;

    private static Connection connection;

    private ProfileService() {}

    public static ProfileService getInstance(HttpSession session) {
        if (profileService == null) {
            profileService = new ProfileService();
            connection = (Connection) session.getAttribute(ConnectionService.CONNECTION_KEY);
        }
        return profileService;
    }

    public Profile findByOwnerId(Long id) {
        String query = "select * from profile where user_id = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                return parseProfile(resultSet);
            }

        } catch (Exception e) {}

        return null;

    }

    public static Profile parseProfile(ResultSet resultSet) throws SQLException {

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
