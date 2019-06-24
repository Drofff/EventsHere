package bean;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDataService implements Serializable {

    private static UserDataService userDataService;
    private static Connection connection;

    private UserDataService(HttpSession session) {
        this.connection = (Connection)session.getAttribute(ConnectionService.CONNECTION_KEY);
    }

    public static UserDataService getInstance(HttpSession session) {

        if (userDataService == null) {

            userDataService = new UserDataService(session);

        }

        return userDataService;
    }

    public Long findUserByUsername(String username) {

        String query = "select * from user_info where username = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                return resultSet.getLong("id");
            }


        } catch (Exception e) {}

        return null;

    }

    public void changePassword(Long id, String password) {

        String query = "update user_info set password = ? where id = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, password);
            preparedStatement.setLong(2, id);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
