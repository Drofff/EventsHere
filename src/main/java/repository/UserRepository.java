package repository;

import entity.Role;
import entity.User;
import service.ConnectionService;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class UserRepository implements Serializable {

    private static UserRepository userRepository;
    private static Connection connection;

    private UserRepository(HttpSession session) {
        connection = (Connection)session.getAttribute(ConnectionService.CONNECTION_KEY);
    }

    public static UserRepository getInstance(HttpSession session) {

        if (userRepository == null) {

            userRepository = new UserRepository(session);

        }

        return userRepository;
    }

    public Long findByUsername(String username) {

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

    public String findById(Long id) {

        String query = "select username from user_info where id = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("username");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void delete(Long id) {

        List<String> queries = Arrays.asList("delete from user_info where id = ?",
                                            "delete from profile where user_id = ?",
                                            "delete from events where owner_id = ?");

        try {

            for (String query : queries) {

                PreparedStatement preparedStatement = connection.prepareStatement(query);

                preparedStatement.setLong(1, id);

                preparedStatement.executeUpdate();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changePassword(Long id, String password) {

        String query = "update user_info set password = ? where id = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, new String(Base64.getEncoder().encode(password.getBytes())));
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

        String query = "insert into user_info (username, password, active) values (?, ?, ?) returning id";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setBoolean(3, false);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                Long userId = resultSet.getLong("id");

                String roleQuery = "insert into user_role (user_id, role) values (?, ?)";

                PreparedStatement roleStatement = connection.prepareStatement(roleQuery);

                roleStatement.setLong(1, userId);
                roleStatement.setString(2, Role.USER.name());

                roleStatement.executeUpdate();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Integer count() {

        String query = "select count(*) as users from user_info";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("users");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;

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

    public boolean isAdmin(Long id) {

        String query = "select role from user_role where user_id = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("role").equals(Role.ADMIN.name());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

}
