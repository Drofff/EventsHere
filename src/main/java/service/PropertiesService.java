package service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesService implements Serializable {

    private static PropertiesService propertiesService;

    private static Properties projectProperties;

    private static final String PROPERTIES_PATH = "/application.properties";

    private PropertiesService() {}

    public static PropertiesService getInstance() {

        if (propertiesService == null) {

            propertiesService = new PropertiesService();

            try {
                loadFile();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return propertiesService;

    }

    private static void loadFile() throws FileNotFoundException, IOException {

        projectProperties = new Properties();
        projectProperties.load(PropertiesService.class.getClassLoader().getResourceAsStream(PROPERTIES_PATH));

    }

    public Map<String, String> getDatabaseProperties() {

        Map<String, String> connectionProperties = new HashMap<>();

        connectionProperties.put("url", projectProperties.getProperty("database.url"));
        connectionProperties.put("username", projectProperties.getProperty("database.username"));
        connectionProperties.put("password", projectProperties.getProperty("database.password"));


        return connectionProperties;
    }

    public Map<String, String> getMailProperties() {

        Map<String, String> connectionProperties = new HashMap<>();

        connectionProperties.put("base_url", projectProperties.getProperty("app.base_url"));
        connectionProperties.put("username", projectProperties.getProperty("mail.username"));
        connectionProperties.put("password", projectProperties.getProperty("mail.password"));
        connectionProperties.put("host", projectProperties.getProperty("mail.host"));
        connectionProperties.put("port", projectProperties.getProperty("mail.port"));

        return connectionProperties;

    }

}
