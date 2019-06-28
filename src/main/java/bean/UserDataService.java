package bean;

import dto.User;

import javax.servlet.http.HttpSession;
import javax.xml.transform.Result;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void activate(String email) {

        String query = "update user_info set active = true where username = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, email);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void save(User user) {

        String query = "insert into user_info (username, password, active) values (?, ?, ?)";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setBoolean(3, false);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isActive(Long id) {
        String query = "select active from user_info where id = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean("active");
            }

        } catch (Exception e) {}

        return false;

    }


}
