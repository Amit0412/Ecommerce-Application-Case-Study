package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {
    public static String getConnectionString(String propertyFileName) {
        Properties properties = new Properties();
        String connectionString = null;

        try (FileInputStream fis = new FileInputStream(propertyFileName)) {
            properties.load(fis);
            String url = properties.getProperty("url");
            String password = properties.getProperty("password");
            String username = properties.getProperty("username");
            String driver = properties.getProperty("driver");
            connectionString = String.join(" ", url,username,password,driver);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return connectionString;
    }
}