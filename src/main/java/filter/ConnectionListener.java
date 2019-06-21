package filter;

import bean.ConnectionService;

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

            se.getSession().setAttribute(ConnectionService.CONNECTION_KEY, connection);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }
}
