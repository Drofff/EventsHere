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

        System.out.println("Creating user info tables");

        String userInfoTableQuery = "create table if not exists user_info ( id serial primary key, username varchar not null, password varchar not null, active boolean )";

        String userRoleTableQuery = "create table if not exists user_role ( user_id bigint not null, role varchar not null, PRIMARY KEY (user_id, role))";

        String sessionTokenTableQuery = "create table if not exists session_token (user_id bigint not null, token varchar not null, PRIMARY KEY(user_id, token))";

        try {

            connection.prepareStatement(userInfoTableQuery).executeUpdate();

            System.out.println("Successfully created table 'user_info'");

            connection.prepareStatement(userRoleTableQuery).executeUpdate();

            System.out.println("Successfully created table 'user_role'");

            connection.prepareStatement(sessionTokenTableQuery).executeUpdate();

            System.out.println("Successfully created table 'session_token'");

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

            System.out.println("Creating admin with credentials:\nusername: " + username + "\npassword: " + password);

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

                System.out.println("Successfully added to 'user_info'");

                PreparedStatement roleStatement = connection.prepareStatement(insertRole);

                roleStatement.setLong(1, id);
                roleStatement.setString(2, Role.ADMIN.name());

                roleStatement.executeUpdate();

                System.out.println("Success while adding info to 'user_role'");

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

            System.out.println("Starting events creation\n");

            System.out.println("1. Creating 'events' table");

            connection.prepareStatement(eventsQuery).executeUpdate();

            System.out.println("Success");

            System.out.println("2. Creating 'event_likes' table");

            connection.prepareStatement(eventLikesQuery).executeUpdate();

            System.out.println("Success");

            System.out.println("3. Creating 'event_members' table");

            connection.prepareStatement(eventMembersQuery).executeUpdate();

            System.out.println("Success");

            System.out.println("4. Creating 'event_tags' table");

            connection.prepareStatement(eventTagsQuery).executeUpdate();

            System.out.println("Successfully finished creating events");

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

            System.out.println("Creating table 'hashtag'");

            connection.prepareStatement(query).executeUpdate();

            System.out.println("Success");

        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }

        assert true;
    }

    @Test
    public void profile() {

        String query = "create table if not exists profile ( id serial primary key, first_name varchar not null, last_name varchar not null, user_id bigint not null, photo_url varchar, phone_number varchar, status varchar )";

        String subscrQuery = "create table if not exists subscription_info (subscriber_id bigint not null, channel_id bigint not null, PRIMARY KEY(subscriber_id, channel_id))";

        try {

            System.out.println("Creating 'profile' table");

            connection.prepareStatement(query).executeUpdate();

            System.out.println("Successfully created table 'profile'");

            System.out.println("Creating 'subscription_info' table");

            connection.prepareStatement(subscrQuery).executeUpdate();

            System.out.println("Successfully create table 'subscription_info'");

        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }

        assert true;

    }

}
