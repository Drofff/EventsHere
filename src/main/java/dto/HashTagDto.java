package dto;

import service.ConnectionService;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HashTagDto {

    private static HashTagDto hashTagDto;

    private static Connection connection;

    private HashTagDto() {}

    public static HashTagDto getInstance(HttpSession session) {
        if (hashTagDto == null) {
            hashTagDto = new HashTagDto();
            connection = (Connection) session.getAttribute(ConnectionService.CONNECTION_KEY);
        }
        return hashTagDto;
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
