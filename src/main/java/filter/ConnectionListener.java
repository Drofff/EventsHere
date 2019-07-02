package filter;

import service.ConnectionService;
import service.OnlineService;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.sql.Connection;

@WebListener
public class ConnectionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {

        try {

            Connection connection = ConnectionService.getConnection();
            OnlineService onlineService = OnlineService.getInstance();

            se.getSession().setAttribute(ConnectionService.CONNECTION_KEY, connection);
            onlineService.online(se.getSession().getId());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

        OnlineService onlineService = OnlineService.getInstance();

        onlineService.offline(se.getSession().getId());

    }
}
