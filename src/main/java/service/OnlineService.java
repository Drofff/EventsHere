package service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OnlineService implements Serializable {

    private static OnlineService onlineService;

    private static List<String> onlineUsers = new ArrayList<>();

    private OnlineService() {}

    public static OnlineService getInstance() {
        if (onlineService == null) {
            onlineService = new OnlineService();
        }

        return onlineService;
    }

    public void online(String id) {
        onlineUsers.add(id);
    }

    public void offline(String id) {

        if (onlineUsers.contains(id)) {
            onlineUsers.remove(id);
        }

    }

    public Integer getOnlineCount() {
        return onlineUsers.size();
    }

}
