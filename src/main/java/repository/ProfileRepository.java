package repository;

import entity.Profile;
import service.ConnectionService;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProfileRepository implements Serializable {

    private static ProfileRepository profileRepository;

    private static Connection connection;

    private ProfileRepository() {}

    public static ProfileRepository getInstance(HttpSession session) {
        if (profileRepository == null) {
            profileRepository = new ProfileRepository();
            connection = (Connection) session.getAttribute(ConnectionService.CONNECTION_KEY);
        }
        return profileRepository;
    }

    public void save(Profile profile) {
        String query = "select * from profile where id = ?";

        String insertQuery = "insert into profile (first_name, last_name, phone_number, photo_url, user_id, status) values (?, ?, ?, ?, ?, ?)";

        String updateQuery = "update profile set first_name = ?, last_name = ?, phone_number = ?, photo_url = ?, user_id = ?, status = ? where id = ?";

        try {

            if (profile.getId() != null) {

                PreparedStatement preparedStatement = connection.prepareStatement(query);

                preparedStatement.setLong(1, profile.getId());

                if (preparedStatement.executeQuery().next()) {

                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

                    updateStatement.setString(1, profile.getFirstName());
                    updateStatement.setString(2, profile.getLastName());
                    updateStatement.setString(3, profile.getPhoneNumber());
                    updateStatement.setString(4, profile.getPhotoUrl());
                    updateStatement.setLong(5, profile.getUserId());
                    updateStatement.setString(6, profile.getStatus());
                    updateStatement.setLong(7, profile.getId());

                    updateStatement.executeUpdate();
                    return;

                }

            }

            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

            insertStatement.setString(1, profile.getFirstName());
            insertStatement.setString(2, profile.getLastName());
            insertStatement.setString(3, profile.getPhoneNumber());
            insertStatement.setString(4, profile.getPhotoUrl());
            insertStatement.setLong(5, profile.getUserId());
            insertStatement.setString(6, profile.getStatus());

            insertStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Profile> findAll() {

        List<Profile> profiles = new ArrayList<>();

        String query = "select * from profile";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                profiles.add(Profile.parse(resultSet));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return profiles;

    }

    public void setEmailNotification(Long userId, Boolean notifyMe) {

        String query = "update profile set notify_me = ? where user_id = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setBoolean(1, notifyMe);
            preparedStatement.setLong(2, userId);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Profile findById(Long id) {

        String query = "select * from profile where id = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Profile.parse(resultSet);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

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

    public void unsubscribe(Long profileId, Long channelId) {

        String query = "select * from profile where id = ?";

        String deleteQuery = "delete from subscription_info where subscriber_id = ? and channel_id = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, channelId);

            if (preparedStatement.executeQuery().next()) {

                PreparedStatement insertStatement = connection.prepareStatement(deleteQuery);

                insertStatement.setLong(1, profileId);
                insertStatement.setLong(2, channelId);

                insertStatement.executeUpdate();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void subscribe(Long profileId, Long channelId) {

        String query = "select * from profile where id = ?";

        String insertQuery = "insert into subscription_info (subscriber_id, channel_id) values (?, ?)";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, channelId);

            if (preparedStatement.executeQuery().next()) {

                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

                insertStatement.setLong(1, profileId);
                insertStatement.setLong(2, channelId);

                insertStatement.executeUpdate();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Profile> getSubscriptions(Long id) {

        String query = "select * from profile where id in ( select channel_id from subscription_info where subscriber_id = ? ) ";

        return getProfileListById(query, id);

    }

    private List<Profile> getProfileListById(String query, Long id) {

        List<Profile> subscribers = new ArrayList<>();

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                subscribers.add(Profile.parse(resultSet));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return subscribers;

    }

    public List<Profile> getSubscribers(Long id) {

        String query = "select * from profile where id in ( select subscriber_id from subscription_info where channel_id = ? ) ";

        return getProfileListById(query, id);

    }

}
