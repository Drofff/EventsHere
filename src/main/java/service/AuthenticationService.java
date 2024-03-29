package service;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthenticationService implements Serializable {

    private Connection connection;
    private static AuthenticationService authenticationService;

    public static final String USER_AUTHENTICATION_KEY = "user_id";

    public static final String REMEMBER_ME_KEY = "session_token";

    public static final Long FAILURE_CODE = -1l;

    private AuthenticationService(HttpSession session) {
        this.connection = (Connection)session.getAttribute(ConnectionService.CONNECTION_KEY);
    }

    public static AuthenticationService getInstance(HttpSession session) {

        if (authenticationService == null) {

            authenticationService = new AuthenticationService(session);

        }

        return authenticationService;
    }

    public Long authenticateByToken(String token) throws SQLException {

        String query = "select user_id from session_token where token = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, token);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next()) {
            throw new SQLException("Wrong token");
        }

        return resultSet.getLong("user_id");

    }

    public Long authenticate(String username, String password) {

        String query = "select * from user_info where username = ? and password = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, username);

            String encodedPassword = EncryptingService.getInstance().encrypt(password);

            preparedStatement.setString(2, encodedPassword);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new SQLException("Invalid credentials");
            }

            return resultSet.getLong("id");

        } catch (SQLException e) {

            return FAILURE_CODE;

        }

    }

    public boolean userExists(String username) {

        String query = "select * from user_info where username = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, username);

            ResultSet set = preparedStatement.executeQuery();

            if (set.next()) {
                return set.getString("username") != null;
            }

        } catch (Exception e) {}

        return false;
    }

    public String getToken(Long id) {

        String selectQuery = "select token from session_token where user_id = ?";

        String insertQuery = "insert into session_token (user_id, token) VALUES (?, ?)";

        try {

            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);

            selectStatement.setLong(1, id);

            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {

                try {

                    return resultSet.getString("token");

                } catch (Exception e) {}

            }

            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

            insertStatement.setLong(1, id);

            String token = UUID.randomUUID().toString();

            insertStatement.setString(2, token);

            if (insertStatement.executeUpdate() > 0) {

                return token;

            } else {

                throw new Exception("Database error");

            }

        } catch (Exception e) {
            return null;
        }

    }

}
