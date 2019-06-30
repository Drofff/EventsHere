package service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionService implements Serializable {

    private static final String CONNECTION_URL = "jdbc:postgresql://localhost:5432/events";

    private static final String USERNAME = "postgres";

    private static final String PASSWORD = "root";

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

        return DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD);
    }

}
