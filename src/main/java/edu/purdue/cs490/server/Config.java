package edu.purdue.cs490.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {
    private static final Logger log = Logger.getLogger(Server.class.getName());

    static Properties defaults;
    Properties properties;

    static {
        defaults = new Properties();
        defaults.setProperty("PlainPort", "5000");
        defaults.setProperty("SSLPort", "5001");

        defaults.setProperty("DatabaseFile", "lab-broadcast.sqlite3");

        defaults.setProperty("Keystore", "keystore.jks");
        defaults.setProperty("KeystorePassword", "");

        defaults.setProperty("WorkerPoolSize", "60");
    }

    public Config (String filename) {
        properties = new Properties(defaults);

        try (Reader r = new FileReader(filename)) {
            properties.load(r);
        } catch (Exception ex) {
            log.log(Level.WARNING, "No config file found, proceeding with defaults.");
        }
        validate();
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    /**
     * Validate config values and remove them so the defaults are used instead.
     */
    private void validate() {
        try {
            Integer.parseInt(properties.getProperty("PlainPort"));
        } catch (NumberFormatException ex){
            log.warning("PlainPort property invalid, defaulting..");
            properties.remove("PlainPort");
        }

        try {
            Integer.parseInt(properties.getProperty("SSLPort"));
        } catch (NumberFormatException ex){
            log.warning("SSLPort property invalid, defaulting..");
            properties.remove("SSLPort");
        }

        try {
            Integer.parseInt(properties.getProperty("WorkerPoolSize"));
        } catch (NumberFormatException ex){
            log.warning("WorkerPoolSize property invalid, defaulting..");
            properties.remove("WorkerPoolSize");
        }
    }
}
