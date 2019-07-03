package repository;

import service.ConnectionService;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HashTagRepository {

    private static HashTagRepository hashTagRepository;

    private static Connection connection;

    private HashTagRepository() {}

    public static HashTagRepository getInstance(HttpSession session) {
        if (hashTagRepository == null) {
            hashTagRepository = new HashTagRepository();
            connection = (Connection) session.getAttribute(ConnectionService.CONNECTION_KEY);
        }
        return hashTagRepository;
    }

    public List<String> findAll() {

        List<String> tags = new ArrayList<>();

        String query = "select * from hashtag";

        try {

            ResultSet resultSet = connection.createStatement().executeQuery(query);

            while (resultSet.next()) {
                tags.add(resultSet.getString("name"));
            }

        } catch (Exception e) {}

        return tags;

    }

    public List<String> findByEventId(Long id) {

        List<String> tags = new ArrayList<>();

        String hashTagsQuery = "select name from hashtag where id in (select tag_id from event_tags where event_id = ?)";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(hashTagsQuery);

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tags.add(resultSet.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tags;

    }

    public void add(String name) {

        String query = "insert into hashtag (name) values (?)";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
