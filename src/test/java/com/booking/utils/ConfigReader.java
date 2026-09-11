package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final String CONFIG_FILE = "config/config.properties";
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + CONFIG_FILE + " on the classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + CONFIG_FILE, e);
        }
    }

    private ConfigReader() {
        // utility class, no instances
    }

    public static String getBaseUrl() {

        return properties.getProperty("base.url");
    }

    public static String getBookingEndpoint() {

        return properties.getProperty("booking.endpoint");
    }

    public static String getBookingHealthCheck() {

        return properties.getProperty("healthcheck.endpoint");
    }
}
