package dto;

import entity.Profile;
import service.ConnectionService;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProfileDto implements Serializable {

    private static ProfileDto profileDto;

    private static Connection connection;

    private ProfileDto() {}

    public static ProfileDto getInstance(HttpSession session) {
        if (profileDto == null) {
            profileDto = new ProfileDto();
            connection = (Connection) session.getAttribute(ConnectionService.CONNECTION_KEY);
        }
        return profileDto;
    }

    public Profile findByOwnerId(Long id) {
        String query = "select * from profile where user_id = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                return Profile.parse(resultSet);
            }

        } catch (Exception e) {}

        return null;

    }

}
