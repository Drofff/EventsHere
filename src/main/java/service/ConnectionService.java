package service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

public class ConnectionService implements Serializable {

    public static final String CONNECTION_KEY = "db_connection";

    private static ConnectionService connectionService;

    private ConnectionService() {}

    public static ConnectionService getConnectionService() {

        if (connectionService == null) {

            connectionService = new ConnectionService();

        }

        return connectionService;
    }

    public static Connection getConnection() throws Exception {

        Class.forName("org.postgresql.Driver");

        Map<String, String> properties = PropertiesService.getInstance().getDatabaseProperties();

        return DriverManager.getConnection(properties.get("url"), properties.get("username"), properties.get("password"));
    }

}
