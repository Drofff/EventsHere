package bean;

import dto.Profile;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    public Profile findById(Long id) {
        String query = "select * from profile where user_id = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                Profile profile = new Profile();

                profile.setId(resultSet.getLong("id"));
                profile.setFirstName(resultSet.getString("first_name"));
                profile.setLastName(resultSet.getString("last_name"));
                profile.setPhotoUrl(resultSet.getString("photo_url"));
                profile.setUserId(resultSet.getLong("user_id"));

                return profile;
            }

        } catch (Exception e) {}

        return null;

    }

}
