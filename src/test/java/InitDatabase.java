import entity.Role;
import org.junit.Before;
import org.junit.Test;
import service.ConnectionService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;

public class InitDatabase {

    public static Connection connection;

    @Before
    public void connect() {

        try {

            if (connection == null) {

                connection = ConnectionService.getConnection();

            }

        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }

        assert true;

    }

    @Test
    public void userInfo() {

        String userInfoTableQuery = "create table if not exists user_info ( id serial primary key, username varchar not null, password varchar not null, active boolean )";

        String userRoleTableQuery = "create table if not exists user_role ( user_id bigint not null, role varchar not null, PRIMARY KEY (user_id, role))";

        String sessionTokenTableQuery = "create table if not exists session_token (user_id bigint not null, token varchar not null, PRIMARY KEY(user_id, token))";

        try {

            connection.prepareStatement(userInfoTableQuery).executeUpdate();
            connection.prepareStatement(userRoleTableQuery).executeUpdate();
            connection.prepareStatement(sessionTokenTableQuery).executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }

        assert true;

    }

    //@Test
    public void createAdmin() {

        String username = "mike@admin.com";
        String password = "123456";

        String insertFirstAdminQuery = "insert into user_info (username, password, active) values (?, ?, ?) returning id";

        String insertRole = "insert into user_role (user_id, role) values (?, ?)";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(insertFirstAdminQuery);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, Base64.getEncoder().encode(password.getBytes()).toString());
            preparedStatement.setBoolean(3, true);

            ResultSet resultSet = preparedStatement.executeQuery();

            Long id = null;

            if (resultSet.next()) {
                id = resultSet.getLong("id");
            }

            if (id != null) {

                PreparedStatement roleStatement = connection.prepareStatement(insertRole);

                roleStatement.setLong(1, id);
                roleStatement.setString(2, Role.ADMIN.name());

                roleStatement.executeUpdate();

            }

        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }

        assert true;

    }

    @Test
    public void events() {

        String eventsQuery = "create table if not exists events ( id serial primary key, name varchar not null, description varchar not null, owner_id bigint not null, photo_url varchar, date_time timestamp )";

        String eventLikesQuery = "create table if not exists event_likes (event_id bigint not null, profile_id bigint not null, PRIMARY KEY (event_id, profile_id))";

        String eventMembersQuery = "create table if not exists event_members (event_id bigint not null, profile_id bigint not null, PRIMARY KEY (event_id, profile_id))";

        String eventTagsQuery = "create table if not exists event_tags (event_id bigint not null, tag_id bigint not null, PRIMARY KEY(event_id, tag_id))";

        try {

            connection.prepareStatement(eventsQuery).executeUpdate();

            connection.prepareStatement(eventLikesQuery).executeUpdate();

            connection.prepareStatement(eventMembersQuery).executeUpdate();

            connection.prepareStatement(eventTagsQuery).executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }

        assert true;

    }

    @Test
    public void hashTag() {

        String query = "create table if not exists hashtag ( id serial primary key, name varchar not null )";

        try {

            connection.prepareStatement(query).executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }

        assert true;
    }

    @Test
    public void profile() {

        String query = "create table if not exists profile ( id serial primary key, first_name varchar not null, last_name varchar not null, user_id bigint not null, photo_url varchar, phone_number varchar, status varchar, notify_me boolean )";

        String subscrQuery = "create table if not exists subscription_info (subscriber_id bigint not null, channel_id bigint not null, PRIMARY KEY(subscriber_id, channel_id))";

        try {

            connection.prepareStatement(query).executeUpdate();

            connection.prepareStatement(subscrQuery).executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }

        assert true;

    }

}
